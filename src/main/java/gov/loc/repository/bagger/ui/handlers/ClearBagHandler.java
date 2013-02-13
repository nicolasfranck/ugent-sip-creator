package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.InfoFormsPane;
import gov.loc.repository.bagger.ui.InfoInputPane;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import ugent.bagger.exceptions.BagFetchForbiddenException;
import ugent.bagger.exceptions.BagNoBagDirException;
import ugent.bagger.exceptions.BagNoDataException;
import ugent.bagger.exceptions.BagUnknownFormatException;
import ugent.bagger.helper.BagitUtils;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.params.BagError;
import ugent.bagger.params.BagErrorNoBagDir;

public class ClearBagHandler extends AbstractAction {
    static final Log log = LogFactory.getLog(ClearBagHandler.class);    
    boolean confirmSaveFlag = false;
    
    public ClearBagHandler() {
        super();      
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            closeExistingBag();
        }catch(BagUnknownFormatException ex) {
            log.error(ex.getMessage());
        }catch(BagNoBagDirException ex){
            log.error(ex.getMessage());
        }catch(BagFetchForbiddenException ex){
            log.error(ex.getMessage());
        }catch(BagNoDataException ex){
            log.error(ex.getMessage());
        }
    }

    public void closeExistingBag() throws BagUnknownFormatException, BagNoBagDirException, BagFetchForbiddenException, BagNoDataException {
    	// Closes Bag without popping up the Save Dialog Box for a Holey and Serialized Bag 
    	// For all other types of Bags the Save Dialog Box pops up
        BagView bagView = BagView.getInstance();
        MetsBag metsBag = bagView.getBag();
        
        if(metsBag.getFile() != null){
            log.error(
                Context.getMessage(
                    "ClearBagHandler.closeExistingBag.label",
                    new Object []{metsBag.getFile()}                
                )
            );
        }else{
            log.error(
                Context.getMessage(
                    "ClearBagHandler.closeNewBag.label",
                    new Object []{}                
                )
            );
        }
        
        
    	if(!metsBag.isChanged()){
            clearExistingBag();
        }else {
            confirmCloseBag();
        }
        if (isConfirmSaveFlag()){          
            bagView.saveBagHandler.setClearAfterSaving(true);
            if(metsBag.getFile() != null && metsBag.getFile().exists()){
                bagView.saveBagHandler.saveBag(metsBag.getFile());       
            }else{                
                bagView.saveBagAsHandler.openSaveBagAsFrame();
            }            
            setConfirmSaveFlag(false);            
        }
    }

    public void confirmCloseBag() {
        final BagView bagView = BagView.getInstance();
       
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                setConfirmSaveFlag(true);
            }
            @Override
            protected void onCancel() {
                super.onCancel();
                try{                   
                    clearExistingBag();                    
                } catch (BagUnknownFormatException ex) {
                    log.error(ex.getMessage());
                }catch(BagNoBagDirException ex){
                    log.error(ex.getMessage());
                }catch(BagFetchForbiddenException e){
                    log.error(e.getMessage());
                }catch(BagNoDataException e){
                    log.error(e.getMessage());
                }
            }
        };
        
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.close"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.close"));        
        dialog.showDialog();
    }
    
    public void clearExistingBag() throws BagUnknownFormatException, BagNoBagDirException, BagFetchForbiddenException, BagNoDataException {
       
    	newDefaultBag(null);
        
        BagView bagView = BagView.getInstance();
    	MetsBag metsBag = bagView.getBag();
        InfoFormsPane infoFormsPane = bagView.getInfoFormsPane();
        InfoInputPane infoInputPane = infoFormsPane.getInfoInputPane();
        
        bagView.setBagPayloadTree(bagView.createBagPayloadTree(AbstractBagConstants.DATA_DIRECTORY, true));        
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	//bagView.setBagTagFileTree(new BagTree(ApplicationContextUtil.getMessage("bag.label.noname"), false));
    	//bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
    	infoFormsPane.setBagName(metsBag.getName());        
        
        infoInputPane.getBagInfoForm().setFieldMap(
            bagView.getBag().getInfo().getFieldMap()
        );
        infoInputPane.getBagInfoForm().resetFields();
        
    	infoFormsPane.updateInfoForms();
        
        Mets mets = new Mets();
        infoInputPane.resetMets(mets);  
        metsBag.setMets(mets);
        
        try{
            PremisUtils.setPremis(mets);
        }catch(Exception e){
            log.error(e.getMessage());            
        }
        infoInputPane.enableForms(false);
    	bagView.updateClearBag();
    }

    public void newDefaultBag(File f) throws BagUnknownFormatException, BagNoBagDirException, BagFetchForbiddenException, BagNoDataException {
        
    	MetsBag bag = null;
    	String bagName = "";
        BagView bagView = BagView.getInstance();        
        
    	try {               
            bag = new MetsBag(f,null);            
    	}catch(BagNoDataException e){
            throw e;
        }catch(BagFetchForbiddenException e){
            throw e;
        }catch(Exception e){     
            String message = e.getMessage();
            log.error(message);            
            BagError error = BagitUtils.parseBagError(message);
            if(error instanceof BagErrorNoBagDir){
                throw new BagNoBagDirException(f);
            }                        
    	}
        
    	if(f == null) {            
            bagName = Context.getMessage("bag.label.noname");
    	}else{
            bagName = f.getName();
            String fileName = f.getAbsolutePath();
            bagView.getInfoFormsPane().setBagName(fileName);
    	}                
        
        //indien bag == null, dan is er iets fout met het formaat van het bestand!
        if(bag != null){
            bag.setName(bagName);
            bagView.setBag(bag);                    
        }else{          
            throw new BagUnknownFormatException(f);
        }        
    }
    public void setConfirmSaveFlag(boolean confirmSaveFlag) {
        this.confirmSaveFlag = confirmSaveFlag;
    }
    public boolean isConfirmSaveFlag() {
        return confirmSaveFlag;
    }
}