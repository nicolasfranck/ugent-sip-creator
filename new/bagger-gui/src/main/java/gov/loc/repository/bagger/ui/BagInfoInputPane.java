package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.bag.BaggerProfile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;

public final class BagInfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(BagInfoInputPane.class);

    private BagView bagView;
    private DefaultBag defaultBag;
    private BaggerProfile bagProfile;
    private BagInfoForm bagInfoForm = null;
    private OrganizationProfileForm profileForm = null;
    private HierarchicalFormModel infoFormModel = null;
    private HierarchicalFormModel profileFormModel = null;

    public BagView getBagView(){
        if(bagView == null){
            bagView = BagView.getInstance();
        }
        return bagView;
    }   
    public DefaultBag getDefaultBag() {
        if(defaultBag == null){
            defaultBag = getBagView().getBag();
        }
        return defaultBag;
    }
    public void setDefaultBag(DefaultBag defaultBag) {
        this.defaultBag = defaultBag;
    }
    public BaggerProfile getBagProfile() {
        if (bagProfile == null) {
            bagProfile = new BaggerProfile();
        }
        return bagProfile;
    }
    public void setBagProfile(BaggerProfile bagProfile) {
        this.bagProfile = bagProfile;
    }
    public BagInfoForm getBagInfoForm() {
        if(bagInfoForm == null){            
            bagInfoForm = new BagInfoForm(FormModelHelper.createChildPageFormModel(getInfoFormModel(), null), getBagView(),getDefaultBag().getInfo().getFieldMap(),false);       
        }
        return bagInfoForm;
    }
    public void setBagInfoForm(BagInfoForm bagInfoForm) {
        this.bagInfoForm = bagInfoForm;
    }    
    public OrganizationProfileForm getProfileForm() {
        if(profileForm == null){
            profileForm = new OrganizationProfileForm(FormModelHelper.createChildPageFormModel(getProfileFormModel(), null),getBagView());    	
        }
        return profileForm;
    }
    public void setProfileForm(OrganizationProfileForm profileForm) {
        this.profileForm = profileForm;
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
    public HierarchicalFormModel getProfileFormModel() {
        if(profileFormModel == null){
            profileFormModel = FormModelHelper.createCompoundFormModel(getBagProfile());
        }
        return profileFormModel;
    }
    public void setProfileFormModel(HierarchicalFormModel profileFormModel) {
        this.profileFormModel = profileFormModel;
    }   

    public BagInfoInputPane(boolean b){
    	populateForms(b); 
        InputMap im = this.getInputMap();
        im.put(KeyStroke.getKeyStroke("F2"), "tabNext");
        ActionMap am = this.getActionMap();
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
                }catch (Exception e){}
            }
        });
        setActionMap(am);
    }

    public void enableForms(boolean b) {
    	getProfileForm().setEnabled(b);
    	getProfileForm().getControl().invalidate();
    	getBagInfoForm().setEnabled(b);
    	getBagInfoForm().getControl().invalidate();
    	setEnabled(b);
    	invalidate();
    }
    
    public void populateForms(boolean enabled){    	        
    	getBagProfile().setOrganization(getDefaultBag().getInfo().getBagOrganization());                
    	getBagProfile().setToContact(getDefaultBag().getInfo().getToContact());    	    	
        createTabbedUiComponentsWithForms();
    }

    // Create a tabbed pane for the information forms and checkbox panel
    private void createTabbedUiComponentsWithForms() {
        removeAll();     
        validate();
        setName("Profile");
        getBagInfoForm().getControl().setToolTipText(getBagView().getPropertyMessage("infoinputpane.tab.details.help"));
        addTab(getBagView().getPropertyMessage("infoInputPane.tab.details"),getBagInfoForm().getControl());
        getProfileForm().getControl().setToolTipText("Profile Form");
        
        //Nicolas Franck
        addTab("mets",new ugent.bagger.panels.MetsPanel(new com.anearalone.mets.Mets()));
        
        //Nicolas Franck
        /* vreemd. ProfileForm wordt hier nergens gebruikt, en indien wel, dan loopt het vast. Waarom bestaat dit?
         * 
         */
        //addTab(getBagView().getPropertyMessage("newProfileWizard.title"),getProfileForm().getControl());
    }
    public String verifyForms(){
        String messages = "";
        if (!getProfileForm().hasErrors()) {
            getProfileForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }        
        if (!getBagInfoForm().hasErrors()) {
            getBagInfoForm().commit();
        } else {
            throw new RuntimeException("Bag-Info has errors");
        }
        updateBagInfo(getDefaultBag());
        return messages;
    }    
    public String updateForms(DefaultBag bag) {        
        String messages = verifyForms();
        createTabbedUiComponentsWithForms();
        update();        
        return messages;
    }    
    public boolean hasFormErrors(DefaultBag bag) {
    	return false;
    }    
    public void update() {
        java.awt.Component[] components = getBagInfoForm().getControl().getComponents();
        for (int i=0; i<components.length; i++) {
            java.awt.Component c = components[i];
            c.invalidate();
            c.repaint();
        }
        getBagInfoForm().getControl().invalidate();
        getProfileForm().getControl().invalidate();
    	invalidate();
    	repaint();
    }

    public void updateProject(BagView bagView) {       
    	getBagView().infoInputPane.updateInfoFormsPane(true);
    }    
    private void updateBagInfo(DefaultBag bag) {
        HashMap<String,String> map = getBagInfoForm().getBagInfoMap();
        bag.updateBagInfo(map);
    }
    @Override
    public void requestFocus() {        
    	getBagInfoForm().getControl().requestFocus();        
    }
}