package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


import at.jku.dke.etutor.evaluation.Analysis;
import at.jku.dke.etutor.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;



public class SQLAnalysis extends DefaultAnalysis implements Analysis {

	private Vector queryResultTuples;
	private HashMap criterionAnalyses;
	private AnalysisException exception;
	private Vector queryResultColumnLabels;

	public SQLAnalysis(){
		super();

		this.exception = null;
		this.queryResultTuples = new Vector();
		this.criterionAnalyses = new HashMap();
		this.queryResultColumnLabels = new Vector();
	}

	public void addQueryResultColumnLabel(String columnLabel){
		this.queryResultColumnLabels.add(columnLabel);
	}
	
	public void addQueryResultTuple(Collection tuple) {
		this.queryResultTuples.add(tuple);
	}

	public Iterator iterQueryResultTuples(){
		return this.queryResultTuples.iterator();
	}

	public Vector getQueryResultTuples() {
		return (Vector)this.queryResultTuples.clone();
	}

	public Vector getQueryResultColumnLabels() {
		return (Vector)this.queryResultColumnLabels.clone();
	}

	public void setQueryResultTuples(Vector queryResultTuples) {
		this.queryResultTuples = queryResultTuples;
	}

	public Iterator iterQueryResultColumnLabels(){
		return this.queryResultColumnLabels.iterator();
	}
	
	public void setQueryResultColumnLabels(Vector queryResultColumnLabels) {
		this.queryResultColumnLabels = queryResultColumnLabels;
	}
	
	public void setAnalysisException(AnalysisException exception){
		this.exception = exception;
	}
	
	public void addCriterionAnalysis(SQLEvaluationCriterion criterion, SQLCriterionAnalysis analysis){
		this.criterionAnalyses.put(criterion, analysis);
	}
	
	public Iterator iterCriterionAnalyses(){
		return this.criterionAnalyses.values().iterator();
	}
	
	public void removeCriterionAnalysis(SQLEvaluationCriterion criterion){
		this.criterionAnalyses.remove(criterion);
	}
	
	public SQLCriterionAnalysis getCriterionAnalysis(SQLEvaluationCriterion criterion){
		Object criterionAnalysis = this.criterionAnalyses.get(criterion);
		
		if (criterionAnalysis == null){
			return null;
		} else {
			return (SQLCriterionAnalysis)criterionAnalysis;
		}
	}
	
	public boolean isCriterionAnalyzed(SQLEvaluationCriterion criterion){
		return this.criterionAnalyses.containsKey(criterion);
	}
	
}
