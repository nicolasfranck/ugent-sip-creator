package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.Profile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagTree;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.NewBagFrame;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StartNewBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(StartNewBagHandler.class);
    
    /*
     * Nicolas Franck: public <init>(BagView bagView)
     * removed, because BagView instance is available in BagView.getInstance()
     */
    public StartNewBagHandler() {
        super();       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        newBag();
    }

    public void newBag() {
        BagView bagView = BagView.getInstance();
    	NewBagFrame newBagFrame = new NewBagFrame(bagView, bagView.getPropertyMessage("bag.frame.new"));
        newBagFrame.setVisible(true);
    }

    public void createNewBag(String bagItVersion, String profileName) {
        BagView bagView = BagView.getInstance();
    	log.info("Creating a new bag with version: " + bagItVersion + ", profile: " + profileName);
    	
    	bagView.clearBagHandler.clearExistingBag();
    	DefaultBag bag = bagView.getBag();
    	bagView.getInfoInputPane().getBagInfoInputPane().enableForms(true);

    	String bagName = bagView.getPropertyMessage("bag.label.noname");
        bag.setName(bagName);
        bagView.getInfoInputPane().setBagName(bagName);

        bagView.setBagTagFileTree(new BagTree(bagView, bag.getName(), false));
        Collection<BagFile> tags = bag.getTags();
        for (Iterator<BagFile> it=tags.iterator(); it.hasNext(); ) {
            BagFile bf = it.next();
            bagView.getBagTagFileTree().addNode(bf.getFilepath());
        }
        bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
        bagView.updateBaggerRules();
        bag.setRootDir(bagView.getBagRootPath());

    	bagView.getInfoInputPane().getBagInfoInputPane().populateForms(true);
    	ApplicationContextUtil.addConsoleMessage("A new bag has been created in memory.");
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoInputPane().getBagVersionValue().setText(bagItVersion);
    	
    	// change profile
    	changeProfile(profileName);
    }
    
    // TODO refactor
    private void changeProfile(String selected) {
        BagView bagView = BagView.getInstance();
    	Profile profile = bagView.getProfileStore().getProfile(selected);        
        DefaultBag bag = bagView.getBag();
        bag.setProfile(profile, true);
        bagView.getInfoInputPane().getBagInfoInputPane().updateProject();        
        bagView.getInfoInputPane().setProfile(bag.getProfile().getName());
    }
}