package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.modules.sql.SQLConstants;
import oracle.jdbc.proxy.annotation.Pre;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.sql.*;

@Service
public class SQLResourceManager {
    private static String URL = SQLConstants.CONN_URL_BASE;
    private static String CONN_SQL_USER = SQLConstants.CONN_USER;
    private static String CONN_SQL_PWD = SQLConstants.CONN_PWD;
    private static String CONN_SUPER_USER = "etutor";
    private static String CONN_SUPER_PWD = "etutor";

    private Connection con;

    public SQLResourceManager() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    public void createSchema(String schemaName) throws DatabaseException{
        try {
            con = DriverManager.getConnection(URL+"/sql_exercises", CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String createSchemaQuery = "CREATE SCHEMA IF NOT EXISTS "+ schemaName+ " AUTHORIZATION "+ CONN_SUPER_USER+";";
            String grantSelectForUserSQLQuery = "GRANT SELECT ON ALL TABLES IN SCHEMA "+schemaName+ " TO "+ CONN_SQL_USER+";";

            PreparedStatement createSmt = con.prepareStatement(createSchemaQuery);
            createSmt.executeUpdate();

            PreparedStatement grantStmt = con.prepareStatement(grantSelectForUserSQLQuery);
            grantStmt.executeUpdate();

            createSmt.close();
            grantStmt.close();
            con.commit();
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

    public void deleteSchema(String schemaName)throws DatabaseException {
        try {
            con = DriverManager.getConnection(URL+"/sql_exercises", CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String dropSchemaQuery = "DROP SCHEMA IF EXISTS "+ schemaName+ " CASCADE;";

            PreparedStatement dropStmt = con.prepareStatement(dropSchemaQuery);
            dropStmt.executeUpdate();

            dropStmt.close();
            con.commit();
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

    public void createTable(String schemaName, String query)throws DatabaseException{
        try {
            con = DriverManager.getConnection(URL+"/sql_exercises?currentSchema="+schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            PreparedStatement createStmt = con.prepareStatement(query);
            createStmt.executeUpdate();

            con.commit();
            createStmt.close();
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

    public void deleteTable(String schemaName, String tableName) throws DatabaseException{
        try {
            con = DriverManager.getConnection(URL+"/sql_exercises?currentSchema="+schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String deleteQuery = "DROP TABLE IF EXISTS " + tableName + " CASCADE";
            PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
            deleteStmt.executeUpdate();

            con.commit();
            deleteStmt.close();
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

    public void insertData(String schemaName, String query) throws DatabaseException{
        if(!query.contains("INSERT INTO")) return;
        try {
            con = DriverManager.getConnection(URL+"/sql_exercises?currentSchema="+schemaName, CONN_SUPER_USER, CONN_SUPER_PWD);
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

    public void createExercise(int id, String schemaName, String solution) throws DatabaseException{
        try {
            con = DriverManager.getConnection(URL+"/sql", CONN_SUPER_USER, CONN_SUPER_PWD);
            con.setAutoCommit(false);

            String connectionExistsQuery = "Select * from connections where conn_string like '%sql_exercises?currentSchema="+schemaName+"';";
            PreparedStatement connExistsStmt = con.prepareStatement(connectionExistsQuery);
            ResultSet connectionSet = connExistsStmt.executeQuery();
            int connID = -1;

            if(connectionSet.next()){
                connID = connectionSet.getInt("id");
            }else{
                String maxIDQuery = "SELECT max(ID) as max_id FROM CONNECTIONS;";
                PreparedStatement maxIDStmt = con.prepareStatement(maxIDQuery);
                ResultSet maxIDSet = maxIDStmt.executeQuery();
                int maxID = -1;
                if(maxIDSet.next()){
                    maxID = maxIDSet.getInt("max_id");
                }
                System.out.println("MAX ID: "+ maxID);
                maxID++;
                connID= maxID;

                String createConnectionString = "INSERT INTO CONNECTIONS VALUES("+maxID+","+ "'" +URL+"/sql_exercises?currentSchema="+schemaName+"'"+ ", 'sql', 'sql')";
                PreparedStatement createConnectionStmt = con.prepareStatement(createConnectionString);
                createConnectionStmt.executeUpdate();
                createConnectionStmt.close();
                maxIDStmt.close();
            }

            String createExerciseQuery = "INSERT INTO exercises VALUES("+ id +", "+ connID + ", "+ connID+", '"+  solution +"');";
            PreparedStatement createExerciseStmt = con.prepareStatement(createExerciseQuery);
            createExerciseStmt.executeUpdate();

            con.commit();
            createExerciseStmt.close();
            connExistsStmt.close();
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
