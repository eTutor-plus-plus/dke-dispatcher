package at.jku.dke.etutor.modules.sql.grading;

import java.util.HashMap;
import java.util.Iterator;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


public class SQLGraderConfig {

	private int maximumPoints;
	private final HashMap<SQLEvaluationCriterion, SQLCriterionGradingConfig> criteriaGradingConfigs;

	public SQLGraderConfig() {
		super();
		this.criteriaGradingConfigs = new HashMap<>();
	}
	
	public void addCriteriaGradingConfig(SQLEvaluationCriterion criterion, SQLCriterionGradingConfig config){
		this.criteriaGradingConfigs.put(criterion, config);
	}
	
	public boolean isCriterionToGrade(SQLEvaluationCriterion criterion){
		return this.criteriaGradingConfigs.containsKey(criterion);
	}
	
	public Iterator<SQLEvaluationCriterion> iterCriterionsToGrade(){
		return this.criteriaGradingConfigs.keySet().iterator();
	}
	
	public SQLCriterionGradingConfig getCriterionGradingConfig(SQLEvaluationCriterion criterion){
		return this.criteriaGradingConfigs.get(criterion);
	}

	public int getMaximumPoints() {
		return this.maximumPoints;
	}

	public void setMaximumPoints(int maximumPoints) {
		this.maximumPoints = maximumPoints;
	}
}
