package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.objects.dispatcher.processmining.PmExerciseConfigDTO;
import at.jku.dke.etutor.objects.dispatcher.processmining.PmExerciseLogDTO;
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
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service used to manipulate schemas and exercises for the PM module
 */
@Service
public class PmResourceService {
    private static final Set<Integer> currentlyBufferingConfigurations  =Collections.synchronizedSet(new HashSet<>());
    private static int configIdCounter = 0;
    private static int exerciseIdCounter = 0;
    private static int logIdCounter = 0;
    private final Logger logger;
    private final ApplicationProperties properties;


    /**
     * The constructor
     * @param properties the injected application properties
     * @throws ClassNotFoundException if class not found
     */
    public PmResourceService(ApplicationProperties properties) throws ClassNotFoundException{
        Class.forName(properties.getGrading().getJDBCDriver());
        this.properties = properties;
        this.logger = (Logger) LoggerFactory.getLogger("at.jku.dke.etutor.pmexercisemanager");
    }

    /**
     * Creates configuration to control for log attributes
     * @param exerciseConfigDTO contains the values needed for exercise configuration
     * @return
     * @throws Exception
     */
    public synchronized int createExerciseConfiguration(PmExerciseConfigDTO exerciseConfigDTO) throws Exception{
        logger.debug("Creating Exercise");

        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);

            int exerciseConfigID = getNextConfigID();
            createExerciseConfigurationUtil(conn, exerciseConfigID, exerciseConfigDTO);
            conn.commit();
            logger.debug("ExerciseConfig created");

