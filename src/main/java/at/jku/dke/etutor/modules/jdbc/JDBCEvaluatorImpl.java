package at.jku.dke.etutor.modules.jdbc;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;

import java.util.Locale;
import java.util.Map;

public class JDBCEvaluatorImpl implements JDBCEvaluator{
    /**
     * Analyzes the submission
     *
     * @param exerciseID       the exercise id
     * @param userID           the user id
     * @param passedAttributes a map containing different attributes
     * @param passedParameters a map containing different parameters
     * @param locale
     * @return an Analysis instance
     * @throws Exception if an error occurs
     */
    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    /**
     * Grades the submission
     *
     * @param analysis         the Analysis
     * @param maxPoints        the maxPoints for this submission
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @return a Grading instance
     * @throws Exception if an error occurs
     */
    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        return null;
    }

    /**
     * Genereates a report
     *
     * @param analysis         the Analysis
     * @param grading          the grading
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @param locale           the locale
     * @return a Report instance
     * @throws Exception if an error occurs
     */
    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    /**
     * Generates an HTML result
     *
     * @param analysis         the Analysis
     * @param passedAttributes the passed attributes
     * @param locale
     * @return an HTML String
     */
    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }
}
