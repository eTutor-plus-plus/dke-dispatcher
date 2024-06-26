package at.jku.dke.etutor.modules.ddl.analysis;

public abstract class AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis {
    //region Fields
    private boolean isSatisfied;
    private AnalysisException exception;
    //endregion

    protected AbstractDDLCriterionAnalysis() {
        this.exception = null;
        this.isSatisfied = true;
    }

    @Override
    public boolean isCriterionSatisfied() {
        return this.isSatisfied;
    }

    @Override
    public void setCriterionIsSatisfied(boolean b) {
        this.isSatisfied = b;
    }

    @Override
    public void setAnalysisException(AnalysisException e) {
        this.exception = e;
    }

    @Override
    public AnalysisException getAnalysisException() {
        return this.exception;
    }
}
