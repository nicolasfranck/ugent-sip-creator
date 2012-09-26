package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagTree;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.BagFactory.Version;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;

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
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.close"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.close"));
        dialog.showDialog();
    }
    
    public void clearExistingBag() {
    	newDefaultBag(null);
        BagView bagView = BagView.getInstance();
    	DefaultBag bag = bagView.getBag();
    	bag.clear();        
    	bagView.setBagPayloadTree(new BagTree(AbstractBagConstants.DATA_DIRECTORY, true));
    	bagView.getBagPayloadTreePanel().refresh(bagView.getBagPayloadTree());
    	bagView.setBagTagFileTree(new BagTree(ApplicationContextUtil.getMessage("bag.label.noname"), false));
    	bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
    	bagView.getInfoInputPane().setBagName(bag.getName());
    	bagView.getInfoInputPane().updateInfoForms();
    	bagView.updateClearBag();
    }

    public void newDefaultBag(File f) {
        
    	MetsBag bag = null;
    	String bagName = "";
        BagView bagView = BagView.getInstance();
        
        System.out.println("ClearBagHandler::newDefaultBag('"+f+"')");
        
    	try {            
            System.out.println("version in infoInputPane: "+bagView.getInfoInputPane().getBagVersion());
            //bag = new MetsBag(f,bagView.getInfoInputPane().getBagVersion());            
            bag = new MetsBag(f,Version.V0_96.versionString);            
    	} catch (Exception e) {                        
            e.printStackTrace();
            bag = new MetsBag(f,null);              
    	}
    	if (f == null) {
            bagName = bagView.getPropertyMessage("bag.label.noname");
    	}else{
            bagName = f.getName();
            String fileName = f.getAbsolutePath();
            bagView.getInfoInputPane().setBagName(fileName);
    	}
        bag.setName(bagName);
        bagView.setBag(bag);
    }
    public void setConfirmSaveFlag(boolean confirmSaveFlag) {
        this.confirmSaveFlag = confirmSaveFlag;
    }
    public boolean isConfirmSaveFlag() {
        return confirmSaveFlag;
    }
}