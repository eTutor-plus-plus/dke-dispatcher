package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;


import at.jku.dke.etutor.objects.dispatcher.drools.*;
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
        String fetchMaxIdQuery = "SELECT max(id) as id from tasks";
        int maxId = -1;

        try (PreparedStatement fetchMaxIdStmt = con.prepareStatement(fetchMaxIdQuery);
             ResultSet maxIdSet = fetchMaxIdStmt.executeQuery();
        ) {
            if (maxIdSet.next()) {
                maxId = maxIdSet.getInt("id");
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

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO tasks (id, solution, max_points) VALUES(?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, solution);
            createTaskStmt.setInt(3, maxPoints);
            logger.debug("Statement for creating task: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Task created");
                con.commit();
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

    public void editTask(int id, DroolsTaskDTO taskDTO) throws SQLException {
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



    public String addEvent(DroolsEventDTO eventDTO) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating event");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            int taskId = eventDTO.getTaskID();
            String clazz = eventDTO.getEventClazz();
            String referenceName = eventDTO.getEventReferenceName();
            String timestamp = eventDTO.getEventTimestamp();
            String instanceName = eventDTO.getEventInstanceName();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO events (task_id, clazz, " +
                    "reference_name, timestamp, instance_name) VALUES(?,?,?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, clazz);
            createTaskStmt.setString(3, referenceName);
            createTaskStmt.setString(4, timestamp);
            createTaskStmt.setString(5, instanceName);

            logger.debug("Statement for creating event: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Event created");
                con.commit();
                return clazz;
            }
            con.rollback(); //TODO: benötigt?? 20231203 LK
            return "";
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    public String addClass(DroolsClassDTO classDTO) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating class");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            int taskId = classDTO.getTaskID();
            String fullClassname = classDTO.getClassFullClassname();
            String classContent = classDTO.getClassContent();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO classes (task_id, full_classname, " +
                    "class_content) VALUES(?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, fullClassname);
            createTaskStmt.setString(3, classContent);

            logger.debug("Statement for creating class: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Event created");
                con.commit();
                return fullClassname;
            }
            con.rollback(); //TODO: benötigt?? 20231203 LK
            return "";
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    public String addFact(DroolsFactDTO factDTO) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating fact");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            int taskId = factDTO.getTaskID();
            String factClazz = factDTO.getFactClazz();
            String factInstanceName = factDTO.getFactInstanceName();
            String[] factParameters = factDTO.getFactParameters();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO facts (task_id, clazz, " +
                    "instance_name, parameters) VALUES(?,?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, factClazz);
            createTaskStmt.setString(3, factInstanceName);
            Array array = con.createArrayOf("text", factParameters);
            createTaskStmt.setArray(4, array);

            logger.debug("Statement for creating fact: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Fact created");
                con.commit();
                return factClazz;
            }
            con.rollback(); //TODO: benötigt?? 20231203 LK
            return "";
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

    public int addTestdata(DroolsTestDTO testDTO) throws DatabaseException, SQLException { //TODO: Exception??
        logger.debug("Enter: Creating Testdata");
        try (Connection con = DriverManager.getConnection(URL, USER, PWD)) {
            con.setAutoCommit(false);
            int taskId = testDTO.getTaskID();
            String inputClassname = testDTO.getTestInputClassname();
            String expectedOutput = testDTO.getTestExpectedOutput();

            PreparedStatement createTaskStmt = con.prepareStatement("INSERT INTO testdata (task_id, input_classname, expected_output) VALUES(?,?,?)");
            createTaskStmt.setInt(1, taskId);
            createTaskStmt.setString(2, inputClassname);
            createTaskStmt.setString(3, expectedOutput);

            logger.debug("Statement for creating testdata: {} ", createTaskStmt);
            int rowsInserted = createTaskStmt.executeUpdate();
            if(rowsInserted > 0){
                logger.debug("Testdata created");
                con.commit();
                return taskId;
            }
            con.rollback(); //TODO: benötigt?? 20231203 LK
            return -1;
        } catch (SQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
            throw new DatabaseException(throwables);
        }
    }

}