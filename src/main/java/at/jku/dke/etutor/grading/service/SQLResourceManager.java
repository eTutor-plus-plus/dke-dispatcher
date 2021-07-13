package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.ETutorGradingConstants;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.logging.Logger;

@Service
public class SQLResourceManager {
    private static final String SQL_BASE_URL = SQLConstants.CONN_URL_BASE;
    private static final String SQL_ADMINISTRATION_URL = SQLConstants.CONN_URL;
    private static final String SQL_EXERCISE_DB = SQLConstants.EXERCISE_DB;
    private static final String SQL_EXERCISE_URL = SQL_BASE_URL+"/"+SQL_EXERCISE_DB;
    private static final String CONN_SQL_USER = SQLConstants.CONN_USER;
    private static final String CONN_SQL_PWD = SQLConstants.CONN_PWD;
    private static final String CONN_SUPER_USER = ETutorGradingConstants.CONN_USER;
    private static final String CONN_SUPER_PWD = ETutorGradingConstants.CONN_PWD;
    private static final String SUBMISSION_SUFFIX = SQLConstants.SUBMISSION_SUFFIX;
    private static final String DIAGNOSE_SUFFIX = SQLConstants.DIAGNOSE_SUFFIX;
    private static final String JDBC_SCHEMA_OPTION = "?currentSchema=";

    private Logger logger;

