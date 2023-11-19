package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;

import java.util.Locale;
import java.util.Map;

public class DroolsEvaluator implements Evaluator {

    private ApplicationProperties applicationProperties;

    public DroolsEvaluator(ApplicationProperties properties) {
        super();
        this.applicationProperties = properties;
    }

    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        DroolsAnalysis analysis = new DroolsAnalysis(exerciseID, passedAttributes.get("submission"));
        return analysis;
    }

    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
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
