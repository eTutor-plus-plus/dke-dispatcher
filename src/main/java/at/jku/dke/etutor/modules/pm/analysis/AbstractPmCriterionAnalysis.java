package at.jku.dke.etutor.modules.pm.analysis;


/**
 * Abstract class implementing PmCriterionAnalysis (INTERFACE) that represents
 * an analysis in regard to a SPECIFIC PmEvaluationCriterion
 */

public abstract class AbstractPmCriterionAnalysis implements PmCriterionAnalysis{

    /**
     * Verifies if the given Criterion is satisfied or not
     */

    private boolean isSatisfied;

    /**
     * The EXCEPTION if the Criterion is NOT satisfied
     */
    private AnalysisException exception;

    // CONSTRUCTOR
    protected AbstractPmCriterionAnalysis(){
        super();
        this.isSatisfied = true;
        this.exception = null;
    }

    // METHODS (implementing PmCriterionAnalysis (INTERFACE))
    @Override
    public boolean isCriterionSatisfied() {
        return this.isSatisfied;
    }

    @Override
    public void setCriterionIsSatisfied(boolean isSatisfied) {
        this.isSatisfied = isSatisfied;
    }

    @Override
    public AnalysisException getAnalysisException() {
        return this.exception;
    }

    @Override
    public void setAnalysisException(AnalysisException exception) {
        this.exception = exception;
    }
}
