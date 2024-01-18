package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

public class SyntaxAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private boolean foundError;
    private String errorDescription;
    //endregion

    public SyntaxAnalysis() {
        this.foundError = false;
        this.errorDescription = "";
    }

    public DDLEvaluationCriterion getEvaluationCriterion() {
        return DDLEvaluationCriterion.CORRECT_SYNTAX;
    }

    //region Getter/Setter
    public boolean isFoundError() {
        return foundError;
    }

    public void setFoundError(boolean foundError) {
        this.foundError = foundError;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    //endregion
}
