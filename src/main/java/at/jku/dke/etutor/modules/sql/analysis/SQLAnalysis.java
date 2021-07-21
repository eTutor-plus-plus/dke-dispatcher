package at.jku.dke.etutor.modules.sql.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;


import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


/**
 * Represents the result of the SQLAnalysis
 */
public class SQLAnalysis extends DefaultAnalysis implements Analysis {

	private Vector<Collection<String>> queryResultTuples;
	private final HashMap<SQLEvaluationCriterion, SQLCriterionAnalysis> criterionAnalyses;
	private AnalysisException exception;
	private Vector<String> queryResultColumnLabels;

	public SQLAnalysis(){
		super();

		this.exception = null;
		this.queryResultTuples = new Vector<>();
		this.criterionAnalyses = new HashMap<>();
		this.queryResultColumnLabels = new Vector<>();
	}

	public void addQueryResultColumnLabel(String columnLabel){
		this.queryResultColumnLabels.add(columnLabel);
	}
	
	public void addQueryResultTuple(Collection<String> tuple) {
		this.queryResultTuples.add(tuple);
	}

	public Iterator<Collection<String>> iterQueryResultTuples(){
		return this.queryResultTuples.iterator();
	}

	public Vector<Collection<String>> getQueryResultTuples() {
		return (Vector<Collection<String>>)this.queryResultTuples.clone();
	}

	public Vector<String> getQueryResultColumnLabels() {
		return (Vector<String>)this.queryResultColumnLabels.clone();
	}

	public void setQueryResultTuples(Vector<Collection<String>> queryResultTuples) {
		this.queryResultTuples = queryResultTuples;
	}

	public Iterator<String> iterQueryResultColumnLabels(){
		return this.queryResultColumnLabels.iterator();
	}
	
	public void setQueryResultColumnLabels(Vector<String> queryResultColumnLabels) {
		this.queryResultColumnLabels = queryResultColumnLabels;
	}
	
	public void setAnalysisException(AnalysisException exception){
		this.exception = exception;
	}
	
	public void addCriterionAnalysis(SQLEvaluationCriterion criterion, SQLCriterionAnalysis analysis){
		this.criterionAnalyses.put(criterion, analysis);
	}
	
	public Iterator<SQLCriterionAnalysis> iterCriterionAnalyses(){
		return this.criterionAnalyses.values().iterator();
	}
	
	public void removeCriterionAnalysis(SQLEvaluationCriterion criterion){
		this.criterionAnalyses.remove(criterion);
	}
	
	public SQLCriterionAnalysis getCriterionAnalysis(SQLEvaluationCriterion criterion){
		return this.criterionAnalyses.get(criterion);
	}
	
	public boolean isCriterionAnalyzed(SQLEvaluationCriterion criterion){
		return this.criterionAnalyses.containsKey(criterion);
	}
	
}
