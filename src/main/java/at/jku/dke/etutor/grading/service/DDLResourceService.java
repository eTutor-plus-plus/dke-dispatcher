package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.objects.dispatcher.ddl.DDLExerciseDTO;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Objects;

@Service
public class DDLResourceService {
    //region Constants

    private final String DDL_BASE_URL;
    private final String DDL_DATABASE_URL;
    private final String CONN_DDL_SYSTEM_USER;
    private final String CONN_DDL_SYSTEM_PWD;
    //endregion

    //region Fields
    private final SubmissionDispatcherService dispatcherService;
    private final GradingDTORepository gradingDTORepository;

    private ApplicationProperties properties;
    private final Logger logger;
    //endregion

    public DDLResourceService(SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository, ApplicationProperties properties) {
        this.logger = (Logger) LoggerFactory.getLogger(DDLResourceService.class);
        this.dispatcherService = dispatcherService;
        this.gradingDTORepository = gradingDTORepository;
        this.properties = properties;

        // Initialize constants
        this.DDL_BASE_URL = properties.getDatasource().getUrl();
        this.DDL_DATABASE_URL = this.DDL_BASE_URL + properties.getDdl().getConnUrl();
        this.CONN_DDL_SYSTEM_USER = properties.getDdl().getSystemConnUser();
        this.CONN_DDL_SYSTEM_PWD = properties.getDdl().getSystemConnPwd();
    }

