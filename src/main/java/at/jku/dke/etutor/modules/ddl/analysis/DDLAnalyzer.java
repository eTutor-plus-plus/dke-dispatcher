package at.jku.dke.etutor.modules.ddl.analysis;


import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DDLAnalyzer {
    //region Constants
    private final String INTERNAL_ERROR = "This is an internal system error.";
    private final String CONTACT_ADMIN = "Please contact the system administrator.";
    //endregion

    //region Fields
    private Logger logger;
    //endregion

    public DDLAnalyzer() {
        try {
            this.logger = (Logger) LoggerFactory.getLogger(DDLAnalyzer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to analyse the submitted solution according to the configuration
     * @param submission Specifies the submitted solution
     * @param config Specifies the analyse configuration
     * @return Returns the analyse object
     */
    public DDLAnalysis analyze(Serializable submission, DDLAnalyzerConfig config) {
        String msg;
        String submittedQuery;
        DDLAnalysis analysis = new DDLAnalysis();
        DDLCriterionAnalysis criterionAnalysis;

        // Check if submission is null
        if(submission == null) {
            msg ="";
            msg = msg.concat("Analsis stopped with errors. ");
            msg = msg.concat("Submission is empty. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg);
            analysis.setAnalysisException(new AnalysisException(msg));
            return analysis;
        }

        // Check if the submission is a string
        if (submission instanceof String) {
            submittedQuery = (String)submission;
        } else {
            msg = "";
            msg = msg.concat("Analysis stopped with errors. ");
            msg = msg.concat("Submission is not utilizable. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg);
            analysis.setAnalysisException(new AnalysisException(msg));
            return analysis;
        }

        // Check if the configuration
        if (config == null) {
            msg = "";
            msg = msg.concat("Analysis stopped with errors. ");
            msg = msg.concat("No configuration found. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg);
            analysis.setAnalysisException(new AnalysisException(msg));
            return analysis;
        }

        // Check correct syntax
        if (config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
            criterionAnalysis = this.analyzeSyntax(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_SYNTAX, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check tables
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_TABLES)) {
            criterionAnalysis = this.analyzeTables(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_TABLES, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check columns
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
            criterionAnalysis = this.analyzeColumns(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_COLUMNS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check primary keys
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
            criterionAnalysis = this.analyzePrimaryKeys(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check foreign keys
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
            criterionAnalysis = this.analyzeForeignKeys(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check constraints
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_CONSTRAINTS)) {
            criterionAnalysis = this.analyzeConstraints(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_CONSTRAINTS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        return analysis;
    }

    //region private Methods

    /**
     * Function to check the correctness of the syntax
     * @param config Specifies the configuration
     * @param submittedQuery Specifies the submitted ddl statement
     * @return
     */
    private DDLCriterionAnalysis analyzeSyntax(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze syntax");

        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();

        try {
            Statement stmt = config.getConn().createStatement();
            ResultSet rs = stmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            syntaxAnalysis.setFoundError(true);
            syntaxAnalysis.setCriterionIsSatisfied(false);
            syntaxAnalysis.setErrorDescription(ex.toString());
        }

        return syntaxAnalysis;
    }

    private DDLCriterionAnalysis analyzeTables(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze tables");

        TablesAnalysis tablesAnalysis = new TablesAnalysis();

        try {
            //todo Execute on different databases
            Statement correctStmt = config.getConn().createStatement();
            ResultSet rs = correctStmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing tables. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            tablesAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        return tablesAnalysis;
    }

    private DDLCriterionAnalysis analyzeColumns(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze columns");

        ColumnsAnalysis columnsAnalysis = new ColumnsAnalysis();

        try {
            //todo Execute on different databases
            Statement correctStmt = config.getConn().createStatement();
            ResultSet rs = correctStmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing columns. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            columnsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        return columnsAnalysis;
    }

    private DDLCriterionAnalysis analyzePrimaryKeys(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze primary keys");

        PrimaryKeysAnalysis primaryKeysAnalysis = new PrimaryKeysAnalysis();

        try {
            //todo Execute on different databases
            Statement correctStmt = config.getConn().createStatement();
            ResultSet rs = correctStmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing primary keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            primaryKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        return primaryKeysAnalysis;
    }

    private DDLCriterionAnalysis analyzeForeignKeys(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze foreign keys");

        ForeignKeysAnalysis foreignKeysAnalysis = new ForeignKeysAnalysis();

        try {
            //todo Execute on different databases
            Statement correctStmt = config.getConn().createStatement();
            ResultSet rs = correctStmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing foreign keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            foreignKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        return foreignKeysAnalysis;
    }

    private DDLCriterionAnalysis analyzeConstraints(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze constraints");

        ConstraintsAnalysis constraintsAnalysis = new ConstraintsAnalysis();

        try {
            //todo Execute on different databases
            Statement correctStmt = config.getConn().createStatement();
            ResultSet rs = correctStmt.executeQuery(submittedQuery);
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing constraints. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            constraintsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        return constraintsAnalysis;
    }
    //endregion
}
