package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import edu.harvard.seas.pl.abcdatalog.ast.validation.DatalogValidationException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;

import java.io.Serializable;
import java.util.HashSet;
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

    private String submission;

    private String[] queries;

    private int exerciseID;

    private Set<PositiveAtom> results;

    private QuerySyntaxException syntaxException;

    private WrappedModel[] models;

    private ModelConsistency consistency;

    public DatalogResult(
            String submission, ABCDatalogProcessor processor, String[] queries, boolean notAllowFacts
    ) throws Exception {
        results = new HashSet<>();
        try {
            this.results = processor.executeQuery(submission, queries, notAllowFacts);
            this.models = getWrappedModels(results);
            this.submission = submission;
            this.queries = queries;
        } catch (DatalogValidationException e) {
            this.syntaxException = new QuerySyntaxException(e);
            this.syntaxException.setDescription(e.getMessage());
            this.models = new WrappedModel[]{};
        } catch (DatalogParseException e) {
            this.syntaxException = new QuerySyntaxException(e);
            this.syntaxException.setDescription(e.getMessage());
            this.models = new WrappedModel[]{};
        }
    }

    private WrappedModel[] getWrappedModels(Set<PositiveAtom> results) {
        WrappedModel[] wrappedModels = new WrappedModel[1];
        wrappedModels[0] = new WrappedModel(results);
        return wrappedModels;
    }

    public WrappedModel[] getWrappedModels(){
        return this.models;
    }


    /**
     * Gets the names of predicates which are considered, if this <code>DatalogResult</code>
     * serves as the "correct" solution for an analysis. The <code>DatalogResult</code>, which is
     * compared with this result object will have to contain the predicates specified here, in order
     * to be correct. All additional predicates will not influence the analysis in any way.
     * 
     * @return The set predicate names.
     */
    public String[] getQueries() {
        return queries;
    }


    /**
     * Returns the query that this <code>DatalogResult</code> was built from.
     * 
     * @return Returns the query.
     */
    public String getQuery() {
        return submission;
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

    public Set<PositiveAtom> getResults() {
        return results;
    }

    public Object getSyntaxException() {
        return this.syntaxException;
    }

    /**
     * Verifies if the query, that this <code>DatalogResult</code> was built from, was evaluated
     * without any problems.
     *
     * @return false if either the query contains syntax errors or a timelimit was reached before
     *         evaluation could be finished.
     * @see #getSyntaxException()
     */
    public boolean isValidResult() {
        return syntaxException == null;
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
    public String getRawResult() {
        String result = "";
        for (int i = 0; i < this.models.length; i++) {
            if (i > 0) {
                result += "\n\n\n";
            }
            WrappedModel model = this.models[i];
            result += model.getRawResult(null);
        }
        return result;
    }

}