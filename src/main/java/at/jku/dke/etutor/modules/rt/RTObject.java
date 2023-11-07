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

    private String dbSolution;

    private List<String> solutionRows;


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

    public String getDbSolution() {
        return dbSolution;
    }

    public void setDbSolution(String dbSolution) {
        this.dbSolution = dbSolution;
        String[] array = dbSolution.split("\r\n");
        this.solutionRows = Arrays.stream(array).toList();
    }

    public List<String> getSolutionRows() {
        return solutionRows;
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
