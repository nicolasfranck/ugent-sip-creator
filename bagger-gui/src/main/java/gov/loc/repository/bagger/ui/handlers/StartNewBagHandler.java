package gov.loc.repository.bagger.ui.handlers;

import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.AbstractAction;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.dialogs.NewBagDialog;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.NewBagParams;

public class StartNewBagHandler extends AbstractAction {
    static final long serialVersionUID = 1L;
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
        
        
        //metsBag.setRootDir(bagView.getBagRootPath());
        metsBag.setFile(null);

    	bagView.getInfoFormsPane().getInfoInputPane().populateForms();
        
        log.error(Context.getMessage("StartNewBagHandler.newBagCreated.label"));
        
    	//ApplicationContextUtil.addConsoleMessage(Context.getMessage("bag.frame.newbaginmemory"));
        
    	bagView.updateNewBag();
    	
    	// set bagItVersion
    	bagView.getInfoFormsPane().getBagVersionValue().setText(params.getVersion());        
        
        //Nicolas Franck: bag-info velden inladen in form blijkbaar niet automatisch (wel na invullen 1ste nieuwe veld)            
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().setFieldMap(
            metsBag.getInfo().getFieldMap()
        );
        bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();
        
        //already done in clearExistingBag
        /*
        //reset mets
        Mets mets = bagView.getInfoFormsPane().getInfoInputPane().getMets();
        try{
            PremisUtils.setPremis(mets);
        }catch(Exception e){
            e.printStackTrace();
        }        
        
        bagView.getInfoFormsPane().getInfoInputPane().resetMets(mets);
        metsBag.setMets(mets);*/
    	        
        SwingUtils.setJComponentEnabled(bagView.getInfoFormsPane().getInfoInputPane().getMdSecPanel().getDmdSecPropertiesPanel().getButtonPanel(),true);                
        
    }    
}