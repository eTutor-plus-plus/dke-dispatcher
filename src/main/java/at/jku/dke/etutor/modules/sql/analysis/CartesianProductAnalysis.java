package at.jku.dke.etutor.modules.sql.analysis;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The SQLCriterionAnalysis representing the analysis with regards to the SQLEvaluationCriterion.CARTESIAN_PRODUCT
 */
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
