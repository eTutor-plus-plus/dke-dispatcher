package at.jku.dke.etutor.modules.sql.analysis;

import java.util.*;


import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;


/**
 * Represents the result of the analysis by wrapping the different SQLCriterionAnalysis´
 */
public class SQLAnalysis extends DefaultAnalysis implements Analysis {
	/**
	 * The result tuples
	 */
	private List<Collection<String>> queryResultTuples;
	/**
	 * Maps the SQLEvaluationCriterion´s to the SQLCriterionAnalysis´
	 */
	private final HashMap<SQLEvaluationCriterion, SQLCriterionAnalysis> criterionAnalyses;
	private AnalysisException exception;
	/**
	 * The result columns
	 */
	private List<String> queryResultColumnLabels;

	public SQLAnalysis(){
		super();

		this.exception = null;
		this.criterionAnalyses = new HashMap<>();

		// note: specific SQL content:
		// here the results of the submitted query are stored
		this.queryResultTuples = new ArrayList<>();
		this.queryResultColumnLabels = new ArrayList<>();
	}

	/**
	 * Adds a result column to queryResultColumnLabels
	 * @param columnLabel the column label
	 */
	public void addQueryResultColumnLabel(String columnLabel){
		this.queryResultColumnLabels.add(columnLabel);
	}

	/**
	 * Adds a tuple to queryResultTuples
	 * @param tuple the tuple
	 */
	public void addQueryResultTuple(Collection<String> tuple) {
		this.queryResultTuples.add(tuple);
	}

	/**
	 * Returns an iterator for the queryResultTuples
	 * @return the iterator
	 */
	public Iterator<Collection<String>> iterQueryResultTuples(){
		return this.queryResultTuples.iterator();
	}

	/**
	 * Returns the queryResultTuples
	 * @return the tuples
	 */
	public List<Collection<String>> getQueryResultTuples() {
		return new ArrayList<>(queryResultTuples);
	}

	/**
	 * Returns the queryResultColumnLabels
	 * @return the column labels
	 */
	public List<String> getQueryResultColumnLabels() {
		return new ArrayList<>(queryResultColumnLabels);
	}

	/**
	 * Sets the queryResultTuples
	 * @param queryResultTuples the tuples
	 */
	public void setQueryResultTuples(List<Collection<String>> queryResultTuples) {
		this.queryResultTuples = queryResultTuples;
	}

	/**
	 * Returns an iterator for the queryResultColumnLabels
	 * @return the iterator
	 */
	public Iterator<String> iterQueryResultColumnLabels(){
		return this.queryResultColumnLabels.iterator();
	}

	/**
	 * Sets the queryResultColumnLabels
	 * @param queryResultColumnLabels the column labels
	 */
	public void setQueryResultColumnLabels(List<String> queryResultColumnLabels) {
		this.queryResultColumnLabels = queryResultColumnLabels;
	}

	/**
	 * Sets the analysis exception
	 * @param exception the exception
	 */
	public void setAnalysisException(AnalysisException exception){
		this.exception = exception;
	}

	/**
	 * Adds an SQLEvaluationCriterion as key and an SQLCriterionAnalysis as value to the map criterionAnalyses
	 * @param criterion the criterion
	 * @param analysis the analysis of the criterion
	 */
	public void addCriterionAnalysis(SQLEvaluationCriterion criterion, SQLCriterionAnalysis analysis){
		this.criterionAnalyses.put(criterion, analysis);
	}

	/**
	 * Returns an iterator for the SQLCriterionAnalyses
	 * @return the iterator
	 */
	public Iterator<SQLCriterionAnalysis> iterCriterionAnalyses(){
		return this.criterionAnalyses.values().iterator();
	}

	/**
	 * Removes the criterion and the analysis from the map criterionAnalyses
	 * @param criterion the criterion to be removed
	 */
	public void removeCriterionAnalysis(SQLEvaluationCriterion criterion){
		this.criterionAnalyses.remove(criterion);
	}

	/**
	 * Returns the SQLCriterionAnalysis for a given criterion
	 * @param criterion the criterion
	 * @return the analysis
	 */
	public SQLCriterionAnalysis getCriterionAnalysis(SQLEvaluationCriterion criterion){
		return this.criterionAnalyses.get(criterion);
	}

	/**
	 * Returns whether a given criterion has been analyzed
	 * @param criterion the criterion
	 * @return a boolean indicating if the criterion has been analyzed
	 */
	public boolean isCriterionAnalyzed(SQLEvaluationCriterion criterion){
		return this.criterionAnalyses.containsKey(criterion);
	}
	
}
