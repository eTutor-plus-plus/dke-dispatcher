package at.jku.dke.etutor.modules.drools.analysis;

import java.util.Date;

public class EventModel {
    private String clazz;
    private Object referenceClass;
    private Date timestamp;
    private String instanceName;

    public EventModel(String clazz, Object referenceClass, String instanceName, Date timestamp) {
        this.clazz = clazz;
        this.referenceClass = referenceClass;
        this.timestamp = timestamp;
        this.instanceName = instanceName;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Object getReferenceClass() {
        return referenceClass;
    }

    public void setReferenceClass(Object input) {
        this.referenceClass = input;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
