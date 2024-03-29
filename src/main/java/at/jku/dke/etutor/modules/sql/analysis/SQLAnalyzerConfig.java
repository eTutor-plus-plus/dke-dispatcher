package at.jku.dke.etutor.modules.sql.analysis;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

/**
 * The configuration for the SQLAnalysis
 */
public class SQLAnalyzerConfig {

	// note: what is the meaning of the different diagnose levels?
	private int diagnoseLevel;
	private String correctQuery;
	private Connection connection;
	// note: most import (Set of Criteria to analyse)
	private HashSet<SQLEvaluationCriterion> criteriaToAnalyze;

	public SQLAnalyzerConfig() {
		super();
		this.diagnoseLevel = 0;
		this.correctQuery = "";
		this.connection = null;
		// note: might be multiple
		this.criteriaToAnalyze = new HashSet<>();
	}
	
	public boolean isCriterionToAnalyze(SQLEvaluationCriterion criterion){
		return this.criteriaToAnalyze.contains(criterion);
	}

	public Iterator<SQLEvaluationCriterion> iterCriteriaToAnalyze(){
		return this.criteriaToAnalyze.iterator();
	}
	
	public void addCriterionToAnalyze(SQLEvaluationCriterion criterion){
		this.criteriaToAnalyze.add(criterion);
	}

	public void removeCriterionToAnalyze(SQLEvaluationCriterion criterion){
		this.criteriaToAnalyze.remove(criterion);
	}

	public int getDiagnoseLevel() {
		return this.diagnoseLevel;
	}

	public String getCorrectQuery() {
		return this.correctQuery;
	}

	public Connection getConnection(){
		return this.connection;
	}

	public void setDiagnoseLevel(int diagnoseLevel) {
		this.diagnoseLevel = diagnoseLevel;
	}

	public void setCorrectQuery(String correctQuery) {
		this.correctQuery = correctQuery;
	}

	public void setConnection(Connection correctQueryConnection){
		this.connection = correctQueryConnection;
	}
}
