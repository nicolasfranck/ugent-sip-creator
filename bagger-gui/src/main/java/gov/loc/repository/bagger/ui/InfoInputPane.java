package gov.loc.repository.bagger.ui;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
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
                    e.printStackTrace();
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
        //getBagInfoForm().getControl().setToolTipText(getBagView().getPropertyMessage("infoinputpane.tab.details.help"));
        addTab(getBagView().getPropertyMessage("infoInputPane.tab.details"),getBagInfoForm().getControl());
        //getProfileForm().getControl().setToolTipText("Profile Form");
        
        //Nicolas Franck
        /* vreemd. ProfileForm wordt hier nergens gebruikt. Waarom bestaat dit?
         * 
         */
        //addTab(getBagView().getPropertyMessage("newProfileWizard.title"),getProfileForm().getControl());        
        
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
        for (int i=0; i<components.length; i++) {
            Component c = components[i];
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