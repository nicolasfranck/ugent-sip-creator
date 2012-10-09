package gov.loc.repository.bagger.bag;

import gov.loc.repository.bagger.Contact;

/**
 * Simple JavaBean domain object representing an organization.
 *
|    (Source-organization: California Digital Library                      )
|    (Organization-address: 415 20th Street, 4th Floor, Oakland, CA. 94612 )
|    (Contact-name: A. E. Newman                                           )
|    (Contact-phone: +1 510-555-1234                                       )
|    (Contact-email: alfred@ucop.edu                                       )
 *
 * @author Jon Steinbach
 */
public class BaggerProfile {
    private BaggerSourceOrganization sourceOrganization = new BaggerSourceOrganization();
    private Contact toContact = new Contact(true);

    public BaggerSourceOrganization getOrganization() {
        return sourceOrganization;
    }

    public void setOrganization(BaggerSourceOrganization organization) {
        this.sourceOrganization = organization;
    }


    public Contact getSourceContact() {
        return sourceOrganization.getContact();
    }

    public void setSourceContact(Contact contact) {
            this.sourceOrganization.setContact(contact);
    }

    public Contact getToContact() {
        return toContact;
    }

    public void setToContact(Contact contact) {
        this.toContact = contact;
    }

    public String getSourceOrganization() {
        return sourceOrganization.getOrganizationName();
    }

    public void setSourceOrganization(String name) {
        sourceOrganization.setOrganizationName(name);
    }

    public String getOrganizationAddress() {
        return sourceOrganization.getOrganizationAddress();
    }

    public void setOrganizationAddress(String address) {
        sourceOrganization.setOrganizationAddress(address);
    }

    public String getSrcContactName() {
        return sourceOrganization.getContact().getContactName().getFieldValue();
    }

    public void setSrcContactName(String name) {
        sourceOrganization.getContact().getContactName().setFieldValue(name);
    }

    public String getSrcContactPhone() {
        return sourceOrganization.getContact().getTelephone().getFieldValue();
    }

    public void setSrcContactPhone(String phone) {
        sourceOrganization.getContact().getTelephone().setFieldValue(phone);
    }

    public String getSrcContactEmail() {
        return sourceOrganization.getContact().getEmail().getFieldValue();
    }

    public void setSrcContactEmail(String email) {
        sourceOrganization.getContact().getEmail().setFieldValue(email);
    }

    public String getToContactName() {
        return toContact.getContactName().getFieldValue();
    }

    public void setToContactName(String name) {
        toContact.getContactName().setFieldValue(name);
    }

    public String getToContactPhone() {
        return toContact.getTelephone().getFieldValue();
    }

    public void setToContactPhone(String phone) {
        toContact.getTelephone().setFieldValue(phone);
    }

    public String getToContactEmail() {
        return toContact.getEmail().getFieldValue();
    }

    public void setToContactEmail(String email) {
        toContact.getEmail().setFieldValue(email);
    }
}
