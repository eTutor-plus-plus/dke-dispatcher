package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import at.jku.dke.etutor.modules.pm.PmEvaluator;
import at.jku.dke.etutor.modules.pm.plg.application.SimulationApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class AlphaAlgorithmApplication {


    /**
     * To test the different logs, please uncomment corresponding Test Log # section
     * !! and Comment previous Test Log #
     * Always just one Log object and corresponding traces active
     */
    public static void main(String[] args) throws Exception {

        /*// GENERATE RANDOM PROCESS, SIMULATE THIS PROCESS, GENERATE CORRESPONDING LOG
        Log simulatedLog = new Log();
        // quite restrictive restrictions (this will be redefined and better adapted - than simulation will be faster)
        // adjusting screw is class plg.generator.process.RandomizationConfiguration
        for(String[] strings: SimulationApplication.finalLogGeneration(
                2,
                8,
                2,
                6,
                "default")){

            simulatedLog.addTrace(new Trace(strings));
        }

        // run AlphaAlgorithm
        AlphaAlgorithm simA = new AlphaAlgorithm(simulatedLog);
        simA.run();*/



        // Further Examples:

        /*// TEST LOG 1
        // Initialize Traces (Aalst2004)
        Trace t1 = new Trace(new String[] {"A", "B", "C", "D"});
        Trace t2 = new Trace(new String[] {"A", "C", "B", "D"});
        Trace t3 = new Trace(new String[] {"A", "E", "D"});

        // generate Log
        Log l1 = new Log(t1, t2, t3);*/


        /*
        // TEST LOG 2
        // Initialize Traces (Aalst2004)
        Trace t1 = new Trace(new String[] {"a","b","c","d","e","f","b","d","c","e","g"});
        Trace t2 = new Trace(new String[] {"a","b","d","c","e","g"});
        Trace t3 = new Trace(new String[] {"a","b","c","d","e","f","b","c","d","e","f","b","d","c","e","g"});

        // generate Log
        Log l1 = new Log(t1, t2, t3);
        */



        // TEST LOG 3
        // Initializes Traces (Aalst2004)
        Trace t1 = new Trace(new String[]{"a", "c", "d"});
        Trace t2 = new Trace(new String[]{"b", "c", "d"});
        Trace t3 = new Trace(new String[]{"a", "c", "e"});
        Trace t4 = new Trace(new String[]{"b", "c", "e"});

        // generate Log
        Map<String, Object> resultMap;
        Log l1 = new Log(t1, t2, t3, t4);
        AlphaAlgorithm simA = new AlphaAlgorithm(l1);
        resultMap = simA.run();

        // test for storing in database:
        // convert to String
        ObjectMapper mapper = new ObjectMapper();
        String orI1 = mapper.writeValueAsString(resultMap.get("orI1"));
        String orI2 = mapper.writeValueAsString(resultMap.get("orI2"));
        String orI3 = mapper.writeValueAsString(resultMap.get("orI3"));
        String orI4 = mapper.writeValueAsString(resultMap.get("orI4"));
        String aaI1 = mapper.writeValueAsString(resultMap.get("aaI1"));
        String aaI2 = mapper.writeValueAsString(resultMap.get("aaI2"));
        String aaI3 = mapper.writeValueAsString(resultMap.get("aaI3"));
        String aaI4 = mapper.writeValueAsString(resultMap.get("aaI4"));
        String aaI5 = mapper.writeValueAsString(resultMap.get("aaI5"));
        String aaI6 = mapper.writeValueAsString(resultMap.get("aaI6"));
        //String aaI7 = mapper.writeValueAsString(resultMap.get("aaI7"));
        List<Arc<?,?>> aa7List = (List<Arc<?,?>>) resultMap.get("aaI7");
        String aaI7 = aa7List.stream().map(Object::toString).collect(Collectors.joining(", "));

        // convert back to object original object type
        Set<Pair<Event, Event>> or1Set = mapper.readValue(orI1, new TypeReference<Set<Pair<Event, Event>>>(){});
        Set<Pair<Event, Event>> or2Set = mapper.readValue(orI2, new TypeReference<>(){});
        Set<Pair<Event, Event>> or3Set = mapper.readValue(orI3, new TypeReference<>(){});
        Set<Pair<Event, Event>> or4Set = mapper.readValue(orI4, new TypeReference<>(){});
        SortedSet<Event> aa1Set = mapper.readValue(aaI1, new TypeReference<>(){});
        SortedSet<Event> aa2Set = mapper.readValue(aaI2, new TypeReference<>(){});
        SortedSet<Event> aa3Set = mapper.readValue(aaI3, new TypeReference<>(){});
        List<Pair<List<Event>, List<Event>>> aa4List = mapper.readValue(aaI4, new TypeReference<>(){});
        List<Pair<List<Event>, List<Event>>> aa5List = mapper.readValue(aaI5, new TypeReference<>(){});
        List<Place> aa6List = mapper.readValue(aaI6, new TypeReference<>(){});

        //aaI7 needs to be converted in a different way (like analysis)
        //List<Arc<?,?>> aa7List = mapper.readValue(aaI7, new TypeReference<>(){});

        System.out.println(or1Set);
        System.out.println(or2Set);
        System.out.println(or3Set);
        System.out.println(or4Set);
        System.out.println(aa1Set);
        System.out.println(aa2Set);
        System.out.println(aa3Set);
        System.out.println(aa4List);
        System.out.println(aa5List);
        System.out.println(aa6List);
        // just the string for control purpose
        System.out.println(aaI7);
        System.out.println(aa7List);


//        List<Pair<List<Event>, List<Event>>> test5 = (List<Pair<List<Event>, List<Event>>>) resultMap.get("aaI4");
//        String listString = test5.stream().map(Object::toString)
//                .collect(Collectors.joining(", "));
//        System.out.println(listString);
//        List<Arc<?,?>> test6 = (List<Arc<?,?>>)resultMap.get("aaI7");
//        String listString2 = test6.stream().map(Object::toString)
//                .collect(Collectors.joining(", "));
//        System.out.println(listString2);
//
//        Set<Pair<Event, Event>> test8 = (Set<Pair<Event, Event>>) resultMap.get("orI1");
//        String listString3 = test8.stream().map(Object::toString)
//                .collect(Collectors.joining(", "));
//        System.out.println(listString3);

        /*
        // TEST LOG 4
        // Initialize Traces (Aalst2004)
        Trace t1 = new Trace(new String[]{"a", "b", "e", "f"});
        Trace t2 = new Trace(new String[]{"a", "b", "e", "c", "d", "b", "f"});
        Trace t3 = new Trace(new String[]{"a", "b", "c", "e", "d", "b", "f"});
        Trace t4 = new Trace(new String[]{"a", "b", "c", "d", "e", "b", "f"});
        Trace t5 = new Trace(new String[]{"a", "e", "b", "c", "d", "b", "f"});

        // generate Log
        Log l1 = new Log(t1, t2, t3, t4, t5);
         */


        /*
        // TEST LOG 5
        // SIMULATED LOG
        Trace t1 = new Trace(new String[] {"A", "D", "B"});
        Trace t2 = new Trace(new String[] {"A", "E", "G", "K", "H", "F", "B"});
        Trace t3 = new Trace(new String[] {"A", "E", "M", "N", "F", "B"});
        Trace t4 = new Trace(new String[] {"A", "C", "B"});
        Trace t5 = new Trace(new String[] {"A", "E", "G", "I", "H", "F", "B"});
        Trace t6 = new Trace(new String[] {"A", "E", "G", "L", "H", "F", "B"});
        Trace t7 = new Trace(new String[] {"A", "E", "G", "J", "H", "F", "B"});

        // generate Log
        Log l1 = new Log(t1, t2, t3, t4, t5, t6, t7);
         */

        /*// TEST LOG 5
        // SIMULATED LOG
        Trace t1 = new Trace(new String[] {"A", "C", "E", "D", "B"});
        Trace t2 = new Trace(new String[] {"A", "C", "E", "F", "G", "E", "F", "G", "E", "D", "B"});
        Trace t3 = new Trace(new String[] {"A", "C", "E", "F", "G", "E", "D", "B"});
        Trace t4 = new Trace(new String[] {"A", "C", "E", "F", "G", "E", "F", "G", "E", "F", "G", "E", "F", "G", "E", "D", "B"});
        Trace t5 = new Trace(new String[] {"A", "C", "E", "F", "G", "E", "F", "G", "E", "F", "G", "E", "D", "B"});

        // generate Log
        Log l1 = new Log(t1, t2, t3, t4, t5);

        // run AlphaAlgorithm
        AlphaAlgorithm simA = new AlphaAlgorithm(l1);
        simA.run();*/

    }
}
