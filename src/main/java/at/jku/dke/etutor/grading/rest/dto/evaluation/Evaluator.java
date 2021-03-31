package at.jku.dke.etutor.grading.rest.dto.evaluation;

import java.rmi.Remote;
import java.util.Locale;
import java.util.Map;

public interface Evaluator extends Remote {
    public Analysis analyze(int exerciseID, int userID, Map passedAttributes, Map passedParameters) throws Exception;

    public Grading grade(Analysis analysis, int maxPoints, Map passedAttributes, Map passedParameters) throws Exception;

    public Report report(Analysis analysis, Grading grading, Map passedAttributes, Map passedParameters, Locale locale) throws Exception;
}
