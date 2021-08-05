package at.jku.dke.etutor.modules.sql.analysis;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The SQLCriterionAnalysis with regards to the SQLEvaluationCriterion.CORRECT_ORDER
 */
public class OrderingAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	public boolean foundIncorrectOrdering;

	public OrderingAnalysis() {
		super();
		this.foundIncorrectOrdering = false;
	}
	
	public void setFoundIncorrectOrdering(boolean foundIncorrectOrdering){
		this.foundIncorrectOrdering = foundIncorrectOrdering;
	}
	
	public boolean foundIncorrectOrdering(){
		return this.foundIncorrectOrdering;
	}

	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_ORDER;
	}
}
