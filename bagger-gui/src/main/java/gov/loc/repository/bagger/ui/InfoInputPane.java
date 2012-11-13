package gov.loc.repository.bagger.ui;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
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

/*
 * Nicolas Franck: renamed from "BagInfoInputPane" to "InfoInputPane"
 */
public final class InfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(InfoInputPane.class);
    //Nicolas Franck: profile niet nuttig
    //private BaggerProfile bagProfile;
    //private OrganizationProfileForm profileForm;
    //private HierarchicalFormModel profileFormModel;
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
    //Nicolas Franck: profile niet nuttig
    /*
    public BaggerProfile getBagProfile() {
        if (bagProfile == null) {
            bagProfile = new BaggerProfile();
        }
        return bagProfile;
    }
    public void setBagProfile(BaggerProfile bagProfile) {
        this.bagProfile = bagProfile;
    }*/
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
    //Nicolas Franck: profile niet nuttig
    /*
    public OrganizationProfileForm getProfileForm() {
        if(profileForm == null){
            profileForm = new OrganizationProfileForm(FormModelHelper.createChildPageFormModel(getProfileFormModel(), null));    	
        }
        return profileForm;
    }
    public void setProfileForm(OrganizationProfileForm profileForm) {
        this.profileForm = profileForm;
    }*/
    public HierarchicalFormModel getInfoFormModel() {
        if(infoFormModel == null){
            infoFormModel = FormModelHelper.createCompoundFormModel(getDefaultBag().getInfo());
        }
        return infoFormModel;
    }
    public void setInfoFormModel(HierarchicalFormModel infoFormModel) {        
        this.infoFormModel = infoFormModel;
    }
    //Nicolas Franck: profile niet nuttig
    /*
    public HierarchicalFormModel getProfileFormModel() {
        if(profileFormModel == null){
            profileFormModel = FormModelHelper.createCompoundFormModel(getBagProfile());
        }
        return profileFormModel;
    }
    public void setProfileFormModel(HierarchicalFormModel profileFormModel) {
        this.profileFormModel = profileFormModel;
    }*/
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
        //Nicolas Franck: profile niet nuttig
    	/*getProfileForm().setEnabled(b);
    	getProfileForm().getControl().invalidate();
    	getBagInfoForm().setEnabled(b);*/
    	getBagInfoForm().getControl().invalidate();
    	setEnabled(b);
    	invalidate();
    }    
    public void populateForms(boolean enabled){    	                
    	/*getBagProfile().setOrganization(getDefaultBag().getInfo().getBagOrganization());                
    	getBagProfile().setToContact(getDefaultBag().getInfo().getToContact());*/    	    	        
        createTabbedUiComponentsWithForms();        
    }

    // Create a tabbed pane for the information forms and checkbox panel
    private void createTabbedUiComponentsWithForms() {                
        removeAll();     
        validate();
        
        //Nicolas Franck
        addTab(ApplicationContextUtil.getMessage("bagView.metsTab.label"),getMetsPanel());        
       
        
        //Nicolas Franck: profile niet nuttig
        //bag-info
        //setName("Profile");
        //getBagInfoForm().getControl().setToolTipText(Context.getMessage("infoinputpane.tab.details.help"));
        JComponent bagInfoComponent = getBagInfoForm().getControl();
        bagInfoComponent.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        JScrollPane bagInfoComponentScrollPane = new JScrollPane(bagInfoComponent);
        bagInfoComponentScrollPane.setBorder(null);
        bagInfoComponentScrollPane.getViewport().setOpaque(false);
        addTab(
            Context.getMessage("infoInputPane.tab.details"),
            bagInfoComponentScrollPane
        );
        
        System.out.println("amdSec found: "+getMets().getAmdSec());
        System.out.println("amdSec.size found: "+getMets().getAmdSec().size());
        
        try{
            new MetsWriter().writeToOutputStream(getMets(),System.out);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        addTab("amdSecs",getAmdSecsPanel());
        
        //getProfileForm().getControl().setToolTipText("Profile Form");
        
        //Nicolas Franck
        /* vreemd. ProfileForm wordt hier nergens gebruikt. Waarom bestaat dit?
         * 
         */
        //addTab(Context.getMessage("newProfileWizard.title"),getProfileForm().getControl());        
        
    }
    public String verifyForms(){        
        /*        
        if(!getProfileForm().hasErrors()){            
            getProfileForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }*/        
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
        //getProfileForm().getControl().invalidate();        
    	invalidate();
    	repaint();        
    }
    
    public void updateProject(){        
    	getBagView().getInfoFormsPane().updateInfoFormsPane(true);        
    }
    private void updateBagInfo(){              
        getDefaultBag().updateBagInfo(getBagInfoForm().getBagInfoMap());        
    }
    /*
     * Nicolas Franck: evil! Bij meerdere tabs wordt een focus gevraagd op een onderdeel van tab 1!
     */
    /*
    @Override
    public void requestFocus(){        
        System.out.println("BagInfoInputPane::requestFocus");
    	getBagInfoForm().getControl().requestFocus();        
    }*/
}