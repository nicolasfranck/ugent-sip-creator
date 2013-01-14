package gov.loc.repository.bagger.model;

public class BagStatus {	
    static final BagStatus instance = new BagStatus();
    StatusModel validationStatus = new StatusModel();
    StatusModel completenessStatus = new StatusModel();
    StatusModel profileComplianceStatus = new StatusModel();

    public StatusModel getValidationStatus() {
        return validationStatus;
    }
    public void setValidationStatus(StatusModel validationStatus) {
        this.validationStatus = validationStatus;
    }
    public StatusModel getCompletenessStatus() {
        return completenessStatus;
    }
    public void setCompletenessStatus(StatusModel completenessStatus) {
        this.completenessStatus = completenessStatus;
    }
    public StatusModel getProfileComplianceStatus() {
        return profileComplianceStatus;
    }
    public void setProfileComplianceStatus(StatusModel profileComplianceStatus) {
        this.profileComplianceStatus = profileComplianceStatus;
    }
    public static BagStatus getInstance() {
        return instance;
    }	
}
