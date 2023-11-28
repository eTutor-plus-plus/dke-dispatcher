package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.junit.Assert;
import org.kie.api.KieServices;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DroolsAnalysis extends DefaultAnalysis {
    private int exerciseID;
    private String inputRules;
    private ApplicationProperties applicationProperties;
    private List<SourceFileModel> javaClasses;
    private List<FactModel> facts;
    private List<EventModel> events;
    private String solution;
    private List<Object[]> testData;
    private static final String route = "http://localhost:8081"; //for etutor: "http://localhost:8081" | for test "http://localhost:9000"


    public DroolsAnalysis(int exerciseID, String inputRules, ApplicationProperties applicationProperties) throws IOException {
        super();
        this.exerciseID = exerciseID;
        this.applicationProperties = applicationProperties;
        this.inputRules = inputRules;
        loadJavaClasses();
        loadFacts();
        loadSolution();
        loadEvents();
    }

    /**
     * Loads the required classes from the task-database.
     *
     */

    public void loadJavaClasses() {
        List<SourceFileModel> classList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(route+"/drools/task/getClasses/" + this.exerciseID);
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
    public void loadTestDataFromDatabase(){
        List<Object[]> testDataList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(route + "/drools/task/getTestData/" + this.exerciseID);
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

        this.testData = testDataList;

    }

    /**
     * Builds the DataProvider for the TestNG framework. Takes the test data from the current object.
     */
    @DataProvider(name = "testData")
    public Object[][] loadTestData() {

        Object[][] testDataArray = new Object[this.testData.size()][];
        for (int i = 0; i < this.testData.size(); i++) {
            testDataArray[i] = this.testData.get(i);
        }

        return testDataArray;
    }

    /**
     * Loads the required facts from the task-database.
     */
    public void loadFacts(){
        List<FactModel> factList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(route + "/drools/task/getFacts/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            String clazz = "";
            String instanceName = "";
            List<Object> parametersList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                clazz = obj.getString("clazz");
                instanceName = obj.getString("instanceName");
                JSONArray parametersArray = obj.getJSONArray("parameters");

                for (int j = 0; j < parametersArray.length(); j++) {
                    JSONObject paramObject = parametersArray.getJSONObject(j);
                    String parameterValue = paramObject.getString((String) paramObject.keys().next());

                    Object value = parseValue(parameterValue);
                    parametersList.add(value);
                }
            }
            FactModel factModel = new FactModel(clazz, instanceName, parametersList);
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
                // Primitive Datentypen nach Bedarf erweitern
            }
        }
        // Standardfall: Wenn keiner der primitiven Datentypen passt, wird einfach der String verwendet
        return value;
    }

    public void loadSolution() {
        String solution = "";

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(route + "/drools/task/getSolution/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject obj = new JSONObject(response.body());
            solution = obj.getString("solution");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.solution = solution;
    }

    public void loadEvents(){
        List<EventModel> eventList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(route + "/drools/task/getEvents/" + this.exerciseID);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            String clazz = "";
            String referenceName = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date timestamp = null;
            String instanceName = "";
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                clazz = obj.getString("clazz");
                referenceName = obj.getString("input");
                timestamp = formatter.parse(obj.getString("timestamp"));
                instanceName = obj.getString("instanceName");
            }
            EventModel eventModel = new EventModel(clazz, referenceName, instanceName, timestamp);
            eventList.add(eventModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.events = eventList;

    }

    public boolean hasSyntaxError() throws IOException, ReflectiveOperationException {
        DynamicDroolsBuilder dyn = new DynamicDroolsBuilder();
        KieContainer kieContainer = dyn.getKieContainer(this.inputRules, EventProcessingOption.CLOUD); //TODO: Richtige KieSession erstellen mit String rules 2023-11-24
        Results results = kieContainer.verify();

        if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
            System.out.println("Syntax errors found:");

            for (org.kie.api.builder.Message message : results.getMessages()) {
                if (message.getLevel() == org.kie.api.builder.Message.Level.ERROR) {
                    System.out.println("Error: " + message.getText());
                }
            }
            return true;
        } else {
            System.out.println("No syntax errors found. Rules are valid.");
            return false;
        }
    }

    @Test(dataProvider = "testData")
    public void hasSemantikError(Object inputClassName, Object expectedOutput) throws IOException, ReflectiveOperationException{
        try(var dyn = new DynamicDroolsBuilder(this.javaClasses)) {
            var ks = dyn.newKieSession(this.inputRules, true);

            SessionClock clock = ks.getSessionClock();
            if (!(clock instanceof SessionPseudoClock spc))
                return;

            Map<String, Object> dynamicFactObjects = new HashMap<>();
            for (var fact : facts) {
                Object obj = dyn.instantiate(fact.getClazz(), fact.getParameters());
                dynamicFactObjects.put(fact.getInstanceName(), obj);
                //Dynamische Objekte sind nicht möglich, daher hier der Umweg mit einer HashMap, die die Objekte speichert
                //TODO: Risikos abklären
            }

            var dynamicEventObjects = new ArrayList<>();
            for (var event : events) {
                Object obj = dyn.instantiate(event.getClazz(), event.getReferenceClass(), event.getTimestamp());
                String referenceName = (String) event.getReferenceClass();
                if (dynamicFactObjects.containsKey(referenceName)) event.setReferenceClass(dynamicFactObjects.get(referenceName));
                dynamicEventObjects.add(obj);
                //Dynamische Objekte sind nicht möglich, daher hier der Umweg mit einer HashMap, die die Objekte speichert
                //TODO: Risikos abklären
            }

            // Fire all rules
            for (var event : dynamicEventObjects) {
                ks.insert(event);
                // Martin: folgendes kann man anders lösen, indem man z.B. Event-Interface generell im eTutor vorgibt.
                Date ts = (Date) event.getClass().getMethod("timestamp").invoke(event);

                var diff = ts.getTime() - clock.getCurrentTime();
                if (diff > 0)
                    spc.advanceTime(diff, TimeUnit.MILLISECONDS);

                ks.fireAllRules();
            }

            // Analyze
            var inputClass = dyn.loadClass((String) inputClassName);
            for (Object invoice : ks.getObjects(obj -> obj.getClass().equals(inputClass))) {
                Assert.assertEquals(invoice.getClass().getMethod("price").invoke(invoice), (Double) expectedOutput);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        //TODO: Klassen laden eventuell mit der Variante von Martin (loadClass) und dann aus der KIESession die Objekte laden
        //TODO Dann den Test ausführen, TestListener implementieren
        //TODO Main methode für einen ersten Test mit hardcoded Testdaten durchführen.


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
