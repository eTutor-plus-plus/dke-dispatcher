package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.PmExerciseConfigDTO;
import at.jku.dke.etutor.grading.rest.dto.PmExerciseLogDTO;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.AlphaAlgorithm;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Arc;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Log;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Trace;
import at.jku.dke.etutor.modules.pm.PmDataSource;
import at.jku.dke.etutor.modules.pm.plg.application.SimulationApplication;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service used to manipulate schemas and exercises for the PM module
 */
@Service
public class PmResourceService {

    private final String CONN_PM_USER;
    private final String CONN_PM_PWD;
    private final String PM_ADMINISTRATION_URL;
    private final String CONN_SUPER_USER;
    private final String CONN_SUPER_PWD;

    private final String TASK_TYPE = "pm";
    private final SubmissionDispatcherService dispatcherService;
    private final GradingDTORepository gradingDTORepository;

    private final Logger logger;


    /**
     * The constructor
     * @param properties the injected application properties
     * @throws ClassNotFoundException if class not found
     */
    public PmResourceService(SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository, ApplicationProperties properties) throws ClassNotFoundException{
        Class.forName(properties.getGrading().getJDBCDriver());
        this.logger = (Logger) LoggerFactory.getLogger("at.jku.dke.etutor.pmexercisemanager");
        this.dispatcherService = dispatcherService;
        this.gradingDTORepository = gradingDTORepository;

        CONN_PM_USER = properties.getProcessMining().getConnUser();
        CONN_PM_PWD = properties.getProcessMining().getConnPwd();
        PM_ADMINISTRATION_URL = properties.getProcessMining().getConnUrl();
        CONN_SUPER_USER = properties.getGrading().getConnSuperUser();
        CONN_SUPER_PWD = properties.getGrading().getConnSuperPwd();
    }

    /**
     * Creates configuration to control for log attributes
     * @param exerciseConfigDTO contains the values needed for exercise configuration
     * @return
     * @throws Exception
     */
    public int createExerciseConfiguration(PmExerciseConfigDTO exerciseConfigDTO) throws Exception{
        logger.debug("Creating Exercise");

        try(Connection conn = PmDataSource.getConnection()){
            var x = Class.forName("org.postgresql.Driver");
            conn.setAutoCommit(false);

            int exerciseConfigID = getAvailableConfigID();
            createExerciseConfigurationUtil(conn, exerciseConfigID, exerciseConfigDTO);
            conn.commit();
            logger.debug("ExerciseConfig created");

            return exerciseConfigID;

        }catch(SQLException e){
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Persists the configuration
     * @param conn the Connection
     * @param id the ConfigurationID
     * @param exerciseConfigDTO the parameters given by user
     * @throws DatabaseException
     */
    private void createExerciseConfigurationUtil(Connection conn, int id, PmExerciseConfigDTO exerciseConfigDTO) throws DatabaseException{
        logger.debug("Creating config...");

        String insertStmt = "INSERT INTO exerciseconfiguration VALUES (?, ?, ?, ?, ?, ?);";
        try(PreparedStatement createConfigStmt = conn.prepareStatement(insertStmt)){
            createConfigStmt.setInt(1, id);
            createConfigStmt.setInt(2, exerciseConfigDTO.getMaxActivity());
            createConfigStmt.setInt(3, exerciseConfigDTO.getMinActivity());
            createConfigStmt.setInt(4, exerciseConfigDTO.getMaxLogSize());
            createConfigStmt.setInt(5, exerciseConfigDTO.getMinLogSize());
            createConfigStmt.setString(6, exerciseConfigDTO.getConfigNum());

            logger.debug("Statement for creating configuration: {}", createConfigStmt);
            createConfigStmt.executeUpdate();
        }catch (SQLException throwables){
            handleThrowables(conn, "Could not create configuration "+id, throwables);
        }
    }

    /**
     * Fetches an available configID
     * @return the configID
     * @throws Exception
     */
    private int getAvailableConfigID() throws Exception{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            return getAvailableConfigIDUtil(conn);
        }catch (SQLException throwable){
            throwable.printStackTrace();
            throw new DatabaseException(throwable);
        }
    }

    private int getAvailableConfigIDUtil(Connection conn) throws Exception{
        String fetchMaxIdQuery = "SELECT max(config_id) as id FROM exerciseconfiguration";
        int maxId = -1;

        try(PreparedStatement fetchMaxIdStmt = conn.prepareStatement(fetchMaxIdQuery);
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery()){
            // check if result set is empty
            // if yes -> assign first value
            // note: check for correctness
            if(!maxIdSet.isBeforeFirst()){
                maxId = 1;
            }else if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                throw new DatabaseException("Internal Error: could not fetch configuration id");
            }

            conn.commit();
        }catch (SQLException throwables){
            handleThrowables(conn, "Could not assign configID" ,throwables);
        }

        return maxId;
    }

//    /**
//     * VERSION 1
//     * Updates the configuration number of an existing configuration
//     * @param id the ID of the existing configuration
//     * @param parameterValue the value to change to
//     * @throws DatabaseException
//     */
//    public void updateExerciseConfiguration(int id, String parameterValue) throws DatabaseException{
//        Objects.requireNonNull(parameterValue);
//        if(parameterValue.isEmpty()) return;
//
//        logger.debug("Updating configuration {}",id);
//        try(Connection conn = PmDataSource.getConnection()){
//            conn.setAutoCommit(false);
//            updateExerciseConfigurationUtil(conn, id, parameterValue);
//            logger.debug("Configuration updated");
//
//        }catch (SQLException throwables){
//            logger.error(throwables.getMessage(), throwables);
//            throw new DatabaseException(throwables);
//        }
//    }

