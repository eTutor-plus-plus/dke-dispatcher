package at.jku.dke.etutor.modules.sql.analysis;

/**
 * Abstract class implementing SQLCriterionAnalysis that represents
 * an analysis with regards to a specific SQLEvaluationCriterion
 */
public abstract class AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis {

	/**
	 * Verifies if the given Criterion is satisfied or not
	 */
	private boolean isSatisfied;

	/**
	 * The exception if the Criterion is not satisfied
	 */
	private AnalysisException exception;

	/**
	 * The constructor
	 */
	protected AbstractSQLCriterionAnalysis() {
		super();
		this.exception = null;
		this.isSatisfied = true;
	}

	public boolean isCriterionSatisfied() {
		return this.isSatisfied;
	}

	public void setCriterionIsSatisfied(boolean isSatisfied) {
		this.isSatisfied = isSatisfied;
	}
	
	public void setAnalysisException(AnalysisException exception){
		this.exception = exception;
	}
	
	public AnalysisException getAnalysisException(){
		return this.exception;
	}
}
