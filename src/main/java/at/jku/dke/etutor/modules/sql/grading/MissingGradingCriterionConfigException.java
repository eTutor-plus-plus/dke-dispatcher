package at.jku.dke.etutor.modules.sql.grading;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class MissingGradingCriterionConfigException extends Exception {

	private SQLEvaluationCriterion criterion;

	public MissingGradingCriterionConfigException() {
		super();
		this.criterion = null;
	}

	public MissingGradingCriterionConfigException(SQLEvaluationCriterion criterion) {
		super();
		this.criterion = criterion;
	}

	public MissingGradingCriterionConfigException(SQLEvaluationCriterion criterion, String message) {
		super(message);
		this.criterion = criterion;
	}

	public MissingGradingCriterionConfigException(SQLEvaluationCriterion criterion, String message, Throwable cause) {
		super(message, cause);
		this.criterion = criterion;
	}

	public MissingGradingCriterionConfigException(SQLEvaluationCriterion criterion, Throwable cause) {
		super(cause);
		this.criterion = criterion;
	}
	
	public SQLEvaluationCriterion getSQLEvaluationCriterion(){
		return this.criterion;
	}
}
