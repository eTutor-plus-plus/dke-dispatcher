package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.InternalException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;

public interface DatalogProcessor {
    WrappedModel[] executeQuery(String submission, String[] queries, boolean notAllowFacts) throws QuerySyntaxException, InternalException;
}
