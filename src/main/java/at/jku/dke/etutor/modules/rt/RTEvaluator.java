package at.jku.dke.etutor.modules.rt;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import at.jku.dke.etutor.modules.rt.grading.rtGrading;

import java.util.Locale;
import java.util.Map;

public class RTEvaluator implements Evaluator {
    private ApplicationProperties applicationProperties;
    public RTEvaluator(ApplicationProperties properties) {
        this.applicationProperties = properties;
    }

    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        RTAnalysis rtAnalysis = new RTAnalysis(exerciseID,passedAttributes.get("submission"),this.applicationProperties);
        System.out.println("DB: " + rtAnalysis.getSolution().toString());
        System.out.println("Punkte: " + rtAnalysis.getMaxPoints());
        System.out.println("Student: " + rtAnalysis.getSolutionStudent().toString());
        System.out.println(rtAnalysis.checkSyntax());
        return rtAnalysis;
    }

    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        rtGrading grading = new rtGrading();
        grading.setPoints(5);
        grading.setMaxPoints(5);
        return grading;
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
