package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.analysis;

/**
 * @author nitsche
 *  
 */
public class XQAnalysisConfig {
	private String query1;

	private String query2;

	private XQProcessor processor;

	private String[] sortedNodes;

	private UrlContentMap urls;

	private boolean debugMode;
	
	private int userID;
	
	private int exerciseID;

	public XQAnalysisConfig() {
		super();
		this.query1 = "";
		this.query2 = "";
		this.processor = null;
		this.sortedNodes = null;
		this.urls = null;
		this.debugMode = false;
		this.userID = 0;
		this.exerciseID = 0;
	}
	
	/**
	 * @return The processor object to use for evaluating the queries.
	 */
	public XQProcessor getProcessor() {
		return processor;
	}

	/**
	 * @param processor The processor object to use for evaluating the queries.
	 */
	public void setProcessor(XQProcessor processor) {
		this.processor = processor;
	}

	/**
	 * @return The "correct" query.
	 */
	public String getQuery1() {
		return query1;
	}

	/**
	 * @param query1 The "correct" query.
	 */
	public void setQuery1(String query1) {
		this.query1 = query1;
	}

	/**
	 * @return The "submitted" query.
	 */
	public String getQuery2() {
		return query2;
	}

	/**
	 * @param query2 The "submitted" query.
	 */
	public void setQuery2(String query2) {
		this.query2 = query2;
	}

	/**
     * @return A number of XPath expressions which denotes XML nodes, which have to be in
     *            certain order in the submitted query.
	 */
	public String[] getSortedNodes() {
		return sortedNodes;
	}

	/**
	 * @param sortedNodes A number of XPath expressions which denotes XML nodes, which have to be in
     *            certain order in the submitted query.
	 */
	public void setSortedNodes(String[] sortedNodes) {
		this.sortedNodes = sortedNodes;
	}

	/**
	 * @return A map object, which may be <code>null</code> if the submitted query is
     *            analyzed, as given. Otherwise the contents of all <code>doc</code> function
     *            statements within the query will be interpreted as alias names and replaced by
     *            their corresponding real document paths, taken from the <code>UrlContentMap</code>.
	 */
	public UrlContentMap getUrls() {
		return urls;
	}

	/**
	 * @param urls A map object, which may be <code>null</code> if the submitted query is
     *            analyzed, as given. Otherwise the contents of all <code>doc</code> function
     *            statements within the query will be interpreted as alias names and replaced by
     *            their corresponding real document paths, taken from the <code>UrlContentMap</code>.
	 */
	public void setUrls(UrlContentMap urls) {
		this.urls = urls;
	}
	
	/**
	 * @return A flag which indicates if intermediate results, which are part of the
     *            analysis, grading and reporting process, should be saved to files.
	 */
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/**
	 * @param debugMode A flag which indicates if intermediate results, which are part of the
     *            analysis, grading and reporting process, should be saved to files.
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}
	
	/**
	 * @return
	 */
	/**
	 * @return
	 */
	public int getExerciseID() {
		return exerciseID;
	}
	
	/**
	 * @param exerciseID
	 */
	public void setExerciseID(int exerciseID) {
		this.exerciseID = exerciseID;
	}
	
	/**
	 * @return
	 */
	public int getUserID() {
		return userID;
	}
	
	/**
	 * @param userID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
}