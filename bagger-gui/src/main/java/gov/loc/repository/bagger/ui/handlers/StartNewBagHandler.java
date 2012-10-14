package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.Profile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagTree;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.NewBagDialog;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.SwingUtils;

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
        NewBagDialog dialog = new NewBagDialog(
            SwingUtils.getFrame(), 
            true, 
            bagView.getPropertyMessage("bag.frame.new")
        );
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);               
        dialog.setLocationRelativeTo(SwingUtils.getFrame());         
        dialog.pack();
        dialog.setVisible(true);
        
        //Nicolas Franck
        /*
                
        NewBagFrame newBagFrame = new NewBagFrame(bagView,bagView.getPropertyMessage("bag.frame.new"));
        newBagFrame.setVisible(true);        
        newBagFrame.pack();*/        
    }

    public void createNewBag(String bagItVersion, String profileName) {
        BagView bagView = BagView.getInstance();
    	log.info("Creating a new bag with version: " + bagItVersion + ", profile: " + profileName);
    	
    	bagView.clearBagHandler.clearExistingBag();
    	DefaultBag bag = bagView.getBag();
    	bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

    	String bagName = bagView.getPropertyMessage("bag.label.noname");
        bag.setName(bagName);
        bagView.getInfoFormsPane().setBagName(bagName);

        bagView.setBagTagFileTree(new BagTree(bag.getName(), false));
        Collection<BagFile> tags = bag.getTags();
        for (Iterator<BagFile> it=tags.iterator(); it.hasNext(); ) {
            BagFile bf = it.next();
            bagView.getBagTagFileTree().addNode(bf.getFilepath());
        }
        bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
        bagView.updateBaggerRules();
        bag.setRootDir(bagView.getBagRootPath());

    	bagView.getInfoFormsPane().getInfoInputPane().populateForms(true);
    	ApplicationContextUtil.addConsoleMessage("A new bag has been created in memory.");
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoFormsPane().getBagVersionValue().setText(bagItVersion);
    	
    	// change profile
    	changeProfile(profileName);
        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);                
        
    }
    
    // TODO refactor
    private void changeProfile(String selected) {
        BagView bagView = BagView.getInstance();
    	Profile profile = bagView.getProfileStore().getProfile(selected);        
        DefaultBag bag = bagView.getBag();
        bag.setProfile(profile, true);        
        bagView.getInfoFormsPane().getInfoInputPane().updateProject();        
        bagView.getInfoFormsPane().setProfile(bag.getProfile().getName());
    }
}