package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.*;
import static at.jku.dke.etutor.modules.pm.AlphaAlgo.Trace.permutations;

/**
 * Workflow Log as HashSet
 * Log is unsorted set of Traces and sequence of Traces is not important ==> HashSet (does not store duplicate items)
 *      => need of appropriate equals() and hashCode() Method
 * Decision support for selection of lists and sets: SW2, Chapter 8, Slide 55
 *
 * Methods needed:
 * First element of each trace => Ti = {...}    => getTi()
 * Last element of each trace => To= {...}      => getTo()
 * Add Traces manually => addTrace()
 * toString()
 *
 * HashSet:
 * https://docs.oracle.com/javase/7/docs/api/java/util/HashSet.html
 * https://www.geeksforgeeks.org/hashset-in-java/
 *
 */



public class Log {

    // OBJECT FIELD
    private final Set<Trace> log;
    private  FootprintMatrix matrix;

    // CONSTRUCTOR
    public Log(){
        log = new HashSet<Trace>();     // default initial capacity is 16
    }

    /**
     * Several Trace objects can be passed to the HashSet
     * @param traces one ore more potential traces
     */
    public Log(Trace... traces){
        log = new HashSet<Trace>();
        log.addAll(Arrays.asList(traces));
    }

    // optional but not necessary: stores unique collection f Objects like ArrayList<>, ...
    public Log(Collection<Trace> traces){
        log = new HashSet<>(traces);
    }

    // METHODS
    /**
     * Adds the specified trace to the set of Traces if it is not already present
     * @param trace is added to method
     */
    public void addTrace(Trace trace){
        //make use of method add() of HashSet Class
        log.add(trace);
    }

    // getter Methods

    /**
     * Store the first event in each trace -> store only events that are not already in set
     * @return set of initial events
     */
    public SortedSet<Event> getTiSet(){
        SortedSet<Event> t_i = new TreeSet<Event>();

        for(Trace trace: log){
            t_i.add(trace.getFirstEvent());
        }

        return t_i;
    }

    /**
     * Store the last event in each trace -> store only events that are not already in set
     * @return set of final events
     */
    public SortedSet<Event> getToSet(){
        SortedSet<Event> t_o = new TreeSet<Event>();

        for(Trace trace: log){
            t_o.add(trace.getLastEvent());
        }

        return t_o;
    }

    /**
     * Method returns number of traces in log
     * @return number of traces
     */
    public int getSizeLog(){
        return log.size();
    }

    /**
     * Method returns log
     * @return a Collection of traces
     */
    public Set<Trace> getLog() {
        return log;
    }

    @Override
    public String toString(){
        return "Log: " + log;
    }

    // LOG-BASED ORDERING RELATION METHODS
    /**
     * Requirements:
     * 4 methods that return 4 sets of log based ordering relations: >, ->, #, ||
     * Type of return: since duplicate elements are not allowed, the choice falls on Sets
     *      => HashSet (unsorted, no sequence)
     *
     * Reason why located in this area:
     * methods use log object to perform operations
     * therefore ordering relation methods are located in Log class
     *
     */


    /**
     * directSuccession():
     * Method returns set of direct successors > based on given workflow log L
     * describes which events/ tasks appeared in sequence (one following the other)
     *
     * Definition:
     * a >L b iff there is a trace sigma= t1t2t3...tn-1 and i e {1,...,n-2} such that sigma e L and ti = a
     * and ti+1 = b
     *
     * check for each trace in workflow log separately
     * e.g. >L = {(a,b), (b,d),... }
     * duplicate elements are not allowed
     *
     * Challenge:
     * Store multiple pairs of events in HashSet = {(a,b), (b,d),... }
     *
     * Solution:
     * https://www.geeksforgeeks.org/java-program-to-create-set-of-pairs-using-hashset/
     * https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples
     * => create Pair class (optional: Generics => Problem of Raw Type)
     *
     * Follow-up work:
     * in class Trace:
     *  => add method sizeTrace()
     *  => add method getEvent(int i)
     *
     * in class Pair:
     *  => add method equals()
     *  => add method hashCode()
     *
     * @return set of direct successors
     */
    public Set<Pair<Event, Event>>directSuccession(){
        Set<Pair<Event, Event>> directSuccessor = new HashSet<>();

        // check for each trace in log
        for(Trace trace: this.log){
            // check for each event in given trace
            // trace.sizeTrace()-1: -1 because last event has no direct successor
            for (int i = 0; i < trace.sizeTrace()-1; i++){
                //Pair p = new Pair(trace.getEvent(i), trace.getEvent(i+1));                        // FIRST version
                Pair<Event, Event> p = new Pair<>(trace.getEvent(i), trace.getEvent(i+1));    //line of code for pair as generics -- SECOND version
                directSuccessor.add(p);
            }
        }
        return directSuccessor;
    }

    /**
     * causality():
     * can be computed from >L and is referred to as the (direct) causal relation
     *
     * Definition:
     * a ->L b iff a >L b and b />L a
     *
     * Check the set directSuccessors for a Pair (a,b) that is also present in reversed order (b,a) and remove
     * do NOT include these two pairs in the new Set causality
     *
     * Type of Collection:
     * HashSet
     *
     *
     * ??static:
     * Relation ->L can be computed from >L
     * Therefore no need for object method but class method with directSuccessor as input
     *
     * in FootprintMatrix:
     * a>b and b<a
     *
     *
     * @return set of causal relations
     */

