package gov.loc.repository.bagger.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class StatusModel {
    private Status status = Status.UNKNOWN;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        Status old = this.status;
        this.status = status;
        pcs.firePropertyChange("status", old, status);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}