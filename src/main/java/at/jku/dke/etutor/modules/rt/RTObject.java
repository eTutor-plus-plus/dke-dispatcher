package at.jku.dke.etutor.modules.rt;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.sourceforge.plantuml.json.JsonString;

import java.util.Arrays;
import java.util.List;

public class RTObject {
    @JsonProperty("solution")
    private JsonString solution;

    @JsonProperty("maxPoints")
    private String maxPoints;

    @JsonProperty("id")
    private String id;

    private List<RTSolution> dbSolution;

    public JsonString getSolution() {
        return solution;
    }

    public Integer getMaxPoints() {
        return Integer.parseInt(maxPoints);
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public void setSolution(JsonString solution) {
        this.solution = solution;
    }

    public void setMaxPoints(String maxPoints) {
        this.maxPoints = maxPoints;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<RTSolution> getDbSolution() {
        return dbSolution;
    }

    public void setDbSolution(List<RTSolution> dbSolution) {
       this.dbSolution = dbSolution;
    }


    @Override
    public String toString() {
        return "RTObject{" +
                "solution=" + solution +
                ", maxPoints='" + maxPoints + '\'' +
                ", id='" + id + '\'' +
                ", dbSolution='" + dbSolution + '\'' +
                '}';
    }
}
