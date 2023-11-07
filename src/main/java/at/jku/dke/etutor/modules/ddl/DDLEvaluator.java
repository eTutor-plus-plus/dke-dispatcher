package at.jku.dke.etutor.modules.ddl;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalysis;
import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalyzer;
import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalyzerConfig;
import at.jku.dke.etutor.modules.ddl.analysis.DDLCriterionAnalysis;
import at.jku.dke.etutor.modules.ddl.grading.DDLCriterionGradingConfig;
import at.jku.dke.etutor.modules.ddl.grading.DDLGrader;
import at.jku.dke.etutor.modules.ddl.grading.DDLGraderConfig;
import at.jku.dke.etutor.modules.ddl.report.DDLReporter;
import at.jku.dke.etutor.modules.ddl.report.DDLReporterConfig;
import at.jku.dke.etutor.modules.ddl.serverAdministration.DBHelper;
import at.jku.dke.etutor.modules.ddl.serverAdministration.DBUserAdmin;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

// Implementation of the Evaluator interface for SQL DDL assignments
public class DDLEvaluator implements Evaluator {
    //region Constants
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");
    //endregion

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
        // Log the exercise id
        logger.info("exerciseID: {}", exerciseID);
        // Log the passed attributes and parameters
        logPassedAttributes(passedAttributes, passedParameters);

        // Intialize variables
        DDLAnalysis analysis = new DDLAnalysis();
        DDLAnalyzerConfig analyzerConfig = new DDLAnalyzerConfig();
        DDLAnalyzer analyzer = new DDLAnalyzer();
        Iterator<DDLCriterionAnalysis> criterionAnalysisIterator;
        DDLCriterionAnalysis criterionAnalysis;

        String solution;
        String query;
        Statement stmt;
        ResultSet rs;

        String action;
        String submission;
        int diagnoseLevel;

        DBUserAdmin admin = DBUserAdmin.getAdmin();
        Connection systemConn;
        Connection userConn;

        // Get the passed values
        action = passedAttributes.get("action");
        submission = passedAttributes.get("submission");
        diagnoseLevel = Integer.parseInt(passedAttributes.get("diagnoseLevel"));

        // Set the submission
        analysis.setSubmission(submission.replace(";", ""));

        try {
            systemConn = DBHelper.getSystemConnection();

            // Check if the connection is successfully up
            if(systemConn == null)
                return null;

            //todo Check database management
            userConn = null;
            solution = "";

            // Get the solution
            query = "";
            query = query.concat("SELECT	solution " + LINE_SEP);
            query = query.concat("FROM 		exercises " + LINE_SEP);
            query = query.concat("WHERE 	id = " + exerciseID + LINE_SEP);

            stmt = systemConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()){
                // note: this is the correct query for the exercise
                solution = rs.getString("solution");
            }

            // Configure analyzer
            if(action.equalsIgnoreCase(DDLEvaluationAction.RUN.toString())) {
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX);
                analyzerConfig.setDiagnoseLevel(1);
            } else {
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX);
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_TABLES);
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_COLUMNS);
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS);
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS);
                analyzerConfig.addCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_CONSTRAINTS);

                if (action.equalsIgnoreCase(DDLEvaluationAction.SUBMIT.toString())) {
                    analyzerConfig.setDiagnoseLevel(1);
                } else {
                    analyzerConfig.setDiagnoseLevel(diagnoseLevel);
                }
            }

            // Analyze the submission
            //todo Assign right value
            analyzerConfig.setConn(userConn);
            analyzerConfig.setSolution(solution);

            // Execute analysis
            analysis = analyzer.analyze(analysis.getSubmission(), analyzerConfig);

            // Check for each criterion if the solution is correct
            criterionAnalysisIterator = analysis.iterCriterionAnalysis();
            while (criterionAnalysisIterator.hasNext()) {
                criterionAnalysis = criterionAnalysisIterator.next();
                if(!criterionAnalysis.isCriterionSatisfied() || criterionAnalysis.getAnalysisException() != null) {
                    analysis.setSubmissionSuitsSolution(false);
                }
            }

            return analysis;
        } finally {
            DBHelper.closeSystemConnection();
        }
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
        // Log analysis and max points
        logger.info("analysis: {}" , analysis);
        logger.info("maxPoints: {}" ,  maxPoints);
        // Log the passed attributes and parameters
        logPassedAttributes(passedAttributes, passedParameters);

        // Initialize variables
        DDLGrader grader = new DDLGrader();
        DDLGraderConfig graderConfig = new DDLGraderConfig();
        DDLCriterionGradingConfig criterionGradingConfig;

        String action = passedAttributes.get("action");

        // Set max points
        graderConfig.setMaxPoints(maxPoints);

        // Grade syntax
        criterionGradingConfig = new DDLCriterionGradingConfig();
        criterionGradingConfig.setPositivePoints(1);
        criterionGradingConfig.setNegativePoints(0);
        graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_SYNTAX, criterionGradingConfig);

        // Check if the action is not run (run only tests syntax)
        if(!action.equalsIgnoreCase(DDLEvaluationAction.RUN.toString())) {
            // Grade tables
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(1);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_TABLES, criterionGradingConfig);

            // Grade columns
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(1);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_COLUMNS, criterionGradingConfig);

            // Grade primary keys
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(1);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS, criterionGradingConfig);

            // Grade foreign keys
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(1);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS, criterionGradingConfig);

            // Grade constraints
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(1);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_CONSTRAINTS, criterionGradingConfig);
        }

        return grader.grade((DDLAnalysis) analysis, graderConfig);
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
        // Log analysis and max points
        logger.info("analysis: {}" , analysis);
        logger.info("grading: {}" ,  grading);
        // Log the passed attributes and parameters
        logPassedAttributes(passedAttributes, passedParameters);

        // Initialize variables
        DDLReporter reporter = new DDLReporter();
        DDLReporterConfig reporterConfig = new DDLReporterConfig();

        String action = passedAttributes.get("action");
        String diagnoseLevel = passedAttributes.get("diagnoseLevel");

        // Set action and diagnose level
        if(action.equalsIgnoreCase(DDLEvaluationAction.RUN.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.RUN);
            reporterConfig.setDiagnoseLevel(1);
        } else if(action.equalsIgnoreCase(DDLEvaluationAction.SUBMIT.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.SUBMIT);
            reporterConfig.setDiagnoseLevel(2);
        } else if(action.equalsIgnoreCase(DDLEvaluationAction.DIAGNOSE.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.DIAGNOSE);
            reporterConfig.setDiagnoseLevel(Integer.parseInt(diagnoseLevel));
        } else if(action.equalsIgnoreCase(DDLEvaluationAction.CHECK.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.CHECK);
            reporterConfig.setDiagnoseLevel(0);
        }

        return reporter.createReport((DDLAnalysis)analysis, reporterConfig, locale);
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
        // Check if action is submit -> return - no html result
        if(passedAttributes.get("action").equalsIgnoreCase(DDLEvaluationAction.SUBMIT.toString()))
            return null;

        // Check if analysis object is DDLAnalysis
        if(analysis instanceof DDLAnalysis) {
            // Initialize variables
            StringBuilder result = new StringBuilder();

            //todo Create HTML result

            return result.toString();
        }

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
