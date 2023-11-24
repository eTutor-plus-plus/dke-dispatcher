package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.rt.RTObject;
import at.jku.dke.etutor.modules.rt.analysis.RTSemanticsAnalysis;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DroolsAnalysis extends DefaultAnalysis {
    private int exerciseID;
    private String inputRules;
    private ApplicationProperties applicationProperties;
    private List<SourceFileModel> javaClasses;
    private Object[][] testData;
    private List<FactModel> facts;


    public DroolsAnalysis(int exerciseID, String inputRules, ApplicationProperties applicationProperties) throws IOException {
        super();
        this.exerciseID = exerciseID;
        this.applicationProperties = applicationProperties;
        this.inputRules = inputRules;
        loadJavaClasses();
        loadTestData();
        loadFacts();
    }

    /**
     * Loads the defined classes from the task-database.
     *
     */

    public void loadJavaClasses() {
        List<SourceFileModel> classList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getTaskProperties/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String fullClassName = obj.getString("fullClassName");
                String classContent = obj.getString("classContent");

                SourceFileModel newSourceFile = new SourceFileModel(fullClassName, classContent);

                classList.add(newSourceFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.javaClasses = classList;
    }

    /**
     * Loads the required test-data from the task-database.
     */
    //TODO: HttpAbrufe in einer Methode zusammenführen? Unklar ob das Zielführend wäre 20231124
    @DataProvider(name = "testData")
    public void loadTestData() {
        List<Object[]> testDataList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getTestData/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Object testInput = obj.get("testInput");
                Object expectedOutput = obj.get("expectedOutput");

                testDataList.add(new Object[]{testInput, expectedOutput});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object[][] testDataArray = new Object[testDataList.size()][];
        for (int i = 0; i < testDataList.size(); i++) {
            testDataArray[i] = testDataList.get(i);
        }

        this.testData = testDataArray;
    }

    /**
     * Loads the required facts from the task-databse.
     */
    public void loadFacts(){
        List<FactModel> factList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getTastFacts/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            String clazz = "";
            List<Object> parametersList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                clazz = obj.getString("clazz");
                JSONArray parametersArray = obj.getJSONArray("parameters");

                for (int j = 0; j < parametersArray.length(); j++) {
                    JSONObject paramObject = parametersArray.getJSONObject(j);
                    String parameterValue = paramObject.getString((String) paramObject.keys().next());

                    Object value = parseValue(parameterValue);
                    parametersList.add(value);
                }
            }
            FactModel factModel = new FactModel(clazz, parametersList);
            factList.add(factModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.facts = factList;
    }

    /**
     * Parses the input as primitive datatype.
     *
     * @return Primitive datatype of the String input.
    * */
    private Object parseValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                    return Boolean.parseBoolean(value);
                }
                // Datentypen nach Bedarf erweitern (Primitive sollten es aber bleiben)
            }
        }
        // Standardfall: Wenn keiner der primitiven Datentypen passt, wird String  verwendet
        return value;
    }




    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getInputRules() {
        return inputRules;
    }

    public void setInputRules(String inputRules) {
        this.inputRules = inputRules;
    }

    public ApplicationProperties getApplicationProperties() {
        return applicationProperties;
    }

    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
}
