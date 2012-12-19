package gov.loc.repository.bagger.ui.handlers;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.bag.impl.MetsBag;
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
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.NewBagParams;

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
        SwingUtils.centerOnParent(dialog,true);        
        dialog.setVisible(true);             
    }
    public void createNewBag(NewBagParams params) {
        BagView bagView = BagView.getInstance();    	
    	
        try{
            bagView.clearBagHandler.clearExistingBag();
        }catch(Exception e){
            e.printStackTrace();
        }
    	MetsBag metsBag = bagView.getBag();
        
        //Bag-Id      
        BagInfoField field = new BagInfoField();
        field.setLabel("Bag-Id");
        field.setName("Bag-Id");
        field.setValue(params.getBagId());        
        metsBag.getInfo().addField(field);
        
    	bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

    	String bagName = Context.getMessage("bag.label.noname");
        metsBag.setName(bagName);
        bagView.getInfoFormsPane().setBagName(bagName);

        //bagView.setBagTagFileTree(new BagTree(metsBag.getName(), false));
        Collection<BagFile> tags = metsBag.getTags();
        /*
        for (Iterator<BagFile> it=tags.iterator(); it.hasNext(); ) {
            BagFile bf = it.next();
            bagView.getBagTagFileTree().addNode(bf.getFilepath());
        }
        bagView.getBagTagFileTreePanel().refresh(bagView.getBagTagFileTree());*/
        
        metsBag.setRootDir(bagView.getBagRootPath());

    	bagView.getInfoFormsPane().getInfoInputPane().populateForms();
    	ApplicationContextUtil.addConsoleMessage(Context.getMessage("bag.frame.newbaginmemory"));
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoFormsPane().getBagVersionValue().setText(params.getVersion());        
        
        //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)            
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().setFieldMap(
            metsBag.getInfo().getFieldMap()
        );
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();
        
        //reset mets
        Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
        try{
            PremisUtils.setPremis(mets);
        }catch(Exception e){
            e.printStackTrace();
        }        
        bagView.getInfoFormsPane().getInfoInputPane().resetMets(mets);
    	        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMdSecPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);                
        
    }    
}