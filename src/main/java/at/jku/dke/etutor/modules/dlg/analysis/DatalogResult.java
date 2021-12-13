package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.AnalysisException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
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

    private String submission;

    private String[] queries;

    private int exerciseID;

    private Set<PositiveAtom> results;

    private QuerySyntaxException syntaxException;

    public DatalogResult(
            String submission, ABCDatalogProcessor processor, String[] queries, boolean notAllowFacts
    ) throws AnalysisException {
        try {
            this.results = processor.executeQuery(submission, queries, notAllowFacts);
            this.submission = submission;
            this.queries = queries;
        } catch (DatalogValidationException e) {
            e.printStackTrace();
            this.syntaxException = new QuerySyntaxException(e);
        } catch (DatalogParseException e) {
            this.syntaxException = new QuerySyntaxException(e);
            e.printStackTrace();
        }
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
}