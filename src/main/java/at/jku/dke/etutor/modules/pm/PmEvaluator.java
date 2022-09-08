package at.jku.dke.etutor.modules.pm;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;

import java.util.Locale;
import java.util.Map;

public class PmEvaluator implements Evaluator {

    // -> may use PmAnalysis instead of Analysis as return type
    @Override
    public PmAnalysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        // cast Analysis to use PmAnalysis
        PmAnalysis pmAnalysis;
        if(analysis instanceof PmAnalysis)
            pmAnalysis = (PmAnalysis) analysis;
        else
            return null; // or Exception
            
        // Todo: grade based on analysis

        return null;
    }

    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }
}
