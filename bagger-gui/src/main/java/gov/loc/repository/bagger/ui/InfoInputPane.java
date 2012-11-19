package gov.loc.repository.bagger.ui;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
import ugent.bagger.helper.Context;
import ugent.bagger.panels.AmdSecsPanel;
import ugent.bagger.panels.MetsPanel;

public final class InfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(InfoInputPane.class);    
    private BagInfoForm bagInfoForm;    
    private HierarchicalFormModel infoFormModel;    
    private Mets mets;
    private MetsPanel metsPanel;
    private AmdSecsPanel amdSecsPanel;

    public AmdSecsPanel getAmdSecsPanel() {
        if(amdSecsPanel == null){
            amdSecsPanel = new AmdSecsPanel((ArrayList<AmdSec>)getMets().getAmdSec());
        }
        return amdSecsPanel;
    }

    
    public MetsPanel getMetsPanel(){
        if(metsPanel == null){
            metsPanel = new MetsPanel(getMets());            
        }
        return metsPanel;
    }
    public void setMetsPanel(MetsPanel metsPanel) {
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
    public void resetMets(Mets m){
        setMets(m);
        getMetsPanel().reset(m);
        getAmdSecsPanel().reset((ArrayList<AmdSec>)m.getAmdSec());        
    }
    public BagView getBagView(){
        return BagView.getInstance();        
    }   
    public DefaultBag getDefaultBag() {
        return getBagView().getBag();
    }       
    public BagInfoForm getBagInfoForm() {
        if(bagInfoForm == null){            
            bagInfoForm = new BagInfoForm(
                FormModelHelper.createChildPageFormModel(getInfoFormModel(), null),
                getDefaultBag().getInfo().getFieldMap(),
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
            infoFormModel = FormModelHelper.createCompoundFormModel(getDefaultBag().getInfo());
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
        
        addTab(ApplicationContextUtil.getMessage("bagView.metsTab.label"),getMetsPanel());        
        JComponent bagInfoComponent = getBagInfoForm().getControl();
        bagInfoComponent.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        JScrollPane bagInfoComponentScrollPane = new JScrollPane(bagInfoComponent);
        bagInfoComponentScrollPane.setBorder(null);
        bagInfoComponentScrollPane.getViewport().setOpaque(false);        
        
        addTab(
            Context.getMessage("infoInputPane.tab.details"),
            bagInfoComponentScrollPane
        );                        
        
        addTab("amdSecs",getAmdSecsPanel());
        
    }
    public String verifyForms(){                      
        if(!getBagInfoForm().hasErrors()){
            getBagInfoForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }
        updateBagInfo();        
        return "";
    }    
    public String updateForms(){        
        String messages = verifyForms();        
        createTabbedUiComponentsWithForms();
        update();        
        return messages;
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
    
    public void updateProject(){        
    	getBagView().getInfoFormsPane().updateInfoFormsPane(true);        
    }
    private void updateBagInfo(){              
        getDefaultBag().updateBagInfo(getBagInfoForm().getBagInfoMap());        
    }    
}