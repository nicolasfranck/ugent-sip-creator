package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.BagFile;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.dialogs.NewBagDialog;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.NewBagParams;

public class StartNewBagHandler extends AbstractAction {   
    static final Log log = LogFactory.getLog(StartNewBagHandler.class);
    
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
            //clear payloads, tags, mets and premis
            bagView.clearBagHandler.clearExistingBag();
        }catch(Exception e){
            log.error(e.getMessage());
        }
    	MetsBag metsBag = bagView.getBag();
        
        //Bag-Id      
        BagInfoField field = new BagInfoField();
        field.setLabel("Bag-Id");
        field.setName("Bag-Id");
        field.setValue(params.getBagId());        
        metsBag.getInfo().addField(field);
        
        SwingUtils.getStatusBar().setMessage(
            Context.getMessage("StatusBar.newBag.message",new Object []{params.getBagId()})
        );
        
        //default fields
        ArrayList<String> list = (ArrayList<String>) Beans.getBean("baginfoStandardFields");
        for(String key:list){
            BagInfoField f = new BagInfoField();
            f.setLabel(key);
            f.setName(key);
            f.setValue("");
            metsBag.getInfo().addField(f);            
        }
        
    	bagView.getInfoFormsPane().getInfoInputPane().enableForms(true);

    	String bagName = Context.getMessage("bag.label.noname");
        metsBag.setName(bagName);
        bagView.getInfoFormsPane().setBagName(bagName);

        //bagView.setBagTagFileTree(new BagTree(metsBag.getName(), false));
        Collection<BagFile> tags = metsBag.getTags();       
        
        metsBag.setFile(null);
        metsBag.setChanged(false);

    	bagView.getInfoFormsPane().getInfoInputPane().populateForms();
        
        log.error(Context.getMessage("StartNewBagHandler.newBagCreated.label",new Object []{params.getBagId()}));
        
        
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoFormsPane().getBagVersionValue().setText(params.getVersion());        
        
        //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)            
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().setFieldMap(
            metsBag.getInfo().getFieldMap()
        );
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();       
        
    	        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMdSecPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);                
        
    }    
}