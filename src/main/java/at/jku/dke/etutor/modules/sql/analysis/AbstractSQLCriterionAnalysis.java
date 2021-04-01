package at.jku.dke.etutor.modules.sql.analysis;

public abstract class AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis {

	private boolean isSatisfied;
	private AnalysisException exception;

	public AbstractSQLCriterionAnalysis() {
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
