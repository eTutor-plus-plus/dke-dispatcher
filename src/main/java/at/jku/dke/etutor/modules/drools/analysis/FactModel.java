package at.jku.dke.etutor.modules.drools.analysis;

import com.fasterxml.jackson.databind.JsonNode;

public class FactModel {
    private String fullClassname;
    private int objectId;
    private JsonNode parameters;

    public FactModel(String fullClassname, int objectId, JsonNode parameters) {
        this.fullClassname = fullClassname;
        this.objectId = objectId;
        this.parameters = parameters;
    }

    public String getFullClassname() {
        return fullClassname;
    }

    public void setFullClassname(String fullClassname) {
        this.fullClassname = fullClassname;
    }

    public JsonNode getParameters() {
        return parameters;
    }

    public void setParameters(JsonNode parameters) {
        this.parameters = parameters;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
}
