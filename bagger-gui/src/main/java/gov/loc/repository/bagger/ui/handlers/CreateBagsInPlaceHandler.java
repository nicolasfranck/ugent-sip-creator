package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.Profile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.DefaultBagInfo;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.NewBagInPlaceDialog;
import gov.loc.repository.bagger.ui.NewBagsInPlaceDialog;
import gov.loc.repository.bagger.ui.Progress;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.SwingUtils;

//Nicolas Franck: based on code of CreateBagInPlaceHandler, but for creation of multiple bagits at once

public class CreateBagsInPlaceHandler extends AbstractAction implements Progress {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(StartNewBagHandler.class);   

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public CreateBagsInPlaceHandler() {
        super();           
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        execute();
    }

    @Override
    public void execute() {
        BusyIndicator.showAt(SwingUtils.getFrame());
        createBagsInPlace();
        BusyIndicator.clearAt(SwingUtils.getFrame());
    }

    public void createBagsInPlace() {
        BagView bagView = BagView.getInstance();     
        
        NewBagsInPlaceDialog dialog = new NewBagsInPlaceDialog(
            SwingUtils.getFrame(),
            true,
            bagView.getPropertyMessage("bag.frame.newbaginplace")
        );
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.pack();
        dialog.setVisible(true);        
    }

    public void createPreBag(File dataFile, String bagItVersion, final String profileName) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null)) {
            log.info("Creating a new bag in place with data: " + dataFile.getName()
                            + ", version: " + bagItVersion + ", profile: " + profileName);
        }
    	bagView.clearBagHandler.clearExistingBag();
    	try {
            bag.createPreBag(dataFile,bagItVersion);
    	} catch (Exception e) {
    	    bagView.showWarningErrorDialog("Error - bagging in place", "No file or directory selection was made!\n");
            return;
    	}    	
    	
    	String bagFileName = dataFile.getName();
        bag.setName(bagFileName);
        bagView.getInfoFormsPane().setBagName(bagFileName);
        
        setProfile(profileName);
        
        bagView.saveBagHandler.save(dataFile);
    }
    
	/*
     * Prepares the call to Create Bag in Place and 
     * adding .keep files in Empty Pay load Folder(s) 
    */
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion, final String profileName) {
        BagView bagView = BagView.getInstance();
        DefaultBag bag = bagView.getBag();
        
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null)) {
            log.info("Creating a new bag in place with data: " + dataFile.getName()
                            + ", version: " + bagItVersion + ", profile: " + profileName);
        }
    	bagView.clearBagHandler.clearExistingBag();
    	try {
            bag.createPreBagAddKeepFilesToEmptyFolders(dataFile,bagItVersion);
    	} catch (Exception e) {
    	    bagView.showWarningErrorDialog("Error - bagging in place", "No file or directory selection was made!\n");
            return;
    	}    	
    	
    	String bagFileName = dataFile.getName();
        bag.setName(bagFileName);
        bagView.getInfoFormsPane().setBagName(bagFileName);        
        setProfile(profileName);        
        bagView.saveBagHandler.save(dataFile);
    }    
    
    private void setProfile(String selected) {
        BagView bagView = BagView.getInstance();
        Profile profile = bagView.getProfileStore().getProfile(selected);
        log.info("bagProject: " + profile.getName());
        Map<String, String> map = new HashMap<String, String>();
        map.put(DefaultBagInfo.FIELD_LC_PROJECT,profile.getName());
        bagView.getBag().updateBagInfo(map);
    }
}