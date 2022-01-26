package at.jku.dke.etutor.grading.rest.dto;

public class SQLExerciseDTO {
    private String schemaName;
    private String solution;

    public SQLExerciseDTO(){

    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
