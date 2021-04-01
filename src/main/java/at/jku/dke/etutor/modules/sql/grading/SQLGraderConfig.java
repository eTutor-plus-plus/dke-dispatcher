package at.jku.dke.etutor.modules.sql.grading;

import java.util.HashMap;
import java.util.Iterator;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


public class SQLGraderConfig {

	private int maximumPoints;
	private HashMap criteriaGradingConfigs;

	public SQLGraderConfig() {
		super();
		this.criteriaGradingConfigs = new HashMap();
	}
	
	public void addCriteriaGradingConfig(SQLEvaluationCriterion criterion, SQLCriterionGradingConfig config){
		this.criteriaGradingConfigs.put(criterion, config);
	}
	
	public boolean isCriterionToGrade(SQLEvaluationCriterion criterion){
		return this.criteriaGradingConfigs.keySet().contains(criterion);
	}
	
	public Iterator iterCriterionsToGrade(){
		return this.criteriaGradingConfigs.keySet().iterator();
	}
	
	public SQLCriterionGradingConfig getCriterionGradingConfig(SQLEvaluationCriterion criterion){
		Object gradingConfig = this.criteriaGradingConfigs.get(criterion);

		if (gradingConfig == null){
			return null;
		} else {
			return (SQLCriterionGradingConfig)gradingConfig;
		}
	}
	public int getMaximumPoints() {
		return this.maximumPoints;
	}

	public void setMaximumPoints(int maximumPoints) {
		this.maximumPoints = maximumPoints;
	}
}
