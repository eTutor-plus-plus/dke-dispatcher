package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.model.repositories.SubmissionRepository;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;
import at.jku.dke.etutor.objects.dispatcher.drools.DroolsObjectDTO;
import at.jku.dke.etutor.objects.dispatcher.drools.DroolsTaskDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class DroolsResourceService {
    private final String TASK_TYPE = "drools";
    private final String URL;
    private final String USER;
    private final String PWD;
    private final Logger logger;

    private final ApplicationProperties applicationProperties;
    private final SubmissionDispatcherService dispatcherService;
    private final GradingDTORepository gradingDTORepository;
    private final ModelMapper modelMapper;
    private final SubmissionRepository submissionRepository;


    public DroolsResourceService(SubmissionDispatcherService dispatcherService,
                                 GradingDTORepository gradingDTORepository,
                                 ApplicationProperties applicationProperties,
                                 ModelMapper modelMapper,
                                 SubmissionRepository submissionRepository) {
        this.logger = LoggerFactory.getLogger(DroolsResourceService.class);
        this.applicationProperties = applicationProperties;
        this.dispatcherService = dispatcherService;
        this.gradingDTORepository = gradingDTORepository;
        URL = applicationProperties.getDatasource().getUrl() + applicationProperties.getDrools().getConnUrl();
        USER = applicationProperties.getDrools().getConnUser();
        PWD = applicationProperties.getDrools().getConnPwd();
        this.modelMapper = modelMapper;
        this.submissionRepository = submissionRepository;
    }

    /**
     * Fetches the required classes for the selected task
     * @param taskId
     * @return
     * @throws SQLException
     */
    public String getClasses(int taskId) throws SQLException {
        logger.debug("getClasses(int taskId)");
        String query = "SELECT full_classname, class_content FROM classes WHERE task_id = ?";
        String[] columnNames = {"full_classname", "class_content"};
        return executeQueryAndReturnJSON(query, columnNames, taskId);
    }

    /**
     * Fetches all input facts and events for the selected task (diagnose/submit)
     * @param id
     * @return JSON with the required columns
     * @throws SQLException
     */
    public String getFacts(int id, boolean isForDiagnose) throws SQLException {
        logger.debug("getFacts(int id, boolean isForDiagnose)");
        String submissionType = "submit";
        if(isForDiagnose) submissionType = "diagnose";
        String query = "SELECT object_id, full_classname, parameter " +
                "FROM objects " +
                "WHERE task_id = ? AND data_type = 'input' AND submission_type = '"+submissionType+"'" +
                "ORDER BY object_id";
        String[] columnNames = {"object_id", "full_classname", "parameter"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    /**
     * Fetches the output objects (diagnose/submit), generated with the sample solution rules
     * @param id
     * @param isForDiagnose
     * @return
     * @throws SQLException
     */
    public String getOutput(int id, boolean isForDiagnose) throws SQLException {
        logger.debug("getObjects(int taskId, boolean isForDiagnose)");
        String submissionType = "submit";
        if(isForDiagnose) submissionType = "diagnose";
        String query = "SELECT parameter AS solution_output " +
                "FROM objects " +
                "WHERE task_id = ? AND data_type = 'output' AND submission_type = '"+submissionType+"'";
        String[] columnNames = {"solution_output"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    /**
     * Fetches the uploaded solution (rules) from the selected task
     * @param taskId
     * @return JSON with the solution rules
     * @throws SQLException
     */
    public String getSolution(int taskId) throws SQLException {
        logger.debug("getSolution(int taskId)");
        String query = "SELECT solution FROM tasks WHERE task_id = ?";
        String[] columnNames = {"solution"};
        return executeQueryAndReturnJSON(query, columnNames, taskId);
    }

    /**
     * Fetches a task
     * @param id
     * @return
     * @throws SQLException
     */
    public String getTask(int id) throws SQLException {
        logger.debug("getTask(int id)");
        String query = "SELECT task_id, solution, max_points, error_weighting, validation_classname FROM tasks WHERE task_id = ?";
        String[] columnNames = {"task_id", "solution", "max_points", "error_weighting", "validation_classname"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    /**
     * Fetches the additional properties of a drools task
     * @param id
     * @return
     * @throws SQLException
     */
    public String getTaskProperties(int id) throws SQLException {
        logger.debug("getTaskProperties(int taskId)");
        String query = "SELECT error_weighting, validation_classname FROM tasks WHERE task_id = ?";
        String[] columnNames = {"error_weighting", "validation_classname"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    private String executeQueryAndReturnJSON(String query, String[] columnNames, int id) throws SQLException {
        logger.debug("executeQueryAndReturnJSON() -" + " id: " + id + " - query: " + query);
        JSONArray jsonArray = new JSONArray();
        try (Connection con = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(!rs.isBeforeFirst()) return "";
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                for (String columnName : columnNames) {
                    if (columnName.equals("parameter")) {
                        String paramValue = rs.getString(columnName);
                        try {
                            JSONArray paramsArray = new JSONArray(paramValue);
                            obj.put(columnName, paramsArray);
                        } catch (JSONException e) {
                            // In case it's not an array, treat it as a regular value
                            obj.put(columnName, paramValue);
                        }
                    } else {
                        obj.put(columnName, rs.getString(columnName));
                    }
                }
                jsonArray.put(obj);
            }
            return jsonArray.toString();
        } catch (SQLException | JSONException throwable) {
            throw new SQLException(throwable);
        }
    }

    /**
     * Fetches an available exercise id
     *
     * @return the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    public int getAvailableExerciseId() throws DatabaseException {
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            return getAvailableExerciseIdUtil(con);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new DatabaseException(throwable);
        }
    }

    private int getAvailableExerciseIdUtil(Connection con) throws DatabaseException {
        String fetchMaxIdQuery = "SELECT max(task_id) as task_id from tasks";
        int maxId = -1;

        try (PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
             ResultSet maxIdSet = fetchMaxIdStmt.executeQuery()
        ) {
            if (maxIdSet.next()) {
                maxId = maxIdSet.getInt("task_id");
                maxId++;
            } else throw new DatabaseException("Internal Error: could not fetch exercise id");

            con.commit();
        } catch (SQLException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new DatabaseException(throwable);
        }
        return maxId;
    }

    /**
     * Method to create a new task in the database
     * @param taskDTO
     * @return taskId
     * @throws DatabaseException
     * @throws SQLException
     */
    public int addTask(DroolsTaskDTO taskDTO) throws DatabaseException, SQLException {
        logger.debug("Enter: Creating task");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {

            con.setAutoCommit(false);
            int taskId = getAvailableExerciseId();
            int maxPoints = taskDTO.getMaxPoints();
            String solution = taskDTO.getSolution();
            int errorWeighting = taskDTO.getErrorWeighting();
            String validationClassname = taskDTO.getValidationClassname();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO tasks " +
                    "(task_id, solution, max_points, error_weighting, validation_classname) " +
                    "VALUES(?,?,?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, solution);
            createTaskStmt.setInt(3, maxPoints);
            createTaskStmt.setInt(4,errorWeighting);
            createTaskStmt.setString(5, validationClassname);

            logger.debug("Statement for creating task: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.debug("Task created");
                if(createClasses(taskDTO, taskId, con) > 0) {
                    if(createObjects(taskDTO, taskId, con) > 0){
                        con.commit();
                        DroolsAnalysis analysisSubmit = new DroolsAnalysis(taskId, solution, false);
                        DroolsAnalysis analysisDiagnose = new DroolsAnalysis(taskId, solution, true);
                        if(!analysisSubmit.isHasSyntaxError()){
                            if(createOutput(analysisSubmit.createSampleSolution()) == 1
                                    && createOutput(analysisDiagnose.createSampleSolution()) == 1){
                                return taskId;
                            }
                        }

                    }
                }
            }
            con.rollback();
            return -1;
        } catch (SQLException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new DatabaseException(throwable);
        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create the classes in the database for the selected task
     * @param taskDTO
     * @param taskId
     * @param con
     * @return
     * @throws DatabaseException
     * @throws SQLException
     */
    public int createClasses(DroolsTaskDTO taskDTO, int taskId, Connection con) throws DatabaseException, SQLException {
        logger.debug("Enter: Creating classes");
        try {
            String classesJson = taskDTO.getClasses();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode[] classesArray = objectMapper.readValue(classesJson, JsonNode[].class);

            PreparedStatement statement = con.prepareStatement("INSERT INTO classes " +
                    "(full_classname, class_content, task_id) " +
                    "VALUES(?,?,?)");

            for (JsonNode node : classesArray) {
                String fullClassName = node.get("full_classname").asText();
                String classContent = node.get("class_content").asText();

                statement.setString(1, fullClassName);
                statement.setString(2, classContent);
                statement.setInt(3, taskId);
                statement.addBatch();
                logger.debug("Added statement batch for creating classes: {} ", statement);
            }
            int[] rowsInserted = statement.executeBatch();

            return rowsInserted.length;


        } catch (Exception throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new DatabaseException(throwable);
        }
    }

    /**
     * Create new objects in the database for the selected task
     * @param taskDTO
     * @param taskId
     * @param con
     * @return
     * @throws DatabaseException
     */
    public int createObjects(DroolsTaskDTO taskDTO, int taskId, Connection con) throws DatabaseException {
        logger.debug("Enter: Creating objects");
        String objectsCsv = taskDTO.getObjects();

        try (CSVParser parser = CSVParser.parse(objectsCsv, CSVFormat.DEFAULT.builder()
                .setHeader("object_id", "parameter", "submission_type", "full_classname", "data_type", "task_id")
                .setIgnoreEmptyLines(true)
                .setSkipHeaderRecord(true)
                .setDelimiter(';')
                .setQuote('"')
                .build())){

            PreparedStatement statement = con.prepareStatement("INSERT INTO objects " +
                    "(object_id, parameter, submission_type, full_classname, data_type, task_id) " +
                    "VALUES (?, ?::JSON, ?, ?, ?, ?)");

            for(CSVRecord record : parser) {

                int objectId = Integer.parseInt(record.get(0));
                String jsonParameter = record.get(1);
                JSONArray parameterArray = new JSONArray(jsonParameter);

                String submissionType = record.get(2);
                String fullClassname = record.get(3);
                //jeder Eintrag, der über die Eingabe erzeugt wird, ist immer ein "input".
                // Outputs werden durch Anwenden der Regeln erzeugt
                String dataType = "input";

                statement.setInt(1, objectId);
                statement.setString(2, parameterArray.toString());
                statement.setString(3, submissionType);
                statement.setString(4, fullClassname);
                statement.setString(5, dataType);
                statement.setInt(6, taskId);

                statement.addBatch();
                logger.debug("Added statement batch for creating objects: {} ", statement);
            }

            int[] rowsInserted = statement.executeBatch();

            return rowsInserted.length;


        } catch (IndexOutOfBoundsException | IOException | SQLException | JSONException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Creates sample solution output objects generated with the sample solution
     * @param droolsObjectDTO
     * @return
     * @throws DatabaseException
     */
    private int createOutput(DroolsObjectDTO droolsObjectDTO) throws DatabaseException{
        logger.debug("Enter: Creating objects");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)){
            con.setAutoCommit(false);

            PreparedStatement statement = con.prepareStatement("INSERT INTO objects " +
                    "(object_id, parameter, submission_type, full_classname, data_type, task_id) " +
                    "VALUES (?, ?::JSON, ?, ?, ?, ?)");

            int taskId = droolsObjectDTO.getTaskId();
            int objectId = getAvailableObjectId(taskId);
            String submissionType = droolsObjectDTO.getSubmissionType();
            String fullClassname = droolsObjectDTO.getFullClassname();
            String dataType = droolsObjectDTO.getDataType();
            JSONArray parameterArray = new JSONArray(droolsObjectDTO.getParameter());

            statement.setInt(1, objectId);
            statement.setString(2, parameterArray.toString());
            statement.setString(3, submissionType);
            statement.setString(4, fullClassname);
            statement.setString(5, dataType);
            statement.setInt(6, taskId);

            logger.debug("Statement for creating output: {} ", statement);
            int rowsInserted = statement.executeUpdate();
            if(rowsInserted == 1){
                con.commit();
                return rowsInserted;
            }
            return -1;

        } catch (SQLException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Fetches an available object id
     *
     * @return the object id
     * @throws DatabaseException if an SQLException occurs
     */
    public int getAvailableObjectId(int taskId) throws DatabaseException {
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            return getAvailableObjectIdUtil(con, taskId);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            throw new DatabaseException(throwable);
        }
    }

    private int getAvailableObjectIdUtil(Connection con, int taskId) throws DatabaseException {
        String fetchMaxIdQuery = "SELECT max(object_id) as object_id FROM objects WHERE task_id = ?";
        int maxId = -1;

        try {
            PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
            fetchMaxIdStmt.setInt(1, taskId);
            ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();

            if (maxIdSet.next()) {
                maxId = maxIdSet.getInt("object_id");
                maxId++;
            } else throw new DatabaseException("Internal Error: could not fetch object id");

            con.commit();
        } catch (SQLException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new DatabaseException(throwable);
        }
        return maxId;
    }


    /**
     * Delete a task from the database
     * @param id
     * @throws SQLException
     */
    public void deleteTask(int id) throws SQLException {
        logger.debug("Enter: Deleting task");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);

            PreparedStatement statementTasks = con.prepareStatement("DELETE FROM tasks WHERE task_id = ?");
            statementTasks.setInt(1, id);
            logger.debug("Statement for deleting task: {}",statementTasks);
            statementTasks.executeUpdate();
            logger.debug("Task deleted");
            con.commit();

        } catch (SQLException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new SQLException(throwable);
        }
    }

    /**
     * Edit a task in the database. First deleting it and then recreating it.
     * @param id
     * @param taskDTO
     * @throws SQLException
     */
    public void editTask(int id, DroolsTaskDTO taskDTO) throws SQLException {
        logger.debug("Enter: editTask()");
        try {
            deleteTask(id);
            addTask(taskDTO);
        }catch (SQLException | DatabaseException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new SQLException(throwable);
        }
    }



}