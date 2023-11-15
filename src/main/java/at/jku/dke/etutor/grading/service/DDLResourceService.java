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
    private final String DDL_ADMINISTRATION_URL;
    private final String DDL_EXERCISE_DB;
    private final String DDL_EXERCISE_URL;
    private final String CONN_DDL_SYSTEM_USER;
    private final String CONN_DDL_SYSTEM_PWD;
    private final String CONN_SUPER_USER;
    private final String CONN_SUPER_PWD;
    private final String TASK_TYPE = "ddl";
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
        this.DDL_ADMINISTRATION_URL = this.DDL_BASE_URL + properties.getDdl().getConnUrl();
        this.DDL_EXERCISE_DB = properties.getDdl().getExerciseDatabase();
        this.DDL_EXERCISE_URL = this.DDL_BASE_URL + this.DDL_EXERCISE_DB;
        this.CONN_DDL_SYSTEM_USER = properties.getDdl().getSystemConnUser();
        this.CONN_DDL_SYSTEM_PWD = properties.getDdl().getSystemConnPwd();
        this.CONN_SUPER_USER = properties.getGrading().getConnSuperUser();
        this.CONN_SUPER_PWD = properties.getGrading().getConnSuperPwd();
    }

    /**
     * Function to create a new exercise
     * @param exerciseDTO Specifies the exercise
     * @return Returns the id of the exercise
     */
    public int createExercise(DDLExerciseDTO exerciseDTO) throws DatabaseException {
        logger.debug("Creating exercise");
        try(Connection con = DriverManager.getConnection(DDL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)) {
            con.setAutoCommit(false);

            // Get exerciseId
            int exerciseId = getAvailableExerciseId();

            // Create exercise and commit in database
            createExerciseUtil(con, exerciseId, exerciseDTO);
            con.commit();

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
        try(Connection con = DriverManager.getConnection(DDL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);

            // Delete the exercise
            deleteExerciseUtil(con , id);
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
        try(Connection con = DriverManager.getConnection(DDL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
            con.setAutoCommit(false);

            // Update the exercise solution
            updateExerciseSolutionUtil(con, id, exerciseDTO.getSolution());
            logger.debug("Solution updated");
        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }

    }

    //region Private Methods

    /**
     * Fetches an available exercise id
     * @return the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    private int getAvailableExerciseId() throws DatabaseException {
        try(Connection con = DriverManager.getConnection(DDL_ADMINISTRATION_URL, CONN_SUPER_USER, CONN_SUPER_PWD)){
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
        logger.debug("Creating exercise");

        try(PreparedStatement createExerciseStmt = con.prepareStatement("INSERT INTO EXERCISES VALUES(?,?,?,?,?,?,?,?,?,?);")){
            //todo Create schema
            String schemaName = "ddl_schema_" + id;

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
     * Utility methods to update the exercise solution
     * @param con the Connection
     * @param id the id
     * @param newSolution the solution
     * @throws DatabaseException if an error occurs
     */
    private void updateExerciseSolutionUtil(Connection con, int id, String newSolution) throws DatabaseException {
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
