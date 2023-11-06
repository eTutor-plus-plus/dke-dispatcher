package at.jku.dke.etutor.modules.ddl;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

// Implementation of the Evaluator interface for SQL DDL assignments
public class DDLEvaluator implements Evaluator {
    //region Fields
    private Logger logger;
    //endregion

    public DDLEvaluator(ApplicationProperties properties) {
        try {
            this.logger = (Logger) LoggerFactory.getLogger(DDLEvaluator.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to analyze the submitted solution
     * @param exerciseID the exercise id
     * @param userID the user id
     * @param passedAttributes a map containing different attributes
     * @param passedParameters a map containing different parameters
     * @param locale
     * @return
     * @throws Exception
     */
    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    /**
     * Function to grade the submission according to the analysis
     * @param analysis the Analysis
     * @param maxPoints the maxPoints for this submission
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @return
     * @throws Exception
     */
    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        return null;
    }

    /**
     * Function to create a report according to the grading and analysis
     * @param analysis the Analysis
     * @param grading the grading
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @param locale the locale
     * @return
     * @throws Exception
     */
    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        return null;
    }

    /**
     * Function to generate a html result
     * @param analysis the Analysis
     * @param passedAttributes the passed attributes
     * @param locale
     * @return
     */
    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }

    /**
     * Logs the passedAttributes and passedParameters
     * @param passedAttributes the passedAttributes
     * @param passedParameters the passedParameters
     */
    public void logPassedAttributes(Map<String, String> passedAttributes, Map<String, String> passedParameters){
        logger.info("passedAttributes (" + passedAttributes.size() + ")");
        for (String key: passedAttributes.keySet()) {
            logger.info("  key: "+key+" value: " + passedAttributes.get(key));
        }
        logger.info("passedParameters (" + passedParameters.size() + ")");
        for (String key: passedParameters.keySet()) {
            logger.info("  key: "+key+" value: " + passedParameters.get(key));
        }
    }
}
