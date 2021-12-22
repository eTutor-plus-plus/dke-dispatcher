package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.InternalException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;

import java.io.Serializable;


/**
 * Represents the evaluation result of a Datalog query as it is returned by a Datalog query
 * processor. The results, which are produced by the underlying query processor, are customized,
 * wrapped and transformed into an internal representation so that analysis, grading and reporting
 * can be carried out.
 * @author Georg Nitsche, Kevin Schütz
 * @version 1.0
 * @since 1.0
 */
public class DatalogResult implements Serializable {

    private String submission;

    private String[] queries;

    private int exerciseID;

    private QuerySyntaxException syntaxException;

    private WrappedModel[] models;


    /**
     * Initializes a new instance
     * @param submission the submission
     * @param processor the {@link at.jku.dke.etutor.modules.dlg.analysis.DatalogProcessor} used to execute the query
     * @param queries the queries that need to be evaluated
     * @throws InternalException if an internal exception occurs
     */
    public DatalogResult(
            String submission, DatalogProcessor processor, String[] queries
    ) throws InternalException {
        try {
            this.models = processor.executeQuery(submission, queries);
            this.submission = submission;
            this.queries = queries;
        } catch (QuerySyntaxException e) {
            this.syntaxException = new QuerySyntaxException(e);
            this.syntaxException.setDescription(e.getMessage());
            this.models = new WrappedModel[]{};
        }
    }


    /**
     * Returns the wrapped models
     * @return the {@link #models}
     */
    public WrappedModel[] getWrappedModels(){
        return this.models;
    }


    /**
     * Gets the queries that have been evaluated
     * @return The set queries.
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
     * @return the model object
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
     */
    public boolean hasConsistentModel() {
        return getConsistentModel() != null;
    }



    /**
     * Returns the complete, unfiltered evaluation result of the query.
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