    /**
     * Function to create a new exercise
     * @param exerciseDTO Specifies the exercise
     * @return Returns the id of the exercise
     */
    public int createExercise(DDLExerciseDTO exerciseDTO) throws DatabaseException {
        logger.debug("Creating exercise");
        try(Connection con = DriverManager.getConnection(DDL_DATABASE_URL, CONN_DDL_SYSTEM_USER, CONN_DDL_SYSTEM_PWD)) {
            con.setAutoCommit(false);

            // Get exerciseId
            int exerciseId = getAvailableExerciseId();

            // Create exercise
            createExerciseUtil(con, exerciseId, exerciseDTO);
            logger.debug("Exercise created");
            return exerciseId;
        } catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Function to delete an existing exercise by the given id
     * @param id Specifies the exercise
     */
    public void deleteExercise(int id) throws DatabaseException {
        logger.debug("Deleting exercise with id {}", id);
        try(Connection con = DriverManager.getConnection(DDL_DATABASE_URL, CONN_DDL_SYSTEM_USER, CONN_DDL_SYSTEM_PWD)){
            con.setAutoCommit(false);

            // Delete the exercise
            deleteExerciseUtil(con , id);
            logger.debug("Exercise deleted");
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }

    }

    /**
     * Function to update an existing exercise
     * @param id Specifies the exercise
     * @param exerciseDTO Specifies the new exerciseDTO
     */
    public void updateExercise(int id, DDLExerciseDTO exerciseDTO) throws DatabaseException {
        Objects.requireNonNull(exerciseDTO.getSolution());
        if(exerciseDTO.getSolution().isEmpty()) {
            return;
        }

        logger.debug("Updating solution of exercise {}", id);
        try(Connection con = DriverManager.getConnection(DDL_DATABASE_URL, CONN_DDL_SYSTEM_USER, CONN_DDL_SYSTEM_PWD)){
            con.setAutoCommit(false);

            // Update the exercise solution
            updateExerciseUtil(con, id, exerciseDTO);
            logger.debug("Exercise updated");
        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }

    }

    /**
     * Function to get the solution of an exercise
     * @param id Specifies the exercise
     * @return Returns the solution as a string
     */
    public String getSolution(int id) throws DatabaseException {
        try(Connection con = DriverManager.getConnection(DDL_DATABASE_URL, CONN_DDL_SYSTEM_USER, CONN_DDL_SYSTEM_PWD)){
            con.setAutoCommit(false);
            return getSolutionUtil(con, id);
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return "";
    }

    //region Private Methods

    /**
     * Fetches an available exercise id
     * @return the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    private int getAvailableExerciseId() throws DatabaseException {
        try(Connection con = DriverManager.getConnection(DDL_DATABASE_URL, CONN_DDL_SYSTEM_USER, CONN_DDL_SYSTEM_PWD)){
            con.setAutoCommit(false);
            return getAvailableExerciseIdUtil(con);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }

    }

    /**
     * Function to get the next free exercise id
     * @param con Specifies the connection
     * @return Returns the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    private int getAvailableExerciseIdUtil(Connection con) throws DatabaseException {
        String fetchMaxIdQuery = "SELECT max(id) as id from exercises";
        int maxId = -1;

        try(PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery()
        ) {
            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else throw new DatabaseException("Internal Error: could not fetch exercise id");

            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not assign exercise-id", throwables);
        }
        return maxId;
    }

    /**
     * Persists the exercise
     * @param con the Connection
     * @param id the exercise id
     * @param exerciseDTO Specifies the exercise
     * @throws DatabaseException if an error occurs
     */
    private void createExerciseUtil(Connection con, int id, DDLExerciseDTO exerciseDTO) throws DatabaseException {
        try(PreparedStatement createExerciseStmt = con.prepareStatement("INSERT INTO EXERCISES VALUES(?,?,?,?,?,?,?,?,?,?)")){
            // Create exercise schema
            String schemaName = "ddl_schema_" + id;
            String query = "CREATE SCHEMA IF NOT EXISTS " + schemaName + " AUTHORIZATION " + CONN_DDL_SYSTEM_USER;
            PreparedStatement createSchemaStmt = con.prepareStatement(query);
            logger.debug("Statement for creating exercise schema: {} ", createSchemaStmt);
            createSchemaStmt.execute();

            // Set schema to exercise schema
            Statement switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO " + schemaName);

            // Execute ddl solution
            PreparedStatement solutionStmt = con.prepareStatement(exerciseDTO.getSolution());
            solutionStmt.execute();

            // Set schema to public schema
            switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO public");

            createExerciseStmt.setInt(1, id);
            createExerciseStmt.setString(2, schemaName);
            createExerciseStmt.setString(3, exerciseDTO.getSolution());
            createExerciseStmt.setString(4, exerciseDTO.getInsertStatements());
            createExerciseStmt.setInt(5, Integer.parseInt(exerciseDTO.getMaxPoints()));
            createExerciseStmt.setInt(6, Integer.parseInt(exerciseDTO.getTablePoints()));
            createExerciseStmt.setInt(7, Integer.parseInt(exerciseDTO.getColumnPoints()));
            createExerciseStmt.setInt(8, Integer.parseInt(exerciseDTO.getPrimaryKeyPoints()));
            createExerciseStmt.setInt(9, Integer.parseInt(exerciseDTO.getForeignKeyPoints()));
            createExerciseStmt.setInt(10, Integer.parseInt(exerciseDTO.getConstraintPoints()));
            logger.debug("Statement for creating exercise: {} ", createExerciseStmt);
            createExerciseStmt.executeUpdate();
            con.commit();
        }catch(SQLException throwables){
            handleThrowables(con, "Could not create exercise "+id, throwables);
        }
    }

    /**
     * Utility method for deleting an exercise
     * @param con the Connection
     * @param id the id of the exercise
     * @throws DatabaseException if an SQLException occurs
     */
    private void deleteExerciseUtil(Connection con, int id) throws DatabaseException {
        try (PreparedStatement exerciseStmt = con.prepareStatement("SELECT e.SCHEMA_NAME as schemaName FROM EXERCISES e WHERE e.ID = ?")){
            // Get the schema name
            exerciseStmt.setInt(1, id);
            ResultSet rs = exerciseStmt.executeQuery();
            // Check if the result set has a solution
            if(!rs.next()) {
                logger.error("Schema not found!");
                return;
            }

            String schemaName = rs.getString("schemaName");

            // Drop schema with all tables
            String query = "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE";
            PreparedStatement dropStmt = con.prepareStatement(query);
            dropStmt.execute();

            // Delete exercise from exercises table
            query = "DElETE FROM EXERCISES WHERE ID = " + id;
            PreparedStatement deleteStmt = con.prepareStatement(query);
            deleteStmt.executeUpdate();
            con.commit();
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not delete exercise", throwables);
        }
    }

    /**
     * Utility methods to update the exercise solution
     * @param con the Connection
     * @param id the id
     * @param exerciseDTO Specifies the new exercise
     * @throws DatabaseException if an error occurs
     */
    private void updateExerciseUtil(Connection con, int id, DDLExerciseDTO exerciseDTO) throws DatabaseException {
        try(PreparedStatement exerciseStmt = con.prepareStatement("SELECT e.SCHEMA_NAME as schemaName FROM EXERCISES e WHERE e.ID = ?")){
            // Get the schema name
            exerciseStmt.setInt(1, id);
            ResultSet rs = exerciseStmt.executeQuery();
            // Check if the result set has a solution
            if(!rs.next()) {
                logger.error("Schema not found!");
                return;
            }

            String schemaName = rs.getString("schemaName");

            // Reset schema
            String query = "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE";
            PreparedStatement dropStmt = con.prepareStatement(query);
            dropStmt.execute();

            query = "CREATE SCHEMA IF NOT EXISTS " + schemaName + " AUTHORIZATION " + CONN_DDL_SYSTEM_USER;
            PreparedStatement createSchemaStmt = con.prepareStatement(query);
            createSchemaStmt.execute();

            // Set schema to exercise schema
            Statement switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO " + schemaName);

            // Execute new solution
            PreparedStatement solutionStmt = con.prepareStatement(exerciseDTO.getSolution());
            solutionStmt.execute();

            // Set schema to public schema
            switchSchemaStmt = con.createStatement();
            switchSchemaStmt.execute("SET search_path TO public");

            // Update exercise in exercise table
            PreparedStatement updateStatement = con.prepareStatement("UPDATE EXERCISES SET (schema_name,solution,insert_statements,max_points,table_points,column_points,primarykey_points,foreignkey_points,constraint_points) = (?,?,?,?,?,?,?,?,?) WHERE ID = ?");
            updateStatement.setString(1, schemaName);
            updateStatement.setString(2, exerciseDTO.getSolution());
            updateStatement.setString(3, exerciseDTO.getInsertStatements());
            updateStatement.setInt(4, Integer.parseInt(exerciseDTO.getMaxPoints()));
            updateStatement.setInt(5, Integer.parseInt(exerciseDTO.getTablePoints()));
            updateStatement.setInt(6, Integer.parseInt(exerciseDTO.getColumnPoints()));
            updateStatement.setInt(7, Integer.parseInt(exerciseDTO.getPrimaryKeyPoints()));
            updateStatement.setInt(8, Integer.parseInt(exerciseDTO.getForeignKeyPoints()));
            updateStatement.setInt(9, Integer.parseInt(exerciseDTO.getConstraintPoints()));
            updateStatement.setInt(10, id);
            updateStatement.executeUpdate();
            con.commit();
        }catch(SQLException throwables){
            handleThrowables(con, "Could not update solution", throwables);
        }
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
            }
        } catch (SQLException throwables) {
            handleThrowables(con, "Could not execute query "+query, throwables);
        }
        return "";
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
    //endregion
}
