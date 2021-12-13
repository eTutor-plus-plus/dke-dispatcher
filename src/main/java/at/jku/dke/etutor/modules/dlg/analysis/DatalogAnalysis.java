package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.modules.dlg.*;
import at.jku.dke.etutor.modules.dlg.grading.DatalogGrading;
import at.jku.dke.etutor.modules.dlg.report.DatalogFeedback;
import at.jku.dke.etutor.modules.dlg.report.DatalogReport;
import at.jku.dke.etutor.modules.dlg.util.FileParameter;
import at.jku.dke.etutor.modules.dlg.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * An instance of this class takes two queries or already the result of two
 * Datalog queries as input, analyzes them and prepares the found differences
 * and errors for further processing (grading and reporting). <br>
 * The first query is considered as the " <i>correct </i>" query and the second
 * as the " <i>submitted </i>" one, which has to be compared with the correct
 * solution. This numbering is used throughout the module to identify the
 * correct and the submitted solution. <br>
 * The correct query must be syntactically correct and evaluated by the query
 * processor before a defined time limit has been reached, otherwise an
 * Exception is thrown. On the other hand, if the <i>submitted </i>query is
 * syntactically incorrect or takes too long to evaluate, this is part of the
 * analysis and will not cause an Exception to be thrown.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogAnalysis implements Analysis, Serializable {

	/**
	 * The logger used for logging.
	 */
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogAnalysis.class);

	private Serializable submission;

	private DatalogResult result1;

	private DatalogResult result2;

	private boolean correct;

	private String[] requPredicates;

	private int exerciseID;

	private ArrayList missingFacts;

	private ArrayList redundantFacts;

	private boolean debugMode;

	/**
	 * Creates a new instance. Unless the query results are not set using
	 * {@link #setResults(DatalogResult, DatalogResult)}, the created object must
	 * be considered as blank analysis, which can not be reported or graded.
	 */
	public DatalogAnalysis() {
		super();
		init(false);
	}


	/**
	 * Creates a new analysis instance by evaluating the given queries and
	 * comparing the produced results.
	 * 
	 * @param submission1
	 *          The "correct" query.
	 * @param submission2
	 *          The "submitted" query.
	 * @param queries
	 *          A number of predicates which are required to be present in the
	 *          result of both queries, and so to be considered when analyzing.
	 *          This may be an empty array or even <code>null</code>, which
	 *          would imply that any result is correct, no matter how different it
	 *          is from the correct solution.
	 * @param processor
	 *          The processor object to use for evaluating the queries. This holds
	 *          the common database for both queries.
	 * @param debugMode
	 *          A flag which indicates if intermediate results, which are part of
	 *          the analysis, grading and reporting process, should be saved to
	 *          files.
	 * @throws ParameterException
	 *           if the specified predicates, which should be considered when
	 *           analyzing do not even exist in the result of the "correct" query.
	 * @throws InitException
	 *           if the processor can not be set up properly for evaluating the
	 *           queries.
	 * @throws TimeoutException
	 *           if the correct query result object was created from a query,
	 *           which could not be evaluated in time.
	 * @throws QuerySyntaxException
	 *           if the correct query result object was created from a query,
	 *           which is syntactically incorrect.
	 * @throws AnalysisException
	 *           if any kind of unexpected Exception occured during analyzing the
	 *           results.
	 */
	public DatalogAnalysis(String submission1, String submission2, String[] queries, ABCDatalogProcessor processor,
			boolean debugMode, boolean notAllowFacts) throws Exception {
		super();
		init(debugMode);
		try {
			DatalogResult result1 = new DatalogResult(submission1, processor, queries, false);
			DatalogResult result2 = new DatalogResult(submission2, processor, queries, notAllowFacts);
			this.setResults(result1, result2);
		}
		catch (Exception e) {
			this.correct = false;
			LOGGER.error("Analysis error", e);
			throw e;
		}
	}

	/**
	 * Sets the results to be compared and analyzes the differences.
	 * 
	 * @param result1
	 *          The "correct" query result.
	 * @param result2
	 *          The "submitted" query result.
	 * @throws NullPointerException
	 *           if one of the results is <code>null</code>
	 * @throws TimeoutException
	 *           if the correct query result object was created from a query,
	 *           which could not be evaluated in time.
	 * @throws QuerySyntaxException
	 *           if the correct query result object was created from a query,
	 *           which is syntactically incorrect.
	 * @throws AnalysisException
	 *           This exception is thrown if the correct solution is not
	 *           applicable for serving as reference solution, because it consists
	 *           of more than one model or of exactly one, but nevertheless
	 *           inconsistent model, or if any kind of unexpected Exception
	 *           occured when analyzing the results.
	 */
	public void setResults(DatalogResult result1, DatalogResult result2) throws NullPointerException, TimeoutException,
			QuerySyntaxException, AnalysisException {
		LOGGER.debug("Start comparing correct and submitted result object");
		this.result1 = result1;
		this.result2 = result2;
		this.requPredicates = result1.getRequPredicates();
		compare(result1, result2);
		this.correct = this.checkCorrectness();
		this.setSubmission(result2.getQuery());
		if (isDebugMode()) {
			LOGGER.info("Writing the correct and submitted Datalog results as files (debug mode)");
			try {
				File tempFile = XMLUtil.generateTempFile(this.getResult1FileParameter());
				XMLUtil.printFile(result1.getRawResult(), tempFile);
				LOGGER.info("Written to file " + tempFile.getAbsolutePath());
				tempFile = XMLUtil.generateTempFile(this.getResult2FileParameter());
				XMLUtil.printFile(result2.getRawResult(), tempFile);
				LOGGER.info("Written to file " + tempFile.getAbsolutePath());
			} catch (IOException e) {
				String msg = "";
				msg += "An internal exception was thrown when writing a file written only in debug modus.";
				LOGGER.error(msg, e);
				throw new AnalysisException(msg, e);
			}
		}
	}

	/**
	 * Initializes the fields of this analysis object.
	 * 
	 * @param debugMode
	 *          Flag which is set to know if intermediate results should be saved
	 *          to files when analyzing, grading, and reporting later on.
	 */
	private void init(boolean debugMode) {
		setDebugMode(debugMode);
		requPredicates = new String[] {};
		this.correct = false;
		initErrorLists();
	}

	/**
	 * Initializes all error lists.
	 */
	private void initErrorLists() {
		missingFacts = new ArrayList();
		redundantFacts = new ArrayList();
	}

	/**
	 * Does the analysis of two Datalog query results. Found differences are
	 * stored in fields of this <code>DatalogAnalysis</code>.
	 * 
	 * @param result1
	 *          The "correct" query result.
	 * @param result2
	 *          The "submitted" query result.
	 */
	private void compare(DatalogResult result1, DatalogResult result2) {
		var results1 = result1.getResults();
		var results2 = result2.getResults();
		if (results1 != null) {
			if (results2 != null) {
				compare(results1, results2);
			}
		}
	}

	private void compare(Set<PositiveAtom> results1, Set<PositiveAtom> results2) {
		for(PositiveAtom p : results1){
			if(!results2.contains(p)){
				missingFacts.add(p);
			}
		}
		for(PositiveAtom p : results2){
			if(!results1.contains(p)){
				redundantFacts.add(p);
			}
		}
	}


	/**
	 * Returns the facts in the correct query result, that are missing in the
	 * submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()} and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of
	 *         {@link WrappedPredicate.WrappedFact}
	 *         objects.
	 */
	public ArrayList getMissingFacts() {
		return missingFacts;
	}

	/**
	 * Returns the redundant facts in the submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()}and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of
	 *         {@link WrappedPredicate.WrappedFact}
	 *         objects.
	 */
	public ArrayList getRedundantFacts() {
		return redundantFacts;
	}

	/**
	 * Returns the correct query result object, which was used for this analysis
	 * object to analyze errors.
	 * 
	 * @return The correct query result object.
	 */
	public DatalogResult getResult1() {
		return result1;
	}

	/**
	 * Returns the submitted query result object, which was used for this analysis
	 * object to analyze errors.
	 * 
	 * @return The submitted query result object.
	 */
	public DatalogResult getResult2() {
		return result2;
	}

	/**
	 * Returns the report object, which is generated from the results of the
	 * analysis done in this <code>DatalogAnalysis</code>.
	 * 
	 * @param grading
	 *          A grading object which contains information about points which
	 *          have been assigned to the submitted query of the analysis. The
	 *          grading information will not be used for the returned
	 *          <code>DatalogFeedback</code>, if the object is
	 *          <code>null</code> or if {@link DatalogGrading#isReporting()}of
	 *          the specified object returns false.
	 * @param filters
	 *          A number of predicate names which indicates the predicates of the
	 *          submitted query result that have to be considered in the output.
	 *          If this is <code>null</code>, by default all available
	 *          predicates of the submitted query result are taken.
	 * @param diagnoseLevel
	 *          A level which defines the degree of how detailed the report is
	 *          going to be.
	 * @return The feedback object.
	 * @throws ParameterException
	 *           if the diagnose level is no valid diagnose level (see
	 *           {@link DatalogFeedback}).
	 * @throws ReportException
	 *           if any kind of unexpected Exception occured when doing the
	 *           report.
	 */
	public DatalogFeedback getFeedback(DatalogGrading grading, String[] filters, int diagnoseLevel)
			throws ParameterException, ReportException {
		return new DatalogReport(this, grading, filters, diagnoseLevel);
	}


	/**
	 * Tells if the result that was analyzed is correct. The requirements are:
	 * Note that a call to this is only sensible if an analysis has actually been
	 * carried out.
	 * 
	 * @return true if the submitted query is correct.
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Tells if the result that was analyzed is correct.
	 *
	 * @return true if the submitted query is correct. Note that a call to this is
	 *         only sensible if queries or their results have been compared.
	 */
	private boolean checkCorrectness() {
		return missingFacts.isEmpty() && redundantFacts.isEmpty();
	}

	/**
	 * Returns the names of the predicates that were set for the analysis. If set,
	 * these predicates have to be in the result of a query result, in order to be
	 * correct.
	 * 
	 * @return An array of predicate names.
	 */
	public String[] getRequPredicates() {
		return requPredicates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see etutor.core.evaluation.Analysis#setSubmission(java.io.Serializable)
	 */
	public void setSubmission(Serializable submission) {
		this.submission = submission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see etutor.core.evaluation.Analysis#getSubmission()
	 */
	public Serializable getSubmission() {
		return this.submission;
	}

	/**
	 * Returns the exercise id.
	 * 
	 * @return The exercise id.
	 */
	public int getExerciseID() {
		return this.exerciseID;
	}

	/**
	 * Sets the exercise id.
	 * 
	 * @param exerciseID
	 *          The exercise id to set.
	 */
	public void setExerciseID(int exerciseID) {
		this.exerciseID = exerciseID;
	}

	/**
	 * Checks if the analysis and subsequently grading and reporting is done in
	 * debug modus.
	 * 
	 * @return true if the analysis is run in debug modus, implying that
	 *         intermediate results of the analysis, grading and reporting process
	 *         are saved to files.
	 */
	public boolean isDebugMode() {
		return this.debugMode;
	}

	/**
	 * Sets the debug modus.
	 * 
	 * @param debugMode
	 *          The debugMode to set.
	 */
	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	/**
	 * This returns an object which holds all necessary information for creating a
	 * file which is included in the analysis, the grading or the reporting
	 * process. Files are created only in debug mode and saved in a folder named
	 * <code>temp</code> within a folder named <code>datalog</code>.
	 * 
	 * @return An object holding information for the file to create.
	 */
	private FileParameter createDefaultFileParameter() {
		boolean defaultOnError = false;
		boolean deleteOnExit = false;
		String folder = DatalogCoreManager.TEMP_FOLDER;
		String prefix = null;
		String suffix = null;
		return new FileParameter(folder, prefix, suffix, deleteOnExit, defaultOnError);
	}

	/**
	 * Gets designated parameters for temp file of the correct query result
	 * 
	 * @return An object holding information for the file to create.
	 */
	public FileParameter getResult1FileParameter() {
		FileParameter parameter = createDefaultFileParameter();
		parameter.setTempPrefix("datalog01a_");
		parameter.setTempSuffix(".txt");
		return parameter;
	}

	/**
	 * Gets designated parameters for XML temp file of the submitted query result
	 * 
	 * @return An object holding information for the file to create.
	 */
	public FileParameter getResult2FileParameter() {
		FileParameter parameter = createDefaultFileParameter();
		parameter.setTempPrefix("datalog01b_");
		parameter.setTempSuffix(".txt");
		return parameter;
	}

	/**
	 * Gets designated parameters for XML temp file of the report document
	 * 
	 * @return An object holding information for the file to create.
	 */
	public FileParameter getReportFileParameter() {
		FileParameter parameter = createDefaultFileParameter();
		parameter.setTempPrefix("datalog02_");
		parameter.setTempSuffix(".xml");
		return parameter;
	}

	/**
	 * Gets designated parameters for HTML temp file of the rendered result
	 * 
	 * @return An object holding information for the file to create.
	 */
	public FileParameter getHTMLFileParameter() {
		FileParameter parameter = createDefaultFileParameter();
		parameter.setTempPrefix("datalog03_");
		parameter.setTempSuffix(".html");
		return parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see etutor.core.evaluation.Analysis#setSubmissionSuitsSolution(boolean)
	 */
	public void setSubmissionSuitsSolution(boolean b) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see etutor.core.evaluation.Analysis#submissionSuitsSolution()
	 */
	public boolean submissionSuitsSolution() {
		return this.isCorrect();
	}
}