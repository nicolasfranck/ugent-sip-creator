package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.Profile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.DefaultBagInfo;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.NewBagInPlaceFrame;
import gov.loc.repository.bagger.ui.Progress;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreateBagInPlaceHandler extends AbstractAction implements Progress {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(StartNewBagHandler.class);   

    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public CreateBagInPlaceHandler() {
        super();           
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createBagInPlace();
    }

    @Override
    public void execute() {
        BagView.getInstance().statusBarEnd();
    }

    public void createBagInPlace() {
        BagView bagView = BagView.getInstance();
    	NewBagInPlaceFrame newBagInPlaceFrame = new NewBagInPlaceFrame(bagView, bagView.getPropertyMessage("bag.frame.newbaginplace"));
        newBagInPlaceFrame.setBag(bagView.getBag());
        newBagInPlaceFrame.setVisible(true);
    }

    public void createPreBag(File dataFile, String bagItVersion, final String profileName) {
        BagView bagView = BagView.getInstance();
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null))
    		log.info("Creating a new bag in place with data: " + dataFile.getName()
    				+ ", version: " + bagItVersion + ", profile: " + profileName);
    	bagView.clearBagHandler.clearExistingBag();
    	try {
    		bagView.getBag().createPreBag(dataFile, bagItVersion);
    	} catch (Exception e) {
    	    bagView.showWarningErrorDialog("Error - bagging in place", "No file or directory selection was made!\n");
    		return;
    	}
    	DefaultBag bag = bagView.getBag();
    	
    	String bagFileName = dataFile.getName();
        bag.setName(bagFileName);
        bagView.infoInputPane.setBagName(bagFileName);
        
        setProfile(profileName);
        
        bagView.saveBagHandler.save(dataFile);
    }
    
	/*
     * Prepares the call to Create Bag in Place and 
     * adding .keep files in Empty Pay load Folder(s) 
    */
    public void createPreBagAddKeepFilesToEmptyFolders(File dataFile, String bagItVersion, final String profileName) {
        BagView bagView = BagView.getInstance();
    	if (((dataFile != null) && (bagItVersion != null)) && (profileName !=null))
    		log.info("Creating a new bag in place with data: " + dataFile.getName()
    				+ ", version: " + bagItVersion + ", profile: " + profileName);
    	bagView.clearBagHandler.clearExistingBag();
    	try {
    		bagView.getBag().createPreBagAddKeepFilesToEmptyFolders(dataFile, bagItVersion);
    	} catch (Exception e) {
    	    bagView.showWarningErrorDialog("Error - bagging in place", "No file or directory selection was made!\n");
    		return;
    	}
    	DefaultBag bag = bagView.getBag();
    	
    	String bagFileName = dataFile.getName();
        bag.setName(bagFileName);
        bagView.infoInputPane.setBagName(bagFileName);        
        setProfile(profileName);        
        bagView.saveBagHandler.save(dataFile);
    }    
    
    private void setProfile(String selected) {
        BagView bagView = BagView.getInstance();
        Profile profile = bagView.getProfileStore().getProfile(selected);
        log.info("bagProject: " + profile.getName());
        Map<String, String> map = new HashMap<String, String>();
        map.put(DefaultBagInfo.FIELD_LC_PROJECT, profile.getName());
        bagView.getBag().updateBagInfo(map);
    }
}
