package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.kie.api.builder.Results;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.SessionClock;
import org.kie.api.time.SessionPseudoClock;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
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
    private List<FactModel> facts;
    private List<EventModel> events;
    private String solution;
    private List<Object[]> testData;
    private static final String route = "http://localhost:8081";

    public DroolsAnalysis(int exerciseID, String inputRules) throws IOException {
        super();
        this.exerciseID = exerciseID;
        if(inputRules.isEmpty())
            inputRules = """
            package at.jku.dke.etutor.modules.drools.jit;

            // Imports nicht notwendig, da gleiches package
            // hier nur damit Auto-Complete zumindest tlw. funktioniert
            import at.jku.dke.etutor.modules.drools.jit.EnterParkingLotEvent
            import at.jku.dke.etutor.modules.drools.jit.ExitParkingLotEvent

            // Konsolenausgaben sind nicht erforderlich; hier nur zum Debugging
            rule "Combine parking intervals if reentry within 15 min"
            when
                $enter1 : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)
                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[0s, 15m] $exit)
            then
                System.out.printf("Re-enter within 15 minutes after exiting [vehicle: %s, enter1: %s, exit: %s, enter2: %s]%n",
                    $enter1.vehicle().licensePlate(),
                    $enter1.timestamp(),
                    $exit.timestamp(),
                    $enter2.timestamp());
                delete($exit);
                delete($enter2);
            end

            rule "Do not combine parking intervals if reentry after 15 min"
            when
                $enter1 : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after $enter1)
                $enter2 : EnterParkingLotEvent(vehicle.licensePlate() == $enter1.vehicle().licensePlate(), this after[15m1s] $exit)
            then
                System.out.printf("Re-enter more than 15 minutes after exiting [vehicle: %s, enter1: %s, exit: %s, enter2: %s]%n",
                    $enter1.vehicle().licensePlate(),
                    $enter1.timestamp(),
                    $exit.timestamp(),
                    $enter2.timestamp());
                delete($enter1);
                delete($exit);
            end

            rule "Issue invoice"
            when
                $enter : EnterParkingLotEvent()
                $exit : ExitParkingLotEvent(vehicle.licensePlate() == $enter.vehicle().licensePlate(), this after[2h1m] $enter)
            then
                System.out.printf("Parking duration more than 2 hours [vehicle: %s, enter: %s, exit: %s]%n",
                    $enter.vehicle().licensePlate(),
                    $enter.timestamp(),
                    $exit.timestamp());
                insert(new Invoice($exit.timestamp(), $enter.vehicle(), (int)Math.ceil(($exit.diffInMinutes($enter) - 120) / 60.0) * 3));
            end""";
        this.inputRules = inputRules;
        loadJavaClasses();
        loadFacts();
        loadSolution();
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
        URI uri = URI.create(route + "/drools/task/getSolution/" + this.exerciseID);
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
        this.solution = solution;
    }



    public boolean hasSyntaxError() throws IOException {
        try (DynamicDroolsBuilder dyn = new DynamicDroolsBuilder(this.javaClasses)) {
            //TODO: Richtige KieSession erstellen mit String rules 2023-11-24

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

    public void hasSemantikError() throws IOException, ReflectiveOperationException{
        boolean isCep = true;
        try(var dyn = new DynamicDroolsBuilder(this.javaClasses)) {
            var ks = dyn.newKieSession(this.inputRules, isCep);

            SessionClock clock = ks.getSessionClock();
            if (!(clock instanceof SessionPseudoClock spc))
                return;

            Map<Integer, Object> dynamicObjectMap = new HashMap<>();
            ArrayList<Object> dynamicEventList = new ArrayList<>();

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

            // Analyze
            System.out.println(ks.getFactHandles().toString());
            Collection<FactHandle> factHandles = ks.getFactHandles();
            for(FactHandle factHandle : factHandles){
                Object object = ks.getObject(factHandle);
                System.out.println(object.toString());
            }
            System.out.println("===================================================");
            System.out.println("INVOICES");
            var invoiceClass = dyn.loadClass("at.jku.dke.etutor.modules.drools.jit.Invoice");
            for (Object invoice : ks.getObjects(obj -> obj.getClass().equals(invoiceClass))) {
                System.out.println(invoice);
            }



//            // Analyze
//            var inputClass = dyn.loadClass((String) inputClassName);
//            for (Object invoice : ks.getObjects(obj -> obj.getClass().equals(inputClass))) {
//                Assert.assertEquals(invoice.getClass().getMethod("price").invoke(invoice), (Double) expectedOutput);
//            }
//            //TODO: wird die anzahl der testdaten vs facten verglichen? 20231205 LK


        } catch (ReflectiveOperationException | IOException e) {
            throw new IOException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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

    public List<SourceFileModel> getJavaClasses() {
        return javaClasses;
    }

    public void setJavaClasses(List<SourceFileModel> javaClasses) {
        this.javaClasses = javaClasses;
    }

    public List<FactModel> getFacts() {
        return facts;
    }

    public void setFacts(List<FactModel> facts) {
        this.facts = facts;
    }

    public List<EventModel> getEvents() {
        return events;
    }

    public void setEvents(List<EventModel> events) {
        this.events = events;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public List<Object[]> getTestData() {
        return testData;
    }

    public void setTestData(List<Object[]> testData) {
        this.testData = testData;
    }

    @Override
    public String toString() {
        return "DroolsAnalysis{" +
                "exerciseID=" + exerciseID +
                ", inputRules='" + inputRules + '\'' +
                ", javaClasses=" + javaClasses +
                ", facts=" + facts +
                ", events=" + events +
                ", solution='" + solution + '\'' +
                ", testData=" + testData +
                '}';
    }
}