    public SQLResourceManager() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        this.logger= Logger.getLogger("at.jku.dke.etutor.sqlexercisemanager");
    }

    /**
     * Takes a name for a schema and creates 1 submission and 1 diagnose schema with it
     * @param schemaName the schema-prefix
     * @throws DatabaseException if an SQLException occurs
     */
    public void createSchemas(String schemaName) throws DatabaseException {
        logger.info("Creating schemas with prefix "+schemaName);
        createSchema(schemaName + SUBMISSION_SUFFIX);
        createSchema(schemaName + DIAGNOSE_SUFFIX);
        logger.info("Schemas with prefix "+schemaName+" created");
    }

    /**
     * creates a schema in the SQL_EXERCISE_DB with the owner etutor and grants select privileges to the SQL_USER for it
     * @param schemaName the schema name
     * @throws DatabaseException if an SQLException occurs
     */
    public void createSchema(String schemaName) throws DatabaseException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_EXERCISE_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String createSchemaQuery = "CREATE SCHEMA " + schemaName + " AUTHORIZATION " + CONN_SUPER_USER + " GRANT USAGE ON SCHEMA " + schemaName + " TO " + CONN_SQL_USER + ";";
            String grantSelectForUserSQLQuery = " ALTER DEFAULT PRIVILEGES IN SCHEMA " + schemaName + " GRANT SELECT ON TABLES TO " + CONN_SQL_USER + ";";

            PreparedStatement createSmt = con.prepareStatement(createSchemaQuery);
            createSmt.executeUpdate();

            PreparedStatement grantStmt = con.prepareStatement(grantSelectForUserSQLQuery);
            grantStmt.executeUpdate();

            createSmt.close();
            grantStmt.close();
            con.commit();
            con.close();
        } catch (SQLException throwables) {
            logger.warning("Could not create schema "+schemaName);
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }

    /**
     * takes the prefix of a schema and deletes the submission and diagnose version of it
     * @param schemaName the prefix of the schema
     * @throws DatabaseException if an error occurs
     */
    public void deleteSchemas(String schemaName) throws DatabaseException {
        logger.info("Deleting schmemas with prefix "+schemaName);
        deleteSchema(schemaName + SUBMISSION_SUFFIX);
        deleteSchema(schemaName + DIAGNOSE_SUFFIX);
    }

    /**
     * deletes a schema in the SQL_EXERCISE_DB
     * @param schemaName the schema to be deleted
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteSchema(String schemaName) throws DatabaseException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_EXERCISE_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            PreparedStatement dropStmt = con.prepareStatement("DROP SCHEMA IF EXISTS "+schemaName+" CASCADE;");
            logger.info("Query for dropping schema: "+dropStmt);
            dropStmt.executeUpdate();

            dropStmt.close();
            con.commit();
            con.close();

        } catch (SQLException throwables) {
            logger.warning("Could not delete schema "+schemaName);
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }
    public void deleteConnection(String schemaName) throws DatabaseException {
        Connection con = null;
        try{
            con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            int connId = fetchConnection(con, schemaName+"_submission");
            PreparedStatement deleteExercisesStmt = con.prepareStatement("DELETE FROM exercises WHERE submission_db = ?");
            deleteExercisesStmt.setInt(1, connId);
            logger.info("Query for deleting exercises: "+deleteExercisesStmt);
            if(connId != -1) deleteExercisesStmt.executeUpdate();


            PreparedStatement deleteConnStmt = con.prepareStatement("DELETE FROM connections WHERE conn_string LIKE '%?currentSchema="+schemaName+"_%'");
            logger.info("Query for deleting connection: "+deleteConnStmt);
            deleteConnStmt.executeUpdate();

            con.commit();
            con.close();
            deleteConnStmt.close();
            deleteExercisesStmt.close();
        }catch (SQLException throwables) {
            logger.warning("Could not delete connection for "+schemaName);
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }
    /**
     * Executes a query intended to create a table in the submission and diagnose schema. Checks if the query contains "create table"
     * @param schemaName the prefix identifying the schemas
     * @param query the query to be executed
     * @throws DatabaseException if an SQLException occurs or the query does not contain "create table"
     */
    public void createTables(String schemaName, String query) throws DatabaseException {
        logger.info("Query for creating table: "+query);
        if (!query.toLowerCase().contains("create table")) {
            logger.warning("Not a crate-table-statement");
            return;
        }
        executeUpdate(schemaName + SUBMISSION_SUFFIX, query);
        executeUpdate(schemaName + DIAGNOSE_SUFFIX, query);
        logger.info("Tables in schemas with prefix "+schemaName+" created");
    }

    /**
     * Deletes a table in the SQL_EXERCISE_DB
     * @param schemaName the schema
     * @param tableName the table to be deleted in the schema
     * @throws DatabaseException if an SQLException occurs
     */
    public void deleteTables(String schemaName, String tableName) throws DatabaseException {
        logger.info("Deleting table "+tableName+" in schemas with prefix "+schemaName);
        deleteTable(schemaName + SUBMISSION_SUFFIX, tableName);
        deleteTable(schemaName + DIAGNOSE_SUFFIX, tableName);
        logger.info("Tables in schema with prefix "+schemaName+" deleted");
    }

    /**
     * deletes a table
     * @param schemaName
     * @param tableName
     * @throws DatabaseException
     */
    public void deleteTable(String schemaName, String tableName) throws DatabaseException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_EXERCISE_URL+ JDBC_SCHEMA_OPTION + schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            PreparedStatement deleteStmt = con.prepareStatement("DROP TABLE IF EXISTS "+tableName+" CASCADE");
            logger.info("Query for deleting table: "+deleteStmt);
            deleteStmt.executeUpdate();

            con.commit();
            deleteStmt.close();
            con.close();
        } catch (SQLException throwables) {
            logger.warning("Could not delete table "+tableName + " in schema "+schemaName);
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }

    }

    /**
     * Executes an "insert into"-statement in the specified schema of the SQL_EXERCISE_DB
     * @param schemaName the prefix identifying the schema
     * @param query the insert into statement
     * @throws DatabaseException if an SQLException occurs or the query does not contain "insert into"
     */
    public void insertDataSubmission(String schemaName, String query) throws DatabaseException {
        logger.info("Query for inserting data: "+query);
        insertData(schemaName + SUBMISSION_SUFFIX, query);
        logger.info("Inserting data into submission schema of "+schemaName+" complete");
    }

    public void insertDataDiagnose(String schemaName, String query) throws DatabaseException {
        logger.info("Query for inserting data: "+query);
        insertData(schemaName + DIAGNOSE_SUFFIX, query);
        logger.info("Inserting data into diagnose schema of "+schemaName+" complete");
    }

    public void insertData(String schemaName, String query) throws DatabaseException {
        if (!query.toLowerCase().contains("insert into")) {
            logger.warning("Not an insert-into-statement");
            return;
        }
        executeUpdate(schemaName, query);
    }

    /**
     * Creates an exercise for matching students submission against it
     * @param id the id of the exercise to be created
     * @param schemaName the prefix of the schema where the data for the exercise are persisted
     * @param solution the solution of the exercise
     * @throws DatabaseException
     */
    public void createExercise(int id, String schemaName, String solution) throws DatabaseException {
        logger.info("Creating exercise in schema with prefix "+schemaName + " and id "+id);
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            int diagnoseConnID = fetchConnection(con, schemaName + DIAGNOSE_SUFFIX);
            int submissionConnID = fetchConnection(con, schemaName + SUBMISSION_SUFFIX);
            if (diagnoseConnID == -1) diagnoseConnID = createConnection(con, schemaName + DIAGNOSE_SUFFIX);
            con.commit();
            if (submissionConnID == -1) submissionConnID = createConnection(con, schemaName + SUBMISSION_SUFFIX);

            if (diagnoseConnID == -1 || submissionConnID == -1) {
                logger.warning("Could not fetch and create connection id");
                throw new SQLException();
            }

            createExerciseUtil(con, id, submissionConnID, diagnoseConnID, solution);

            con.commit();
            con.close();
            logger.info("Exercise created");
        } catch (SQLException throwables) {
            logger.warning("Could not create exercise");
            throwables.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }

    /**
     * Fetches the connection id for the schema
     * @param con
     * @param schemaName the schema name
     * @return -1 if no connection for the specified schmema could be found or the id of the connection otherwise
     * @throws SQLException
     */
    public int fetchConnection(Connection con, String schemaName) throws SQLException {
        logger.info("Fetching connection id for schema "+ schemaName);
        int connID = -1;

        PreparedStatement connExistsStmt = con.prepareStatement("SELECT * FROM CONNECTIONS WHERE conn_string LIKE ?");
        connExistsStmt.setString(1, "%"+SQL_EXERCISE_DB+JDBC_SCHEMA_OPTION+schemaName+"%");
        logger.info("Query for fetching connection:" +connExistsStmt);
        ResultSet connectionSet = connExistsStmt.executeQuery();

        if (connectionSet.next()) {
            connID = connectionSet.getInt("id");
            logger.info("Connection id found");
        }else{
            logger.info("No connection found");
        }
        connExistsStmt.close();

        return connID;
    }

    /**
     * creates a new connection for the specified schema by incrementing the max(id) of the existing connections by 1
     * @param con
     * @param schemaName the schema name for the connection
     * @return
     * @throws SQLException
     */
    public int createConnection(Connection con, String schemaName) throws SQLException {
        logger.info("Creating connection for schema "+schemaName);
        int connID = -1;

        String maxIDQuery = "SELECT max(ID) as max_id FROM CONNECTIONS;";
        PreparedStatement maxIDStmt = con.prepareStatement(maxIDQuery);
        ResultSet maxIDSet = maxIDStmt.executeQuery();

        int maxID = -1;
        if (maxIDSet.next()) {
            maxID = maxIDSet.getInt("max_id");
        }
        if (maxID == -1) return connID;

        maxID++;
        connID = maxID;

        PreparedStatement createConnectionStmt = con.prepareStatement("INSERT INTO CONNECTIONS VALUES(?,?,?,?)");
        createConnectionStmt.setInt(1, connID);
        createConnectionStmt.setString(2, SQL_EXERCISE_URL+"?currentSchema="+schemaName);
        createConnectionStmt.setString(3, CONN_SQL_USER);
        createConnectionStmt.setString(4, CONN_SQL_PWD);
        logger.info("Statement for creating connection: "+ createConnectionStmt);
        createConnectionStmt.executeUpdate();


        createConnectionStmt.close();
        maxIDStmt.close();
        return connID;
    }

    /**
     * Persists the exercise
     * @param con
     * @param id
     * @param submissionConnID
     * @param diagnoseConnID
     * @param solution
     * @throws SQLException
     */
    public void createExerciseUtil(Connection con, int id, int submissionConnID, int diagnoseConnID, String solution) throws SQLException {
        logger.info("Creating exercise");
        PreparedStatement createExerciseStmt = con.prepareStatement("INSERT INTO EXERCISES VALUES(?,?,?,?);");
        createExerciseStmt.setInt(1, id);
        createExerciseStmt.setInt(2, submissionConnID);
        createExerciseStmt.setInt(3, diagnoseConnID);
        createExerciseStmt.setString(4, solution);
        logger.info("Statement for creating exercise: "+createExerciseStmt);
        createExerciseStmt.executeUpdate();
        createExerciseStmt.close();
    }
    public int reserveExerciseID() throws DatabaseException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String fetchMaxIdQuery = "SELECT max(id) as id from exercises";
            String insertQuery = "INSERT INTO EXERCISES VALUES(?, 1, 1, '-1')";
            PreparedStatement stmt = con.prepareStatement(fetchMaxIdQuery);
            ResultSet maxIdSet = stmt.executeQuery();
            int maxId = -1;
            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
            }else throw new DatabaseException();

            maxId++;
            stmt = con.prepareStatement(insertQuery);
            stmt.setInt(1, maxId);
            stmt.executeUpdate();

            con.commit();
            stmt.close();
            con.close();
            return maxId;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }

    /**
     * deletes an exercise identified by the given id
     * @param id
     * @throws DatabaseException
     */
    public void deleteExercise(int id) throws DatabaseException {
        logger.info("Deleting exercise with id "+id);
        Connection con = null;
        try {
            con = DriverManager.getConnection(SQL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            PreparedStatement deleteStmt = con.prepareStatement("DElETE FROM EXERCISES WHERE ID ="+id);
            deleteStmt.executeUpdate();
            con.commit();
            deleteStmt.close();
            con.close();
            logger.info("Exercise deleted");
        } catch (SQLException throwables) {
            logger.warning("Could not delete exercise");
            throwables.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }

    }

    /**
     * utility method that executes a query on a given schema
     * @param schemaName
     * @param query
     * @throws DatabaseException
     */
    void executeUpdate(String schemaName, String query) throws DatabaseException {
        Connection con = null;
        try{
            con = DriverManager.getConnection(SQL_EXERCISE_URL+ JDBC_SCHEMA_OPTION + schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            PreparedStatement insertStmt = con.prepareStatement(query);
            insertStmt.executeUpdate();

            con.commit();
            insertStmt.close();
            con.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                con.rollback();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw new DatabaseException();
        }
    }
}
