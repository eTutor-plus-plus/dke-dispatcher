package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;


import at.jku.dke.etutor.objects.dispatcher.drools.DroolsTaskDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.swagger2.mappers.ModelMapper;

import java.sql.Connection;
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

    private ApplicationProperties applicationProperties;
    private SubmissionDispatcherService dispatcherService;
    private GradingDTORepository gradingDTORepository;
    private final ModelMapper modelMapper;


    public DroolsResourceService(SubmissionDispatcherService dispatcherService,
                                 GradingDTORepository gradingDTORepository,
                                 ApplicationProperties applicationProperties,
                                 ModelMapper modelMapper) {
        this.logger = (Logger) LoggerFactory.getLogger(DroolsResourceService.class);
        this.applicationProperties = applicationProperties;
        this.dispatcherService = dispatcherService;
        this.gradingDTORepository = gradingDTORepository;
        URL = applicationProperties.getDatasource().getUrl() + applicationProperties.getDrools().getConnUrl();
        USER = applicationProperties.getDrools().getConnUser();
        PWD = applicationProperties.getDrools().getConnPwd();
        this.modelMapper = modelMapper;
    }

    public String getClasses(int id) throws SQLException {
        logger.debug("getClasses(int id)");
        String query = "SELECT full_classname, class_content FROM classes WHERE task_id = ?";
        String[] columnNames = {"full_classname", "class_content"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getTestData(int id) throws SQLException {
        logger.debug("getTestData(int id)");
        String query = "SELECT input_classname, expected_output FROM testdata WHERE task_id = ?";
        String[] columnNames = {"input_classname", "expected_output"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getFacts(int id) throws SQLException {
        logger.debug("getFacts(int id)");
        String query = "SELECT clazz, instance_name, parameters FROM facts WHERE task_id = ?";
        String[] columnNames = {"clazz", "instance_name", "parameters"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getSolution(int id) throws SQLException {
        logger.debug("getSolution(int id)");
        String query = "SELECT solution FROM tasks WHERE id = ?";
        String[] columnNames = {"solution"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getEvents(int id) throws SQLException {
        logger.debug("getEvents(int id)");
        String query = "SELECT clazz, reference_name, timestamp, instance_name FROM events WHERE task_id = ?";
        String[] columnNames = {"clazz", "reference_name", "timestamp", "instance_name"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getTask(int id) throws SQLException {
        logger.debug("getTask(int id)");
        String query = "SELECT id, solution, max_points FROM tasks WHERE id = ?";
        String[] columnNames = {"id", "solution", "max_points"};
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
                    if (columnName.equals("parameters")) {
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
        } catch (SQLException | JSONException throwables) {
            throw new SQLException(throwables);
        }
    }

    /**
     * Fetches an available exercise id
     *
     * @return the exercise id
     * @throws DatabaseException if an SQLException occurs
     */
    public int getAvailableExerciseId() throws DatabaseException {
        try (Connection con = DriverManager.getConnection(URL, USER, PWD);) {
            con.setAutoCommit(false);
            return getAvailableExerciseIdUtil(con);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DatabaseException(throwables);
        }
    }

    private int getAvailableExerciseIdUtil(Connection con) throws DatabaseException {
        String fetchMaxIdQuery = "SELECT max(task_id) as task_id from tasks";
        int maxId = -1;

        try (PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
             ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();
        ) {
            if (maxIdSet.next()) {
                maxId = maxIdSet.getInt("task_id");
                maxId++;
            } else throw new DatabaseException("Internal Error: could not fetch exercise id");

            con.commit();
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
        return maxId;
    }

    public int addTask(DroolsTaskDTO taskDTO) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating task");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            int taskId = getAvailableExerciseId();
            int maxPoints = taskDTO.getMaxPoints();
            String solution = taskDTO.getSolution();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO tasks (task_id, solution, max_points) VALUES(?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, solution);
            createTaskStmt.setInt(3, maxPoints);
            logger.debug("Statement for creating task: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Task created");
                createClasses(taskDTO, taskId);
                return taskId;
            }
            con.rollback();
            return -1;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    public void deleteTask(int id) throws SQLException {
        logger.debug("Enter: Deleting task");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            //TODO: Prüfen ob Task existiert?? 20231202

            PreparedStatement statement = con.prepareStatement("DELETE FROM tasks WHERE id = ?");
            statement.setInt(1, id);

            logger.debug("Statement for deleting task: {} ", statement);
            int rowsDeleted = statement.executeUpdate();
            if(rowsDeleted > 0){
                logger.debug("Task deleted");
                con.commit();
                return;
            }
            con.rollback();
        } catch (SQLException throwable) {
            logger.error(throwable.getMessage(), throwable);
            throw new SQLException(throwable);
        }
    }

    public void editTask(int id, DroolsTaskDTO taskDTO) throws SQLException, DatabaseException {
        logger.debug("Enter: editTask()");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)){
            con.setAutoCommit(false);
            String newSolution = taskDTO.getSolution();
            int newMaxPoints = taskDTO.getMaxPoints();
            //TODO: Fehleingabe crashed das Programm (z.B "lösung" statt 'lösung')
            StringBuilder queryBuilder = new StringBuilder("UPDATE tasks SET ");
            List<Object> parameters = new ArrayList<>();

            if(newSolution != null && !newSolution.isEmpty()) {
                queryBuilder.append("solution = ?, ");
                parameters.add(taskDTO.getSolution());
            }

            if(newMaxPoints > 0 ) { //TODO: braucht es jemals 0 Punkte bei einer Aufgabe? derzeit über update nicht möglich. 20231202 Lukas Knogler
                queryBuilder.append("max_points = ?, ");
                parameters.add(taskDTO.getMaxPoints());
            }

            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());

            queryBuilder.append(" WHERE id = ?");

            PreparedStatement statement = con.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            statement.setInt(parameters.size() + 1, id);
            statement.executeUpdate();

            con.commit();
        }

    }

    public int createClasses(DroolsTaskDTO taskDTO, int taskId) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating classes");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            String classesArray = taskDTO.getClasses();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(classesArray);

            PreparedStatement statement = con.prepareStatement("INSERT INTO classes (class_id, full_classname, class_content, task_id) VALUES(?,?,?,?)");

            for (JsonNode node : jsonNode) {
                int classId = node.get("class_id").asInt();
                String fullClassName = node.get("full_classname").asText();
                String classContent = node.get("class_content").asText();

                statement.setInt(1, classId);
                statement.setString(2, fullClassName);
                statement.setString(3, classContent);
                statement.setInt(4, taskId);
                statement.addBatch();
                logger.debug("Added statement batch for creating classes: {} ", statement);
            }
            int[] rowsInserted = statement.executeBatch();
            if(rowsInserted.length > 0){
                logger.debug("{} classes created", rowsInserted.length);
                con.commit();
                return rowsInserted.length;
            }
            con.rollback();
            return -1;

        } catch (Exception throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }
}