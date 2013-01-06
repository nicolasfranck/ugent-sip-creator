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
    private boolean clearAfterSaving = false;
    private String messages;
    
    public SaveBagHandler() {
        super();        
    }
    //wordt uitgevoerd indien men op de toolbarbutton drukt
    @Override
    public void actionPerformed(ActionEvent e) {
        File file = BagView.getInstance().getBag().getFile();        
        if(file != null){
            if(file.exists()) {            
                confirmWriteBag();
            }else {
                saveBag(file);
            }
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
    public void setClearAfterSaving(boolean b) {
        this.clearAfterSaving = b;
    }

    public boolean getClearAfterSaving() {
        return this.clearAfterSaving;
    }

    public void saveBag(File file) {        
    	DefaultBag bag = BagView.getInstance().getBag();
        bag.setFile(file);        
        execute();
    }

    public void confirmWriteBag() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            boolean isCancel = true;
            @Override
            protected void onConfirm() {                
                MetsBag metsBag = bagView.getBag();
                if(metsBag.getSize() > DefaultBag.MAX_SIZE) {
                    confirmAcceptBagSize();
                } else {                                        
                    saveBag(metsBag.getFile());
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
            protected void onConfirm(){                
                saveBag(bagView.getBag().getFile());
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }
    public void save(File file) {
        BagView bagView = BagView.getInstance();
    	MetsBag metsBag = bagView.getBag();       
        metsBag.setName(file.getName());        
        
    	if(file.exists()){            
            confirmWriteBag();
    	} else {
            if(metsBag.getSize() > DefaultBag.MAX_SIZE){                
                confirmAcceptBagSize();
            }else{                                
                saveBag(file);
            }
    	}        
    }
    
    private class SaveBagWorker extends LongTask{
        boolean saveOk = false;
        final BagView bagView = BagView.getInstance();
        MetsBag metsBag = bagView.getBag();
        
        @Override
        protected Object doInBackground() throws Exception {
            
            Mets mets = metsBag.getMets();
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
                saveOk = metsBag.write(bagWriter);

                                
            }catch(Exception e){                    
                //log(e.getMessage());
                log.error(e.getMessage());                
            }             
            
            return null;
        }   
        @Override
        public void done(){            
            super.done();
            
            if (!saveOk) {                    
                String message = Context.getMessage("bag.warning.savingFailed");
                SwingUtils.ShowError(null,message);
                log.error(message);
                //log(message);
            } else {
                String message = Context.getMessage("bag.saved.description");
                String title = Context.getMessage("bag.saved.title");
                SwingUtils.ShowMessage(title,message);                
                //log(message);
            }

            if(metsBag.isSerialized()){
                if(clearAfterSaving){         
                    try{
                        bagView.clearBagHandler.clearExistingBag();
                    }catch(Exception e){                        
                        log.error(e);
                    }                    
                    setClearAfterSaving(false);
                }else{
                    if(metsBag.isValidateOnSave()) {
                        bagView.validateBagHandler.validateBag();
                    }                     
                    File bagFile = metsBag.getFile();
                    log.info("BagView.openExistingBag: " + bagFile);
                    bagView.openBagHandler.openExistingBag(bagFile);
                    bagView.updateSaveBag();
                }
            }else{                   
                bagView.updateManifestPane();
            }
        }
           
    }    
}