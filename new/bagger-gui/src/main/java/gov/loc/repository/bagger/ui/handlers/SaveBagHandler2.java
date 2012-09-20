package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.FileSec;
import com.anearalone.mets.FileSec.FileGrp;
import com.anearalone.mets.FileSec.FileGrp.File.FLocat;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFile;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.FUtils;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.workers.Handler;
import ugent.bagger.workers.LongTask2;

public class SaveBagHandler2 extends Handler {
    private static final Log log = LogFactory.getLog(SaveBagHandler2.class);
    private static final long serialVersionUID = 1L;    
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;
    
    public SaveBagHandler2() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
        bagView.getInfoInputPane().getUpdateBagHandler().updateBag();
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
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.create"));
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
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.accept"));
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
    	fs.addChoosableFileFilter(bagView.getInfoInputPane().getNoFilter());
    	fs.addChoosableFileFilter(bagView.getInfoInputPane().getZipFilter());
        fs.addChoosableFileFilter(bagView.getInfoInputPane().getTarFilter());
        fs.setDialogTitle("Save Bag As");
    	fs.setCurrentDirectory(bag.getRootDir());
    	if (bag.getName() != null && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label.noname"))) {
            String selectedName = bag.getName();
            if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                selectedName += "."+DefaultBag.ZIP_LABEL;
                fs.setFileFilter(bagView.getInfoInputPane().getZipFilter());
            }
            else if (bag.getSerialMode() == DefaultBag.TAR_MODE) {
                selectedName += "."+DefaultBag.TAR_LABEL;
                fs.setFileFilter(bagView.getInfoInputPane().getTarFilter());
            }
            else {
                fs.setFileFilter(bagView.getInfoInputPane().getNoFilter());
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
        bagView.infoInputPane.setBagName(fileName);
        bagView.getControl().invalidate();
    }
    
    private class SaveBagWorker extends LongTask2{
        @Override
        protected Object doInBackground() throws Exception {
            
            BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());                        

            final BagView bagView = BagView.getInstance();
            DefaultBag bag = bagView.getBag(); 
            
            //write mets before creation bag
            try{              
                
                Mets mets = bagView.getInfoInputPane().getBagInfoInputPane().getMets();            
                FileSec fileSec = mets.getFileSec();
                if(fileSec == null){
                    fileSec = new FileSec();
                    mets.setFileSec(fileSec);
                }                       
                List<FileGrp> fileGroups = fileSec.getFileGrp();            
                fileGroups.clear();
                FileGrp fileGroup = new FileGrp();            
                fileGroup.setID("DATASTREAMS");
                List<FileSec.FileGrp.File>files = fileGroup.getFile();            
                int i = 0;
                for(BagFile bagFile:bag.getPayload()){                                       
                    FileSec.FileGrp.File metsFile = new FileSec.FileGrp.File("DS."+i);                                        
                    metsFile.setSIZE(bagFile.getSize());
                    metsFile.setMIMETYPE(FUtils.getMimeType(new File(bagFile.getFilepath())));
                    metsFile.setCHECKSUM(bag.getPayloadManifestAlgorithm());
                    FLocat flocat = new FLocat();
                    flocat.setXlinkHREF(bagFile.getFilepath());                 
                    metsFile.getFLocat().add(flocat);
                    files.add(metsFile);
                    log.debug("adding file to fileGrp: "+metsFile);
                    i++;
                }
                fileGroups.add(fileGroup);            
                File tempFile = new File(System.getProperty("java.io.tmpdir")+"/mets.xml");                
                tempFile.deleteOnExit();                                                            
                MetsUtils.writeMets(mets,new FileOutputStream(tempFile));                        
                bag.addTagFile(tempFile);   
                
            }catch(Exception e){
                log.debug(e.getMessage());                
            }
            
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
                    if (clearAfterSaving){                       
                        bagView.clearBagHandler.clearExistingBag();
                        setClearAfterSaving(false);
                    } else {
                        if (bag.isValidateOnSave()) {
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