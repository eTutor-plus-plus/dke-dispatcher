package at.jku.dke.etutor.modules.ddl.analysis;


import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;
import at.jku.dke.etutor.modules.ddl.serverAdministration.DBHelper;
import at.jku.dke.etutor.modules.ddl.serverAdministration.DBUserAdmin;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;

public class DDLAnalyzer {
    //region Constants
    private final String INTERNAL_ERROR = "This is an internal system error.";
    private final String CONTACT_ADMIN = "Please contact the system administrator.";
    private final String VARCHAR = "varchar";
    //endregion

    //region Fields
    private Logger logger;
    private Connection systemConn;
    private DatabaseMetaData userMetadata;
    private DatabaseMetaData systemMetadata;
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
    public DDLAnalysis analyze(Serializable submission, DDLAnalyzerConfig config) throws SQLException {
        String msg;
        String submittedQuery;
        DDLAnalysis analysis = new DDLAnalysis();
        DDLCriterionAnalysis criterionAnalysis;
        DBUserAdmin admin = DBUserAdmin.getAdmin();

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

        //todo Manage databases

        // Get connections
        systemConn = DBHelper.getSystemConnection();

        // Execute query
        // Check correct syntax
        if (config.isCriterionToAnalyze(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
            criterionAnalysis = this.analyzeSyntax(config, submittedQuery);
            analysis.addCriterionAnalysis(DDLEvaluationCriterion.CORRECT_SYNTAX, criterionAnalysis);
            if(criterionAnalysis.getAnalysisException() != null && !criterionAnalysis.isCriterionSatisfied()) {
                analysis.setAnalysisException(criterionAnalysis.getAnalysisException());
                return analysis;
            }
        }

        // Get metadata
        try {
            systemMetadata = systemConn.getMetaData();
            userMetadata = config.getConn().getMetaData();
        } catch (SQLException ex) {
            msg = "";
            msg = msg.concat("Error encounted while getting metadata. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            analysis.setAnalysisException(new AnalysisException(msg, ex));
            return analysis;
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
            // Execute query to check the correctness of the query
            Statement stmt = config.getConn().createStatement();
            stmt.executeQuery(submittedQuery);
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
        boolean exists = false;
        String msg;

        try {
            ResultSet userRs = userMetadata.getTables(null, null, null, new String[]{"TABLE"});
            ResultSet systemRS = systemMetadata.getTables(null, null, null, new String[]{"TABLE"});

            // Search for missing tables
            while (systemRS.next()) {
                String systemTable = systemRS.getString("TABLE_NAME");
                while (userRs.next()) {
                    String userTable = userRs.getString("TABLE_NAME");

                    //todo Check if ignorecase is correct
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

            // Search for surplus tables
            while (userRs.next()) {
                String userTable = userRs.getString("TABLE_NAME");
                while (systemRS.next()) {
                    String systemTable = systemRS.getString("TABLE_NAME");

                    //todo Check if ignorecase is correct
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
            msg = msg.concat("Error encounted while analyzing tables. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            tablesAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        // Set submission is correct for this criterion
        tablesAnalysis.setCriterionIsSatisfied(tablesAnalysis.isMissingTablesEmpty() && tablesAnalysis.isSurplusTablesEmpty());
        this.logger.info("Finished table analysis.");
        return tablesAnalysis;
    }

    private DDLCriterionAnalysis analyzeColumns(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze columns");

        ColumnsAnalysis columnsAnalysis = new ColumnsAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, null, null, new String[]{"TABLE"});

            // Run through all tables and look at the columns
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userColumns = userMetadata.getColumns(null, null, tableName, null);
                ResultSet systemColumns = systemMetadata.getColumns(null, null, tableName, null);

                // Search for missing columns
                while (systemColumns.next()) {
                    String systemColumn = systemColumns.getString("COLUMN_NAME");
                    while (userColumns.next()) {
                        String userColumn = userColumns.getString("COLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;

                            // Check datatype
                            int systemDatatype = systemColumns.getInt("DATA_TYPE");
                            int userDatatype = userColumns.getInt("DATA_TYPE");

                            if(systemDatatype != userDatatype) {
                                // Check if both datatypes are varchar -> every varchar should be treated as equal
                                //todo Check if this is correct
                                String sd = systemColumns.getString("TYPE_NAME");
                                String ud = userColumns.getString("TYPE_NAME");
                                if(!(sd.toLowerCase().contains(VARCHAR) && ud.toLowerCase().contains(VARCHAR))) {
                                    columnsAnalysis.addWrongDatatypeColumn(systemColumn);
                                }
                            }

                            // Check nullable
                            int systemNullable = systemColumns.getInt("NULLABLE");
                            int userNullable = userColumns.getInt("NULLABLE");

                            if(systemNullable != userNullable) {
                                columnsAnalysis.addWrongNullColumn(systemColumn);
                            }

                            // Check default value
                            String systemDefault = systemColumns.getString("COLUMN_DEF");
                            String userDefault = userColumns.getString("COLUMN_DEF");

                            if(!systemDefault.equals(userDefault)) {
                                columnsAnalysis.addWrongDefaultColumn(systemColumn);
                            }

                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        columnsAnalysis.addMissingColumn(systemColumn);
                    }

                    // Reset variables
                    userColumns.beforeFirst();
                    exists = false;
                }

                // Search for surplus columns
                while (userColumns.next()) {
                    String userColumn = userColumns.getString("COLUMN_NAME");
                    while (systemColumns.next()) {
                        String systemColumn = systemColumns.getString("COLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare table names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        columnsAnalysis.addSurplusColumn(userColumn);
                    }

                    // Reset variables
                    systemColumns.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing columns. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            columnsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        columnsAnalysis.setCriterionIsSatisfied(columnsAnalysis.isMissingColumnsEmpty() && columnsAnalysis.isSurplusColumnsEmpty() && columnsAnalysis.isWrongNullColumnsEmpty() && columnsAnalysis.isWrongDatatypeColumnsEmpty() && columnsAnalysis.isWrongDefaultColumnsEmpty());
        this.logger.info("Finished column analysis.");
        return columnsAnalysis;
    }

    private DDLCriterionAnalysis analyzePrimaryKeys(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze primary keys");

        PrimaryKeysAnalysis primaryKeysAnalysis = new PrimaryKeysAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, null, null, new String[]{"TABLE"});

            // Run through all tables and look at the primary keys
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userPrimaryKeys = userMetadata.getPrimaryKeys(null, null, tableName);
                ResultSet systemPrimaryKeys = systemMetadata.getPrimaryKeys(null, null, tableName);

                // Search for missing primary keys
                while (systemPrimaryKeys.next()) {
                    String systemColumn = systemPrimaryKeys.getString("COLUMN_NAME");
                    while (userPrimaryKeys.next()) {
                        String userColumn = userPrimaryKeys.getString("COLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        primaryKeysAnalysis.addMissingPrimaryKey(systemColumn);
                    }

                    // Reset variables
                    userPrimaryKeys.beforeFirst();
                    exists = false;
                }

                // Search for surplus primary keys
                while (userPrimaryKeys.next()) {
                    String userColumn = userPrimaryKeys.getString("COLUMN_NAME");
                    while (systemPrimaryKeys.next()) {
                        String systemColumn = systemPrimaryKeys.getString("COLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare table names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        primaryKeysAnalysis.addSurplusPrimaryKey(userColumn);
                    }

                    // Reset variables
                    systemPrimaryKeys.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing primary keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            primaryKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        primaryKeysAnalysis.setCriterionIsSatisfied(primaryKeysAnalysis.isMissingPrimaryKeysEmpty() && primaryKeysAnalysis.isSurplusPrimaryKeysEmpty());
        this.logger.info("Finished primary key analysis.");
        return primaryKeysAnalysis;
    }

    private DDLCriterionAnalysis analyzeForeignKeys(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze foreign keys");

        ForeignKeysAnalysis foreignKeysAnalysis = new ForeignKeysAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, null, null, new String[]{"TABLE"});

            // Run through all tables and look at the foreign keys
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userForeignKeys = userMetadata.getImportedKeys(null, null, tableName);
                ResultSet systemForeignKeys = systemMetadata.getImportedKeys(null, null, tableName);

                // Search for missing primary keys
                while (systemForeignKeys.next()) {
                    String systemColumn = systemForeignKeys.getString("FKCOLUMN_NAME");
                    while (userForeignKeys.next()) {
                        String userColumn = userForeignKeys.getString("FKCOLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        foreignKeysAnalysis.addMissingForeignKey(systemColumn);
                    }

                    // Reset variables
                    userForeignKeys.beforeFirst();
                    exists = false;
                }

                // Search for surplus primary keys
                while (userForeignKeys.next()) {
                    String userColumn = userForeignKeys.getString("FKCOLUMN_NAME");
                    while (systemForeignKeys.next()) {
                        String systemColumn = systemForeignKeys.getString("FKCOLUMN_NAME");

                        //todo Check if ignorecase is correct
                        // Compare table names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        foreignKeysAnalysis.addSurplusForeignKey(userColumn);
                    }

                    // Reset variables
                    systemForeignKeys.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing foreign keys. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            foreignKeysAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        foreignKeysAnalysis.setCriterionIsSatisfied(foreignKeysAnalysis.isMissingForeignKeysEmpty() && foreignKeysAnalysis.isSurplusForeignKeysEmpty());
        this.logger.info("Finished foreign key analysis.");
        return foreignKeysAnalysis;
    }

    private DDLCriterionAnalysis analyzeConstraints(DDLAnalyzerConfig config, String submittedQuery) {
        this.logger.info("Analyze constraints");

        ConstraintsAnalysis constraintsAnalysis = new ConstraintsAnalysis();
        boolean exists = false;

        try {
            ResultSet userTables = userMetadata.getTables(null, null, null, new String[]{"TABLE"});

            // Run through all tables and look at the unique constraints
            while (userTables.next()) {
                String tableName = userTables.getString("TABLE_NAME");

                ResultSet userConstraints = userMetadata.getIndexInfo(null, null, tableName, true, true);
                ResultSet systemConstraints = systemMetadata.getIndexInfo(null, null, tableName, true, true);

                // Search for missing unique constraints
                while (systemConstraints.next()) {
                    String systemColumn = systemConstraints.getString("INDEX_NAME");
                    while (userConstraints.next()) {
                        String userColumn = userConstraints.getString("INDEX_NAME");

                        //todo Check if ignorecase is correct
                        // Compare primary key column names
                        if(userColumn.equalsIgnoreCase(systemColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        constraintsAnalysis.addMissingConstraint(systemColumn);
                    }

                    // Reset variables
                    userConstraints.beforeFirst();
                    exists = false;
                }

                // Search for surplus unique constraints
                while (userConstraints.next()) {
                    String userColumn = userConstraints.getString("INDEX_NAME");
                    while (systemConstraints.next()) {
                        String systemColumn = systemConstraints.getString("INDEX_NAME");

                        //todo Check if ignorecase is correct
                        // Compare table names
                        if(systemColumn.equalsIgnoreCase(userColumn)) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if the table exists
                    if(!exists) {
                        constraintsAnalysis.addSurplusConstraint(userColumn);
                    }

                    // Reset variables
                    systemConstraints.beforeFirst();
                    exists = false;
                }
            }
        } catch (SQLException ex) {
            String msg = "";
            msg = msg.concat("Error encounted while analyzing constraints. ");
            msg = msg.concat(INTERNAL_ERROR);
            msg = msg.concat(CONTACT_ADMIN);

            this.logger.error(msg, ex);
            constraintsAnalysis.setAnalysisException(new AnalysisException(msg, ex));
        }

        constraintsAnalysis.setCriterionIsSatisfied(constraintsAnalysis.isMissingConstraintsEmpty() && constraintsAnalysis.isSurplusConstraintsEmpty());
        this.logger.info("Finished constraint analysis.");
        return constraintsAnalysis;
    }
    //endregion
}
