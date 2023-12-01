package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.*;


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


    public DroolsResourceService(SubmissionDispatcherService dispatcherService,
                                 GradingDTORepository gradingDTORepository,
                                 ApplicationProperties applicationProperties) {
        this.logger = (Logger) LoggerFactory.getLogger(DroolsResourceService.class);
        this.applicationProperties = applicationProperties;
        this.dispatcherService = dispatcherService;
        this.gradingDTORepository = gradingDTORepository;
        URL = applicationProperties.getDatasource().getUrl() + applicationProperties.getDrools().getConnUrl();
        USER = applicationProperties.getDrools().getConnUser();
        PWD = applicationProperties.getDrools().getConnPwd();
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

    private String executeQueryAndReturnJSON(String query, String[] columnNames, int id) throws SQLException {
        logger.debug("executeQueryAndReturnJSON() -" + " id: " + id + " - query: "+ query);
        JSONArray jsonArray = new JSONArray();
        try (Connection con = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                for (String columnName : columnNames) {
                    if(columnName.equals("parameters")){
                        String paramValue = rs.getString(columnName);
                        try {
                            JSONArray paramsArray = new JSONArray(paramValue);
                            obj.put(columnName, paramsArray);
                        } catch (JSONException e) {
                            // In case it's not an array, treat it as a regular value
                            obj.put(columnName, paramValue);
                        }
                    } else{
                        obj.put(columnName, rs.getString(columnName));
                    }
                }
                jsonArray.put(obj);
            }
        } catch (SQLException | JSONException throwables) {
            throw new SQLException(throwables);
        }
        return jsonArray.toString();
    }


}