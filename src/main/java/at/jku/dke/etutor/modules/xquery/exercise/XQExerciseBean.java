package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.exercise;

import java.io.Serializable;
import java.util.List;

import etutor.modules.xquery.analysis.UrlContentMap;


/**
 * Class to be used as JavaBean object when specifying xq exercises.
 * 
 * @author Georg Nitsche (01.02.2006)
 * 
 */
public class XQExerciseBean implements Serializable {

	private Double points;
	private String query;
	private List sortedNodes;
	private UrlContentMap urls;
	private String urlToAdd;
	private String hiddenUrlToAdd;
	private String sortedNodeToAdd;
	
	public XQExerciseBean() {
		super();
	}

	/**
	 * Returns the predicates designated to be required in the result of an
	 * analyzed query in order to be considered as correct.
	 * @return a list of <code>String</code> objects representing the name 
	 * of required predicates
	 */
	public List getSortedNodes() {
		return sortedNodes;
	}

	/**
	 * Sets the predicates designated to be required in the result of an
	 * analyzed query in order to be considered as correct.
	 * @param predicates a list of <code>String</code> objects representing the name 
	 * of required predicates 
	 */
	public void setSortedNodes(List sortedNodes) {
		this.sortedNodes = sortedNodes;
	}

	/**
	 * Returns the XQuery which represents the correct solution.
	 * @return the XQuery
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * Sets the XQuery which represents the correct solution.
	 * @param query the XQuery
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Returns the maximum number of points designated for the exercise. This
	 * should not be <code>null</code> and a value greater than zero. The actual
	 * number of points is defined by the eTutor core.
	 * @return the maximum number of points to be reached for the exercise.
	 */
	public Double getPoints() {
		return points;
	}

	/**
	 * Sets the maximum number of points designated for the exercise. This
	 * should not be <code>null</code> and a value greater than zero. The actual
	 * number of points is defined by the eTutor core.
	 * @param points the maximum number of points to be reached for the exercise.
	 */
	public void setPoints(Double points) {
		this.points = points;
	}

	public String toString() {
		return super.toString();
	}

	public UrlContentMap getUrls() {
		return urls;
	}

	public void setUrls(UrlContentMap urls) {
		this.urls = urls;
	}

	public String getHiddenUrlToAdd() {
		return hiddenUrlToAdd;
	}

	public void setHiddenUrlToAdd(String hiddenUrlToAdd) {
		this.hiddenUrlToAdd = hiddenUrlToAdd;
	}

	public String getSortedNodeToAdd() {
		return sortedNodeToAdd;
	}

	public void setSortedNodeToAdd(String sortedNodeToAdd) {
		this.sortedNodeToAdd = sortedNodeToAdd;
	}

	public String getUrlToAdd() {
		return urlToAdd;
	}

	public void setUrlToAdd(String urlToAdd) {
		this.urlToAdd = urlToAdd;
	}
}