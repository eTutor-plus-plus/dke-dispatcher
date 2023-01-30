package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Class Combination:
 * basic idea for all possible combinations of r elements in given array of size n from:
 * https://www.geeksforgeeks.org/print-all-possible-combinations-of-r-elements-in-a-given-array-of-size-n/
 *      => the approach has been adopted in its basic features, but adapted for the purposes of the work
 *      => methods deriveCombinations() and combinationUtil()

 *      
 * class Combination consists of methods:
 * deriveCombinations() and combinationUtil()
 *      => produce all possible combinations of given List of Events
 *
 * checkIndependence()
 * checkCausality()
 *  => used to check conditions necessary for step 4 in alphaAlgorithm: derive Xl set
 */


public class Combination{


    // List of list with all possible combinations
    protected static final List<List<Event>> resultList = new ArrayList<>();

    /**
     * method combinationUtil() is a recursive function
     * derives all possible combinations from given input List of events
     *
     * @param transitions List of Events with transitions
     * @param data temporary List to store combinations
     * @param start int that is increased by 1 every time method is called - to iterate through transitions
     * @param end int size of transitions -1 (for index)
     * @param index int to track combination size
     * @param sizeCombination variable to control for combination size
     */
    static void combinationUtil(List<Event> transitions, List<Event> data, int start, int end, int index, int sizeCombination) {

        // First call => index = 0;
        if (index == sizeCombination) {
            List<Event> newListTemp = new ArrayList<>();
            for (int j=0; j<sizeCombination; j++) {
                newListTemp.add(data.get(j));
            }
            resultList.add(newListTemp);

            // ends method end returns to forLoop in method combinationUtil()
            return;
        }

        for (int i=start; i<=end && end-i+1 >= sizeCombination-index; i++) {
            data.add(index, transitions.get(i));
            combinationUtil(transitions, data, i+1, end, index+1, sizeCombination);
        }
    }
    static void deriveCombinations(List<Event> transitions, int sizeTransitions, int sizeCombination) {
        List<Event> data = new ArrayList<>();

        combinationUtil(transitions, data, 0, sizeTransitions-1, 0, sizeCombination);

    }


    /**
     * method checks causality condition (a->b) for each product of independence checked combinations
     * checks for each pair of event a element of A and b element of B whether there is a causal relationship
     *
     * @param A List of Events A, from independenceChecked List
     * @param B List of Events B, from independenceCheck List
     * @param causality result list with pairs of events that have causal relation
     * @return returns true if product consists of events with causal relationship
     */

    public static boolean checkCausality(List<Event> A, List<Event> B, Set<Pair<Event,Event>> causality){
        for(Event a: A){
            for(Event b: B){
                if(!causality.contains(new Pair<>(a, b))){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * method checks independence condition (a1#a2 and b1#b2) for each possible combination of transitions
     * if List of Events has size of 1 => no special check necessary, instant true
     * if size >= 2 (e.g.:  a b c):
     *      -> derive all possible combinations for this list with length 2! (e.g.: ab ac bc)
     *      -> with that: check if pair of event (e.g.: P(a,b)) is in independence result list
     * @param A -  list A of events ai (one of the possible transition combinations)
     * @param independence Set of independent Event Pairs - used to check if forall a1, a2 element of A: a1#a2
     * @return returns true, if a1#a2 otherwise false
     */

    public static boolean checkIndependence(List<Event> A, Set<Pair<Event, Event>> independence){
        // for lists with just one element e.g. [a] => return true because they are independent
        if (A.size() != 1) { // (A.size() >= 2){
            resultList.clear();
            deriveCombinations(A, A.size(), 2);
            for (List<Event> l : resultList) {
                if (!independence.contains(new Pair<>(l.get(0), l.get(1)))) {
                    return false;
                }
            }
        }
        return true;
    }




    
    
    
    
    




    /* +++++++++Test Area ++++++++ */
    public static void main (String[] args) {

        Trace t1 = new Trace(new String[] {"a","b", "c"/* , "d", "e" */ });
        List<Event> eventList = new ArrayList<>(Trace.transitions);


        for (int j= 1; j< eventList.size() +1; j++){
            deriveCombinations(eventList, eventList.size(), j);
        }

        System.out.println(resultList.size());
        System.out.println(resultList);





    }

}
