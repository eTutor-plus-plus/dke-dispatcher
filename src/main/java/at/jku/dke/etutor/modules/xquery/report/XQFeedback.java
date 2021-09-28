package at.jku.dke.etutor.modules.xquery.report;

import etutor.core.evaluation.Report;
import etutor.modules.xquery.ReportException;

/**
 * This interface defines a number of methods which are required when presenting the results,
 * respectively the report of a query analysis. The static constant flags of this interface are
 * intended to be related to the degree of information which is returned by the methods defined in
 * this interface.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */

public interface XQFeedback extends Report {

    /**
     * Flag which represents feedback on the <i>lowest</i> level.
     */
    public final static int DIAGNOSE_NONE = 0;

    /**
     * Flag which represents feedback on <i>low</i> level.
     */
    public final static int DIAGNOSE_LOW = 1;

    /**
     * Flag which represents feedback on <i>medium</i> level.
     */
    public final static int DIAGNOSE_MEDIUM = 2;

    /**
     * Flag which represents feedback on the <i>highest</i> level.
     */
    public final static int DIAGNOSE_HIGH = 3;

    /**
     * A general analysis text holds information which may serve as some kind of introduction, as
     * header or as summary.
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return The general analysis text which may be an empty String.
     */
	public String getGeneralAnalysis(boolean rendered);
    /**
     * Returns a list of mistakes analyzed in the submitted query result. Depending on the diagnose
     * level, the information goes more or less into detail or may be omitted at all.
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return An array of error description objects.
     */
    public ErrorCategory[] getSemanticErrors(boolean rendered);

    /**
     * Returns the result which is rendered with regard to HTML compatibility. The rendered result
     * may contain information which goes beyond the mere rendering of the raw result as it is
     * output by the query processor. Additionally, elements may be added for example to
     * display missing or redundant XML elements in the result of the query using colors. Whether this very
     * detailed result is returned depends on the diagnose level.
     * 
     * @return The rendered result.
     * @see #isSupplementedResult()
     */
    public String getRenderedResult() throws ReportException;

    /**
     * Returns the raw result as it is output by the query processor.
     * 
     * @return The raw result of the query which may be an empty String.
     */
    public String getRawResult();

    /**
     * Returns the syntax errors, as they are output by the query processor. If urls have been 
     * detected in the submitted query, which are not permitted to be accessed for this exercise, 
     * this is treated as syntax error as well.
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return The syntax errors if the query contains any, otherwise <code>null</code>.
     */
    public ErrorCategory getSyntaxError(boolean rendered);
    /**
     * Returns the wellformedness errors of the query result. The result is considered to be not wellformed if the result, 
     * as it is output by the query processor, is embedded into a root element but still can not be parsed as valid XML
     * document. It must be noted that this does not imply the query is incorrect. In fact it just can not be analyzed
     * going into detail, if it is not wellformed.   
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return The wellformedness errors if the query contains any, otherwise an empty String.
     */
    /*
    public String getWellformednessErrors(boolean rendered);
    */
    /**
     * Returns the errors of the query result with regard to document urls and their alias names. Such an error describes
     * an alias name, specified in a document statement within the query, where the corresponding url was not specified
     * in the exercise settings.
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return The url content errors if the query contains any, otherwise an empty String.
     */
    /*
    public String getUrlContentErrors(boolean rendered);
    */
    /**
     * Returns the diagnose level used for generating the feedback messages and rendering the result.
     * 
     * @return The diagnose level.
     */
	public int getDiagnoseLevel();
    /**
     * Returns the submission mode which is used for generating the feedback messages and rendering the result.
     * 
     * @return The submission mode.
     */
	public String getMode();
    /**
     * Indicates whether the rendered result contains information which goes beyond the mere rendering of the raw result as it is
     * output by the query processor. 
     * 
     * @return true if there are additional elements in the rendered query result, false otherwise.
     * @see #getRenderedResult()
     */
    public boolean isSupplementedResult();

    /**
     * A report can be represented in the form of an XML document, which may contain the initial query, the result of that query, maybe
     * supplemented with additional elements expressing the correctness or the error of some node.
     * 
     * @return An XML document representing the overall analysis and grading of a query, returned as String.
     */
    public String getXMLReport();

	
    /**
     * Returns an information about the grading of some query result, respectively the points which
     * have been assigned for the query.
     * 
     * @param rendered Indicates if the returned message should escape specific characters for HTML
     *            compatibility.
     * @return The grading information.
     */
    public String getGrading(boolean rendered);
}