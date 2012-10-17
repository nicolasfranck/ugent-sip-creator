package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.bagitmets.DSpaceBagItMets;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask2;

public class SaveBagHandler6 extends Handler {
    private static final Log log = LogFactory.getLog(SaveBagHandler6.class);
    private static final long serialVersionUID = 1L;    
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;
    
    public SaveBagHandler6() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
        bagView.getInfoFormsPane().getUpdateBagHandler().updateBag();
        if(bagView.getBagRootPath().exists()) {
            tmpRootPath = bagView.getBagRootPath();
            confirmWriteBag();
        }else {
            saveBag(bagView.getBagRootPath());
        }
    }

    @Override
    public void execute(){        
        SwingUtils.monitor(new SaveBagWorker(),"saving bag","");        
    }

    public void setTmpRootPath(File f) {
        this.tmpRootPath = f;
    }

    public File getTmpRootPath() {
        return this.tmpRootPath;
    }

    public void setClearAfterSaving(boolean b) {
        this.clearAfterSaving = b;
    }

    public boolean getClearAfterSaving() {
        return this.clearAfterSaving;
    }

    public void saveBag(File file) {
    	DefaultBag bag = BagView.getInstance().getBag();
        bag.setRootDir(file);        
        execute();
    }

    public void confirmWriteBag() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            boolean isCancel = true;
            @Override
            protected void onConfirm() {
                DefaultBag bag = bagView.getBag();
                if (bag.getSize() > DefaultBag.MAX_SIZE) {
                    confirmAcceptBagSize();
                } else {                    
                    bagView.setBagRootPath(tmpRootPath);
                    saveBag(bagView.getBagRootPath());
                }
            }
            @Override
            protected void onCancel(){
                super.onCancel();
                if (isCancel) {
                    cancelWriteBag();
                    isCancel = false;
                }
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.create"));
        dialog.showDialog();
    }

    private void cancelWriteBag() {
    	clearAfterSaving = false;
    }

    public void confirmAcceptBagSize() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                bagView.setBagRootPath(tmpRootPath);
                saveBag(bagView.getBagRootPath());
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }

    public void saveBagAs() {
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
        File selectFile = new File(File.separator+".");
        JFrame frame = new JFrame();
        JFileChooser fs = new JFileChooser(selectFile);
    	fs.setDialogType(JFileChooser.SAVE_DIALOG);
    	fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	fs.addChoosableFileFilter(bagView.getInfoFormsPane().getNoFilter());
    	fs.addChoosableFileFilter(bagView.getInfoFormsPane().getZipFilter());
        fs.addChoosableFileFilter(bagView.getInfoFormsPane().getTarFilter());
        fs.setDialogTitle("Save Bag As");
    	fs.setCurrentDirectory(bag.getRootDir());
    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(Context.getMessage("bag.label.noname"))) {
            String selectedName = bag.getName();
            if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                selectedName += "."+DefaultBag.ZIP_LABEL;
                fs.setFileFilter(bagView.getInfoFormsPane().getZipFilter());
            }
            else if (bag.getSerialMode() == DefaultBag.TAR_MODE) {
                selectedName += "."+DefaultBag.TAR_LABEL;
                fs.setFileFilter(bagView.getInfoFormsPane().getTarFilter());
            }
            else {
                fs.setFileFilter(bagView.getInfoFormsPane().getNoFilter());
            }
            fs.setSelectedFile(new File(selectedName));
    	}
    	int option = fs.showSaveDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fs.getSelectedFile();
            save(file);
        }
    }

    public void save(File file) {
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
        if (file == null) {
            file = bagView.getBagRootPath();
        }
        bag.setName(file.getName());
        File bagFile = new File(file, bag.getName());
    	if (bagFile.exists()) {
            tmpRootPath = file;
            confirmWriteBag();
    	} else {
            if (bag.getSize() > DefaultBag.MAX_SIZE) {
                tmpRootPath = file;
                confirmAcceptBagSize();
            } else {
                bagView.setBagRootPath(file);
                saveBag(BagView.getInstance().getBagRootPath());
            }
    	}
        String fileName = bagFile.getAbsolutePath();
        bagView.getInfoFormsPane().setBagName(fileName);
        bagView.getControl().invalidate();
    }
    
    private class SaveBagWorker extends LongTask2{
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());                        

            final BagView bagView = BagView.getInstance();
            MetsBag bag = bagView.getBag();
            Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
            
            bag.setBagItMets(new DSpaceBagItMets());
            
            Writer bagWriter = null;

            try {
                BagFactory bagFactory = new BagFactory();
                short mode = bag.getSerialMode();
                if (mode == DefaultBag.NO_MODE) {
                    bagWriter = new FileSystemWriter(bagFactory);
                } else if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    bagWriter = new ZipWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_MODE) {
                    bagWriter = new TarWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_GZ_MODE) {
                    bagWriter = new TarGzWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_BZ2_MODE) {
                    bagWriter = new TarBz2Writer(bagFactory);
                }                               
                
                bagWriter.addProgressListener(this);                
                messages = bag.write(bagWriter);

                if (messages != null && !messages.trim().isEmpty()) {
                    bagView.showWarningErrorDialog("Warning - bag not saved", "Problem saving bag:\n" + messages);
                } else {
                    bagView.showWarningErrorDialog("Bag saved", "Bag saved successfully.\n" );
                }
               
                if (bag.isSerialized()) {
                    if(clearAfterSaving){                       
                        bagView.clearBagHandler.clearExistingBag();
                        setClearAfterSaving(false);
                    }else{
                        if(bag.isValidateOnSave()) {
                            bagView.validateBagHandler.validateBag();
                        }                     
                        File bagFile = bag.getBagFile();
                        log.info("BagView.openExistingBag: " + bagFile);
                        bagView.openBagHandler.openExistingBag(bagFile);
                        bagView.updateSaveBag();
                    }
                } else {
                    ApplicationContextUtil.addConsoleMessage(messages);
                    bagView.updateManifestPane();
                }                
            }catch(Exception e){
                log.debug(e.getMessage());                
            }            
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());            
            return null;
        }    
        @Override
        public void cancel(){ 
            BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());            
        }    
    }    
}