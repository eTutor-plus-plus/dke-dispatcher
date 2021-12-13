package at.jku.dke.etutor.modules.dlg.analysis;

import DLV.Model;
import at.jku.dke.etutor.modules.dlg.*;
import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import edu.harvard.seas.pl.abcdatalog.ast.validation.DatalogValidationException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;

import java.io.Serializable;
import java.util.Set;


/**
 * Represents the evaluation result of a Datalog query as it is returned by a Datalog query
 * processor. The results, which are produced by the underlying query processor, are customized,
 * wrapped and transformed into an internal representation so that analysis, grading and reporting
 * can be carried out. The term "result" stands for the following elements: <br>
 * <ul>
 * <li>The result might consist of a number of <i>models </i>. Each of these models is detected by
 * the Datalog processor as satisfying the query. The cases are:
 * <ul>
 * <li>In the context of this class, there is <b>no </b> model at all, if the query has syntactical
 * errors or caused timeout errors.</li>
 * <li>There is <b>no consistent </b> model, if the query is inconsistent. This is indicated by a
 * model returned by this <code>DatalogResult</code>, which infact is no consistent model.</li>
 * <li>There is exactly <b>one consistent </b> model, which is the normal case.</li>
 * <li>There is <b>more than one </b> model, which usually is the consequence of a query, which
 * consists of some <i>non-stratified </i> or <i>disjunctive </i> rules.</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogResult implements Serializable {

    private String query;

    private String database;

    private String[] requPredicates;

    private int maxInt;

    private int exerciseID;

    private WrappedModel[] models;

    private QuerySyntaxException syntaxException;

    private TimeoutException timeoutException;

    private ModelConsistency consistency;

    private Set<PositiveAtom> results;

    /**
     * Constructs a new object that represents the evaluation result of the specified query,
     * evaluated by the specified processor. The query may contain syntax errors, which can be
     * checked subsequently. It is also possible that the query can not be evaluated before the
     * processor stops evaluation due to a set time limit, which can be checked as well.
     * 
     * @param query The Datalog query which is evaluated using the specified processor.
     * @param processor The Datalog processor, which is used to evaluate the query and which holds
     *            the database as well as additional parameters like the time limit for evaluating
     *            queries.
     * @throws InitException if any unexpected Exception occured when setting up the Datalog
     *             processor for evaluation.
     * @see #getSyntaxException()
     * @see #getTimeoutException()
     */
    public DatalogResult(
            String query, DatalogProcessor processor) throws InitException {
        this.query = query;
        this.maxInt = processor.getMaxInt();
        this.database = processor.getDatabase();
        try {
        	  System.out.println("DatalogResult before executeQuery");
            Model[] models = processor.executeQuery(query, false);
        	  System.out.println("DatalogResult after executeQuery");
        	  System.out.println("Models length: " + models.length);
        	  System.out.println("Models: " + models);
            this.models = getWrappedModels(models);
        	  System.out.println("DatalogResult after wrapping");
        } catch (QuerySyntaxException e) {
            this.models = new WrappedModel[]{};
            this.syntaxException = e;
        } catch (TimeoutException e) {
            this.models = new WrappedModel[]{};
            this.timeoutException = e;
        }
        this.consistency = new ModelConsistency(this);
    	  System.out.println("DatalogResult after consistency");
    }


    public DatalogResult(
            String submission, ABCDatalogProcessor processor, String[] queries
    ) throws AnalysisException {
        try {
            this.results = processor.executeQuery(submission, queries, true);
            this.query = submission;
        } catch (DatalogValidationException e) {
            e.printStackTrace();
        } catch (DatalogParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Customizes a number of models by transforming each model including all predicates, facts and
     * terms into an internal representation, which provides all necessary information for analysis,
     * grading and reporting within this module.
     * 
     * @param models The models which are the original result of the query processor.
     * @return An array of customized, wrapped objects representing the passed models.
     */
    private WrappedModel[] getWrappedModels(Model[] models) {
        WrappedModel[] wrappedModels = new WrappedModel[models.length];
        for (int i = 0; i < models.length; i++) {
            wrappedModels[i] = new WrappedModel(models[i]);
        }
        return wrappedModels;
    }

    /**
     * Returns the models which were created from the query result of this
     * <code>DatalogResult</code>.
     * 
     * @return an array containing all models produced by the Datalog processor and transformed for
     *         internal use.
     */
    public WrappedModel[] getWrappedModels() {
        return models;
    }

    /**
     * Sets the names of predicates which will be considered, if this <code>DatalogResult</code>
     * is compared with another <code>DatalogResult</code> when analyzing, thus serving as
     * "correct" solution. The <code>DatalogResult</code>, which is compared with this result
     * object will have to contain the predicates specified here, in order to be correct. All
     * additional predicates will not influence the analysis in any way. This requires that there is
     * just a single and consistent model which resulted from the query.
     * 
     * @param requPredicates A number of predicates which are required to be present in the result
     *            of both queries, and so to be considered when analyzing. This may be an empty
     *            array or even <code>null</code>, which would imply that any result is correct,
     *            no matter how different it is from the correct solution.
     * @throws ParameterException if the array defines at least one predicate and one of the
     *             following applies:
     *             <ul>
     *             <li>This <code>DatalogResult</code> represents more than one model.</li>
     *             <li>There is exactly one model, which is nevertheless inconsistent.</li>
     *             <li>The model is consistent but there is at least one name among the given
     *             predicate names, where the corresponding predicate does not exist.</li>
     *             </ul>
     */
    public void setRequPredicates(String[] requPredicates) throws ParameterException {
        this.requPredicates = checkRequPredicates(requPredicates);
    }


    /**
     * Verifies if a set of given predicates, identified by name, are existing in a model of this
     * <code>DatalogResult</code>.
     * 
     * @param predicates Names of the predicates to check.
     * @return The very same array that was passed.
     * @throws ParameterException if this <code>DatalogResult</code> represents more than one
     *             model, if there is exactly one model, which is nevertheless inconsistent, or if
     *             the model is consistent but there is at least one name among the given predicate
     *             names, where the corresponding predicate does not exist.
     */
    private String[] checkRequPredicates(String[] predicates) throws ParameterException {
        if (predicates != null) {
            if (this.getTimeoutException() != null) {
            	String message = "";
                message += "Required predicates cannot be set for the correct query result. ";
                message += this.getTimeoutException().getMessage();
                throw new ParameterException(message);
            }
            if (this.getSyntaxException() != null) {
            	String message = "";
                message += "Required predicates cannot be set for the correct query result. ";
                message += "Query result cannot be processed due to syntax errors.\n";
                message += this.getSyntaxException().getDescription();
                throw new ParameterException(message);
            }
            WrappedModel consistentModel = getConsistentModel();

            if (consistentModel == null) {
                //The result has no single and consistent model, an exception is only raised,
                //if predicates were attempted to be set at all.
                if (predicates.length > 0) {
                	String message = "";
                    message += "Required predicates cannot be set for the correct query result. ";
                    message += "The result has more than one model, or a single but ";
                    message += "nevertheless inconsistent model.";
                    throw new ParameterException(message);
                }
            }
            String missingPredicates = null;
            for (int i = 0; i < predicates.length; i++) {
                if (consistentModel.getPredicate(predicates[i]) == null) {
                    if (missingPredicates == null) {
                        missingPredicates = "";
                    }
                    missingPredicates += "\n\t" + predicates[i];
                }
            }
            if (missingPredicates != null) {
            	String msg = "";
                msg += "Required predicates cannot be set for the correct query result. ";
                msg += "The following predicates do not exist in datalog result:";
                msg += missingPredicates;
                throw new ParameterException(msg);
            }
        }
        return predicates;

    }

    /**
     * Gets the names of predicates which are considered, if this <code>DatalogResult</code>
     * serves as the "correct" solution for an analysis. The <code>DatalogResult</code>, which is
     * compared with this result object will have to contain the predicates specified here, in order
     * to be correct. All additional predicates will not influence the analysis in any way.
     * 
     * @return The set predicate names.
     */
    public String[] getRequPredicates() {
        return requPredicates;
    }


    /**
     * Returns the Datalog database facts as defined in the <code>DatalogProcessor</code>, which
     * was used to evaluate the query of this <code>DatalogResult</code>.
     * 
     * @return The String representing the Datalog database.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Returns the highest integer as defined in the {@link DatalogProcessor}, which was used to
     * evaluate the query of this <code>DatalogResult</code>.
     * 
     * @return The integer that was set for the processor which evaluated the query of this
     *         <code>DatalogResult</code>.
     * @see #getSyntaxException()
     */
    public int getMaxInt() {
        return maxInt;
    }

    /**
     * Returns the query that this <code>DatalogResult</code> was built from.
     * 
     * @return Returns the query.
     */
    public String getQuery() {
        return query;
    }


    /**
     * Returns an analysis object which analyzes the differences between the query result of another
     * <code>DatalogResult</code> and this object, where this object is considered to be the
     * correct solution. Therefor predicates, which are required to be in the result and which are
     * considered in the analysis, are taken from this <code>DatalogResult</code>.
     * 
     * @param result The query result, which is compared with this <code>DatalogResult</code> and
     *            analyzed with regard to errors in this context.
     * @param debugMode A flag which indicates if intermediate results, which are part of the
     *            analysis, grading and reporting process, should be saved to files.
     * @return The analysis object, which contains the detected errors in the result and which can
     *         be used for further processing, like grading and reporting.
     * @throws NullPointerException if the passed result is <code>null</code>.
     * @throws TimeoutException if this <code>DatalogResult</code> was created from a query, which
     *             could not be evaluated in time.
     * @throws QuerySyntaxException if this <code>DatalogResult</code> was created from a query,
     *             which is syntactically incorrect.
     * @throws AnalysisException if any kind of unexpected Exception occured during analyzing the
     *             results.
     * @see DatalogAnalysis#DatalogAnalysis(DatalogResult, DatalogResult, boolean)
     */
    public DatalogAnalysis getDiff(DatalogResult result, boolean debugMode)
            throws NullPointerException, TimeoutException, QuerySyntaxException, AnalysisException {
        return new DatalogAnalysis(this, result, debugMode);
    }

    /**
     * Returns the syntax exceptions that were detected when evaluating the query, that this
     * <code>DatalogResult</code> was built from.
     * 
     * @return The QuerySyntaxException, which contains information about the syntax exceptions that
     *         were detected, or <code>null</code> if the query is correct and was evaluated
     *         without syntax problems.
     * @see QuerySyntaxException#getDescription()
     */
    public QuerySyntaxException getSyntaxException() {
        return syntaxException;
    }

    /**
     * Returns the timeout exception that was generated during evaluation of the query, that this
     * <code>DatalogResult</code> was built from. This exception indicates that a time limit was
     * reached before the query could be evaluated successfully.
     * 
     * @return The TimeoutException, which contains information about the time limit, or
     *         <code>null</code> if the query was evaluated without timeout problems.
     * @see TimeoutException#getTimeout()
     * @see DatalogProcessor
     */
    public TimeoutException getTimeoutException() {
        return timeoutException;
    }


    /**
     * Verifies if the query, that this <code>DatalogResult</code> was built from, was evaluated
     * without any problems.
     * 
     * @return false if either the query contains syntax errors or a timelimit was reached before
     *         evaluation could be finished.
     * @see #getSyntaxException()
     * @see #getTimeoutException()
     */
    public boolean isValidResult() {
        return syntaxException == null && timeoutException == null;
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
     * @param exerciseID The exercise id to set.
     */
    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    /**
     * Returns the consistence of this <code>DatalogResult</code>, which represents the result of
     * a datalog query. If true is returned, this object contains a single model, which is
     * consistent and thus can be used for analyzing it to another query result.
     * 
     * @return true, if the query is not contradictory in any sense, so that there is a single model
     *         in which the query is satisfied, else false.
     * @see ModelConsistency
     */
    public boolean hasConsistentModel() {
        return getConsistentModel() != null;
    }

    /**
     * Tells if the result consists of a number of possible models in which the query is
     * true.
     * <p>
     * 
     * @return true if the result consists of more than a single model, otherwise false.
     */
    public boolean hasMultipleModels() {
        return getWrappedModels().length > 1;
    }

    /**
     * Returns the consistent model of this <code>DatalogResult</code>, which represents the
     * result of a datalog query. This will only return an object, if the result of the query
     * consists of a single model, which is consistent and thus can be used for analyzing it to
     * another query result. In any other case this returns <code>null</code>.
     * 
     * @return the model object, if the query is not contradictory in any sense, so that there is a
     *         single model in which the query is satisfied, else <code>null</code>.
     * @see ModelConsistency
     */
    public WrappedModel getConsistentModel() {
        if (this.models.length == 1) {
            WrappedModel model = this.models[0];
            if (model.isConsistent()) {
                return model;
            }
        }
        return null;
    }

    /**
     * Returns the result of the query, which may consist of more than one model. Models are
     * separated from each other by blank lines. This is equivalent to invoking
     * {@link #getRawResult(String[]) getRawResult(null)}.
     * 
     * @return The evaluation result of the query. This might not only be an empty String if there
     *         are no predicates within some models, but also if the query contains syntax errors or
     *         evaluation was stopped after reaching a time limit.
     */
    public String getRawResult() {
        return getRawResult(null);
    }

    /**
     * Returns the complete, unfiltered evaluation result of the query. This is equivalent to
     * invoking {@link #getRawResult(String[]) getRawResult(null)}.
     * 
     * @param filters A set of predicate names indicating which predicates should be included in the
     *            returned result. If this parameter is <code>null</code>, all available
     *            predicates of the actually considered model are selected by default.
     * @return The evaluation result of the query. This might not only be an empty String if there
     *         are no facts returned, but also if the query contains syntax errors or evaluation was
     *         stopped after reaching a time limit.
     */
    public String getRawResult(String[] filters) {
        String result = "";
        for (int i = 0; i < this.models.length; i++) {
            if (i > 0) {
                result += "\n\n\n";
            }
            WrappedModel model = this.models[i];
            result += model.getRawResult(filters);
        }
        return result;
    }

    /**
     * Returns an object which provides information about the characteristics of the query result 
     * with regard to the number of models it contains, whether these models are empty or contain 
     * some predicates, or whether they are <i>consistent</i> at all.
     * 
     * @return An object holding information about the consistency analysis.
     */
    public ModelConsistency getModelConsistency() {
        return this.consistency;
    }

    public Set<PositiveAtom> getResults() {
        return results;
    }

    public void setResults(Set<PositiveAtom> results) {
        this.results = results;
    }
}