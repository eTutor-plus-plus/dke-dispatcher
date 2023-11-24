package at.jku.dke.etutor.modules.drools.analysis;

import java.time.LocalTime;
import java.util.Date;

public class EventModel {
    private String clazz;
    private Object reference;
    private Date timestamp;

    public EventModel(String clazz, Object input, Date timestamp) {
        this.clazz = clazz;
        this.reference = input;
        this.timestamp = timestamp;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object input) {
        this.reference = input;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
