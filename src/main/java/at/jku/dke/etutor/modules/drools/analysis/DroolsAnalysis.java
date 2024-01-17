package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.objects.dispatcher.drools.DroolsObjectDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kie.api.builder.Results;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Represents the result of the analysis and builds/fires the drools rules
 */
public class DroolsAnalysis extends DefaultAnalysis {
    private final int taskId;
    private final String inputRules;
    private List<SourceFileModel> javaClasses;
    private List<FactModel> facts;
    private String solutionRules;
    private List<Map<String, Object>> sampleSolutionOutput;
    private int taskErrorWeighting;
    private String taskValidationClassname;
    private final boolean isForDiagnose;
    private List<Map<String, Object>> studentOutput;
    private List<Map<String, Object>> wrongFactList;
    private Map<String, Long> additionalFactInformation;
    private long additionalFacts;
    private final boolean hasSyntaxError;
    private final StringBuilder syntaxErrorMessage = new StringBuilder();

    public DroolsAnalysis(int taskId, String inputRules, Boolean isForDiagnose) throws IOException, ReflectiveOperationException {
        super();
        this.taskId = taskId;
        this.inputRules = inputRules;
        this.isForDiagnose = isForDiagnose;
        loadJavaClasses();
        loadFacts();
        loadSolution();
        loadTaskProperties();
        this.hasSyntaxError = hasSyntaxError();
    }

    /**
     * Initializes the DroolsAnalysis for comparing the given rules results with the sample solution
     *
     * @throws ReflectiveOperationException
     * @throws IOException
     */
    public void analyze() throws ReflectiveOperationException, IOException {
        if (hasSyntaxError) return;

        loadSampleSolutionOutput();
        this.studentOutput = createOutputFactList();
        this.additionalFactInformation = buildComparisonReport();
        this.wrongFactList = compareOutputs();
        this.setSubmissionSuitsSolution(!hasSyntaxError && wrongFactList.isEmpty() && additionalFactInformation.isEmpty());
    }

