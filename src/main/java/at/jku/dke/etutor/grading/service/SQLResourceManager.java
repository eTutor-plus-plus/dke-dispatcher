package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service used to manipulate schemas and exercises for the SQL module
 */
@Service
public class SQLResourceManager {

    private final String SQL_BASE_URL;
    private final String SQL_ADMINISTRATION_URL;
    private final String SQL_EXERCISE_DB;
    private final String SQL_EXERCISE_URL;
    private final String CONN_SQL_USER;
    private final String CONN_SQL_PWD;
    private final String CONN_SUPER_USER;
    private final String CONN_SUPER_PWD;
    private final String SUBMISSION_SUFFIX;
    private final String DIAGNOSE_SUFFIX;
    private final String JDBC_SCHEMA_OPTION = "?currentSchema=";

    private final Logger logger;

    /**
     * The construcotr
     * @param properties the injected application properties
     * @throws ClassNotFoundException if class not found
     */
    public SQLResourceManager(ApplicationProperties properties) throws ClassNotFoundException {
        Class.forName(properties.getGrading().getJDBCDriver());
        this.logger= (Logger) LoggerFactory.getLogger("at.jku.dke.etutor.sqlexercisemanager");

        SQL_BASE_URL=properties.getSql().getConnBaseUrl();
        SQL_ADMINISTRATION_URL=properties.getSql().getConnUrl();
        SQL_EXERCISE_DB=properties.getSql().getExerciseDatabase();
        SQL_EXERCISE_URL= SQL_BASE_URL+"/"+SQL_EXERCISE_DB;
        CONN_SQL_USER=properties.getSql().getConnUser();
        CONN_SQL_PWD=properties.getSql().getConnPwd();
        SUBMISSION_SUFFIX=properties.getSql().getSubmissionSuffix();
        DIAGNOSE_SUFFIX=properties.getSql().getDiagnoseSuffix();
        CONN_SUPER_USER=properties.getGrading().getConnSuperUser();
        CONN_SUPER_PWD=properties.getGrading().getConnSuperPwd();
    }

    /**
     * Takes a name for a schema and creates a submission and a diagnose schema with it
     * @param schemaName the schema-prefix
     * @throws DatabaseException if an SQLException occurs
     */
    public void createSchemas(String schemaName) throws DatabaseException {
        logger.debug("Creating schemas with prefix {}", schemaName);
        createSchema(schemaName + SUBMISSION_SUFFIX);
        createSchema(schemaName + DIAGNOSE_SUFFIX);
        logger.debug("Schemas with prefix {} created", schemaName);
    }

