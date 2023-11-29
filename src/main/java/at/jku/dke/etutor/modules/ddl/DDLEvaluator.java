package at.jku.dke.etutor.modules.ddl;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.ddl.analysis.*;
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
import java.util.*;

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
            DBHelper.init(properties);
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
     * @param locale Specifies the local information of the user
     * @return Returns the analysis object
     * @throws Exception if an error occurs
     */
    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        // Log the exercise id
        logger.info("exerciseID: {}", exerciseID);

        // Initialize variables
        DDLAnalysis analysis = new DDLAnalysis();
        DDLAnalyzerConfig analyzerConfig = new DDLAnalyzerConfig();
        DDLAnalyzer analyzer = new DDLAnalyzer();
        Iterator<DDLCriterionAnalysis> criterionAnalysisIterator;
        DDLCriterionAnalysis criterionAnalysis;

        String solutionSchema;
        String tempDMLStatements;
        List<String> dmlStatements;
        String query;
        Statement stmt;
        ResultSet rs;

        String action;
        String submission;
        int diagnoseLevel;

        String user;
        String userPwd;
        String userSchema;

        DBUserAdmin admin;
        Connection systemConn;
        Connection exerciseConnection;
        Connection userConn;

        // Get the passed values
        action = passedAttributes.get("action");
        submission = passedAttributes.get("submission");
        diagnoseLevel = Integer.parseInt(passedAttributes.get("diagnoseLevel"));

        // Set the submission
        analysis.setSubmission(submission);

        // Get the system connection
        systemConn = DBHelper.getSystemConnection();

        // Check if the connection is successfully up
        if(systemConn == null)
            return null;

        // Get the schema with the solution for the exercise
        solutionSchema = "";
        tempDMLStatements = "";

        query = "";
        query = query.concat("SELECT	schema_name, insert_statements " + LINE_SEP);
        query = query.concat("FROM 		exercises " + LINE_SEP);
        query = query.concat("WHERE 	id = " + exerciseID + LINE_SEP);

        stmt = systemConn.createStatement();
        rs = stmt.executeQuery(query);
        if (rs.next()){
            // note: this is the correct query for the exercise
            solutionSchema = rs.getString("schema_name");
            tempDMLStatements = rs.getString("insert_statements");
        }

        // Get the system connection with the schema of the exercise solution
        exerciseConnection = DBHelper.getSystemConnectionWithSchema(solutionSchema);

        // Check if the exercise connection is successfully up
        if(exerciseConnection == null)
            return null;

        // Set the exercise connection
        analyzerConfig.setExerciseConn(exerciseConnection);

        // Set the dml statements for the check constraints
        if(tempDMLStatements != null) {
            dmlStatements = List.of(tempDMLStatements.replace("\n", "").split(";"));
            analyzerConfig.setDmlStatements(dmlStatements);
        }

        // Get user connection
        admin = DBUserAdmin.getAdmin();
        user = admin.getUser();
        userPwd = admin.getPwd(user);
        userSchema = admin.getSchema(user);

        userConn = DBHelper.getUserConnection(user, userPwd, userSchema);

        // Check if the user connection is successfully up
        if(userConn == null)
            return null;

        // Set the user connection
        analyzerConfig.setUserConn(userConn);

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

        // Reset user schema and release user
        DBHelper.resetUserConnection(userConn, user);
        admin.releaseUser(user);

        // Set the exercise id in the analysis object
        analysis.setExerciseId(exerciseID);

        // Close connection
        DBHelper.closeSystemConnectionWithSchema();

        return analysis;
    }

    /**
     * Function to grade the submission according to the analysis
     * @param analysis the Analysis
     * @param maxPoints the maxPoints for this submission
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @return Returns the grading object
     * @throws Exception if an error occurs
     */
    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        // Log analysis and max points
        logger.info("analysis: {}" , analysis);

        // Initialize variables
        Connection systemConn;
        DDLGrader grader = new DDLGrader();
        DDLGraderConfig graderConfig = new DDLGraderConfig();
        DDLCriterionGradingConfig criterionGradingConfig;
        DDLAnalysis ddlAnalysis;

        Statement stmt;
        ResultSet rs;

        String query;
        int totalPoints = 0;
        int tablePoints = 0;
        int columnPoints = 0;
        int primaryKeyPoints = 0;
        int foreignKeyPoints = 0;
        int constraintPoints = 0;

        String action = passedAttributes.get("action");

        try {
            // Get the system connection
            systemConn = DBHelper.getSystemConnection();

            // Check if the connection is successfully up
            if(systemConn == null)
                return null;

            // Get the analysis object
            ddlAnalysis = (DDLAnalysis)analysis;

            query = "";
            query = query.concat("SELECT	max_points, table_points, column_points, primarykey_points, foreignkey_points, constraint_points " + LINE_SEP);
            query = query.concat("FROM 		exercises " + LINE_SEP);
            query = query.concat("WHERE 	id = " + ddlAnalysis.getExerciseId() + LINE_SEP);

            stmt = systemConn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()){
                // note: this is the correct query for the exercise
                totalPoints = rs.getInt("max_points");
                tablePoints = rs.getInt("table_points");
                columnPoints = rs.getInt("column_points");
                primaryKeyPoints = rs.getInt("primarykey_points");
                foreignKeyPoints = rs.getInt("foreignkey_points");
                constraintPoints = rs.getInt("constraint_points");
            }

            // Set max points
            graderConfig.setMaxPoints(totalPoints);
            graderConfig.setTablePoints(tablePoints);
            graderConfig.setColumnPoints(columnPoints);
            graderConfig.setPrimaryKeyPoints(primaryKeyPoints);
            graderConfig.setForeignKeyPoints(foreignKeyPoints);
            graderConfig.setConstraintPoints(constraintPoints);

            // Grade syntax
            criterionGradingConfig = new DDLCriterionGradingConfig();
            criterionGradingConfig.setPositivePoints(0);
            criterionGradingConfig.setNegativePoints(0);
            graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_SYNTAX, criterionGradingConfig);

            // Check if the action is not check (check only tests syntax)
            if(!action.equalsIgnoreCase(DDLEvaluationAction.CHECK.toString())) {
                // Grade tables
                criterionGradingConfig = new DDLCriterionGradingConfig();
                criterionGradingConfig.setPositivePoints(tablePoints);
                criterionGradingConfig.setNegativePoints(0);
                graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_TABLES, criterionGradingConfig);

                // Grade columns
                criterionGradingConfig = new DDLCriterionGradingConfig();
                criterionGradingConfig.setPositivePoints(columnPoints);
                criterionGradingConfig.setNegativePoints(0);
                graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_COLUMNS, criterionGradingConfig);

                // Grade primary keys
                criterionGradingConfig = new DDLCriterionGradingConfig();
                criterionGradingConfig.setPositivePoints(primaryKeyPoints);
                criterionGradingConfig.setNegativePoints(0);
                graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS, criterionGradingConfig);

                // Grade foreign keys
                criterionGradingConfig = new DDLCriterionGradingConfig();
                criterionGradingConfig.setPositivePoints(foreignKeyPoints);
                criterionGradingConfig.setNegativePoints(0);
                graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS, criterionGradingConfig);

                // Grade constraints
                criterionGradingConfig = new DDLCriterionGradingConfig();
                criterionGradingConfig.setPositivePoints(constraintPoints);
                criterionGradingConfig.setNegativePoints(0);
                graderConfig.addCriteriaGradingConfig(DDLEvaluationCriterion.CORRECT_CONSTRAINTS, criterionGradingConfig);
            }

            return grader.grade(ddlAnalysis, graderConfig);
        } finally {
            DBHelper.closeSystemConnection();
        }
    }

    /**
     * Function to create a report according to the grading and analysis
     * @param analysis the Analysis
     * @param grading the grading
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @param locale the locale Specifies the locale of the user
     * @return Returns the report object
     * @throws Exception if an error occurs
     */
    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        // Log analysis and max points
        logger.info("analysis: {}" , analysis);
        logger.info("grading: {}" ,  grading);

        // Initialize variables
        DDLReporter reporter = new DDLReporter();
        DDLReporterConfig reporterConfig = new DDLReporterConfig();

        String action = passedAttributes.get("action");
        String diagnoseLevel = passedAttributes.get("diagnoseLevel");

        // Set action and diagnose level
        if(action.equalsIgnoreCase(DDLEvaluationAction.SUBMIT.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.SUBMIT);
            reporterConfig.setDiagnoseLevel(0);
        } else if(action.equalsIgnoreCase(DDLEvaluationAction.DIAGNOSE.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.DIAGNOSE);
            reporterConfig.setDiagnoseLevel(Integer.parseInt(diagnoseLevel));
        } else if(action.equalsIgnoreCase(DDLEvaluationAction.CHECK.toString())) {
            reporterConfig.setAction(DDLEvaluationAction.CHECK);
            reporterConfig.setDiagnoseLevel(3);
        }

        return reporter.createReport((DDLAnalysis)analysis, reporterConfig, locale);
    }

    /**
     * Function to generate a html result
     * @param analysis the Analysis
     * @param passedAttributes the passed attributes
     * @param locale Specifies the locale of the user
     * @return Return the html result as a string
     */
    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        // Check if action is submit -> return - no html result
        if(passedAttributes.get("action").equalsIgnoreCase(DDLEvaluationAction.SUBMIT.toString()))
            return null;

        // Check if analysis object is DDLAnalysis
        if(analysis instanceof DDLAnalysis ddlAnalysis) {
            // Initialize variables
            StringBuilder result = new StringBuilder();

            // Add syntax exception if there is one
            SyntaxAnalysis correctSyntaxCriterion = (SyntaxAnalysis) ddlAnalysis.getCriterionAnalysis(DDLEvaluationCriterion.CORRECT_SYNTAX);
            if(!correctSyntaxCriterion.isCriterionSatisfied()) {
                result.append("<br><strong>").append(correctSyntaxCriterion.getErrorDescription()).append("</strong>").append("<br>");
                return result.toString();
            }

            if(locale == Locale.GERMAN) {
                result.append("<strong>Query erfolgreich ausgeführt!</strong>");
            } else {
                result.append("<strong>Query successfully executed!</strong>");
            }

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