    /**
     * VERSION 2 - 01.11.2022
     * Updates the configuration parameters of an existing configuration
     * @param id the ID of the existing configuration
     * @param exerciseConfigDTO config parameters to be changed
     * @throws DatabaseException
     */
    public void updateExerciseConfiguration(int id, PmExerciseConfigDTO exerciseConfigDTO) throws DatabaseException{

        logger.debug("Updating configuration {}",id);
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            updateExerciseConfigurationUtil(conn, id, exerciseConfigDTO);
            logger.debug("Configuration updated");

        }catch (SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

//    /**
//     * VERSION 1
//     * Utility to update the configuration
//     * @param conn the Connection
//     * @param id the configuration ID
//     * @param parameterValue the value to change to
//     */
//    public void updateExerciseConfigurationUtil(Connection conn, int id, String parameterValue) throws DatabaseException {
//        String updateQuery = "UPDATE exerciseconfiguration SET configuration_number = ? WHERE config_id = ?;";
//        try(PreparedStatement updateStmt = conn.prepareStatement(updateQuery)){
//            updateStmt.setString(1, parameterValue);
//            updateStmt.setInt(2, id);
//            updateStmt.executeUpdate();
//            conn.commit();
//        }catch(SQLException throwables){
//            handleThrowables(conn, "Could not update configuration", throwables);
//        }
//    }

    /**
     * VERSION 2 - 01.11.2022
     * Utility to update the configuration
     * @param conn the Connection
     * @param id the configuration ID
     * @param exerciseConfigDTO parameters of configuration
     */
    public void updateExerciseConfigurationUtil(Connection conn, int id, PmExerciseConfigDTO exerciseConfigDTO) throws DatabaseException {
        String updateQueryMaxActivity = "UPDATE exerciseconfiguration SET max_activity = ? WHERE config_id = ?;";
        String updateQueryMinActivity = "UPDATE exerciseconfiguration SET min_activity = ? WHERE config_id = ?;";
        String updateQueryMaxLog = "UPDATE exerciseconfiguration SET max_logsize = ? WHERE config_id = ?;";
        String updateQueryMinLog = "UPDATE exerciseconfiguration SET min_logsize = ? WHERE config_id = ?;";
        String updateQueryConfig = "UPDATE exerciseconfiguration SET configuration_number = ? WHERE config_id = ?;";
        try(
            PreparedStatement updateStmtMaxActivity = conn.prepareStatement(updateQueryMaxActivity);
            PreparedStatement updateStmtMinActivity = conn.prepareStatement(updateQueryMinActivity);
            PreparedStatement updateStmtMaxLog = conn.prepareStatement(updateQueryMaxLog);
            PreparedStatement updateStmtMinLog = conn.prepareStatement(updateQueryMinLog);
            PreparedStatement updateStmtConfig = conn.prepareStatement(updateQueryConfig)){

            // update max_activity
            updateStmtMaxActivity.setInt(1, exerciseConfigDTO.getMaxActivity());
            updateStmtMaxActivity.setInt(2,id);
            updateStmtMaxActivity.executeUpdate();

            // update min_activity
            updateStmtMinActivity.setInt(1, exerciseConfigDTO.getMinActivity());
            updateStmtMinActivity.setInt(2, id);
            updateStmtMinActivity.executeUpdate();

            // update max_logsize
            updateStmtMaxLog.setInt(1, exerciseConfigDTO.getMaxLogSize());
            updateStmtMaxLog.setInt(2, id);
            updateStmtMaxLog.executeUpdate();

            // update min_logsize
            updateStmtMinLog.setInt(1, exerciseConfigDTO.getMinLogSize());
            updateStmtMinLog.setInt(2, id);
            updateStmtMinLog.executeUpdate();

            // update config_number
            updateStmtConfig.setString(1, exerciseConfigDTO.getConfigNum());
            updateStmtConfig.setInt(2, id);
            updateStmtConfig.executeUpdate();

            // commit
            conn.commit();
        }catch(SQLException throwables){
            handleThrowables(conn, "Could not update configuration", throwables);
        }
    }

    /**
     * Deletes an exercise configuration by the given config id
     * @param id the configuration id
     * @throws DatabaseException
     */
    public void deleteExerciseConfiguration(int id) throws DatabaseException{
        logger.debug("Deleting exercise configuration with id: {}", id);
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            deleteExerciseConfigurationUtil(conn, id);
            logger.debug("Configuration deleted");

        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility method for deleting exercise configuration
     * @param conn the connection
     * @param id the configuration to be deleted
     * @throws DatabaseException
     */
    public void deleteExerciseConfigurationUtil(Connection conn, int id) throws DatabaseException{
        final String deleteQuery = "DELETE FROM exerciseconfiguration WHERE config_id = ?;";

        try(PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)){
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
            conn.commit();
        } catch (SQLException throwables){
            handleThrowables(conn, "Could not delete configuration", throwables);
        }
    }

    /**
     * Method creates a random exercise based on the given configuration id
     * @param configId the configuration id
     * @return returns exercise id of created exercise
     * @throws Exception
     */
    public int createRandomExercise(int configId) throws Exception{

            logger.debug("Creating random exercise with config {}", configId);
            try(Connection conn = PmDataSource.getConnection()){
                conn.setAutoCommit(false);

                int exerciseId = getAvailableExerciseId();
                int logId = getAvailableLogId();
                createRandomExerciseUtil(conn, exerciseId, configId, logId);
                conn.commit();

                logger.debug("Random Exercise created");
                return exerciseId;
            }catch (SQLException throwables){
                logger.error(throwables.getMessage(), throwables);
                throw new DatabaseException(throwables);
            }
    }

    /**
     *
     * @param conn
     * @param exerciseId
     * @param configId
     * @throws DatabaseException
     */
    private void createRandomExerciseUtil(Connection conn, int exerciseId, int configId, int logId) throws Exception{
        logger.debug("Creating random exercise...");
        Log simulatedLog = new Log();
        AlphaAlgorithm alphaAlgorithm;
        Map<String, Object> resultMap;

        // combine generated traces to log
        try{
            logger.debug("Combine traces to log.");
            for(String [] strings: createCorrespondingLog(configId, exerciseId, logId)){
                simulatedLog.addTrace(new Trace(strings));
            }
        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }
        alphaAlgorithm = new AlphaAlgorithm(simulatedLog);
        // solve the alpha algorithm
        resultMap = alphaAlgorithm.run();

        // convert to String
        ObjectMapper mapper = new ObjectMapper();
        String orI1 = mapper.writeValueAsString(resultMap.get("orI1"));
        String orI2 = mapper.writeValueAsString(resultMap.get("orI2"));
        String orI3 = mapper.writeValueAsString(resultMap.get("orI3"));
        String orI4 = mapper.writeValueAsString(resultMap.get("orI4"));
        String aaI1 = mapper.writeValueAsString(resultMap.get("aaI1"));
        String aaI2 = mapper.writeValueAsString(resultMap.get("aaI2"));
        String aaI3 = mapper.writeValueAsString(resultMap.get("aaI3"));
        String aaI4 = mapper.writeValueAsString(resultMap.get("aaI4"));
        String aaI5 = mapper.writeValueAsString(resultMap.get("aaI5"));
        String aaI6 = mapper.writeValueAsString(resultMap.get("aaI6"));

        List<Arc<?,?>> aa7List = (List<Arc<?,?>>) resultMap.get("aaI7");
        String aaI7 = aa7List.stream().map(Object::toString).collect(Collectors.joining(", "));

        String createRandomExerciseQuery = "INSERT INTO randomexercises " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?);";

        try(PreparedStatement createRdExStmt = conn.prepareStatement(createRandomExerciseQuery)){
            createRdExStmt.setInt(1, exerciseId);
            createRdExStmt.setInt(13, configId);

            createRdExStmt.setString(2, orI1);
            createRdExStmt.setString(3, orI2);
            createRdExStmt.setString(4, orI3);
            createRdExStmt.setString(5, " ");
            //createRdExStmt.setString(5, orI4);
            createRdExStmt.setString(6, aaI1);
            createRdExStmt.setString(7, aaI2);
            createRdExStmt.setString(8, aaI3);
            createRdExStmt.setString(9, aaI4);
            createRdExStmt.setString(10, aaI5);
            createRdExStmt.setString(11, aaI6);
            createRdExStmt.setString(12, aaI7);

            logger.debug("Statement for creating exercise: {} ", createRdExStmt);
            createRdExStmt.executeUpdate();

        }catch (SQLException throwables){
            handleThrowables(conn, "Could not create exercise " + exerciseId, throwables);
        }
    }

