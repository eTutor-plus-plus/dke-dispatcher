package at.jku.dke.etutor.modules.dlg.analysis;

import at.jku.dke.etutor.modules.dlg.AnalysisException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import edu.harvard.seas.pl.abcdatalog.ast.Clause;
import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import edu.harvard.seas.pl.abcdatalog.ast.validation.DatalogValidationException;
import edu.harvard.seas.pl.abcdatalog.engine.DatalogEngine;
import edu.harvard.seas.pl.abcdatalog.engine.bottomup.sequential.SemiNaiveEngine;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParser;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogTokenizer;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class ABCDatalogProcessor implements DatalogProcessor{
    String facts;

    /**
     * Constructs a new <code>ABCDatalogProcessor</code>, which can be used for evaluating queries.
     *
     * @param database       The database facts or any Datalog statements, which will be appended to any
     *                       query which is evaluated using this <code>DatalogProcessor</code>.
     */
    public ABCDatalogProcessor(String database){
        this.facts=database;
    }


    public WrappedModel[] executeQuery(String submission, String[] queries, boolean notAllowFacts) throws QuerySyntaxException {
            try {
                if(notAllowFacts) checkSubmissionForFacts(submission);
                String database = facts + "\n";
                database += submission;
                var r = new StringReader(database);
                DatalogTokenizer t = new DatalogTokenizer(r);
                Set<Clause> prog = DatalogParser.parseProgram(t);
                // You can choose what sort of engine you want here.
                DatalogEngine e = SemiNaiveEngine.newEngine();
                e.init(prog);
                Set<PositiveAtom> results = new HashSet<>();
                for(String query : queries){
                    PositiveAtom q = makeQuery(query);
                    results.addAll(e.query(q));
                }
                return getWrappedModels(results);
            } catch (DatalogParseException | DatalogValidationException | AnalysisException e) {
                throw new QuerySyntaxException(e);
            }
    }

    private WrappedModel[] getWrappedModels(Set<PositiveAtom> results) {
        WrappedModel[] wrappedModels = new WrappedModel[1];
        wrappedModels[0] = new WrappedModel(results);
        return wrappedModels;
    }

    private void checkSubmissionForFacts(String submission) throws DatalogParseException, AnalysisException {
        var r = new StringReader(submission);
        DatalogTokenizer t = new DatalogTokenizer(r);
        Set<Clause> prog = DatalogParser.parseProgram(t);
        for(Clause c : prog){
           if (c.getBody().isEmpty()) throw new AnalysisException("Analysis stopped, as the submission contains fact declarations: \n "+ c);
        }
    }

    private PositiveAtom makeQuery(String query) throws DatalogParseException {
        PositiveAtom q;
        DatalogTokenizer t = new DatalogTokenizer(new StringReader(query));
        q = DatalogParser.parseQuery(t);
        return q;
    }
}
