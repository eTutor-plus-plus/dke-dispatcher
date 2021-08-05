package at.jku.dke.etutor.core.evaluation;

import java.util.Locale;
import java.util.Map;

/**
 * Interface that needs to be implemented by the module´s to facilitate the evaluation of a submission.
 */
public interface Evaluator {
    /**
     * Analyzes the submission
     * @param exerciseID the exercise id
     * @param userID the user id
     * @param passedAttributes a map containing different attributes
     * @param passedParameters a map containing different parameters
     * @return an Analysis instance
     * @throws Exception if an error occurs
     */
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception;

    /**
     * Grades the submission
     * @param analysis the Analysis
     * @param maxPoints the maxPoints for this submission
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @return a Grading instance
     * @throws Exception if an error occurs
     */
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception;

    /**
     * Genereates a report
     * @param analysis the Analysis
     * @param grading the grading
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @param locale the locale
     * @return a Report instance
     * @throws Exception if an error occurs
     */
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception;

    /**
     * Generates an HTML result
     * @param analysis the Analysis
     * @param passedAttributes the passed attributes
     * @return an HTML String
     */
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes);
}