            new Thread(() -> bufferRandomExercisesForConfiguration(exerciseConfigID, properties.getProcessMining().getInitialExercisesToBuffer())).start();
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
            updateConfigIdCounter(-1);
        }catch (Exception throwables){
            updateConfigIdCounter(-1);
            handleThrowables(conn, "Could not create configuration "+id, throwables);
        }
    }

    /**
     * Fetches an available configID
     * @return the configID
     * @throws Exception
     */
    private synchronized int getNextConfigID() throws Exception{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            int id = getNextConfigIDUtil(conn) + configIdCounter;
            updateConfigIdCounter(1);
            return  id;
        }catch (SQLException throwable){
            throwable.printStackTrace();
            throw new DatabaseException(throwable);
        }
    }

    private static synchronized void updateConfigIdCounter(int increment){
        configIdCounter = configIdCounter + increment;
        if(configIdCounter < 0) configIdCounter = 0;
    }

    private int getNextConfigIDUtil(Connection conn) throws Exception{
        String fetchMaxIdQuery = "SELECT max(config_id) as id FROM exerciseconfiguration";
        int maxId = -1;

        try(PreparedStatement fetchMaxIdStmt = conn.prepareStatement(fetchMaxIdQuery);
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery()){
            // check if result set is empty
            // if yes -> assign first value
            // note: check for correctness
            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                return 0;
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
     * Returns the id of an available exercise for a configuration; i.e. an exercise that has not already been exposed to a client.
     * If no available exercise exists, a number of exercises is initialized for future use as well.
     * @param configId the id of the configuration
     * @return the id of the exercise
     * @throws Exception if an error occurs
     */
    public int getAvailableExerciseForConfiguration(int configId) throws Exception {
        Optional<Integer> availableExerciseId = getAvailableExerciseIdForConfiguration(configId);
        int resultId;
        if(availableExerciseId.isEmpty()) {
            resultId = createRandomExercise(configId, false);
        } else resultId = availableExerciseId.get();

        if(getNumberOfAvailableExercisesForConfiguration(configId) < properties.getProcessMining().getMinimumThresholdForExercises()){
            new Thread(() -> bufferRandomExercisesForConfiguration(configId, 20)).start();
        }
        return  resultId;
    }

    private int getNumberOfAvailableExercisesForConfiguration(int configId) {
         String query = """
                SELECT COUNT(exercise_id)
                FROM randomexercises
                WHERE config_id = ? AND is_available;""";

         try(Connection con = PmDataSource.getConnection(); var stmt = con.prepareStatement(query)){
            stmt.setInt(1, configId);
            var set = stmt.executeQuery();
            if(set != null && set.next()){
                return set.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
         return 0;
    }

    /**
     * Checks if an availalbe exercise for a configuration exists, sets it to unavailable and retuns its id.
     * @param configId the id of the configuration
     * @return the id of the exercise
     */
    private synchronized Optional<Integer> getAvailableExerciseIdForConfiguration(int configId){
        String query = """
                SELECT exercise_id
                FROM randomexercises
                WHERE config_id = ? AND is_available;""";
        String update = """
                UPDATE randomexercises
                SET is_available = false
                WHERE exercise_id = ?;""";

        try(Connection con = PmDataSource.getConnection(); var stmt = con.prepareStatement(query)
        ; var updateStmt = con.prepareStatement(update)){
            stmt.setInt(1, configId);
            var set = stmt.executeQuery();
            if(set != null && set.next()){
                int id = set.getInt("exercise_id");
                updateStmt.setInt(1, id);
                updateStmt.executeUpdate();
                con.commit();
                return Optional.of(id);
            }

        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Creates a number of exercises for a configuration.
     * Sets the flags indicating if the exercises are available to true for all of them.
     * @param configId config id
     * @param n number of exercises to be prepared
     */
    public void bufferRandomExercisesForConfiguration(int configId, int n) {
        if(currentlyBufferingConfigurations.add(configId)){
            for(int i = 0; i < n; i++) { try {
                createRandomExercise(configId, true);
            } catch (Exception e) {
                logger.error("%s %s %s %s".formatted("Could not buffer exercise for ", configId, ". With exception: \n ", e.getMessage()));
            }
            }
            currentlyBufferingConfigurations.remove(configId);
        }
    }

    /**
     * Method creates a random exercise based on the given configuration id
     * @param configId the configuration id
     * @return returns exercise id of created exercise
     * @throws Exception
     */
    public int createRandomExercise(int configId, boolean setAvailable) throws Exception{
            logger.debug("Creating random exercise with config {}", configId);
            try(Connection conn = PmDataSource.getConnection()){
                conn.setAutoCommit(false);
                var optMaxLogSize = getMaxLogSizeForExerciseConfiguration(configId);
                if(optMaxLogSize.isEmpty()){
                    throw new DatabaseException("Could not fetch max log size for configuration: " + configId);
                }
                int exerciseId = getNextExerciseId();
                int logId = getAvailableLogId(optMaxLogSize.orElseThrow());
                createRandomExerciseUtil(conn, exerciseId, configId, logId, optMaxLogSize.orElseThrow(), setAvailable);
                logger.debug("Random Exercise created");
                return exerciseId;
            }catch (SQLException throwables){
                logger.error(throwables.getMessage(), throwables);
                throw new DatabaseException(throwables);
            }
    }

    private Optional<Integer> getMaxLogSizeForExerciseConfiguration(int configId) {
         String configReadQuery = "SELECT max_logsize FROM exerciseconfiguration WHERE config_id = ?;";
         try(Connection con = PmDataSource.getConnection()){
            try(PreparedStatement configReadStmt = con.prepareStatement(configReadQuery)) {
               configReadStmt.setInt(1, configId);
               var set = configReadStmt.executeQuery();
               if(set.next()){
                   return Optional.of(set.getInt("max_logsize"));
               }
            }
         }catch(SQLException ignored){
             // handled upwards
         }
         return Optional.empty();
    }

    /**
     * @param conn
     * @param exerciseId
     * @param configId
     * @param integer
     * @param setAvailable
     * @throws DatabaseException
     */
    private void createRandomExerciseUtil(Connection conn, int exerciseId, int configId, int logId, Integer maxLogSize, boolean setAvailable) throws Exception{
        logger.debug("Creating random exercise...");
        Log simulatedLog = new Log();
        AlphaAlgorithm alphaAlgorithm;
        Map<String, Object> resultMap;

        // combine generated traces to log
        logger.debug("Combine traces to log.");
        for(String [] strings: createCorrespondingLog(configId, exerciseId, logId)){
            simulatedLog.addTrace(new Trace(strings));
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
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

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
            createRdExStmt.setBoolean(14, setAvailable);

            logger.debug("Statement for creating exercise: {} ", createRdExStmt);
            createRdExStmt.executeUpdate();
            conn.commit();
            updateLogIdCounter(maxLogSize * -1);
            updateExerciseIdCounter(-1);
        }catch (Exception throwables){
            updateLogIdCounter(maxLogSize * -1);
            updateExerciseIdCounter(-1);
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
        }catch(Exception throwables){
            handleThrowables(conn, "Could not create log to exercise " +exerciseId, throwables);
            throw new Exception(throwables);    // note: question: possible to throw 2 times same exception?
        }
    }

    private static synchronized void updateLogIdCounter(int increment){
        logIdCounter = logIdCounter + increment;
        if(logIdCounter < 0) logIdCounter = 0;
    }

    private static synchronized void updateExerciseIdCounter(int increment){
        PmResourceService.exerciseIdCounter = exerciseIdCounter + increment;
        if(exerciseIdCounter < 0) exerciseIdCounter = 0;
    }

    /**
     * Fetches an available exerciseId
     * @return returns exerciseId
     * @throws DatabaseException
     */
    private synchronized int getNextExerciseId() throws DatabaseException{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            int id = getNextExerciseIdUtil(conn) + exerciseIdCounter;
            updateExerciseIdCounter(1);
            return id;
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    private int getNextExerciseIdUtil(Connection conn) throws DatabaseException{
        String fetchMaxIdQuery = "SELECT max(exercise_id) as id FROM randomexercises;";
        int maxId = -1;

        try (PreparedStatement fetchMaxIdStmt = conn.prepareStatement(fetchMaxIdQuery)){
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();

            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                return 0;
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
    private synchronized int getAvailableLogId(Integer maxLogSize) throws DatabaseException{
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);
            int id = getNextLogIdUtil(conn) + logIdCounter;
            updateLogIdCounter(maxLogSize);
            return id;
        }catch(SQLException throwables){
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    private int getNextLogIdUtil(Connection conn) throws DatabaseException{
        String fetchMaxLogIdQuery = "SELECT max(log_id) as id FROM logs;";
        int maxId = -1;

        try (PreparedStatement fetchMaxLogIdStmt = conn.prepareStatement(fetchMaxLogIdQuery)){
            ResultSet maxIdSet = fetchMaxLogIdStmt.executeQuery();

            if(maxIdSet.next()){
                maxId = maxIdSet.getInt("id");
                maxId++;
            }else{
                return 0;
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
