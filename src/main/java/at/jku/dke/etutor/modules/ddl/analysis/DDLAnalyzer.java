package at.jku.dke.etutor.modules.ddl.analysis;


import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
import java.util.List;

public class DDLAnalyzer {
    //region Constants
    private final String INTERNAL_ERROR = "This is an internal system error.";
    private final String CONTACT_ADMIN = "Please contact the system administrator.";
    private final String VARCHAR = "varchar";
    private final String DECIMAL = "decimal";
    //endregion

    //region Fields
    private Logger logger;
    private Connection exerciseConn;
    private Connection userConn;
    private DatabaseMetaData userMetadata;
    private DatabaseMetaData exerciseMetadata;
    private String userSchema;
    private String exerciseSchema;
    private boolean isEveryCriterionSatisfied;
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
     * @param config Specifies the analysis configuration
     * @return Returns the analysis object
     */
    public DDLAnalysis analyze(Serializable submission, DDLAnalyzerConfig config) throws SQLException {
        String msg;
        String submittedQuery;
        List<String> submittedStatements;
        DDLAnalysis analysis = new DDLAnalysis();
        DDLCriterionAnalysis criterionAnalysis;

        // Check if submission is null
        if(submission == null) {
            msg ="";
            msg = msg.concat("Analysis stopped with errors. ");
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

        // Check if the configuration is null
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

        // Get connections
        exerciseConn = config.getExerciseConn();
        userConn = config.getUserConn();

        // Set schema variables
        exerciseSchema = exerciseConn.getSchema();
        userSchema = userConn.getSchema();

        // Execute query
        // Check correct syntax
        if (config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
            criterionAnalysis = this.analyzeSyntax(submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_SYNTAX, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Get metadata
        try {
            exerciseMetadata = exerciseConn.getMetaData();
            userMetadata = userConn.getMetaData();
        } catch (SQLException ex) {
            msg = "";
            msg = msg.concat("Error encountered while getting metadata. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            analysis.setAnalysisException(new AnalysisException(msg, ex));
            return analysis;
        }

        isEveryCriterionSatisfied = true;

        // Check tables
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_TABLES)) {
            criterionAnalysis = this.analyzeTables();
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_TABLES, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check columns
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
            criterionAnalysis = this.analyzeColumns();
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_COLUMNS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check primary keys
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
            criterionAnalysis = this.analyzePrimaryKeys();
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check foreign keys
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
            criterionAnalysis = this.analyzeForeignKeys();
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Check constraints
        if(config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_CONSTRAINTS)) {
            criterionAnalysis = this.analyzeConstraints(config);
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
     * @param submittedQuery Specifies the submitted ddl statement
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzeSyntax(String submittedQuery) {
        this.logger.info("Analyze syntax");

        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();

        try {
            // Execute query to check the correctness of the syntax
            Statement stmt = userConn.createStatement();

            // Call executeUpdate to prevent "Query does not return results" exception
            stmt.executeUpdate(submittedQuery);
        } catch (SQLException ex) {
            syntaxAnalysis.setFoundError(true);
            syntaxAnalysis.setCriterionIsSatisfied(false);
            syntaxAnalysis.setErrorDescription(ex.toString());
            syntaxAnalysis.setAnalysisException(new AnalysisException(ex.toString()));
            return syntaxAnalysis;
        }

        syntaxAnalysis.setCriterionIsSatisfied(true);
        this.logger.info("Finished syntax analysis. Criterion satisfied: " + syntaxAnalysis.isCriterionSatisfied());
        return syntaxAnalysis;
    }

    /**
     * Function to check the correctness of the tables
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzeTables() {
        this.logger.info("Analyze tables");

        TablesAnalysis tablesAnalysis = new TablesAnalysis();
        boolean exists = false;
        String msg;

        try {
            ResultSet userRs = userMetadata.getTables(null, userSchema, null, new String[]{"TABLE"});
            ResultSet systemRS = exerciseMetadata.getTables(null, exerciseSchema, null, new String[]{"TABLE"});

            // Search for missing tables
            while (systemRS.next()) {
                String systemTable = systemRS.getString("TABLE_NAME");
                while (userRs.next()) {
                    String userTable = userRs.getString("TABLE_NAME");

                    // Compare table names
                    if(userTable.equalsIgnoreCase(systemTable)) {
                        exists = true;
                        break;
                    }
                }

                // Check if the table exists
                if(!exists) {
                    tablesAnalysis.addMissingTables(systemTable);
                }

                // Reset variables
                userRs.beforeFirst();
                exists = false;
            }

            // Reset variable
            systemRS.beforeFirst();

            // Search for surplus tables
            while (userRs.next()) {
                String userTable = userRs.getString("TABLE_NAME");
                while (systemRS.next()) {
                    String systemTable = systemRS.getString("TABLE_NAME");

                    // Compare table names
                    if(systemTable.equalsIgnoreCase(userTable)) {
                        exists = true;
                        break;
                    }
                }

                // Check if the table exists
                if(!exists) {
                    tablesAnalysis.addSurplusTable(userTable);
                }

                // Reset variables
                systemRS.beforeFirst();
                exists = false;
            }
        } catch (SQLException ex) {
            msg = "";
            msg = msg.concat("Error encountered while analyzing tables. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            tablesAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        // Set submission is correct for this criterion
        tablesAnalysis.setCriterionIsSatisfied(tablesAnalysis.isMissingTablesEmpty() && tablesAnalysis.isSurplusTablesEmpty());
        isEveryCriterionSatisfied = isEveryCriterionSatisfied && tablesAnalysis.isCriterionSatisfied();
        this.logger.info("Finished table analysis. Criterion satisfied: " + tablesAnalysis.isCriterionSatisfied());
        return tablesAnalysis;
    }

    /**
     * Function to check the correctness of the columns
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzeColumns() {
        this.logger.info("Analyze columns");

        ColumnsAnalysis columnsAnalysis = new ColumnsAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, userSchema, null, new String[]{"TABLE"});

            // Run through all tables and look at the columns
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userColumns = userMetadata.getColumns(null, userSchema, tableName, null);
                ResultSet systemColumns = exerciseMetadata.getColumns(null, exerciseSchema, tableName, null);

                // Search for missing columns
                while (systemColumns.next()) {
                    String systemColumn = systemColumns.getString("COLUMN_NAME");
                    while (userColumns.next()) {
                        String userColumn = userColumns.getString("COLUMN_NAME");

                        // Compare column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;

                            // Check datatype
                            int systemDatatype = systemColumns.getInt("DATA_TYPE");
                            int userDatatype = userColumns.getInt("DATA_TYPE");

                            if(systemDatatype != userDatatype) {
                                // Check if both datatypes are varchar -> every varchar should be treated as equal
                                String sd = systemColumns.getString("TYPE_NAME");
                                String ud = userColumns.getString("TYPE_NAME");
                                if(!(sd.toLowerCase().contains(VARCHAR) && ud.toLowerCase().contains(VARCHAR)) || !(sd.toLowerCase().contains(DECIMAL) && ud.toLowerCase().contains(DECIMAL))) {
                                    columnsAnalysis.addWrongDatatypeColumn(new ErrorTupel(tableName, systemColumn));
                                }
                            }

                            // Check nullable
                            int systemNullable = systemColumns.getInt("NULLABLE");
                            int userNullable = userColumns.getInt("NULLABLE");

                            if(systemNullable != userNullable) {
                                columnsAnalysis.addWrongNullColumn(new ErrorTupel(tableName, systemColumn));
                            }

                            // Check default value
                            String systemDefault = systemColumns.getString("COLUMN_DEF");
                            String userDefault = userColumns.getString("COLUMN_DEF");


                            // Check like this and not with equals to avoid null exception
                            if(systemDefault != userDefault) {
                                if(systemDefault == null || !systemDefault.equals(userDefault)) {
                                    columnsAnalysis.addWrongDefaultColumn(new ErrorTupel(tableName, systemColumn));
                                }
                            }

                            break;
                        }
                    }

                    // Check if the column exists
                    if(!exists) {
                        columnsAnalysis.addMissingColumn(new ErrorTupel(tableName, systemColumn));
                    }

                    // Reset variables
                    userColumns.beforeFirst();
                    exists = false;
                }

                // Reset variable
                systemColumns.beforeFirst();

                // Search for surplus columns
                while (userColumns.next()) {
                    String userColumn = userColumns.getString("COLUMN_NAME");
                    while (systemColumns.next()) {
                        String systemColumn = systemColumns.getString("COLUMN_NAME");

                        // Compare column names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the column exists
                    if(!exists) {
                        columnsAnalysis.addSurplusColumn(new ErrorTupel(tableName, userColumn));
                    }

                    // Reset variables
                    systemColumns.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encountered while analyzing columns. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            columnsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        columnsAnalysis.setCriterionIsSatisfied(columnsAnalysis.isMissingColumnsEmpty() && columnsAnalysis.isSurplusColumnsEmpty() && columnsAnalysis.isWrongNullColumnsEmpty() && columnsAnalysis.isWrongDatatypeColumnsEmpty() && columnsAnalysis.isWrongDefaultColumnsEmpty());
        isEveryCriterionSatisfied = isEveryCriterionSatisfied && columnsAnalysis.isCriterionSatisfied();
        this.logger.info("Finished column analysis. Criterion satisfied: " + columnsAnalysis.isCriterionSatisfied());
        return columnsAnalysis;
    }

    /**
     * Function to check the correctness of the primary keys
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzePrimaryKeys() {
        this.logger.info("Analyze primary keys");

        PrimaryKeysAnalysis primaryKeysAnalysis = new PrimaryKeysAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, userSchema, null, new String[]{"TABLE"});

            // Run through all tables and look at the primary keys
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userPrimaryKeys = userMetadata.getPrimaryKeys(null, userSchema, tableName);
                ResultSet systemPrimaryKeys = exerciseMetadata.getPrimaryKeys(null, exerciseSchema, tableName);

                // Search for missing primary keys
                while (systemPrimaryKeys.next()) {
                    String systemColumn = systemPrimaryKeys.getString("COLUMN_NAME");
                    while (userPrimaryKeys.next()) {
                        String userColumn = userPrimaryKeys.getString("COLUMN_NAME");

                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the primary key exists
                    if(!exists) {
                        primaryKeysAnalysis.addMissingPrimaryKey(new ErrorTupel(tableName, systemColumn));
                    }

                    // Reset variables
                    userPrimaryKeys.beforeFirst();
                    exists = false;
                }

                // Reset variable
                systemPrimaryKeys.beforeFirst();

                // Search for surplus primary keys
                while (userPrimaryKeys.next()) {
                    String userColumn = userPrimaryKeys.getString("COLUMN_NAME");
                    while (systemPrimaryKeys.next()) {
                        String systemColumn = systemPrimaryKeys.getString("COLUMN_NAME");

                        // Compare column names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the primary key exists
                    if(!exists) {
                        primaryKeysAnalysis.addSurplusPrimaryKey(new ErrorTupel(tableName, userColumn));
                    }

                    // Reset variables
                    systemPrimaryKeys.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encountered while analyzing primary keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            primaryKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        primaryKeysAnalysis.setCriterionIsSatisfied(primaryKeysAnalysis.isMissingPrimaryKeysEmpty() && primaryKeysAnalysis.isSurplusPrimaryKeysEmpty());
        this.logger.info("Finished primary key analysis. Criterion satisfied: " + primaryKeysAnalysis.isCriterionSatisfied());
        return primaryKeysAnalysis;
    }

    /**
     * Function to check the correctness of the foreign keys
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzeForeignKeys() {
        this.logger.info("Analyze foreign keys");

        ForeignKeysAnalysis foreignKeysAnalysis = new ForeignKeysAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, userSchema, null, new String[]{"TABLE"});

            // Run through all tables and look at the foreign keys
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userForeignKeys = userMetadata.getImportedKeys(null, userSchema, tableName);
                ResultSet systemForeignKeys = exerciseMetadata.getImportedKeys(null, exerciseSchema, tableName);

                // Search for missing foreign keys
                while (systemForeignKeys.next()) {
                    String systemColumn = systemForeignKeys.getString("FKCOLUMN_NAME");
                    String systemTable = systemForeignKeys.getString("FKTABLE_NAME");
                    short systemUpdateConstraint = systemForeignKeys.getShort("UPDATE_RULE");
                    short systemDeleteConstraint = systemForeignKeys.getShort("DELETE_RULE");
                    while (userForeignKeys.next()) {
                        String userColumn = userForeignKeys.getString("FKCOLUMN_NAME");
                        String userTable = userForeignKeys.getString("FKTABLE_NAME");
                        short userUpdateConstraint = userForeignKeys.getShort("UPDATE_RULE");
                        short userDeleteConstraint = userForeignKeys.getShort("DELETE_RULE");

                        // Compare foreign key column and table names and the delete/update rules
                        if(userColumn.equalsIgnoreCase(systemColumn) && userTable.equalsIgnoreCase(systemTable)) {
                            exists = true;

                            // Compare update rules
                            if(userUpdateConstraint != systemUpdateConstraint) {
                                foreignKeysAnalysis.addWrongUpdateForeignKey(new ErrorTupel(tableName, systemColumn));
                            }

                            // Compare delete rules
                            if(userDeleteConstraint != systemDeleteConstraint) {
                                foreignKeysAnalysis.addWrongDeleteForeignKey(new ErrorTupel(tableName, systemColumn));
                            }

                            break;
                        }
                    }

                    // Check if the foreign key exists
                    if(!exists) {
                        foreignKeysAnalysis.addMissingForeignKey(new ErrorTupel(tableName, systemColumn));
                    }

                    // Reset variables
                    userForeignKeys.beforeFirst();
                    exists = false;
                }

                // Reset variable
                systemForeignKeys.beforeFirst();

                // Search for surplus primary keys
                while (userForeignKeys.next()) {
                    String userColumn = userForeignKeys.getString("FKCOLUMN_NAME");
                    while (systemForeignKeys.next()) {
                        String systemColumn = systemForeignKeys.getString("FKCOLUMN_NAME");

                        // Compare column names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the foreign key exists
                    if(!exists) {
                        foreignKeysAnalysis.addSurplusForeignKey(new ErrorTupel(tableName, userColumn));
                    }

                    // Reset variables
                    systemForeignKeys.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encountered while analyzing foreign keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            foreignKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        foreignKeysAnalysis.setCriterionIsSatisfied(foreignKeysAnalysis.isMissingForeignKeysEmpty() && foreignKeysAnalysis.isSurplusForeignKeysEmpty() && foreignKeysAnalysis.isWrongUpdateForeignKeysEmpty() && foreignKeysAnalysis.isWrongDeleteForeignKeysEmpty());
        this.logger.info("Finished foreign key analysis. Criterion satisfied: " + foreignKeysAnalysis.isCriterionSatisfied());
        return foreignKeysAnalysis;
    }

    /**
     * Function to check the correctness of the constraints
     * @param config Specifies the configuration
     * @return Returns a criterion analysis object
     */
    private DDLCriterionAnalysis analyzeConstraints(DDLAnalyzerConfig config) {
        this.logger.info("Analyze constraints");

        ConstraintsAnalysis constraintsAnalysis = new ConstraintsAnalysis();
        boolean exists = false;
        boolean satisfied = false;

        Statement systemStmt;
        Statement userStmt;

        try {
            // Analyze unique constraints
            ResultSet userTables = userMetadata.getTables(null, userSchema, null, new String[]{"TABLE"});

            // Run through all tables and look at the unique constraints
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userConstraints = userMetadata.getIndexInfo(null, userSchema, tableName, true, true);
                ResultSet systemConstraints = exerciseMetadata.getIndexInfo(null, exerciseSchema, tableName, true, true);

                // Search for missing unique constraints
                while (systemConstraints.next()) {
                    String systemColumn = systemConstraints.getString("INDEX_NAME");
                    while (userConstraints.next()) {
                        String userColumn = userConstraints.getString("INDEX_NAME");

                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        constraintsAnalysis.addMissingConstraint(new ErrorTupel(tableName, systemConstraints.getString("COLUMN_NAME")));
                    }

                    // Reset variables
                    userConstraints.beforeFirst();
                    exists = false;
                }

                // Reset variable
                systemConstraints.beforeFirst();

                // Search for surplus unique constraints
                while (userConstraints.next()) {
                    String userColumn = userConstraints.getString("INDEX_NAME");
                    while (systemConstraints.next()) {
                        String systemColumn = systemConstraints.getString("INDEX_NAME");

                        // Compare table names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        constraintsAnalysis.addSurplusConstraint(new ErrorTupel(tableName, userConstraints.getString("COLUMN_NAME")));
                    }

                    // Reset variables
                    systemConstraints.beforeFirst();
                    exists = false;
                }
            }

            satisfied = constraintsAnalysis.isMissingConstraintsEmpty() && constraintsAnalysis.isSurplusConstraintsEmpty();

            // Only check check-constraints when every other criterion is satisfied
            if(isEveryCriterionSatisfied) {
                constraintsAnalysis.setInsertStatementsChecked(true);

                int systemAffects;
                int userAffects;

                // Analyze check constraints
                for(String stmt : config.getDmlStatements()) {
                    // Execute the DML statements
                    try {
                        systemStmt = exerciseConn.createStatement();
                        systemAffects = systemStmt.executeUpdate(stmt);
                    } catch (SQLException e) {
                        systemAffects = -1;
                    }

                    try {
                        userStmt = userConn.createStatement();
                        userAffects = userStmt.executeUpdate(stmt);
                    } catch (SQLException e) {
                        userAffects = -1;
                    }

                    // Check if the row count for the affected rows is the same
                    if(systemAffects != userAffects) {
                        constraintsAnalysis.addDmlStatementWithMistake(stmt);
                    }
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encountered while analyzing constraints. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            constraintsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        constraintsAnalysis.setCriterionIsSatisfied(satisfied && constraintsAnalysis.isDmlStatementsWithMistakesEmpty() && constraintsAnalysis.isInsertStatementsChecked());
        this.logger.info("Finished constraint analysis. Criterion satisfied: " + constraintsAnalysis.isCriterionSatisfied());
        return constraintsAnalysis;
    }
    //endregion
}
