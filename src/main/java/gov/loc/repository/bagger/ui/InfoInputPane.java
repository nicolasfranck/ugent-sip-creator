package gov.loc.repository.bagger.ui;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.PremisUtils;
import ugent.bagger.panels.MdSecPanel;

public final class InfoInputPane extends JTabbedPane {
    
    static final Log log = LogFactory.getLog(InfoInputPane.class);    
    BagInfoForm bagInfoForm;    
    HierarchicalFormModel infoFormModel;    
    Mets mets;
    MdSecPanel metsPanel;    
    
    public MdSecPanel getMdSecPanel(){
        if(metsPanel == null){
            metsPanel = new MdSecPanel((ArrayList<MdSec>)getMets().getDmdSec(),"");            
        }
        return metsPanel;
    }
    public void setMdSecPanel(MdSecPanel metsPanel) {
        this.metsPanel = metsPanel;
    }    
    public Mets getMets() {
        if(mets == null){            
            mets = new Mets();
            try{
                PremisUtils.setPremis(mets);
            }catch(Exception e){
                log.error(e.getMessage());              
            }            
        }
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }
    public void resetMets(Mets mets){        
        setMets(mets);
        getMdSecPanel().reset((ArrayList<MdSec>)mets.getDmdSec());        
        getBagView().getBag().setMets(mets);
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
    public InfoInputPane(){
    	populateForms();                       
        getInputMap().put(KeyStroke.getKeyStroke("F2"), "tabNext");
        ActionMap am = getActionMap();
        am.put("tabNext", new AbstractAction("tabNext") {            
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
                    log.error(e.getMessage());                 
                }
            }
        });
        setActionMap(am);                
    }
    public void enableForms(boolean b){                  	
    	setEnabled(b);        
        getBagInfoForm().setEnabled(b);
        getMdSecPanel().setEnabled(b);        
    	invalidate();
    }    
    public void populateForms(){    	                    	
        createTabbedUiComponentsWithForms();        
    }
    // Create a tabbed pane for the information forms and checkbox panel
    void createTabbedUiComponentsWithForms() {                
        
        //revalidate
        removeAll();     
        validate();                
        
        //add tabs
        addTab(
            Context.getMessage("infoInputPane.dmdSecTab.label"),
            null,
            getMdSecPanel(),
            Context.getMessage("infoInputPane.dmdSecTab.tooltip")
        );
        addTab(
            Context.getMessage("infoInputPane.baginfoTab.label"),  
            null,
            getBagInfoForm().getControl(),
            Context.getMessage("infoInputPane.baginfoTab.tooltip")
        );                       
        
        Dimension mdim = new Dimension(550,400);        
        setPreferredSize(mdim);
        setMinimumSize(mdim);        
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
    void updateBagInfo(){                      
        getMetsBag().updateBagInfo(getBagInfoForm().getFieldMap());        
    }    
}