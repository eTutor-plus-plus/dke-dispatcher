package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.logging.Logger;

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
        this.logger= Logger.getLogger("at.jku.dke.etutor.sqlexercisemanager");

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
        logger.info(()->"Creating schemas with prefix "+schemaName);
        createSchema(schemaName + SUBMISSION_SUFFIX);
        createSchema(schemaName + DIAGNOSE_SUFFIX);
        logger.info(()->"Schemas with prefix "+schemaName+" created");
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
        logger.info(()->"Deleting schmemas with prefix "+schemaName);
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
            logger.info(()->"Query for dropping schema: "+dropStmt);
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
        final String deleteExercisesQuery = "DELETE FROM exercises WHERE submission_db = ?";
        final String deleteConnQuery = "DELETE FROM connections WHERE conn_string LIKE '%?currentSchema="+schemaName+"_%'";

        try(PreparedStatement deleteExercisesStmt = con.prepareStatement(deleteExercisesQuery);
            PreparedStatement deleteConnStmt = con.prepareStatement(deleteConnQuery)){

            int connId = fetchConnection(con, schemaName+"_submission");
            if(connId != -1) {
                logger.info(()->"Query for deleting exercises: "+deleteExercisesStmt);
                deleteExercisesStmt.setInt(1, connId);
                deleteExercisesStmt.executeUpdate();
                logger.info(()->"Query for deleting connection: "+deleteConnStmt);
                deleteConnStmt.executeUpdate();
                con.commit();
            }else{
                logger.info(()->"No connections found for schema "+schemaName);
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
        logger.warning(message);
        throwables.printStackTrace();
        try {
            con.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
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
        logger.info(()->"Query for creating table: "+query);
        if (!query.replace(" ", "").toLowerCase().contains("createtable")) {
            logger.warning("Not a crate-table-statement");
            return;
        }
        executeUpdate(schemaName + SUBMISSION_SUFFIX, query);
        executeUpdate(schemaName + DIAGNOSE_SUFFIX, query);
        logger.info(()->"Tables in schemas with prefix "+schemaName+" created");
    }

    /**
     * Deletes a table in the SQL_EXERCISE_DB
     * @param schemaName the schema
     * @param tableName the table to be deleted in the schema
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteTables(String schemaName, String tableName) throws DatabaseException {
        logger.info(()->"Deleting table "+tableName+" in schemas with prefix "+schemaName);
        deleteTable(schemaName + SUBMISSION_SUFFIX, tableName);
        deleteTable(schemaName + DIAGNOSE_SUFFIX, tableName);
        logger.info(()->"Tables in schema with prefix "+schemaName+" deleted");
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
            logger.info(()->"Query for deleting table: "+deleteStmt);
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
        logger.info(()->"Query for inserting data: "+query);
        insertData(schemaName + SUBMISSION_SUFFIX, query);
        logger.info(()->"Inserting data into submission schema of "+schemaName+" complete");
    }

    /**
     * Inserts data into the diagnose-version of schema
     * @param schemaName the schema
     * @param query the query
     * @throws DatabaseException if an SQLException occurs
     */
    public void insertDataDiagnose(String schemaName, String query) throws DatabaseException {
        logger.info(()->"Query for inserting data: "+query);
        insertData(schemaName + DIAGNOSE_SUFFIX, query);
        logger.info(()->"Inserting data into diagnose schema of "+schemaName+" complete");
    }

    public void insertData(String schemaName, String query) throws DatabaseException {
        if (!query.replace(" ", "").toLowerCase().contains("insertinto")) {

            logger.warning("Not an insert-into-statement " + query);
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
        logger.info(()->"Creating exercise in schema with prefix "+schemaName + " and id "+id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            int diagnoseConnID = fetchConnection(con, schemaName + DIAGNOSE_SUFFIX);
            int submissionConnID = fetchConnection(con, schemaName + SUBMISSION_SUFFIX);
            if (diagnoseConnID == -1) diagnoseConnID = createConnection(con, schemaName + DIAGNOSE_SUFFIX);
            if (submissionConnID == -1) submissionConnID = createConnection(con, schemaName + SUBMISSION_SUFFIX);

            if (diagnoseConnID == -1 || submissionConnID == -1) {
                logger.warning("Could not fetch / create connection id");
                throw new SQLException();
            }
            createExerciseUtil(con, id, submissionConnID, diagnoseConnID, solution);
            con.commit();
            logger.info("Exercise created");
        }catch(SQLException throwables){
            throwables.printStackTrace();
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
            logger.info(()->"Query for fetching connection:" +connExistsStmt);
            logger.info(()->"Fetching connection id for schema "+ schemaName);

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
        logger.info(()->"Creating connection for schema "+schemaName);
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

            logger.info(()->"Statement for creating connection: "+ createConnectionStmt);
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
        logger.info("Creating exercise");

        try(PreparedStatement createExerciseStmt = con.prepareStatement("INSERT INTO EXERCISES VALUES(?,?,?,?);")){
            createExerciseStmt.setInt(1, id);
            createExerciseStmt.setInt(2, submissionConnID);
            createExerciseStmt.setInt(3, diagnoseConnID);
            createExerciseStmt.setString(4, solution);
            logger.info(()->"Statement for creating exercise: "+createExerciseStmt);
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
        logger.info(()->"Deleting exercise with id "+id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            deleteExerciseUtil(con , id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    public void updateExerciseSolution(int id, String newSolution) throws DatabaseException {
        logger.info(()->"Updating solution of exercise "+id);
        try(Connection con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);
            updateExerciseSolutionUtil(con, id, newSolution);
            logger.info(()->"Solution updated");
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    public void updateExerciseSolutionUtil(Connection con, int id, String newSolution) throws DatabaseException {
        String updateQuery = "UPDATE EXERCISES SET SOLUTION = '" + newSolution +"' WHERE ID = " + id;
        try(PreparedStatement updateStatement = con.prepareStatement(updateQuery)){
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
            logger.info("Exercise deleted");
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

    private void executeUpdateUtil(Connection con, String query) throws DatabaseException {
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not execute query "+query, throwables);
        }
    }

}
