package at.jku.dke.etutor.modules.drools;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;
import at.jku.dke.etutor.modules.drools.grading.DroolsGrading;
import at.jku.dke.etutor.modules.drools.report.DroolsReport;

import java.util.Locale;
import java.util.Map;

public class DroolsEvaluator implements Evaluator {

    private ApplicationProperties applicationProperties;

    public DroolsEvaluator(ApplicationProperties properties) {
        super();
        this.applicationProperties = properties;
    }

    @Override
    public Analysis analyze(int taskId, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        Boolean isForDiagnose = null;

        if (passedAttributes.get("action").equalsIgnoreCase("diagnose")) isForDiagnose = true;
        else if (passedAttributes.get("action").equalsIgnoreCase("submit")) isForDiagnose = false;

        DroolsAnalysis analysis = new DroolsAnalysis(taskId, passedAttributes.get("submission"), isForDiagnose);
        analysis.analyze();
        return analysis;
    }

    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        DroolsAnalysis droolsAnalysis = (DroolsAnalysis) analysis;
        DroolsGrading grading = new DroolsGrading(droolsAnalysis, maxPoints);
        return grading;
    }

    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        DroolsAnalysis droolsAnalysis = (DroolsAnalysis) analysis;
        DroolsReport report = new DroolsReport(droolsAnalysis, passedAttributes, locale);
        return report;
    }

    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }
}
