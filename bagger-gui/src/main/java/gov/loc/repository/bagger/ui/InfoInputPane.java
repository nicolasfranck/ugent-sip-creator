package gov.loc.repository.bagger.ui;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.panels.AmdSecPanel;
import ugent.bagger.panels.MdSecPanel;

public final class InfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(InfoInputPane.class);    
    private BagInfoForm bagInfoForm;    
    private HierarchicalFormModel infoFormModel;    
    private Mets mets;
    private MdSecPanel metsPanel;
    AmdSecPanel amdSecPanel;
    AmdSec amdSecBagit;

    public AmdSec getAmdSecBagit() {
        if(amdSecBagit == null){                
            amdSecBagit = PremisUtils.getAmdSecBagit((ArrayList<AmdSec>)mets.getAmdSec());       
            if(amdSecBagit == null){                
                amdSecBagit = new AmdSec();
                amdSecBagit.setID("bagit");
                getMets().getAmdSec().add(amdSecBagit);
            }
        }
        return amdSecBagit;
    }    
    public AmdSecPanel getAmdSecPanel() {
        if(amdSecPanel == null){            
            amdSecPanel = new AmdSecPanel(getAmdSecBagit());
        }
        return amdSecPanel;
    }
    public MdSecPanel getMdSecPanel(){
        if(metsPanel == null){
            metsPanel = new MdSecPanel((ArrayList<MdSec>)getMets().getDmdSec());            
        }
        return metsPanel;
    }
    public void setMdSecPanel(MdSecPanel metsPanel) {
        this.metsPanel = metsPanel;
    }    
    public Mets getMets() {
        if(mets == null){
            mets = new Mets();
        }
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }
    public void resetMets(Mets mets){
        setMets(mets);
        getMdSecPanel().reset((ArrayList<MdSec>)mets.getDmdSec());
        amdSecBagit = null;        
        getAmdSecPanel().reset(getAmdSecBagit());        
    }
    public BagView getBagView(){
        return BagView.getInstance();        
    }   
    public MetsBag getMetsBag() {
        return getBagView().getBag();
    }       
    public BagInfoForm getBagInfoForm() {
        if(bagInfoForm == null){            
            bagInfoForm = new BagInfoForm(
                FormModelHelper.createChildPageFormModel(getInfoFormModel(), null),
                getMetsBag().getInfo().getFieldMap(),
                false
            );                   
        }
        return bagInfoForm;
    }
    public void setBagInfoForm(BagInfoForm bagInfoForm) {
        this.bagInfoForm = bagInfoForm;
    }     
    public HierarchicalFormModel getInfoFormModel() {
        if(infoFormModel == null){
            infoFormModel = FormModelHelper.createCompoundFormModel(getMetsBag().getInfo());
        }
        return infoFormModel;
    }
    public void setInfoFormModel(HierarchicalFormModel infoFormModel) {        
        this.infoFormModel = infoFormModel;
    }   
    public InfoInputPane(boolean b){
    	populateForms(b);                       
        getInputMap().put(KeyStroke.getKeyStroke("F2"), "tabNext");
        ActionMap am = getActionMap();
        am.put("tabNext", new AbstractAction("tabNext") {
            private static final long serialVersionUID = 1L;
            @Override
            public void actionPerformed(ActionEvent evt) {                
                try {
                    int selected = getSelectedIndex();
                    int count = getComponentCount();
                    if (selected >= 0 && selected < count-1) {
                        setSelectedIndex(selected+1);
                    } else {
                        setSelectedIndex(0);
                    }
                    invalidate();
                    repaint();
                }catch (Exception e){
                    logger.error(e);                    
                }
            }
        });
        setActionMap(am);                
    }
    public void enableForms(boolean b) {              
    	getBagInfoForm().getControl().invalidate();
    	setEnabled(b);
    	invalidate();
    }    
    public void populateForms(boolean enabled){    	                    	
        createTabbedUiComponentsWithForms();        
    }
    // Create a tabbed pane for the information forms and checkbox panel
    private void createTabbedUiComponentsWithForms() {                
        
        //revalidate
        removeAll();     
        validate();        
        
        //add tabs  
        addTab(
            Context.getMessage("infoInputPane.baginfoTab.label"),  
            null,
            getBagInfoForm().getControl(),
            Context.getMessage("infoInputPane.baginfoTab.tooltip")
        );
        addTab(
            Context.getMessage("infoInputPane.dmdSecTab.label"),
            null,
            getMdSecPanel(),
            Context.getMessage("infoInputPane.dmdSecTab.tooltip")
        );                              
        addTab(
            Context.getMessage("infoInputPane.amdSecTab.label"),
            null,
            getAmdSecPanel(),
            Context.getMessage("infoInputPane.amdSecTab.tooltip")                
        );
        
    }
    public void verifyForms(){                      
        if(!getBagInfoForm().hasErrors()){
            getBagInfoForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }
        updateBagInfo();              
    }    
    public void updateForms(){        
        verifyForms();        
        createTabbedUiComponentsWithForms();
        update();                
    }    
    public void update(){       
        Component[] components = getBagInfoForm().getControl().getComponents();
        for (Component c:components) {            
            c.invalidate();
            c.repaint();
        }
        getBagInfoForm().getControl().invalidate();        
    	invalidate();
    	repaint();        
    }    
    private void updateBagInfo(){                      
        getMetsBag().updateBagInfo(getBagInfoForm().getFieldMap());        
    }    
}