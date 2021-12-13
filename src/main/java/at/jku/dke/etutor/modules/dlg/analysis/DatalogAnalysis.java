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
import java.util.List;
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

	private boolean hasCorrectModel;

	private String[] requPredicates;

	private int exerciseID;

	private ArrayList missingPredicates;

	private ArrayList redundantPredicates;

	private ArrayList highArityPredicates;

	private ArrayList lowArityPredicates;

	private ArrayList missingFacts;

	private ArrayList redundantFacts;

	private ArrayList positiveFacts;

	private ArrayList negativeFacts;

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
	 * Creates a new analysis instance by comparing the results of two Datalog
	 * queries.
	 * 
	 * @param result1
	 *          The "correct" query result.
	 * @param result2
	 *          The "submitted" query result.
	 * @param debugMode
	 *          A flag which indicates if intermediate results, which are part of
	 *          the analysis, grading and reporting process, should be saved to
	 *          files.
	 * @throws NullPointerException
	 *           if one of the results is <code>null</code>
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
	public DatalogAnalysis(DatalogResult result1, DatalogResult result2, boolean debugMode) throws NullPointerException,
			TimeoutException, QuerySyntaxException, AnalysisException {
		super();
		init(debugMode);
		this.setResults(result1, result2);
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
	public DatalogAnalysis(String submission1, String submission2, String[] queries, DatalogProcessor processor,
			boolean debugMode) throws Exception {
		super();
		init(debugMode);
		try {
			DatalogResult result1 = new DatalogResult(submission1, (ABCDatalogProcessor) processor, queries);
			DatalogResult result2 = new DatalogResult(submission2, (ABCDatalogProcessor) processor, queries);
			this.setResults(result1, result2);
		} catch (Exception e) {
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
		this.hasCorrectModel = false;
		initErrorLists();
	}

	/**
	 * Initializes all error lists.
	 */
	private void initErrorLists() {
		missingPredicates = new ArrayList();
		redundantPredicates = new ArrayList();
		highArityPredicates = new ArrayList();
		lowArityPredicates = new ArrayList();
		missingFacts = new ArrayList();
		redundantFacts = new ArrayList();
		positiveFacts = new ArrayList();
		negativeFacts = new ArrayList();
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
	 * Does the analysis of two models. Found differences are stored in fields of
	 * this <code>DatalogAnalysis</code>.
	 * 
	 * @param model1
	 *          The "correct" model.
	 * @param model2
	 *          The "submitted" model.
	 */
	private void compare(WrappedModel model1, WrappedModel model2, List requPredicates) {
		if (model1 != null && model2 != null) {
			WrappedPredicate[] predicates2 = model2.getPredicates();
			for (int i = 0; i < predicates2.length; i++) {
				WrappedPredicate pred2 = predicates2[i];
				// only compare predicates denoted by specified filters
				if (requPredicates.contains(pred2.getName())) {
					WrappedPredicate pred1;
					if ((pred1 = model1.getPredicate(pred2.getName())) == null) {
						redundantPredicates.add(pred2);
					} else if (pred2.getArity() < pred1.getArity()) {
						// add the correct predicate, which is used to get the
						// correct arity for the feedback
						lowArityPredicates.add(pred2);
					} else if (pred2.getArity() > pred1.getArity()) {
						// add the correct predicate, which is used to get the
						// correct arity for the feedback
						highArityPredicates.add(pred2);
					}
				}
			}
			// System.out.println("Checking the given solution");
			WrappedPredicate[] predicates1 = model1.getPredicates();
			for (int i = 0; i < predicates1.length; i++) {
				WrappedPredicate pred1 = (WrappedPredicate) predicates1[i];
				// only compare predicates denoted by specified filters
				if (requPredicates.contains(pred1.getName())) {
					WrappedPredicate pred2;
					if ((pred2 = model2.getPredicate(pred1.getName())) == null) {
						missingPredicates.add(pred1);
					} else if (!highArityPredicates.contains(model2.getPredicate(pred1.getName()))
							&& !lowArityPredicates.contains(model2.getPredicate(pred1.getName()))) {
						comparePredicates(pred1, pred2);
					}
				}
			}
		}

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
		for (int i = 0; i < facts2.length; i++) {
			WrappedPredicate.WrappedFact fact2 = facts2[i];
			WrappedPredicate.WrappedFact fact1;
			if ((fact1 = p1.getFact(fact2)) == null) {
				redundantFacts.add(fact2);
			} else if (fact2.isPositive() && !fact1.isPositive()) {
				positiveFacts.add(fact2);
			} else if (!fact2.isPositive() && fact1.isPositive()) {
				negativeFacts.add(fact2);
			}
		}
		WrappedPredicate.WrappedFact[] facts1 = p1.getFacts();
		for (int i = 0; i < facts1.length; i++) {
			WrappedPredicate.WrappedFact fact1 = facts1[i];
			if (p2.getFact(fact1) == null) {
				missingFacts.add(fact1);
			}
		}
	}

	/**
	 * Returns the predicates in the correct query result, which are missing in
	 * the submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()} and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of {@link WrappedPredicate}objects.
	 */
	public ArrayList getMissingPredicates() {
		return missingPredicates;
	}

	/**
	 * Returns the redundant predicates in the submitted query result.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()} and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of {@link WrappedPredicate}objects.
	 */
	public ArrayList getRedundantPredicates() {
		return redundantPredicates;
	}

	/**
	 * Returns the predicates in the submitted query result, which should have
	 * more terms.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()} and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of {@link WrappedPredicate}objects.
	 */
	public ArrayList getLowArityPredicates() {
		return lowArityPredicates;
	}

	/**
	 * Returns the predicates in the submitted query result, which should have
	 * more terms.
	 * <p>
	 * This is only relevant if the submitted result like the correct result
	 * consists of exactly one model which is consistent (see
	 * {@link DatalogResult#hasMultipleModels()} and
	 * {@link DatalogResult#hasConsistentModel()}).
	 * 
	 * @return A list of {@link WrappedPredicate}objects.
	 */
	public ArrayList getHighArityPredicates() {
		return highArityPredicates;
	}

	/**
	 * Returns the facts in the submitted query result, which should be negated
	 * but are not.
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
	public ArrayList getPositiveFacts() {
		return positiveFacts;
	}

	/**
	 * Returns the facts in the submitted query result, which are falsely negated.
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
	public ArrayList getNegativeFacts() {
		return negativeFacts;
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
	 * Tells if the submitted result consists of one or a number of possible
	 * models in which the query is true plus one model among these would be
	 * correct with regard to the correct result.
	 * <p>
	 * Note: The result won't be correct as long as there is more than one model.
	 * The requirement is not only that the model is correct, but that there is no
	 * other possible model.
	 * 
	 * @return true if the result consists of one or more models, where one of
	 *         these is correct.
	 */
	public boolean hasCorrectModel() {
		return hasCorrectModel;
	}

	/**
	 * Tells if the result that was analyzed is correct. The requirements are:
	 * <ul>
	 * <li>{@link #isAnalyzable()}must return true</li>
	 * <li>{@link #hasErrors()}must return true</li>
	 * <li>{@link DatalogResult#hasMultipleModels()}must return false</li>
	 * <li>{@link #hasCorrectModel()}must return true</li>
	 * </ul>
	 * <p>
	 * Note that a call to this is only sensible if an analysis has actually been
	 * carried out.
	 * 
	 * @return true if the submitted query is correct.
	 */
	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Tells if the result that was analyzed is correct. The requirements are:
	 * <ul>
	 * <li>{@link #isAnalyzable()}must return true</li>
	 * <li>{@link #hasErrors()}must return false</li>
	 * <li>{@link DatalogResult#hasMultipleModels()}must return false</li>
	 * <li>{@link #hasCorrectModel()}must return true</li>
	 * </ul>
	 * 
	 * @return true if the submitted query is correct. Note that a call to this is
	 *         only sensible if queries or their results have been compared.
	 */
	private boolean checkCorrectness() {
		return missingFacts.isEmpty() && redundantFacts.isEmpty();
	}

	/**
	 * Tells if there have been found errors like missing or redundant predicates
	 * or facts.
	 * <p>
	 * Note that this alone does not imply that the submitted query is correct
	 * because there are further criteria.
	 * 
	 * @return true if any error entry has been added to the error lists as
	 *         defined in this class.
	 * @see #isCorrect()
	 */
	private boolean hasErrors() {
		boolean hasErrors = (getHighArityPredicates().size() > 0) || (getLowArityPredicates().size() > 0)
				|| (getMissingPredicates().size() > 0) || (getRedundantPredicates().size() > 0)
				|| (getMissingFacts().size() > 0) || (getRedundantFacts().size() > 0) || (getPositiveFacts().size() > 0)
				|| (getNegativeFacts().size() > 0);
		return hasErrors;
	}

	/**
	 * Provides information about whether the query results that were used for
	 * this analysis objects are comparable and analyzable. The requirements are:
	 * <ul>
	 * <li>An analysis has been carried out before.</li>
	 * <li>Both, the submitted and the correct query must be syntactically
	 * correct and evaluated successfully before a <code>TimeoutException</code>
	 * has been thrown.</li>
	 * <li>The correct query result must consist of a single, consistent model,
	 * no matter if empty or not.</li>
	 * <li>The submitted query result must consist of at least one model, no
	 * matter if empty, non-empty, consistent or inconsistent.</li>
	 * </ul>
	 * 
	 * @return true if the results used for this analysis object are analyzable,
	 *         otherwise false.
	 */
	public boolean isAnalyzable() {
		boolean correct = result1 != null && result1.isValidResult() && result2 != null && result2.isValidResult();
		if (correct) {
			int cons1 = result1.getModelConsistency().getConsistency();
			int cons2 = result2.getModelConsistency().getConsistency();
			// The reference solution only has to consist of a single, consistent
			// model, no matter
			// if empty or not.
			correct = cons1 == ModelConsistency.CONSISTENT || cons1 == ModelConsistency.CONSISTENT_EMPTY;
			// The analyzed solution just has to be built from a valid query, no
			// matter if the result is empty
			// or inconsistent or has more than one models.
			correct = correct && cons2 != ModelConsistency.DEFAULT;
		}
		return correct;
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