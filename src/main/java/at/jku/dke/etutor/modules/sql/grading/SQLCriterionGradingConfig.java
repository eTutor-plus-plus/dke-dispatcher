package at.jku.dke.etutor.modules.sql.grading;

public class SQLCriterionGradingConfig {

	private int positivePoints;
	private int negativePoints;
	private GradingScope positiveScope;
	private GradingScope negativeScope;

	public SQLCriterionGradingConfig() {
		super();
		this.positivePoints = 0;
		this.negativePoints = 0;
		this.positiveScope = GradingScope.CRITERION;
		this.negativeScope = GradingScope.CRITERION;
	}
	
	public SQLCriterionGradingConfig(int positivePoints, int negativePoints, GradingScope positiveScope, GradingScope negativeScope){
		this.positiveScope = positiveScope;
		this.negativeScope = negativeScope;
		this.positivePoints = positivePoints;
		this.negativePoints = negativePoints;
	}
	
	public int getNegativePoints() {
		return this.negativePoints;
	}

	public GradingScope getNegativeScope() {
		return this.negativeScope;
	}

	public int getPositivePoints() {
		return this.positivePoints;
	}

	public GradingScope getPositiveScope() {
		return this.positiveScope;
	}

	public void setNegativePoints(int negativePoints) {
		this.negativePoints = negativePoints;
	}

	public void setNegativeScope(GradingScope negativeScope) {
		this.negativeScope = negativeScope;
	}

	public void setPositivePoints(int positivePoints) {
		this.positivePoints = positivePoints;
	}

	public void setPositiveScope(GradingScope positiveScope) {
		this.positiveScope = positiveScope;
	}
}
