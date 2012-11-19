package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.BagTree;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.dialogs.NewBagDialog;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;

public class StartNewBagHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(StartNewBagHandler.class);
    
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
            true            
        );        
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);               
        dialog.setLocationRelativeTo(SwingUtils.getFrame());         
        SwingUtils.centerAt(SwingUtils.getFrame(),dialog);
        dialog.pack();
        dialog.setVisible(true);             
    }
    public void createNewBag(String bagItVersion) {
        BagView bagView = BagView.getInstance();    	
    	
        try{
            bagView.clearBagHandler.clearExistingBag();
        }catch(Exception e){
            e.printStackTrace();
        }
    	DefaultBag bag = bagView.getBag();
    	bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

    	String bagName = Context.getMessage("bag.label.noname");
        bag.setName(bagName);
        bagView.getInfoFormsPane().setBagName(bagName);

        bagView.setBagTagFileTree(new BagTree(bag.getName(), false));
        Collection<BagFile> tags = bag.getTags();
        for (Iterator<BagFile> it=tags.iterator(); it.hasNext(); ) {
            BagFile bf = it.next();
            bagView.getBagTagFileTree().addNode(bf.getFilepath());
        }
        bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());
        
        bag.setRootDir(bagView.getBagRootPath());

    	bagView.getInfoFormsPane().getInfoInputPane().populateForms(true);
    	ApplicationContextUtil.addConsoleMessage(Context.getMessage("bag.frame.newbaginmemory"));
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoFormsPane().getBagVersionValue().setText(bagItVersion);
    	        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);                
        
    }    
}