    /**
     * Loads the output that was generated by building the solution rules
     *
     */
    private void loadSampleSolutionOutput() {
        String solutionOutput = "";

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getOutput/" + this.taskId
                + "?isForDiagnose=" + this.isForDiagnose);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject obj = jsonArray.getJSONObject(0);
            solutionOutput = obj.getString("solution_output");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sampleSolutionOutput = convertOutputJsonToListUtil(solutionOutput);
    }

    /**
     * Loads the required classes from the task-database.
     */
    private void loadJavaClasses() {
        List<SourceFileModel> classList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getClasses/" + this.taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String fullClassName = obj.getString("full_classname");
                String classContent = obj.getString("class_content");

                SourceFileModel newSourceFile = new SourceFileModel(fullClassName, classContent);

                classList.add(newSourceFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.javaClasses = classList;
    }


    /**
     * Loads the required facts from the objects-table.
     */
    private void loadFacts() {
        List<FactModel> factList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getFacts/" + this.taskId
                + "?isForDiagnose=" + this.isForDiagnose);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonArray = objectMapper.readTree(response.body());

            String fullClassname = "";
            int objectId = -1;
            JsonNode parameterArray = null;

            for (JsonNode node : jsonArray) {
                fullClassname = node.get("full_classname").asText();
                objectId = node.get("object_id").asInt();

                if (node.has("parameter") && node.get("parameter").isArray()) {
                    parameterArray = node.get("parameter");
                }

                FactModel factModel = new FactModel(fullClassname, objectId, parameterArray);
                factList.add(factModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.facts = factList;
    }


    /**
     * Loads the solution rules from the task table
     */
    private void loadSolution() {
        String solution = "";

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getSolution/" + this.taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject obj = jsonArray.getJSONObject(0);
            solution = obj.getString("solution");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.solutionRules = solution;
    }

    /**
     * Loads the solution rules from the task table
     */
    private void loadTaskProperties() {
        String validationClassname = "";
        int errorWeighting = 100;

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getTaskProperties/" + this.taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                validationClassname = obj.getString("validation_classname");
                errorWeighting = obj.getInt("error_weighting");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.taskErrorWeighting = errorWeighting;
        this.taskValidationClassname = validationClassname;
    }


    /**
     * Checks whether the submitted rules have Syntax Errors
     *
     * @return boolean and sets the syntaxErrorMessage
     * @throws IOException
     */
    private boolean hasSyntaxError() throws IOException {
        try (DynamicDroolsBuilder dyn = new DynamicDroolsBuilder(this.javaClasses)) {

            Results results = dyn.checkDroolsSyntax(this.inputRules);

            if (results.hasMessages(org.kie.api.builder.Message.Level.ERROR)) {

                for (org.kie.api.builder.Message message : results.getMessages()) {
                    if (message.getLevel() == org.kie.api.builder.Message.Level.ERROR) {
                        this.syntaxErrorMessage.append(message.getText()).append(". ");
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Private method to build and fire the submitted rules
     *
     * @return Collection of generated output objects
     * @throws IOException
     * @throws ReflectiveOperationException
     */
    private Collection<?> fireRules() throws IOException, ReflectiveOperationException {
        boolean isCep = true; //könnte man in die Angabe beim Erstellen aufnehmen für "normale" Drools Beispiele
        try (var dyn = new DynamicDroolsBuilder(this.javaClasses)) {
            var ks = dyn.newKieSession(this.inputRules, isCep);

            SessionClock clock = ks.getSessionClock();
            if (!(clock instanceof SessionPseudoClock spc))
                return null;

            Map<Integer, Object> dynamicObjectMap = new HashMap<>();
            ArrayList<Object> dynamicEventList = new ArrayList<>();

            for (var fact : this.facts) {
                List<Object> parameterList = new ArrayList<>();
                JsonNode jsonNode = fact.getParameters();
                Class<?> myClass = dyn.loadClass(fact.getFullClassname());

                // Erhalte den einzigen öffentlichen Konstruktor
                Constructor<?> targetConstructor = myClass.getConstructors()[0];

                // Parameter-Typen des Zielkonstruktors
                Class<?>[] parameterTypes = targetConstructor.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    JsonNode paramNode = jsonNode.get(i); // Annahme: Die JSON-Struktur entspricht der Reihenfolge der Parameter im Konstruktor
                    if (paramNode.isObject()) {
                        int objectId = paramNode.get("object_id").asInt();
                        Object object = dynamicObjectMap.get(objectId);
                        parameterList.add(object);
                    } else if (parameterType == Integer.class) {
                        parameterList.add(paramNode.asInt());
                    } else if (parameterType == String.class) {
                        parameterList.add(paramNode.asText());
                    } else if (parameterType == Date.class) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                        Date timestamp = dateFormat.parse(paramNode.asText());
                        parameterList.add(timestamp);
                    }

                    // Weitere Typen können entsprechend verarbeitet werden
                }

                Object event = targetConstructor.newInstance(parameters);
                System.out.println(event);

                Object obj = dyn.instantiate(fact.getFullClassname(), parameterList.toArray());

                if (parameterList.stream().anyMatch(e -> e.getClass() == Date.class)) {
                    dynamicEventList.add(obj);
                } else {
                    dynamicObjectMap.put(fact.getObjectId(), obj);
                }
            }

            var dynamicFactList = new ArrayList<>(dynamicObjectMap.values());

            var dynamicObjectList = new ArrayList<>();

            if (isCep) {
                dynamicObjectList = dynamicEventList;
            } else {
                dynamicObjectList = dynamicFactList;
            }


            // Fire all rules
            for (var object : dynamicObjectList) {
                ks.insert(object);

                // folgendes kann man anders lösen, indem man z.B. Event-Interface generell im eTutor vorgibt.
                if (isCep) {
                    Date ts = (Date) object.getClass().getMethod("timestamp").invoke(object);

                    var diff = ts.getTime() - clock.getCurrentTime();
                    if (diff > 0)
                        spc.advanceTime(diff, TimeUnit.MILLISECONDS);
                }


                ks.fireAllRules();
            }

            Collection<Object> objectCollection = new ArrayList<>();

            if (taskValidationClassname.isEmpty()) {
                return ks.getObjects();
            } else {
                String[] validationClassnames = taskValidationClassname.split("(?<=;)");
                for(String name : validationClassnames){
                    name = name.trim();
                    if (name.endsWith(";")) {
                        name = name.substring(0, name.length() - 1);
                    }
                    var validationClass = dyn.loadClass(name);
                    objectCollection.addAll(ks.getObjects(obj -> obj.getClass().equals(validationClass)));
                }

                return objectCollection;
            }

        } catch (ReflectiveOperationException | IOException e) {
            throw new IOException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Builds a JSON string of the output objects and calls the http-method to create a database entry
     *
     * @return Statuscode
     * @throws ReflectiveOperationException
     * @throws IOException
     */
    public DroolsObjectDTO createSampleSolution() throws ReflectiveOperationException, IOException {

        try {
            List<Map<String, Object>> listOfKeyValuePairs = createOutputFactList();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonOutput = null;

            // Konvertierung der Liste von Key-Value-Paaren in JSON
            jsonOutput = objectMapper.writeValueAsString(listOfKeyValuePairs);

            DroolsObjectDTO objectDTO = new DroolsObjectDTO();
            objectDTO.setDataType("output");
            objectDTO.setFullClassname("");
            objectDTO.setTaskId(taskId);
            objectDTO.setParameter(jsonOutput);
            String submissionType = "submit";
            if (this.isForDiagnose) submissionType = "diagnose";
            objectDTO.setSubmissionType(submissionType);

            return objectDTO;

        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> convertOutputJsonToListUtil(String jsonOutput) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Map<String, Object>> listOfMaps = objectMapper.readValue(jsonOutput, new TypeReference<>() {
            });

            // Rekursive Umwandlung der verschachtelten Maps
            List<Map<String, Object>> convertedList = new ArrayList<>();
            for (Map<String, Object> map : listOfMaps) {
                Map<String, Object> convertedMap = new HashMap<>();
                convertNestedMap(map, convertedMap);
                convertedList.add(convertedMap);
            }

            return convertedList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void convertNestedMap(Map<String, Object> source, Map<String, Object> destination) {
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> nestedDestination = new HashMap<>();
                convertNestedMap((Map<String, Object>) entry.getValue(), nestedDestination);
                destination.put(entry.getKey(), nestedDestination);
            } else {
                destination.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private List<Map<String, Object>> createOutputFactList() throws ReflectiveOperationException, IOException {

        Collection<?> generatedFacts = fireRules();

        List<Map<String, Object>> listOfKeyValuePairs = new ArrayList<>();


        if(generatedFacts != null){
            for (Object fact : generatedFacts) {
                Map<String, Object> keyValuePairs = new HashMap<>();
                createKeyValuePairs(fact, keyValuePairs);
                listOfKeyValuePairs.add(keyValuePairs);
            }
        }
        return listOfKeyValuePairs;
    }

    private static void createKeyValuePairs(Object obj, Map<String, Object> keyValuePairs) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                keyValuePairs.put("class", obj.getClass().getName());
                if (value != null) {
                    if (isPrimitiveOrSpecificType(field.getType())) {
                        if (field.getType().equals(Date.class)) {
                            value = value.toString(); // Anpassen Sie dies an das gewünschte Datumsformat
                        }
                        keyValuePairs.put(field.getName(), value);
                    } else {
                        Map<String, Object> subKeyValuePairs = new HashMap<>();
                        createKeyValuePairs(value, subKeyValuePairs);
                        keyValuePairs.put(field.getName(), subKeyValuePairs);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isPrimitiveOrSpecificType(Class<?> type) {
        return type.isPrimitive() || isWrapperOrSpecificType(type);
    }

    private static boolean isWrapperOrSpecificType(Class<?> type) {
        return type.equals(Boolean.class) ||
                type.equals(Byte.class) ||
                type.equals(Character.class) ||
                type.equals(Short.class) ||
                type.equals(Integer.class) ||
                type.equals(Long.class) ||
                type.equals(Float.class) ||
                type.equals(Double.class) ||
                type.equals(String.class) ||
                type.equals(Date.class);
    }

    private Map<String, Long> buildComparisonReport() {
        List<Map<String, Object>> list1 = this.sampleSolutionOutput;
        List<Map<String, Object>> list2 = this.studentOutput;

        Map<String, Long> classCountMap1 = list1.stream()
                .map(map -> (String) map.get("class"))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        className -> className,
                        Collectors.counting()
                ));

        Map<String, Long> classCountMap2 = list2.stream()
                .map(map -> (String) map.get("class"))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        className -> className,
                        Collectors.counting()
                ));

        Map<String, Long> differenceMap = new HashMap<>(classCountMap2);

        // Vergleiche die Einträge in classCountMap1 mit differenceMap und finde die Unterschiede
        classCountMap1.forEach((className, count) -> differenceMap.compute(className, (key, value) -> {
            if (value == null) {
                return -count; // Die Klasse existiert nur in Map 2
            } else {
                return value - count; // Berechne den Unterschied in den Vorkommnissen
            }
        }));

        // Entferne Einträge, die in beiden Maps vorkommen (d.h. Vorkommen = 0)
        differenceMap.entrySet().removeIf(entry -> entry.getValue() == 0);

        this.additionalFacts = differenceMap.values().stream()
                .mapToLong(Long::longValue)
                .sum();

        return differenceMap;
    }

    private List<Map<String, Object>> compareOutputs() {
        List<Map<String, Object>> list1 = this.sampleSolutionOutput;
        List<Map<String, Object>> list2 = this.studentOutput;
        List<Map<String, Object>> wrongFactList = new ArrayList<>();

        // Sortiere die Listen, um sicherzustellen, dass die Elemente in der gleichen Reihenfolge sind
        list1.sort(Comparator.comparing(Object::toString));
        list2.sort(Comparator.comparing(Object::toString));


        if (list1.size() == list2.size()) {
            for (int i = 0; i < list1.size(); i++) {
                Map<String, Object> map1 = list1.get(i);
                Map<String, Object> map2 = list2.get(i);

                // Vergleiche die Maps
                if (!compareNestedMaps(map1, map2)) {
                    wrongFactList.add(map2);
                }
            }
        }

        return wrongFactList;
    }

    private static boolean compareNestedMaps(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1 == null || map2 == null) return false;

        if (map1.size() != map2.size()) {
            return false;
        }

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            String key = entry.getKey();
            Object value1 = entry.getValue();
            Object value2 = map2.get(key);

            if (value1 instanceof Map && value2 instanceof Map) {
                if (!compareNestedMaps((Map<String, Object>) value1, (Map<String, Object>) value2)) {
                    return false;
                }
            } else {
                if (value1 == null) {
                    if (value2 != null) {
                        return false;
                    }
                } else if (!value1.equals(value2)) {
                    return false;
                }
            }
        }
        return true;
    }


    public int getTaskId() {
        return taskId;
    }

    public String getInputRules() {
        return inputRules;
    }

    public List<SourceFileModel> getJavaClasses() {
        return javaClasses;
    }

    public List<FactModel> getFacts() {
        return facts;
    }

    public void setFacts(List<FactModel> facts) {
        this.facts = facts;
    }

    public int getTaskErrorWeighting() {
        return taskErrorWeighting;
    }

    public List<Map<String, Object>> getSampleSolutionOutput() {
        return sampleSolutionOutput;
    }

    public List<Map<String, Object>> getStudentOutput() {
        return studentOutput;
    }

    public long getAdditionalFacts() {
        return additionalFacts;
    }

    public List<Map<String, Object>> getWrongFactList() {
        return wrongFactList;
    }

    public Map<String, Long> getAdditionalFactInformation() {
        return additionalFactInformation;
    }


    public boolean isHasSyntaxError() {
        return hasSyntaxError;
    }

    public String getSyntaxErrorMessage() {
        return syntaxErrorMessage.toString();
    }



    @Override
    public String toString() {
        return "DroolsAnalysis{" +
                "exerciseID=" + taskId +
                ", inputRules='" + inputRules + '\'' +
                ", javaClasses=" + javaClasses +
                ", facts=" + facts +
                ", solution='" + solutionRules + '\'' +
                '}';
    }
}