    /**
     * creates a schema in the SQL_EXERCISE_DB with the owner etutor and grants select privileges to the SQL_USER for it
     * @param schemaName the schema name
     * @throws DatabaseException if an SQLException occurs
     */
    public void createSchema(String schemaName) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_EXERCISE_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            createSchemaUtil(con, schemaName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility method for creating a schema to avoid nested try-with-resources blocks
     * @param con the Connection
     * @param schemaName the schema
     * @throws DatabaseException to leverage exception handling
     */
    private void createSchemaUtil(Connection con, String schemaName) throws DatabaseException {
        final String createSchemaQuery = "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + CONN_SUPER_USER + " GRANT USAGE ON SCHEMA " + schemaName + " TO " + CONN_SQL_USER + ";";
        final String grantSelectForUserSQLQuery = " ALTER DEFAULT PRIVILEGES IN SCHEMA " + schemaName + " GRANT SELECT ON TABLES TO " + CONN_SQL_USER + ";";

        try (PreparedStatement createSmt = con.prepareStatement(createSchemaQuery);
             PreparedStatement grantStmt = con.prepareStatement(grantSelectForUserSQLQuery)){
            createSmt.executeUpdate();
            grantStmt.executeUpdate();

            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not create schema "+schemaName, throwables);
        }
    }
    /**
     * takes the prefix of a schema and deletes the submission and diagnose version of it
     * @param schemaName the prefix of the schema
     * @throws DatabaseException if an error occurs
     */
    public void deleteSchemas(String schemaName) throws DatabaseException {
        logger.debug("Deleting schmemas with prefix {}",schemaName);
        deleteSchema(schemaName + SUBMISSION_SUFFIX);
        deleteSchema(schemaName + DIAGNOSE_SUFFIX);
    }

    /**
     * deletes a schema in the SQL_EXERCISE_DB
     * @param schemaName the schema to be deleted
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteSchema(String schemaName) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_EXERCISE_URL, CONN_SUPER_USER, CONN_SUPER_PWD);){
            con.setAutoCommit(false);
            deleteSchemaUtil(con, schemaName);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility method for deleting schema to avoid nested try-with-resources blocks
      * @param con the Connection
     * @param schemaName the schema
     */
    private void deleteSchemaUtil(Connection con, String schemaName) throws DatabaseException {
        final String dropQuery = "DROP SCHEMA IF EXISTS "+schemaName+" CASCADE;";

        try(PreparedStatement dropStmt = con.prepareStatement(dropQuery)) {
            logger.debug("Query for dropping schema: {}",dropStmt);
            dropStmt.executeUpdate();
            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not delete schema "+schemaName, throwables);
        }
    }

    /**
     * Deletes a connection
     * @param schemaName the schema
     * @throws DatabaseException to leverage exception handling
     */
    public void deleteConnection(String schemaName) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            deleteConnectionUtil(con, schemaName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility method for deleting a connection
     * @param con the connection
     * @param schemaName the schemaName
     * @throws DatabaseException to leverage exception handling
     */
    private void deleteConnectionUtil(Connection con, String schemaName) throws DatabaseException {
        final String deleteExercisesQuery = "DELETE FROM exercises WHERE practise_db = ?";
        final String deleteConnQuery = "DELETE FROM connections WHERE conn_string LIKE '%?currentSchema="+schemaName+"_%'";
        final String deleteConnMappingQuery = "DELETE FROM connectionmapping WHERE connection = ?";

        try(PreparedStatement deleteExercisesStmt = con.prepareStatement(deleteExercisesQuery);
            PreparedStatement deleteConnStmt = con.prepareStatement(deleteConnQuery);
            PreparedStatement deleteConnMappingStmt = con.prepareStatement(deleteConnMappingQuery)){

            int connId = fetchConnection(con, schemaName+"_diagnose");
            if(connId != -1) {
                deleteExercisesStmt.setInt(1, connId);
                logger.debug("Query for deleting exercises: {}",deleteExercisesStmt);
                deleteExercisesStmt.executeUpdate();

                deleteConnMappingStmt.setInt(1, connId);
                logger.debug("Query for deleting connection-mapping {}", deleteConnMappingStmt);
                deleteConnMappingStmt.executeUpdate();

                logger.debug("Query for deleting connection: {}", deleteConnStmt);
                deleteConnStmt.executeUpdate();


                con.commit();
            }else{
                logger.info("No connections found for schema {}", schemaName);
            }
        }catch (SQLException throwables) {
            handleThrowables(con, "Could not delete connection for "+schemaName, throwables);
        }
    }
    /**
     * Handles an SQLException by rolling back the connection, logging a message and rethrowing a
     * DatabaseException
     * @param con the Connection
     * @param message the message to be logged
     * @param throwables the SQLException
     * @throws DatabaseException to leverage exception handling
     */
    private void handleThrowables(Connection con, String message, Exception throwables) throws DatabaseException {
        logger.warn(message, throwables);
        try {
            con.rollback();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        throw new DatabaseException(throwables);
    }

    /**
     * Executes a query intended to create a table in the submission and diagnose schema. Checks if the query contains "create table"
     * @param schemaName the prefix identifying the schemas
     * @param query the query to be executed
     * @throws DatabaseException if an SQLException occurs or the query does not contain "create table"
     */
    public void createTables(String schemaName, String query) throws DatabaseException {
        logger.debug("Query for creating table: {}", query);
        if (!query.replace(" ", "").toLowerCase().contains("createtable")) {
            logger.warn("Not a crate-table-statement");
            return;
        }
        if(query.contains("--") || query.contains("/*")){
            logger.warn("No comments allowed");
            return;
        }
        executeUpdate(schemaName + SUBMISSION_SUFFIX, query);
        executeUpdate(schemaName + DIAGNOSE_SUFFIX, query);
        logger.debug("Tables in schemas with prefix {} created", schemaName);
    }

    /**
     * Deletes a table in the SQL_EXERCISE_DB
     * @param schemaName the schema
     * @param tableName the table to be deleted in the schema
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteTables(String schemaName, String tableName) throws DatabaseException {
        logger.debug("Deleting table {} in schemas with prefix {}", tableName, schemaName);
        deleteTable(schemaName + SUBMISSION_SUFFIX, tableName);
        deleteTable(schemaName + DIAGNOSE_SUFFIX, tableName);
        logger.debug("Tables in schema with prefix {} deleted", schemaName);
    }

    /**
     * deletes a table
     * @param schemaName the schema name
     * @param tableName the table name
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteTable(String schemaName, String tableName) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_EXERCISE_URL+ JDBC_SCHEMA_OPTION + schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);){
           con.setAutoCommit(false);
           deleteTableUtil(con, tableName, schemaName);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }

    }

    private void deleteTableUtil(Connection con, String tableName, String schemaName) throws DatabaseException {
        final String deleteQuery = "DROP TABLE IF EXISTS "+tableName+" CASCADE";
        try(PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);) {
            logger.debug("Query for deleting table: {}", deleteStmt);
            deleteStmt.executeUpdate();
            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not delete table "+tableName + " in schema "+schemaName, throwables);
        }
    }
    /**
     * Executes an "insert into"-statement in the specified schema of the SQL_EXERCISE_DB
     * @param schemaName the prefix identifying the schema
     * @param query the insert into statement
     * @throws DatabaseException if an SQLException occurs or the query does not contain "insert into"
     */
    public void insertDataSubmission(String schemaName, String query) throws DatabaseException {
        logger.debug("Query for inserting data: {}", query);
        insertData(schemaName + SUBMISSION_SUFFIX, query);
        logger.debug("Inserting data into submission schema of {} complete", schemaName);
    }

    /**
     * Inserts data into the diagnose-version of schema
     * @param schemaName the schema
     * @param query the query
     * @throws DatabaseException if an SQLException occurs
     */
    public void insertDataDiagnose(String schemaName, String query) throws DatabaseException {
        logger.debug("Query for inserting data: {}", query);
        insertData(schemaName + DIAGNOSE_SUFFIX, query);
        logger.debug("Inserting data into diagnose schema of {} complete", schemaName);
    }

    public void insertData(String schemaName, String query) throws DatabaseException {
        if (!query.replace(" ", "").toLowerCase().contains("insertinto")) {
            logger.warn("Not an insert-into-statement: {}", query);
            return;
        }
        if(query.contains("--") || query.contains("/*")){
            logger.warn("No comments allowed");
            return;
        }
        executeUpdate(schemaName, query);
    }

    /**
     * Creates an exercise to match students submission against it
     * @param id the id of the exercise to be created
     * @param schemaName the prefix of the schema where the data for the exercise are persisted
     * @param solution the solution of the exercise
     * @throws DatabaseException if an SQLExcption occurs
     */
    public void createExercise(int id, String schemaName, String solution) throws DatabaseException {
        logger.debug("Creating exercise in schema with prefix {} and id {} " , schemaName, id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            int diagnoseConnID = fetchConnection(con, schemaName + DIAGNOSE_SUFFIX);
            int submissionConnID = fetchConnection(con, schemaName + SUBMISSION_SUFFIX);
            if (diagnoseConnID == -1) diagnoseConnID = createConnection(con, schemaName + DIAGNOSE_SUFFIX);
            if (submissionConnID == -1) submissionConnID = createConnection(con, schemaName + SUBMISSION_SUFFIX);

            if (diagnoseConnID == -1 || submissionConnID == -1) {
                logger.error("Could not fetch / create connection id");
                throw new SQLException();
            }
            createExerciseUtil(con, id, submissionConnID, diagnoseConnID, solution);
            con.commit();
            addConnectionMapping(schemaName, diagnoseConnID);
            logger.debug("Exercise created");
        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }



    }

    /**
     * Fetches the connection id for a given schema
     * @param con the Connection
     * @param schemaName the schema name
     * @return -1 if no connection for the specified schema could be found
     * @throws DatabaseException if an error occurs
     */
    public int fetchConnection(Connection con, String schemaName) throws DatabaseException {
       final String connExistsQuery=  "SELECT * FROM CONNECTIONS WHERE conn_string LIKE ?";
       int connID = -1;
       try(PreparedStatement connExistsStmt = con.prepareStatement(connExistsQuery)){
            connExistsStmt.setString(1, "%"+SQL_EXERCISE_DB+JDBC_SCHEMA_OPTION+schemaName+"%");
            logger.debug("Query for fetching connection: {}", connExistsStmt);
            logger.debug("Fetching connection id for schema {}",  schemaName);

            try(ResultSet connectionSet = connExistsStmt.executeQuery()){
                if (connectionSet.next()) {
                    connID = connectionSet.getInt("id");
                    logger.info("Connection id found");
                }else{
                    logger.info("No connection found");
                }
            }
        }catch(SQLException throwables){
            handleThrowables(con, "No connection-id found", throwables);
        }
        return connID;
    }

    /**
     * Creates a new connection for the specified schema by incrementing the max(id) of the existing connections by 1
     * @param con the connection
     * @param schemaName the schema name for the connection
     * @return the connection id
     * @throws DatabaseException if an error occurs
     */
    public int createConnection(Connection con, String schemaName) throws DatabaseException {
        logger.info("Creating connection for schema {}", schemaName);
        final String maxIDQuery = "SELECT max(ID) as max_id FROM CONNECTIONS;";
        final String createConnectionQuery = "INSERT INTO CONNECTIONS VALUES(?,?,?,?)";
        int connID = -1;
        int maxID = -1;

        try(PreparedStatement maxIDStmt = con.prepareStatement(maxIDQuery);
            ResultSet maxIDSet = maxIDStmt.executeQuery();
            PreparedStatement createConnectionStmt = con.prepareStatement(createConnectionQuery)){
            if (maxIDSet.next()) {
                maxID = maxIDSet.getInt("max_id");
                maxID++;
                connID = maxID;
            }
            if (maxID == -1) return connID;

            createConnectionStmt.setInt(1, connID);
            createConnectionStmt.setString(2, SQL_EXERCISE_URL+"?currentSchema="+schemaName);
            createConnectionStmt.setString(3, CONN_SQL_USER);
            createConnectionStmt.setString(4, CONN_SQL_PWD);

            logger.debug("Statement for creating connection: {}",  createConnectionStmt);
            createConnectionStmt.executeUpdate();
        }catch(SQLException throwables){
            handleThrowables(con, "Could not create connection", throwables);
        }
        return connID;
    }

    /**
     * Persists the exercise
     * @param con the Connection
     * @param id the exercise id
     * @param submissionConnID the connection id for submissions
     * @param diagnoseConnID the connection id for diagnosis
     * @param solution the solution for the exercise
     * @throws DatabaseException if an error occurs
     */
    public void createExerciseUtil(Connection con, int id, int submissionConnID, int diagnoseConnID, String solution) throws DatabaseException {
        logger.debug("Creating exercise");

        try(PreparedStatement createExerciseStmt = con.prepareStatement("INSERT INTO EXERCISES VALUES(?,?,?,?);")){
            createExerciseStmt.setInt(1, id);
            createExerciseStmt.setInt(2, submissionConnID);
            createExerciseStmt.setInt(3, diagnoseConnID);
            createExerciseStmt.setString(4, solution);
            logger.debug("Statement for creating exercise: {} ", createExerciseStmt);
            createExerciseStmt.executeUpdate();
        }catch(SQLException throwables){
            handleThrowables(con, "Could not create exercise "+id, throwables);
        }
    }

    /**
     * Fetches an available exercise id and reserves it by inserting default values for the given id
     * @return the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    public int reserveExerciseID() throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);){
            con.setAutoCommit(false);
            return reserveExerciseIDUtil(con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }

    }

    private int reserveExerciseIDUtil(Connection con) throws DatabaseException {
        String fetchMaxIdQuery = "SELECT max(id) as id from exercises";
        String insertQuery = "INSERT INTO EXERCISES VALUES(?, 1, 1, '-1')";
        int maxId = -1;

        try(PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();
            PreparedStatement insertStmt = con.prepareStatement(insertQuery);) {
            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else throw new DatabaseException("Internal Error: could not reserve exercise id");
            insertStmt.setInt(1, maxId);
            insertStmt.executeUpdate();

            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not assign exercise-id", throwables);
        }
        return maxId;
    }

    /**
     * deletes an exercise identified by the given id
     * @param id the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteExercise(int id) throws DatabaseException {
        logger.debug("Deleting exercise with id {}", id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            deleteExerciseUtil(con , id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Updates the solution of an existing exercise
     * @param id the id
     * @param newSolution the solution to be updated
     * @throws DatabaseException if an error occurs
     */
    public void updateExerciseSolution(int id, String newSolution) throws DatabaseException {
        logger.debug("Updating solution of exercise {}", id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            updateExerciseSolutionUtil(con, id, newSolution);
            logger.debug("Solution updated");
        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility methods to update the exercise solution
     * @param con the Connection
     * @param id the id
     * @param newSolution the solution
     * @throws DatabaseException if an error occurs
     */
    public void updateExerciseSolutionUtil(Connection con, int id, String newSolution) throws DatabaseException {
        String updateQuery = "UPDATE EXERCISES SET SOLUTION = ? WHERE ID = ?";
        try(PreparedStatement updateStatement = con.prepareStatement(updateQuery)){
            updateStatement.setString(1, newSolution);
            updateStatement.setInt(2, id);
            updateStatement.executeUpdate();
            con.commit();
        }catch(SQLException throwables){
            handleThrowables(con, "Could not update solution", throwables);
        }
    }

    /**
     * Utility method for deleting a exercise
     * @param con the Connection
     * @param id the id of the exercise
     * @throws DatabaseException if an SQLException occurs
     */
    private void deleteExerciseUtil(Connection con, int id) throws DatabaseException {
        final String deleteQuery = "DElETE FROM EXERCISES WHERE ID = ?";

        try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)){
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
            con.commit();
            logger.debug("Exercise deleted");
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not delete exercise", throwables);
        }
    }
    /**
     * Method that executes a query on a given schema
     * @param schemaName the schema
     * @param query the query
     * @throws DatabaseException if an SQLException occurs
     */
    private void executeUpdate(String schemaName, String query) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_EXERCISE_URL+ JDBC_SCHEMA_OPTION + schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);){
            con.setAutoCommit(false);
            executeUpdateUtil(con, query);
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Updates a given query on a given Conection
     * @param con the connection
     * @param query the query
     * @throws DatabaseException if an error occurs
     */
    private void executeUpdateUtil(Connection con, String query) throws DatabaseException {
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not execute query "+query, throwables);
        }
    }

    /**
     * Returns the solution for a given exercise
     * @param id the id
     */
    public String getSolution(int id) throws DatabaseException{
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);){
            con.setAutoCommit(false);
            return getSolutionUtil(con, id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return "";
    }

    /**
     * Utility method for fetching the solution of a given exercise
     * @param con the Connection
     * @param id the id
     * @return the solution
     * @throws DatabaseException if no solution found or SQlException gets thrown
     */
    private String getSolutionUtil(Connection con, int id) throws DatabaseException {
        String query = "SELECT solution FROM exercises where id = "+id;
        try(PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rs = stmt.executeQuery()){
            if(rs.next()){
                logger.debug("Solution found: ");
                logger.debug(rs.getString("solution"));
                return rs.getString("solution");
            }else throw new DatabaseException("No solution found");
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not execute query "+query, throwables);
        }
        return "";
    }

    /**
     * Searches for a given table in the available database connections according to the connectionmapping table in the sql-administration-database.
     * Returns an HTML representation of the table if found.
     * @param tableName the table
     * @param taskGroup the optional taskgroup
     * @return the table as HTML-table
     * @throws DatabaseException if an error occurs
     */
    public String getHTMLTable(String tableName, String taskGroup) throws DatabaseException {
        logger.debug("Searching for table {}",tableName);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);){
            con.setAutoCommit(false);
            var connections = getConnectionsForHTMLTable(con);
            return getHTMLTableUtil(con, connections, tableName);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Returns an HTML-Table of a given table-name in the context of a given exercise.
     * @param exerciseId the id of the exercise
     * @param tableName the name of the table
     * @return the HTML-Table
     * @throws DatabaseException if an error occurs
     */
    public String getHTMLTableByExerciseID(int exerciseId, String tableName) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            int id = getDiagnoseConnectionFromExerciseID(con, exerciseId);
            if(id == -1) return "";

            var tmpList = new ArrayList<Integer>();
            tmpList.add(id);
            return getHTMLTableUtil(con, tmpList, tableName);
        }catch(SQLException e){
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    /**
     * Fetches the ID of the connection for diagnose submission of the given exercise
     * @param con the Connection
     * @param exerciseId the id
     * @return the connection-id
     * @throws DatabaseException if an error occurs
     */
    public int getDiagnoseConnectionFromExerciseID(Connection con, int exerciseId) throws DatabaseException {
        String query = "SELECT * FROM exercises WHERE id = "+exerciseId;
        try(PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rset = stmt.executeQuery()){
            if(rset.next()){
                return rset.getInt("practise_db");
            }
        }catch(SQLException throwables){
            throw new DatabaseException(throwables);
        }
        return -1;
    }

    /**
     * Searches the provided connection-ids for a given table. Returns an HTML-table if found
     * @param con the Connection
     * @param connections the connection-id´s to be searched
     * @param tableName the name of the table
     * @return the table as HTML-table
     * @throws SQLException if an error occurs
     */
    private String getHTMLTableUtil(Connection con, List<Integer> connections, String tableName) throws SQLException {
        var conQuery = "SELECT * FROM connections WHERE id = ?";
        var tableQuery = "SELECT * FROM "+tableName;
        PreparedStatement conStmt = con.prepareStatement(conQuery );


        var url = "";
        var pwd = "";
        var user = "";

        ResultSet conRset;
        for(var id : connections){
            conStmt.setInt(1, id);
            conRset = conStmt.executeQuery();
            if(conRset.next()){
                url = conRset.getString("conn_string");
                pwd = conRset.getString("conn_user");
                user = conRset.getString("conn_pwd");
                try(var tmpCon = DriverManager.getConnection(url, user, pwd)){
                    try(PreparedStatement tableStmt = tmpCon.prepareStatement(tableQuery);
                    ResultSet tableRset = tableStmt.executeQuery()){
                        logger.debug("Table found in connection with id "+id);
                        return generateHTMLTable(tableRset);
                    }catch(SQLException ignore){
                    }
                }catch(SQLException ignore){
                    ignore.printStackTrace();
                }
            }
        }

        conStmt.close();
        return "";
    }

    /**
     * Takes a ResultSet and generates an HTML-Table
     * @param tableRset the ResultSet
     * @return the table as HTML-table
     * @throws SQLException if an error occurs
     */
    private String generateHTMLTable(ResultSet tableRset) throws SQLException {
        logger.debug("Generating HTML table from ResultSet");
        ResultSetMetaData metaData = tableRset.getMetaData();
        var columnCount = metaData.getColumnCount();
        var tableStart = "<table border=1 frame=void rules=rows>";
        var tableEnd = "</table>";
        var tableRowStart = "<tr>";
        var tableRowEnd = "</tr>";
        var headerStart = "<th>";
        var headerEnd = "</th>";
        var dataStart = "<td>";
        var dataEnd = "</td>";


        StringBuilder table = new StringBuilder();

        table.append(tableStart);
        table.append(tableRowStart);
        for(var i = 1; i<=columnCount; i++){
            table.append(headerStart);
            table.append(metaData.getColumnLabel(i));
            table.append(headerEnd);
        }
        table.append(tableRowEnd);

        while(tableRset.next()){
            table.append(tableRowStart);
            for(var j = 1; j<=columnCount; j++){
                table.append(dataStart);
                table.append(tableRset.getObject(j));
                table.append(dataEnd);
            }
            table.append(tableRowEnd);
        }
        table.append(tableEnd);

        logger.debug("Table generated");
        return table.toString();
    }

    /**
     * Fetches the connection ids from the diagnose-connections that are available
     * @param con the Connection
     * @return a list with all the available id´s
     * @throws DatabaseException if an error occurs
     */
    public List<Integer> getConnectionsForHTMLTable(Connection con) throws DatabaseException {
        logger.debug("Fetching available connections to search for table");
        var connectionIds = new ArrayList<Integer>();
        var query = "SELECT connection FROM connectionmapping";
        try(PreparedStatement stmt = con.prepareStatement(query);
        ResultSet rset = stmt.executeQuery()){
            while(rset.next()){
                connectionIds.add(rset.getInt("connection"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }

        logger.debug("Connections fetched");
        return connectionIds;
    }

    /**
     * Adds a newly crated schema to the mapping of the schema and database-name to the connection id
     * @param schema the schema
     * @param id the id
     */
    public void addConnectionMapping(String schema, int id) throws DatabaseException {
        logger.debug("Adding connection to mapping-table");
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            addConnectionMappingUtil(con, schema, id);
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
        logger.debug("Mapping added");
    }

    /**
     * Adds the connection mapping
     * @param con the Connection
     * @param schema the schema
     * @param id the id
     * @throws DatabaseException if an error occurs
     */
    private void addConnectionMappingUtil(Connection con, String schema, int id) throws DatabaseException {
        String query = "INSERT INTO connectionmapping VALUES(?,?,?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, "exercises");
            stmt.setString(2, schema.toLowerCase());
            stmt.setInt(3, id);
            stmt.executeUpdate();
            con.commit();
        }catch(SQLException ignore){
        }
    }
}
