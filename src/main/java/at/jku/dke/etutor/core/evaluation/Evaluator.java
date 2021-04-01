package at.jku.dke.etutor.core.evaluation;

import java.util.Locale;
import java.util.Map;

public interface Evaluator {
    public Analysis analyze(int exerciseID, int userID, Map passedAttributes, Map passedParameters) throws Exception;

    public Grading grade(Analysis analysis, int maxPoints, Map passedAttributes, Map passedParameters) throws Exception;

    public Report report(Analysis analysis, Grading grading, Map passedAttributes, Map passedParameters, Locale locale) throws Exception;
}
