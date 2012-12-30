package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.*;
import java.awt.event.ActionEvent;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import ugent.bagger.bagitmets.DefaultBagItMets;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask;

public class SaveBagHandler extends Handler {
    private static final Log log = LogFactory.getLog(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;    
    //private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;
    
    public SaveBagHandler() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
        //bagView.getInfoFormsPane().getUpdateBagHandler().updateBag();
        if(bagView.getBagRootPath().exists()) {
            //tmpRootPath = bagView.getBagRootPath();
            confirmWriteBag();
        }else {
            saveBag(bagView.getBagRootPath());
        }
    }
    

    @Override
    public void execute(){        
        SwingUtils.monitor(
            new SaveBagWorker(),
            Context.getMessage("SaveBagHandler.saving.title"),
            Context.getMessage("SaveBagHandler.saving.description")
        );        
    }
    /*
    public void setTmpRootPath(File f) {
        this.tmpRootPath = f;
    }

    public File getTmpRootPath() {
        return this.tmpRootPath;
    }*/

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
                    //bagView.setBagRootPath(tmpRootPath);
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
                //bagView.setBagRootPath(tmpRootPath);
                saveBag(bagView.getBagRootPath());
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }

    /*
    public void saveBagAs() {
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();        
        
        JFrame frame = new JFrame();
        JFileChooser fs = new JFileChooser();
    	fs.setDialogType(JFileChooser.SAVE_DIALOG);
    	fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
    	fs.addChoosableFileFilter(BagitUtils.getNoFilter());
    	fs.addChoosableFileFilter(BagitUtils.getZipFilter());
        fs.addChoosableFileFilter(BagitUtils.getTarFilter());
        fs.addChoosableFileFilter(BagitUtils.getTarGzFilter());
        fs.addChoosableFileFilter(BagitUtils.getTarBz2Filter());
        
        fs.setDialogTitle(Context.getMessage("SaveBagHandler.saveBagAs.title"));
    	fs.setCurrentDirectory(bag.getRootDir());        
    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(Context.getMessage("bag.label.noname"))) {
            String selectedName = bag.getName();
            
            if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                selectedName += "."+DefaultBag.ZIP_LABEL;
                fs.setFileFilter(BagitUtils.getZipFilter());
            }else if (bag.getSerialMode() == DefaultBag.TAR_MODE) {
                selectedName += "."+DefaultBag.TAR_LABEL;
                fs.setFileFilter(BagitUtils.getTarFilter());
            }else if(bag.getSerialMode() == DefaultBag.TAR_GZ_MODE){
                selectedName += "."+DefaultBag.TAR_GZ_LABEL;
                fs.setFileFilter(BagitUtils.getTarGzFilter());
            }else if(bag.getSerialMode() == DefaultBag.TAR_BZ2_MODE){
                selectedName += "."+DefaultBag.TAR_BZ2_LABEL;
                fs.setFileFilter(BagitUtils.getTarBz2Filter());
            }else {
                fs.setFileFilter(BagitUtils.getNoFilter());
            }
            fs.setSelectedFile(new File(selectedName));
    	}
    	int option = fs.showSaveDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fs.getSelectedFile();
            save(file);
        }
    }*/

    public void save(File file) {
        BagView bagView = BagView.getInstance();
    	MetsBag metsBag = bagView.getBag();
        if (file == null) {
            file = bagView.getBagRootPath();
        }
        metsBag.setName(file.getName());        
        
    	if(file.exists()){
            //tmpRootPath = file;
            confirmWriteBag();
    	} else {
            if (metsBag.getSize() > DefaultBag.MAX_SIZE) {
                //tmpRootPath = file;
                confirmAcceptBagSize();
            } else {
                bagView.setBagRootPath(file);
                saveBag(file);
            }
    	}        
    }
    
    private class SaveBagWorker extends LongTask{
        @Override
        protected Object doInBackground() throws Exception {
            
            SwingUtils.ShowBusy();            

            final BagView bagView = BagView.getInstance();
            MetsBag metsBag = bagView.getBag();
            Mets mets = metsBag.getMets();
            //Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
            
            metsBag.setBagItMets(new DefaultBagItMets());
            
            Writer bagWriter = null;

            try {
                BagFactory bagFactory = new BagFactory();
                short mode = metsBag.getSerialMode();
                if (mode == DefaultBag.NO_MODE) {
                    bagWriter = new FileSystemWriter(bagFactory);
                } else if (metsBag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    bagWriter = new ZipWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_MODE) {
                    bagWriter = new TarWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_GZ_MODE) {
                    bagWriter = new TarGzWriter(bagFactory);
                } else if (mode == DefaultBag.TAR_BZ2_MODE) {
                    bagWriter = new TarBz2Writer(bagFactory);
                }                               
                
                bagWriter.addProgressListener(this);                
                boolean saveOk = metsBag.write(bagWriter);

                if (!saveOk) {                    
                    String message = Context.getMessage("bag.warning.savingFailed");
                    SwingUtils.ShowError(null,message);
                    log(message);
                } else {
                    String message = Context.getMessage("bag.saved.description");
                    String title = Context.getMessage("bag.saved.title");
                    SwingUtils.ShowMessage(title,message);
                    log(message);
                }
               
                if(metsBag.isSerialized()){
                    if(clearAfterSaving){                       
                        bagView.clearBagHandler.clearExistingBag();
                        setClearAfterSaving(false);
                    }else{
                        if(metsBag.isValidateOnSave()) {
                            bagView.validateBagHandler.validateBag();
                        }                     
                        File bagFile = metsBag.getBagFile();
                        log.info("BagView.openExistingBag: " + bagFile);
                        bagView.openBagHandler.openExistingBag(bagFile);
                        bagView.updateSaveBag();
                    }
                }else{                   
                    bagView.updateManifestPane();
                }                
            }catch(Exception e){
                log(e.getMessage());
                log.debug(e.getMessage());                
            } 
            
            SwingUtils.ShowDone();
            
            return null;
        }   
           
    }    
}