package gov.loc.repository.bagger.ui;

import gov.loc.repository.bagger.Contact;
import gov.loc.repository.bagger.Organization;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;

public class OrganizationProfileForm extends AbstractForm implements FocusListener {
    
    public static final String PROFILE_FORM_PAGE = "profilePage";
    private JComponent form;
    private JComponent contactName;
    private JComponent field;  

    public OrganizationProfileForm(FormModel formModel) {
        super(formModel,PROFILE_FORM_PAGE);        
    }
    public BagView getBagView(){
        return BagView.getInstance();
    }
    @Override
    protected JComponent createFormControl() {
        return getForm();    	
    }   
    public JComponent getForm() {
        if(form == null){
            form = new JPanel();
            form.setLayout(new BorderLayout());           
            form.add(createFormFields(), BorderLayout.CENTER);
        }            
        return form;
    }
    public void setForm(JComponent form) {
        this.form = form;
    }    
    protected JComponent createFormFields() {
        JComponent fieldForm;
        ImageIcon requiredIcon = getBagView().getPropertyImage("bag.required.image");
        BagTableFormBuilder formBuilder = new BagTableFormBuilder(getBindingFactory(), requiredIcon);
        int rowCount = 0;

        formBuilder.row();
        rowCount++;
        
        formBuilder.addSeparator("Send from Organization");
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("sourceOrganization")[1];
        Organization organization = getBagView().getBag().getProfile().getOrganization();
        if(organization != null &&  organization.getName().isReadOnly()){
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        JComponent orgAddress = formBuilder.add("organizationAddress")[1];
        if(organization != null &&  organization.getAddress().isReadOnly()){
            field.setEnabled(false);
        }        
        orgAddress.addFocusListener(this);

        Contact fromContact = getBagView().getBag().getProfile().getSendFromContact();
        
        formBuilder.row();
        rowCount++;
        formBuilder.addSeparator("Send from Contact");
        formBuilder.row();
        
        rowCount++;
        contactName = formBuilder.add("srcContactName")[1];
        
        if(fromContact != null &&  fromContact.getContactName().isReadOnly()){
            field.setEnabled(false);
        }
        
        contactName.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("srcContactPhone")[1];
        if(fromContact != null &&  fromContact.getTelephone().isReadOnly()){
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("srcContactEmail")[1];
        
        if(fromContact != null &&  fromContact.getEmail().isReadOnly()){
            field.setEnabled(false);
        }
        
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        formBuilder.addSeparator("Send to Contact");
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("toContactName")[1];
        Contact contact = getBagView().getBag().getProfile().getSendToContact();
        if(contact != null && contact.getContactName().isReadOnly()){
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("toContactPhone")[1];
        
        if(contact != null &&  contact.getTelephone().isReadOnly()){
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;
        field = formBuilder.add("toContactEmail")[1];
        if(contact != null && contact.getEmail().isReadOnly()){
            field.setEnabled(false);
        }
        field.addFocusListener(this);
        formBuilder.row();
        rowCount++;

        contactName.requestFocus();
        fieldForm = formBuilder.getForm();
        rowCount++;       

        return fieldForm;
    }
    public boolean requestFocusInWindow() {
        return contactName.requestFocusInWindow();
    }
    @Override
    public void focusGained(FocusEvent evt) {
    }
    @Override
    public void focusLost(FocusEvent evt) {             
    	if (getBagView() != null && !hasErrors() && isDirty()) {
            getBagView().getInfoInputPane().getUpdateBagHandler().updateBag();           
    	}
    }
}