package at.jku.dke.etutor.grading.rest.dto;

import java.util.List;

public class SchemaDTO {
    List<String> createStatements;
    List<String> insertStatements;
    String solution;

    public List<String> getCreateStatements() {
        return createStatements;
    }

    public void setCreateStatements(List<String> createStatements) {
        this.createStatements = createStatements;
    }

    public List<String> getInsertStatements() {
        return insertStatements;
    }

    public void setInsertStatements(List<String> insertStatements) {
        this.insertStatements = insertStatements;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