    /**
     * Method creates a random process with the given configId, simulates this process and returns created log
     * @param configId the Configuration Id
     * @param exerciseId the corresponding exerciseId which is also used as corresponding logId
     * @return returns list of traces (log)
     * @throws Exception
     */
    private List<String[]> createCorrespondingLog(int configId, int exerciseId, int logId) throws Exception{
        logger.debug("Creating random generated Log with id {}", exerciseId);
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);

            List<String[]> resultList= createCorrespondingLogUtil(conn, exerciseId, configId, logId);
            conn.commit();
            logger.debug("Corresponding Log created");
            return resultList;
        }catch (SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility to generate a random process, simulate this process and storing resulting log in database
     * @param conn the Connection
     * @param exerciseId traces are stored with corresponding exerciseId
     * @param configId the configuration
     * @return a list of traces (log)
     * @throws Exception
     */
    private List<String[]> createCorrespondingLogUtil(Connection conn, int exerciseId, int configId, int logId) throws Exception{
        logger.debug("Creating log... ");
        int maxActivity;
        int minActivity;
        int maxLogSize;
        int minLogSize;
        String configNum;

        String configReadQuery = "SELECT * FROM exerciseconfiguration WHERE config_id = ?;";
        String logUpdateQuery = "INSERT INTO logs VALUES (?,?,?);";

        try(    PreparedStatement configReadStmt = conn.prepareStatement(configReadQuery);
                PreparedStatement createLogStmt = conn.prepareStatement(logUpdateQuery)){

            configReadStmt.setInt(1, configId);
            ResultSet rs = configReadStmt.executeQuery();

            if(rs.next()){
                maxActivity = rs.getInt("max_activity");
                minActivity = rs.getInt("min_activity");
                maxLogSize = rs.getInt("max_logsize");
                minLogSize = rs.getInt("min_logsize");
                configNum = rs.getString("configuration_number");
            }else{
                logger.error("Internal Error: could not fetch Configuration");
                throw new DatabaseException("Internal Error: could not fetch Configuration");
            }

            // actual random process generation
            List<String[]> traces = SimulationApplication.finalLogGeneration(minLogSize,maxLogSize,minActivity,maxActivity,configNum);

            for(String [] string: traces){
                Array traceArray = conn.createArrayOf("VARCHAR", string);
                createLogStmt.setInt(1, logId);
                createLogStmt.setInt(2,exerciseId);
                createLogStmt.setArray(3, traceArray);
                logger.debug("Statement for creating log {}", createLogStmt);
                createLogStmt.executeUpdate();
                logId++;
            }

            return traces;
        }catch(SQLException throwables){
            handleThrowables(conn, "Could not create log to exercise " +exerciseId, throwables);
            throw new DatabaseException(throwables);    // note: question: possible to throw 2 times same exception?
        }
    }

    /**
     * Fetches an available exerciseId
     * @return returns exerciseId
     * @throws DatabaseException
     */
    private int getAvailableExerciseId() throws DatabaseException{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            return getAvailableExerciseIdUtil(conn);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    private int getAvailableExerciseIdUtil(Connection conn) throws DatabaseException{
        String fetchMaxIdQuery = "SELECT max(exercise_id) as id FROM randomexercises;";
        int maxId = -1;

        try (PreparedStatement fetchMaxIdStmt = conn.prepareStatement(fetchMaxIdQuery)){
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();

            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                throw new DatabaseException("Internal Error: Could not fetch exerciseId");
            }
            conn.commit();

        }catch (SQLException throwables){
            handleThrowables(conn, "Could not assign exerciseId", throwables);
        }
        return maxId;
    }

    /**
     * Fetches an available logId
     * @return returns logId
     * @throws DatabaseException
     */
    private int getAvailableLogId() throws DatabaseException{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            return getAvailableLogIdUtil(conn);
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    private int getAvailableLogIdUtil(Connection conn) throws DatabaseException{
        String fetchMaxLogIdQuery = "SELECT max(log_id) as id FROM logs;";
        int maxId = -1;

        try (PreparedStatement fetchMaxLogIdStmt = conn.prepareStatement(fetchMaxLogIdQuery)){
            ResultSet maxIdSet = fetchMaxLogIdStmt.executeQuery();

            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                throw new DatabaseException("Internal Error: Could not fetch logId");
            }
            conn.commit();

        }catch (SQLException throwables){
            handleThrowables(conn, "Could not assign logId", throwables);
        }
        return maxId;
    }

    /**
     * Returns an existing configuration
     * @param id the dispatcher id of the configuration
     * @return the configuration
     * @throws ExerciseManagementException
     */
    public PmExerciseConfigDTO fetchExerciseConfiguration(int id) throws DatabaseException{
        logger.debug("Fetching Configuration {}", id);
        PmExerciseConfigDTO exerciseConfigDTO = null;

        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            exerciseConfigDTO = fetchExerciseConfigurationUtil(conn, id);
            logger.debug("Configuration fetched");

            return exerciseConfigDTO;
        }catch (SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }

    }

    /**
     * Utility to fetch existing configuration
     * @param conn the connection
     * @param id the configuration id
     * @return a {@link PmExerciseConfigDTO} with the corresponding parameter
     */
    private PmExerciseConfigDTO fetchExerciseConfigurationUtil(Connection conn, int id) throws DatabaseException{
        String fetchConfigQuery = "SELECT * FROM exerciseconfiguration WHERE config_id = ?;";
        PmExerciseConfigDTO exerciseConfigDTO = new PmExerciseConfigDTO();

        try(PreparedStatement fetchConfigStmt = conn.prepareStatement(fetchConfigQuery)){
            fetchConfigStmt.setInt(1, id);
            ResultSet rs = fetchConfigStmt.executeQuery();

            if(rs.next()){
                exerciseConfigDTO.setMaxActivity(rs.getInt("max_activity"));
                exerciseConfigDTO.setMinActivity(rs.getInt("min_activity"));
                exerciseConfigDTO.setMaxLogSize(rs.getInt("max_logsize"));
                exerciseConfigDTO.setMinLogSize(rs.getInt("min_logsize"));
                exerciseConfigDTO.setConfigNum(rs.getString("configuration_number"));

            }else{
                throw new DatabaseException("Internal Error: Could not fetch configuration");
            }
            conn.commit();
        }catch(SQLException throwables){
            handleThrowables(conn, "Could not fetch configuration", throwables);
        }

        return exerciseConfigDTO;
    }

    /**
     * Returns a log corresponding to a exercise
     * @param exerciseId the dispatcher id of the exercise
     * @return
     * @throws DatabaseException
     */
    public PmExerciseLogDTO fetchExerciseLog(int exerciseId) throws DatabaseException{
        logger.debug("Fetching log {}", exerciseId);
        PmExerciseLogDTO pmExerciseLogDTO = null;

        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            pmExerciseLogDTO = fetchExerciseLogUtil(conn, exerciseId);
            logger.debug("Log fetched");

            return pmExerciseLogDTO;
        }catch(SQLException throwables){
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    /**
     * Utility to fetch existing log
     * @param conn the connection
     * @param exerciseId the corresponding exercise id
     * @return a {@link PmExerciseLogDTO} wrapping the information
     * @throws DatabaseException
     */
    private PmExerciseLogDTO fetchExerciseLogUtil(Connection conn, int exerciseId) throws DatabaseException{
        String fetchLogQuery = "SELECT trace FROM logs WHERE exercise_id = ?";
        PmExerciseLogDTO pmExerciseLogDTO = new PmExerciseLogDTO();
        pmExerciseLogDTO.setExerciseId(exerciseId);

        try(PreparedStatement fetchLogStmt = conn.prepareStatement(fetchLogQuery)){
            fetchLogStmt.setInt(1, exerciseId);
            ResultSet rs = fetchLogStmt.executeQuery();

            while(rs.next()){
                Array a = rs.getArray("trace");
                pmExerciseLogDTO.addTrace((String[])a.getArray());
            }

            conn.commit();
        }catch(SQLException throwables){
            handleThrowables(conn, "Could not fetch Log", throwables);
        }


        return pmExerciseLogDTO;
    }

    /**
     * Handles an SQLException by rolling back the connection, logging a message and rethrowing a DatabaseException
     * @param conn the Connection
     * @param message the message to be logged
     * @param throwables the SQL Exception
     * @throws DatabaseException
     */
    private void handleThrowables(Connection conn, String message, Exception throwables) throws DatabaseException{
        logger.warn(message, throwables);
        try{
            conn.rollback();
        }catch (SQLException e){
            logger.error(e.getMessage(), e);
        }
        throw new DatabaseException(throwables);
    }
}
