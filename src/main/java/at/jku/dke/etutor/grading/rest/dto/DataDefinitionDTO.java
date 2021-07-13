package at.jku.dke.etutor.grading.rest.dto;

import java.util.List;

/**
 * Represents the statements required to create schemas, tables and populate them with data
 */
public class DataDefinitionDTO {
    List<String> createStatements;
    List<String> insertStatementsSubmission;
    List<String> insertStatementsDiagnose;
    String schemaName;

    public List<String> getCreateStatements() {
        return createStatements;
    }

    public void setCreateStatements(List<String> createStatements) {
        this.createStatements = createStatements;
    }

    public List<String> getInsertStatementsSubmission() {
        return insertStatementsSubmission;
    }

    public void setInsertStatementsSubmission(List<String> insertStatementsSubmission) {
        this.insertStatementsSubmission = insertStatementsSubmission;
    }

    public List<String> getInsertStatementsDiagnose() {
        return insertStatementsDiagnose;
    }

    public void setInsertStatementsDiagnose(List<String> insertStatementsDiagnose) {
        this.insertStatementsDiagnose = insertStatementsDiagnose;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
