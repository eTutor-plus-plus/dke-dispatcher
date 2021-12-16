package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.AnalysisException;
import at.jku.dke.etutor.modules.dlg.ParameterException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import at.jku.dke.etutor.modules.dlg.ReportException;
import at.jku.dke.etutor.modules.dlg.grading.DatalogGrading;
import at.jku.dke.etutor.modules.dlg.report.DatalogFeedback;
import at.jku.dke.etutor.modules.dlg.report.DatalogReport;
import at.jku.dke.etutor.modules.dlg.util.FileParameter;
import at.jku.dke.etutor.modules.dlg.util.XMLUtil;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class DatalogAnalysis implements Analysis {

	/**
	 * The logger used for logging.
	 */
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogAnalysis.class);

	private final String TEMP_FOLDER;

	private Serializable submission;

	private DatalogResult result1;

	private DatalogResult result2;

	private boolean correct;

	private String[] requPredicates;

	private int exerciseID;

	private ArrayList<WrappedPredicate> missingPredicates;

	private ArrayList<WrappedPredicate.WrappedFact> missingFacts;

	private ArrayList<WrappedPredicate.WrappedFact> redundantFacts;

	private boolean debugMode;

	/**
	 * Creates a new instance. Unless the query results are not set using
	 * {@link #setResults(DatalogResult, DatalogResult)}, the created object must
	 * be considered as blank analysis, which can not be reported or graded.
	 */
	public DatalogAnalysis() {
		super();
		this.TEMP_FOLDER="";
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
	 *          A number of queries which are executed for both submissions, and so to be considered when analyzing.
	 * @param processor
	 *          The processor object to use for evaluating the queries. This holds
	 *          the common database for both queries.
	 * @param debugMode
	 *          A flag which indicates if intermediate results, which are part of
	 *          the analysis, grading and reporting process, should be saved to
	 *          files.
	 */
	public DatalogAnalysis(String submission1, String submission2, String[] queries, DatalogProcessor processor,
						   boolean debugMode, boolean notAllowFacts, ApplicationProperties properties) {
		super();
		this.TEMP_FOLDER = properties.getDatalog().getTempFolder();
		init(debugMode);
		try {
			var r1 = new DatalogResult(submission1, processor, queries, false);
			var r2 = new DatalogResult(submission2, processor, queries, notAllowFacts);
			this.setResults(r1, r2);
		}
		catch (Exception e) {
			this.correct = false;
			LOGGER.error("Analysis error", e);
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
	public void setResults(DatalogResult result1, DatalogResult result2) throws NullPointerException,
			QuerySyntaxException, AnalysisException {
		LOGGER.debug("Start comparing correct and submitted result object");
		Objects.requireNonNull(result1);
		Objects.requireNonNull(result2);

		this.result1 = result1;
		this.result2 = result2;
		this.requPredicates = result1.getQueries();

		if(result1.getSyntaxException() != null || result2.getSyntaxException() != null){
			this.correct = false;
			return;
		}

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
		missingPredicates = new ArrayList<>();
		missingFacts = new ArrayList<>();
		redundantFacts = new ArrayList<>();
	}


	private void compare(DatalogResult result1, DatalogResult result2) throws AnalysisException {
		var model1 = result1.getConsistentModel();
		var model2 = result2.getConsistentModel();
		if (model1 != null && model2 != null) {
			var predicates1 = model1.getPredicates();
			for (WrappedPredicate pred1 : predicates1) {
				WrappedPredicate pred2;
				if ((pred2 = model2.getPredicate(pred1.getName())) == null) {
					missingPredicates.add(pred1);
				} else {
					comparePredicates(pred1, pred2);
				}
			}
		}else throw new AnalysisException("Analysis stopped, as one of the results does not contain a consistent model");
	}

	/**
	 * Compares two predicates with regard to the differences concerning their
	 * facts.
	 *
	 * @param p1
	 *          The first predicate.
	 * @param p2
	 *          The second predicate.
	 */
	private void comparePredicates(WrappedPredicate p1, WrappedPredicate p2) {
		WrappedPredicate.WrappedFact[] facts2 = p2.getFacts();
		for (WrappedPredicate.WrappedFact fact2 : facts2) {
			WrappedPredicate.WrappedFact fact1;
			if ((p1.getFact(fact2)) == null) {
				redundantFacts.add(fact2);
			}
		}
		WrappedPredicate.WrappedFact[] facts1 = p1.getFacts();
		for (WrappedPredicate.WrappedFact fact1 : facts1) {
			if (p2.getFact(fact1) == null) {
				missingFacts.add(fact1);
			}
		}
	}


	/**
	 * Returns the facts in the correct query result, that are missing in the
	 * submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of
	 *         {@link WrappedPredicate.WrappedFact}
	 *         objects.
	 */
	public List<WrappedPredicate.WrappedFact> getMissingFacts() {
		return missingFacts;
	}

	/**
	 * Returns the redundant facts in the submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of
	 *         {@link WrappedPredicate.WrappedFact}
	 *         objects.
	 */
	public List<WrappedPredicate.WrappedFact> getRedundantFacts() {
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
		return missingFacts.isEmpty() && redundantFacts.isEmpty() && missingPredicates.isEmpty();
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

	public List<WrappedPredicate> getMissingPredicates() {
		return missingPredicates;
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
		String prefix = null;
		String suffix = null;
		return new FileParameter(TEMP_FOLDER, prefix, suffix, deleteOnExit, defaultOnError);
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