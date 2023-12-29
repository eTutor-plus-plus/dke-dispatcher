package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.objects.dispatcher.drools.DroolsObjectDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
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

public class DroolsAnalysis extends DefaultAnalysis {
    private int exerciseID;
    private String inputRules;
    private List<SourceFileModel> javaClasses;
    private List<FactModel> factsForDiagnose;
    private List<FactModel> factsForSubmit;
    private String solutionRules;

    public DroolsAnalysis(int exerciseID, String inputRules) throws IOException {
        super();
        this.exerciseID = exerciseID;
        this.inputRules = inputRules;
        loadJavaClasses();
        factsForDiagnose = loadFacts(true);
        factsForSubmit = loadFacts(false);
        loadSolution();
    }

    /**
     * Loads the required classes from the task-database.
     *
     */
    public void loadJavaClasses() {
        List<SourceFileModel> classList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getClasses/" + this.exerciseID);
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
    public List<FactModel> loadFacts(Boolean isForDiagnose){
        List<FactModel> factList = new ArrayList<>();

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getFacts/" + this.exerciseID
                + "?isForDiagnose=" + isForDiagnose);
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
        return factList;
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

    /**
     * Loads the solution rules from the task table
     */
    public void loadSolution() {
        String solution = "";

        final HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8081/drools/task/getSolution/" + this.exerciseID);
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



    public boolean hasSyntaxError() throws IOException {
        try (DynamicDroolsBuilder dyn = new DynamicDroolsBuilder(this.javaClasses)) {

            Results results = dyn.checkDroolsSyntax(this.inputRules);

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
    }

    public Collection<?> fireRules(Boolean isForDiagnose) throws IOException, ReflectiveOperationException{
        boolean isCep = true; //könnte man in die Angabe beim Erstellen aufnehmen für "normale" Drools Beispiele
        try(var dyn = new DynamicDroolsBuilder(this.javaClasses)) {
            var ks = dyn.newKieSession(this.inputRules, isCep);

            SessionClock clock = ks.getSessionClock();
            if (!(clock instanceof SessionPseudoClock spc))
                return null;

            Map<Integer, Object> dynamicObjectMap = new HashMap<>();
            ArrayList<Object> dynamicEventList = new ArrayList<>();
            List<FactModel> facts;

            if(isForDiagnose) facts = factsForDiagnose;
            else facts = factsForSubmit;

            for (var fact : facts) {
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
                    if(paramNode.isObject()){
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

                if(parameterList.stream().anyMatch(e -> e.getClass() == Date.class)){
                    dynamicEventList.add(obj);
                } else {
                    dynamicObjectMap.put(fact.getObjectId(), obj);
                }
            }

            var dynamicFactList = new ArrayList<>(dynamicObjectMap.values());

            var dynamicObjectList = new ArrayList<>();

            if(isCep){
                dynamicObjectList = dynamicEventList;
            }else{
                dynamicObjectList = dynamicFactList;
            }
            // Fire all rules
            for (var object : dynamicObjectList) {
                ks.insert(object);

                // folgendes kann man anders lösen, indem man z.B. Event-Interface generell im eTutor vorgibt.
                if(isCep){
                    Date ts = (Date) object.getClass().getMethod("timestamp").invoke(object);

                    var diff = ts.getTime() - clock.getCurrentTime();
                    if (diff > 0)
                        spc.advanceTime(diff, TimeUnit.MILLISECONDS);
                }

                ks.fireAllRules();
            }

            return ks.getObjects();

        } catch (ReflectiveOperationException | IOException e) {
            throw new IOException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public int createSampleSolution(Boolean isForDiagnose) throws ReflectiveOperationException, IOException {

        try {
            List<Map<String, Object>> listOfKeyValuePairs = createOutputFactList(isForDiagnose);

            // Erstellen eines ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonOutput = null;
            String requestBody = null;
            try {
                // Konvertierung der Liste von Key-Value-Paaren in JSON
                jsonOutput = objectMapper.writeValueAsString(listOfKeyValuePairs);

                DroolsObjectDTO objectDTO = new DroolsObjectDTO();
                objectDTO.setDataType("output");
                objectDTO.setFullClassname("");
                objectDTO.setTaskId(exerciseID);
                objectDTO.setParameter(jsonOutput);
                String submissionType = "submit";
                if(isForDiagnose) submissionType = "diagnose";
                objectDTO.setSubmissionType(submissionType);

                requestBody = objectMapper.writeValueAsString(objectDTO);

            } catch (Exception e) {
                e.printStackTrace();
            }

            final HttpClient client = HttpClient.newHttpClient();
            URI uri = URI.create("http://localhost:8081/drools/task/createOutput");
            assert requestBody != null;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type","application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return response.statusCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException | ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    public List<Map<String, Object>> convertOutputJsonToListUtil(String jsonOutput){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Map<String, Object>> listOfMaps = objectMapper.readValue(jsonOutput, new TypeReference<>() {});

            // Rekursive Umwandlung der verschachtelten Maps
            List<Map<String, Object>> convertedList = new ArrayList<>();
            for (Map<String, Object> map : listOfMaps) {
                Map<String, Object> convertedMap = new HashMap<>();
                convertNestedMap(map, convertedMap);
                convertedList.add(convertedMap);
            }

            return convertedList;

            // Verarbeitung der umgewandelten Liste
//            for (Map<String, Object> convertedMap : convertedList) {
//                for (Map.Entry<String, Object> entry : convertedMap.entrySet()) {
//                    System.out.println(entry.getKey() + ": " + entry.getValue());
//                }
//                System.out.println("---------------");
//            }
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

    public List<Map<String, Object>> createOutputFactList(Boolean isForDiagnose) throws ReflectiveOperationException, IOException {

        Collection<?> generatedFacts = fireRules(isForDiagnose);

        List<Map<String, Object>> listOfKeyValuePairs = new ArrayList<>();

        for (Object fact : generatedFacts) {
            Map<String, Object> keyValuePairs = new HashMap<>();
            createKeyValuePairs(fact, keyValuePairs);
            listOfKeyValuePairs.add(keyValuePairs);
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
                            value = ((Date) value).toString(); // Anpassen Sie dies an das gewünschte Datumsformat
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

    public boolean compareLists(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        // Sortiere die Listen, um sicherzustellen, dass die Elemente in der gleichen Reihenfolge sind
        list1.sort(Comparator.comparing(Object::toString));
        list2.sort(Comparator.comparing(Object::toString));

        for (int i = 0; i < list1.size(); i++) {
            Map<String, Object> map1 = list1.get(i);
            Map<String, Object> map2 = list2.get(i);

            // Vergleiche die Maps
            if (!compareNestedMaps(map1, map2)) {
                return false;
            }
        }

        return true;
    }


    public static boolean compareNestedMaps(Map<String, Object> map1, Map<String, Object> map2) {
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

    public boolean hasSemantikError(){
        // Analyze

//        System.out.println(ks.getFactHandles().toString());
//        Collection<FactHandle> factHandles = ks.getFactHandles();
//        for(FactHandle factHandle : factHandles){
//            Object object = ks.getObject(factHandle);
//            System.out.println(object.toString());
//        }
//        System.out.println("===================================================");
//        System.out.println("INVOICES");

//            // Analyze
//            var inputClass = dyn.loadClass((String) inputClassName);
//            for (Object invoice : ks.getObjects(obj -> obj.getClass().equals(inputClass))) {
//                Assert.assertEquals(invoice.getClass().getMethod("price").invoke(invoice), (Double) expectedOutput);
//            }
//            //TODO: wird die anzahl der testdaten vs facten verglichen? 20231205 LK

        return false;
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

    public List<SourceFileModel> getJavaClasses() {
        return javaClasses;
    }

    public void setJavaClasses(List<SourceFileModel> javaClasses) {
        this.javaClasses = javaClasses;
    }

    public List<FactModel> getFactsForDiagnose() {
        return factsForDiagnose;
    }

    public void setFactsForDiagnose(List<FactModel> factsForDiagnose) {
        this.factsForDiagnose = factsForDiagnose;
    }


    public String getSolutionRules() {
        return solutionRules;
    }

    public void setSolutionRules(String solutionRules) {
        this.solutionRules = solutionRules;
    }

    @Override
    public String toString() {
        return "DroolsAnalysis{" +
                "exerciseID=" + exerciseID +
                ", inputRules='" + inputRules + '\'' +
                ", javaClasses=" + javaClasses +
                ", facts=" + factsForDiagnose +
                ", solution='" + solutionRules + '\'' +
                '}';
    }
}
