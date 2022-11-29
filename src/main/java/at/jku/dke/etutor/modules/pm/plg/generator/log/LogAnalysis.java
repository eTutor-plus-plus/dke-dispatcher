package at.jku.dke.etutor.modules.pm.plg.generator.log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogAnalysis {

    // OBJECT FIELDS
    private int maxActivityTrace;
    private int minActivityTrace;
    private final List<String[]> alphaInput;

    // CONSTRUCTOR
    public LogAnalysis(List<String[]> arrayListLog){
        this.maxActivityTrace = arrayListLog.get(0).length;
        this.minActivityTrace = arrayListLog.get(0).length;
        this.alphaInput = new ArrayList<>();
        alphaAlgoInputTraces(arrayListLog);
    }

    // METHODS
    /**
     * Input List of String[] for AlphaAlgorithm
     * Since theory (vanAalst2004) abstracts from identity and frequency of cases/ workflow traces
     * method returns List of String[] (traces), but includes only one trace at a time,
     * even if it occurred multiple times in the simulation (arrayListLog)
     *
     */

    public void alphaAlgoInputTraces(List<String[]> log){
        for(String[] strings: log){

            // allow no duplicate elements
            if(alphaInput.size() == 0){
                alphaInput.add(strings);
            }else if(!containsTrace(strings)){
                alphaInput.add(strings);
            }

            // track max and min number of activities in trace information
            if(strings.length > maxActivityTrace){
                maxActivityTrace = strings.length;
            }else if(strings.length < minActivityTrace){
                minActivityTrace = strings.length;
            }
        }

    }

    public boolean containsTrace(String[] trace){
        for(String[] traceLog: alphaInput){
            if (Arrays.equals(traceLog, trace)){
                return true;
            }
        }
        return false;
    }

    public List<String[]> getAlphaInput() {
        return alphaInput;
    }

    /**
     * To control over number of traces
     * @return the maximum of activities of all traces
     */
    public int getMaxActivityTrace() {
        return maxActivityTrace;
    }

    /**
     * To control over number of traces
     * @return the minimum of activities of all traces
     */
    public int getMinActivityTrace() {
        return minActivityTrace;
    }

}

