package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.InternalException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;

/**
 * Interface that has to be implemented by classes that are intended to function as
 * processors for evaluating datalog submissions
 */
public interface DatalogProcessor {
    /**
     * Executes a datalog program and returns the result,
     * wrapped in an array of {@link at.jku.dke.etutor.modules.dlg.analysis.WrappedModel}
     * @param submission the submission (rules)
     * @param queries the queries that have to be executed
     * @return an array of {@link at.jku.dke.etutor.modules.dlg.analysis.WrappedModel}
     * @throws QuerySyntaxException if a syntax error occurs
     * @throws InternalException if an internal error occurs
     */
    WrappedModel[] executeQuery(String submission, String[] queries) throws QuerySyntaxException, InternalException;
}
