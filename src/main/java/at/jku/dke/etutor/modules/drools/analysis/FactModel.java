package at.jku.dke.etutor.modules.drools.analysis;

import java.util.List;

public class FactModel {
    private String clazz;
    private List<Object> parameters;

    public FactModel(String clazz, List<Object> parameters) {
        this.clazz = clazz;
        this.parameters = parameters;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
}
