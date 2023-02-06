package at.jku.dke.etutor.modules.pm.plg.application;

// package OpenXES
import org.deckfour.xes.model.*;
import org.deckfour.xes.model.XLog;

// PLG Packages
import at.jku.dke.etutor.modules.pm.plg.generator.ProgressAdapter;
import at.jku.dke.etutor.modules.pm.plg.generator.log.LogAnalysis;
import at.jku.dke.etutor.modules.pm.plg.generator.log.LogGenerator;
import at.jku.dke.etutor.modules.pm.plg.generator.log.SimulationConfiguration;
import at.jku.dke.etutor.modules.pm.plg.generator.log.noise.NoiseConfiguration;
import at.jku.dke.etutor.modules.pm.plg.generator.process.ProcessGenerator;
import at.jku.dke.etutor.modules.pm.plg.generator.process.RandomizationConfiguration;
import at.jku.dke.etutor.modules.pm.plg.model.Process;
//import at.jku.dke.etutor.modules.pm.plg.io.exporter.PNMLExporter;

// Java Util
import java.util.*;

public class SimulationApplication {

    private static int logSize;
    private static int maxActivityTrace;
    private static int minActivityTrace;
    private static List<String[]> resultLog;

    /**
     * Method that converts XAttribute elements to Strings and cuts prefix "Activity " before actual Activity name
     * @param event
     * @return returns name of activity
     */
    private static String serialize(XEvent event){
        Object o = event.getAttributes().get("concept:name").clone();
        return o.toString().replaceAll("Activity ", "");
    }

    /**
     * Main method to:
     * 1. Generate a random process
     * 2. Simulate this process and generate corresponding log
     *
     * What happens here?
     * 1. Random process is generated (with a priori defined parameters)
     * 2. Process gets verified that it is valid
     * 3. Process is simulated to create log (number of simulations is a priori defined)
     * 4. Log is converted to required format (XTraces, XEvent => String[]) with use of serialize() method
     * 5. Resulting log is then truncated since it can contain multiple occurrences of the same trace
     *
     * @throws Exception
     */
    public static void simulate(String configNr) throws Exception{
        // Create random process
        Process p =  new Process("TestProcess");

        // initialize randomize configurations
        RandomizationConfiguration runConfig = switch (configNr) {
            case "config1" -> RandomizationConfiguration.CONFIG_ONE;
            case "config2" -> RandomizationConfiguration.CONFIG_TWO;
            case "config3" -> RandomizationConfiguration.CONFIG_THREE;
            default -> RandomizationConfiguration.BASIC_VALUES.setDepth(3);
        };

        // fill process with life (generate random process)
        ProcessGenerator.randomizeProcess(p, runConfig);
        // verify that process is valid
        p.check();

        // todo: think about exporting generated result and return corresponding petriNet model
        //new PNMLExporter().exportModel(p, "p.pnml");

        // initialize simulation configuration
        int numberTraces = 1000;
        SimulationConfiguration sc = new SimulationConfiguration(numberTraces);
        sc.setNoiseConfiguration(NoiseConfiguration.NO_NOISE);

        // create new LogGenerator object (empty) and fill Log with simulations
        LogGenerator generator = new LogGenerator(p, sc, new ProgressAdapter());    // create new LogGenerator Object => empty log
        XLog log = generator.generateLog();

        // Convert log into required format
        List<String[]> arrayListLog = new ArrayList<String[]>();
        int i = 1;
        for(XTrace trace: log){
            List<String> tempList = new ArrayList<>();
            for (XEvent e: trace){
                tempList.add(serialize(e));
            }
            String[] tempStringArray = new String[tempList.size()];
            tempStringArray = tempList.toArray(tempStringArray);
            arrayListLog.add(tempStringArray);
        }

        // Truncate Log corresponding to AlphaAlgorithm requirements
        LogAnalysis alphaLog = new LogAnalysis(arrayListLog);

        // store RESULTS
        resultLog = alphaLog.getAlphaInput();
        logSize = alphaLog.getAlphaInput().size();
        maxActivityTrace = alphaLog.getMaxActivityTrace();
        minActivityTrace = alphaLog.getMinActivityTrace();
    }

    /**
     * Final method which is called in AlphaAlgorithm as input for Algo
     * Generate and simulate process until conditions/ restrictions are met
     * @param maxLogSize control for level of complexity
     * @param minLogSize control for level of complexity
     * @param maxActivity control for level of complexity
     * @param minActivity control for level of complexity
     * @param configNr controls for the randomization configuration (control for probabilities)
     * @return returns List of Traces == LOG (with a priori defined range of number of possible traces and number of possible activities/ events)
     * @throws Exception
     */
    public static List<String[]> finalLogGeneration(int minLogSize, int maxLogSize, int minActivity, int maxActivity, String configNr) throws Exception{
        int simulationCounter = 0;

        do{
            simulate(configNr);
            simulationCounter++;
        } while ((logSize >= minLogSize || logSize <= maxLogSize || maxActivityTrace <= maxActivity || minActivityTrace >= minActivity) && simulationCounter < 2000); //TODO: externalize 2000 and handle abortion without result

        System.out.println("Simulation complete! \nResult Log as input for AlphaAlgo (" + simulationCounter + " simulation(s)):");
        for (String[] s: resultLog){
            System.out.println(Arrays.toString(s));
        }

        return resultLog;
    }

    public static void main(String[] args) throws Exception{
        //finalLogGeneration(3, 6, 3, 6, "config3");
        simulate("config1");
        for (String[] s: resultLog){
            System.out.println(Arrays.toString(s));
        }
    }

}