    //todo: store result of causality in seperate variable
    public Set<Pair<Event,Event>> causality(Set<Pair<Event,Event>> directSuccessor){
        // create new set of pairs
        Set<Pair<Event, Event>> causal = new HashSet<>();

        for(Pair<Event, Event> p: directSuccessor){
            // if there is no pair in reversed order to p in the set => add to Set causal
            if(!directSuccessor.contains(p.flipEvents())){
                causal.add(p);
            }
        }
        return causal;
    }

    /**
     * parallel():
     * can be from >L and suggests potential parallelism
     *
     * Definition:
     * a ||L b iff a >L b and b >L a
     *
     * Type of Collection:
     * HashSet
     *
     * Could be easily computed in method causality but for better understanding and visibility own method
     *
     * in Footprint matrix:
     * a|b and b|a
     *
     * @return set of potential parallel pairs
     */
    public Set<Pair<Event, Event>> parallel(Set<Pair<Event, Event>> directSuccessor){
        // create new set of pairs
        Set<Pair<Event, Event>> parallel = new HashSet<>();

        for(Pair<Event, Event> p: directSuccessor){
            // if there is a pair in reversed order to p in the set => add to Set parallel
            if(directSuccessor.contains(p.flipEvents())){
                parallel.add(p);
            }
        }
        return parallel;
    }

    /**
     * independence()
     * gives pairs of transitions/ events that never follow each other directly
     * no direct causal relations and parallelism is unlikely
     *
     * Definition:
     * a #L b iff a >/L b and b >/L a
     *
     * Type of Collection:
     * HashSet
     *
     * Idea how to construct this method:
     * a and b are independent, if neither a > b nor b> a
     * take SortedSet transitions in Class Trace
     * with that: create all possible permutations
     * reduce this set by pairs contained in the result set of causality and parallelism
     *
     * Follow- up work:
     * method for all permutations of set transitions
     */
    public Set<Pair<Event, Event>> independence(Set<Pair<Event, Event>> directSuccessor){
        Set<Pair<Event, Event>> independent = new HashSet<>();

        // set of all permutations of the transitions of given log and its traces
        Set<Pair<Event, Event>> all_permutations = permutations(Trace.transitions);

        for(Pair<Event, Event> p: all_permutations){
            if(!directSuccessor.contains(p) && !directSuccessor.contains(p.flipEvents())){
                independent.add(p);
            }
        }
        return independent;
    }

    /**
     * Method generates and prints footprintMatrix based on the result sets
     * of parallel, causality and independence.
     * called with respective log
     */
    // FOOTPRINT MATRIX
    public void generateFootprintMatrix(){
        matrix  = new FootprintMatrix(Trace.transitions, independence(directSuccession()), causality(directSuccession()), parallel(directSuccession()));
    }



    /* ********TestArea******** */
    public static void main(String[] args) {


        Trace t1 = new Trace(new String[] {"a","b","c","d","e","f","b","d","c","e","g"});
        Trace t2 = new Trace(new String[] {"a","b","d","c","e","g"});
        Trace t3 = new Trace(new String[] {"a","b","c","d","e","f","b","c","d","e","f","b","d","c","e","g"});

        /*
        Trace t4 = new Trace(new String[] {"a","b","c","d"});
        Trace t5 = new Trace(new String[] {"a","b","c","d"});



        Trace t6 = new Trace(new String[]{"a", "c", "d"});
        Trace t7 = new Trace(new String[]{"b", "c", "d"});
        Trace t8 = new Trace(new String[]{"a", "c", "e"});
        Trace t9 = new Trace(new String[]{"b", "c", "e"});

        Log l1 = new Log(t2, t4, t5);
        Log l2 = new Log();
        Log l3 = new Log(t1, t2, t3);


        Log l4 = new Log(t6, t7, t8, t9);

        l2.addTrace(t4);


        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l1.getSizeLog());

        System.out.println(l1.getTiSet());
        System.out.println(l1.getToSet());

        System.out.println("log based ordering relations: ");

        System.out.println(l3);
        System.out.println(l3.directSuccession());
        System.out.println(causality(l3.directSuccession()));
        System.out.println(parallel(l3.directSuccession()));
        System.out.println(l3.independence(l3.directSuccession()));

        System.out.println();



        System.out.println(l4);
        System.out.println(">: " + l4.directSuccession());
        System.out.println("->: " + l4.causality(l4.directSuccession()));
        System.out.println("||: " + l4.parallel(l4.directSuccession()));
        System.out.println("#: " + l4.independence(l4.directSuccession()));
        l4.generateFootprintMatrix();

        */

        Log l3 = new Log(t1, t2, t3);
        System.out.println(l3);
        System.out.println(">: " + l3.directSuccession());
        System.out.println("->: " + l3.causality(l3.directSuccession()));
        System.out.println("||: " + l3.parallel(l3.directSuccession()));
        System.out.println("#: " + l3.independence(l3.directSuccession()));
        l3.generateFootprintMatrix();
    }
}
