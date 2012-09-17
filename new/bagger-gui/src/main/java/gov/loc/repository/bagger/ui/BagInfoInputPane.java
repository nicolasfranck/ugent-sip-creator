package gov.loc.repository.bagger.ui;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.BaggerProfile;
import gov.loc.repository.bagger.bag.impl.DefaultBag;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.HierarchicalFormModel;
import org.springframework.richclient.form.FormModelHelper;
import ugent.bagger.panels.MetsPanel;

public final class BagInfoInputPane extends JTabbedPane {
    private static final long serialVersionUID = 1L;
    private static final Log logger = LogFactory.getLog(BagInfoInputPane.class);
    private BaggerProfile bagProfile;
    private BagInfoForm bagInfoForm;
    private OrganizationProfileForm profileForm;
    private HierarchicalFormModel infoFormModel;
    private HierarchicalFormModel profileFormModel;

    public BagView getBagView(){
        return BagView.getInstance();        
    }   
    public DefaultBag getDefaultBag() {
        return BagView.getInstance().getBag();
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
            bagInfoForm = new BagInfoForm(FormModelHelper.createChildPageFormModel(getInfoFormModel(), null),getDefaultBag().getInfo().getFieldMap(),false);       
        }
        return bagInfoForm;
    }
    public void setBagInfoForm(BagInfoForm bagInfoForm) {
        this.bagInfoForm = bagInfoForm;
    }    
    public OrganizationProfileForm getProfileForm() {
        if(profileForm == null){
            profileForm = new OrganizationProfileForm(FormModelHelper.createChildPageFormModel(getProfileFormModel(), null));    	
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
                System.out.println("tabNext!!!");
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
        System.out.println("enableForms");
    	getProfileForm().setEnabled(b);
    	getProfileForm().getControl().invalidate();
    	getBagInfoForm().setEnabled(b);
    	getBagInfoForm().getControl().invalidate();
    	setEnabled(b);
    	invalidate();
    }    
    public void populateForms(boolean enabled){    	        
        System.out.println("populateForms");
    	getBagProfile().setOrganization(getDefaultBag().getInfo().getBagOrganization());                
    	getBagProfile().setToContact(getDefaultBag().getInfo().getToContact());    	    	        
        createTabbedUiComponentsWithForms();        
    }

    // Create a tabbed pane for the information forms and checkbox panel
    private void createTabbedUiComponentsWithForms() {        
        System.out.println("createTabbedUiComponentsWithForms()");
        removeAll();     
        validate();
        
        setName("Profile");
        getBagInfoForm().getControl().setToolTipText(getBagView().getPropertyMessage("infoinputpane.tab.details.help"));
        addTab(getBagView().getPropertyMessage("infoInputPane.tab.details"),getBagInfoForm().getControl());
        getProfileForm().getControl().setToolTipText("Profile Form");
        
        //Nicolas Franck
        //addTab("mets",new MetsPanel(new Mets()));
        
        //Nicolas Franck
        /* vreemd. ProfileForm wordt hier nergens gebruikt. Waarom bestaat dit?
         * 
         */
        //addTab(getBagView().getPropertyMessage("newProfileWizard.title"),getProfileForm().getControl());
    }
    public String verifyForms(){        
        System.out.println("verifyForms");        
        if(!getProfileForm().hasErrors()){            
            getProfileForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }        
        if(!getBagInfoForm().hasErrors()){
            getBagInfoForm().commit();
        }else{
            throw new RuntimeException("Bag-Info has errors");
        }
        updateBagInfo();        
        return "";
    }    
    public String updateForms(){
        System.out.println("BagInfoInputPane::updateForms");
        String messages = verifyForms();        
        createTabbedUiComponentsWithForms();
        update();        
        return messages;
    }    
    public void update(){        
        System.out.println("update");        
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

    public void updateProject(){
        System.out.println("updateProject");
    	getBagView().getInfoInputPane().updateInfoFormsPane(true);        
    }    
    private void updateBagInfo(){      
        System.out.println("BagInfoInputPane::updateBagInfo");        
        getDefaultBag().updateBagInfo(getBagInfoForm().getBagInfoMap());
        System.out.println("BagInfoInputPane::updateBagInfo done");                
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