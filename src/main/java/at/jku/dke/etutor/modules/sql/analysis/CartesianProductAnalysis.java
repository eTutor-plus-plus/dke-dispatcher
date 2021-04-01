package at.jku.dke.etutor.modules.sql.analysis;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class CartesianProductAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis {

	private boolean cartesianProductSuspected;

	public CartesianProductAnalysis() {
		super();
		this.cartesianProductSuspected = false;
	}
	
	public void setCartesianProductSuspected(boolean cartesianProductSuspected){
		this.cartesianProductSuspected = cartesianProductSuspected;
	}
	
	public boolean isCartesianProductSuspected(){
		return this.cartesianProductSuspected;
	}
	
	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CARTESIAN_PRODUCT;
	}
}
