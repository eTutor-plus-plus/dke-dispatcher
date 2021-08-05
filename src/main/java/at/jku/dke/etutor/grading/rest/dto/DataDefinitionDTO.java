package at.jku.dke.etutor.grading.rest.dto;

import java.util.List;

/**
 * Represents the statements required to create SQL- schemas, tables and populate them with data
 */
public class DataDefinitionDTO {
    /**
     * The create table statments
     */
    List<String> createStatements;
    /**
     * The insert into statements for the submission schema
     */
    List<String> insertStatementsSubmission;
    /**
     * The insert into statements for the diagnose schema
     */
    List<String> insertStatementsDiagnose;
    /**
     * The schema name
     */
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
