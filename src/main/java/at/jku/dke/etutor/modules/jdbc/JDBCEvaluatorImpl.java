package at.jku.dke.etutor.modules.jdbc;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.jdbc.analysis.JDBCAnalysis;
import at.jku.dke.etutor.modules.jdbc.analysis.JDBCAnalyzer;
import at.jku.dke.etutor.modules.jdbc.report.JDBCReporter;
import at.jku.dke.etutor.modules.jdbc.report.JDBCReporterConfig;
import com.sun.xml.bind.v2.TODO;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCEvaluatorImpl implements JDBCEvaluator{

    Logger logger = null;
    private MessageSource messageSource;


    public JDBCEvaluatorImpl(){
        try{
            this.logger = (Logger) LoggerFactory.getLogger(JDBCEvaluatorImpl.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
        String action;
        String mode;
        JDBCAnalysis analysis;
        JDBCAnalyzer analyzer = new JDBCAnalyzer();

        action = passedAttributes.get("action").toString();
        mode = JDBCConstants.ACTION_SUBMIT.equals(action) ? JDBCConstants.MODE_SUBMIT : JDBCConstants.MODE_PRACTISE;
        this.logger.info("Mode: " + mode);

        JDBCFile file = new JDBCFile(exerciseID + ".java", passedAttributes.get("submission"));

        analysis = analyzer.analyze(userID, exerciseID, mode, file);
        analysis.setSubmission(file);

        return analysis;
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
        Grading grading = new DefaultGrading();
        grading.setMaxPoints(1);


        if (analysis.submissionSuitsSolution()){
            grading.setPoints(1);
        } else {
            grading.setPoints(0);
        }

        return grading;
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
        JDBCReporterConfig config = new JDBCReporterConfig();
        String diagnoseLevel = passedAttributes.get("diagnoseLevel").toString();
        String action = passedAttributes.get("action").toString();

        config.setAction(action);
        config.setDiagnoseLevel(Integer.parseInt(diagnoseLevel));
        this.logger.info("SET config FOR REPORTER. Diagnose Level: " + config.getDiagnoseLevel());

        return JDBCReporter.report((JDBCAnalysis)analysis, (DefaultGrading)grading, config, messageSource, locale);
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

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

}


