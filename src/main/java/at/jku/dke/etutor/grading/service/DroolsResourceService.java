package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.modules.pm.plg.utils.Logger;
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

    public String getClasses(int id) {
        logger.debug("getClasses(int id)");
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT fullClassName, classContent FROM classes WHERE task_id = ?";
        String[] columnNames = {"fullClassName", "classContent"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getTestData(int id) {
        logger.debug("getTestData(int id)");
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT testInput, expectedOutput FROM testdata WHERE task_id = ?";
        String[] columnNames = {"testInput", "expectedOutput"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getFacts(int id) {
        logger.debug("getFacts(int id)");
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT clazz, instanceName, parameters FROM facts WHERE task_id = ?";
        String[] columnNames = {"clazz", "instanceName", "parameters"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getSolution(int id) {
        logger.debug("getSolution(int id)");
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT solution FROM solutions WHERE task_id = ?";
        String[] columnNames = {"solution"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    public String getEvents(int id) {
        logger.debug("getEvents(int id)");
        JSONArray jsonArray = new JSONArray();
        String query = "SELECT clazz, input, timestamp, instanceName FROM events WHERE task_id = ?";
        String[] columnNames = {"clazz", "input", "timestamp", "instanceName"};
        return executeQueryAndReturnJSON(query, columnNames, id);
    }

    private String executeQueryAndReturnJSON(String query, String[] columnNames, int id) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.toString();
    }


}