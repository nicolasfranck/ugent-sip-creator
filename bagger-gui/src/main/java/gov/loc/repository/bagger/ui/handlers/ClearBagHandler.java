package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagTree;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.Context;

public class ClearBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;   
    private boolean confirmSaveFlag = false;

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public ClearBagHandler() {
        super();      
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        closeExistingBag();
    }

    public void closeExistingBag() {
    	// Closes Bag without popping up the Save Dialog Box for a Holey and Serialized Bag 
    	// For all other types of Bags the Save Dialog Box pops up
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
    	if(bag.isHoley() || bag.isSerial()){
            clearExistingBag();
        }else{
            confirmCloseBag();
        }    
        if (isConfirmSaveFlag()){            
            bagView.saveBagHandler.setClearAfterSaving(true);
            bagView.saveBagAsHandler.openSaveBagAsFrame();
            setConfirmSaveFlag(false);            
        }
    }

    private void confirmCloseBag() {
        final BagView bagView = BagView.getInstance();
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                setConfirmSaveFlag(true);
            }
            @Override
            protected void onCancel() {
                super.onCancel();
                clearExistingBag();
            }
        };
        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(Context.getMessage("bag.dialog.title.close"));
        dialog.setConfirmationMessage(Context.getMessage("bag.dialog.message.close"));
        dialog.showDialog();
    }
    
    public void clearExistingBag() {
    	newDefaultBag(null);
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
    	bag.clear();        
    	//bagView.setBagPayloadTree(new BagTree(AbstractBagConstants.DATA_DIRECTORY, true));
        //Nicolas Franck
        bagView.setBagPayloadTree(bagView.createBagPayloadTree(AbstractBagConstants.DATA_DIRECTORY, true));
        
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	bagView.setBagTagFileTree(new BagTree(ApplicationContextUtil.getMessage("bag.label.noname"), false));
    	bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
    	bagView.getInfoFormsPane().setBagName(bag.getName());
    	bagView.getInfoFormsPane().updateInfoForms();
        Mets mets = new Mets();
        bagView.getInfoFormsPane().getInfoInputPane().setMets(mets);
        bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().reset(mets);      
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getButtonPanel(),false);        
    	bagView.updateClearBag();
    }

    public void newDefaultBag(File f) {
        
    	MetsBag bag = null;
    	String bagName = "";
        BagView bagView = BagView.getInstance();
        
        
    	try {                    
            //bag = new MetsBag(f,bagView.getInfoInputPane().getBagVersion());            
            bag = new MetsBag(f,null);            
    	} catch (Exception e) {                        
            e.printStackTrace();            
    	}
    	if (f == null) {
            bagName = Context.getMessage("bag.label.noname");
    	}else{
            bagName = f.getName();
            String fileName = f.getAbsolutePath();
            bagView.getInfoFormsPane().setBagName(fileName);
    	}
        bag.setName(bagName);
        bagView.setBag(bag);
        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);
    }
    public void setConfirmSaveFlag(boolean confirmSaveFlag) {
        this.confirmSaveFlag = confirmSaveFlag;
    }
    public boolean isConfirmSaveFlag() {
        return confirmSaveFlag;
    }
}