package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.*;

/**
 * A Trace is a set of tasks/ events => naming convention unclear -> use events
 * a workflow log consists of traces
 * ordering of tasks within a trace is relevant
 * a trace is also known as a sequence
 *
 * Decision guidance for the selection of interfaces: SW2, Chapter 8, Slide 55
 *
 * Requirements for this class:
 * fields
 ** class field: to store transition's
 * constructor:
 ** constructor that takes StringArray with activities as input
 * methods:
 ** getFirstElement of trace -> is needed for one step of algorithm => Ti= {}
 ** getLastElement of trace -> is needed for one step of algorithm => To = {}
 ** toString() -> print trace as needed
 ** equals() => needed for TreeSet --> @Override
 ** one method that returns set of unique transitions/ tasks
 ** method that adds Events after initializing: addEvent
 */

public class Trace{

    // CLASS FIELD
    /**
     * Idea behind this class field:
     * Add events from different Traces to SortedSet one by one if the event is not already in the set
     * Constructs a new, empty tree set, sorted according to the natural ordering of its elements.
     * corresponds to Tw = {...} of AlphaAlgorithm
     * TreeSet<Event>() implements Comparable <Event>
     *
     * Problem to think about:
     * Transitions for each Log separately?
     */

    // OBJECT FIELD
    private final List<Event> traceEvents;     // INTERFACE https://docs.oracle.com/javase/8/docs/api/java/util/List.html & SW2, Chapter Libraries, Slide18

    // CONSTRUCTOR
    /**
     * TWO public Trace constructor:
     *
     * FIRST
     * takes as input StringArray with sequence of activities
     * initializes List as ArrayList (contains of objects with type Event)
     *
     * SECOND
     * constructor with no input parameter
     * only initializes empty list
     * can be filled with events with addEvent() method
     *
     * OTHER Information
     * Reasons using ArrayList and NOT Arrays: https://www.geeksforgeeks.org/array-vs-arraylist-in-java/
     * ArrayList implements List Interface (SW2, Chapter8, Slide18)
     * List Interface (https://www.geeksforgeeks.org/list-interface-java-examples/)
     *
     * @param traceActivitiesArray takes String array with sequence of events as input
     *
     *
     */
    public Trace (String[] traceActivitiesArray){
        traceEvents = new ArrayList<Event>();  // since list is an interface, objects can not be created ot the type list

        for (int i = 0; i < traceActivitiesArray.length; i++) {
            Event newEvent = new Event(traceActivitiesArray[i]);
            // add element of traceActivitiesArray at end of ArrayList
            // make use of add() method of interface List/ class ArrayList
            traceEvents.add(newEvent);
        }
    }

    public Trace (){
        traceEvents = new ArrayList<Event>(); //Constructs an empty list with an initial capacity of ten
    }

    // METHODS
    /**
     * method adds event after trace is initialized
     * AND/OR:
     * method can be used to initialize empty trace
     *  => need of constructor that initializes empty trace => public Trace(){...}
     * @param newEvents is varArg parameter that appends one or more Events at the end of the list
     */
    public void addEvent (Event... newEvents){
        // make use of add() method of class ArrayList
        traceEvents.addAll(Arrays.asList(newEvents));
    }

    // getter methods
    /**
     * Method gets first Event of Trace => useful for AlphaAlgorithm set Ti={}
     * @return Event at index 0 (first object)
     */
    public Event getFirstEvent(){
        // make use of get() method of ArrayList
        return traceEvents.get(0);
    }

    /**
     * Method gets last Event of Trace => useful for AlphaAlgorithm set To={}
     * @return Event at last position
     */
    public Event getLastEvent(){
        // make use of get() method of ArrayList
        return traceEvents.get(traceEvents.size()-1);
    }

    /**
     * Method returns the entire Trace
     * @return the entire Trace of Type List
     */
    public List<Event> getTrace(){
        return traceEvents;
    }

    /**
     * Method that returns size/ length of trace
     * @returns int value size of trace
     * used to iterate over trace
     */
    public int sizeTrace(){
        return traceEvents.size();
    }

    public Event getEvent(int i){
        return traceEvents.get(i);
    }


    // print methods
    @Override
    public String toString() {
        return "<" + removedBrackets() +
                '>';
    }

    /**
     * Support method to delete brackets if trace is printed
     * @return trace without brackets []
     */
    public String removedBrackets(){
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (Event event : traceEvents) {
            builder.append(prefix);
            prefix = ", ";
            builder.append(event);
        }
        return builder.toString();
    }


    // equals/ hashcode
    /**
     * Because class Log uses HashSet to store traces
     * we need to override equals and hashcode for traces
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){     // Referenzgleichheit prüfen => referenzgleiche Objekte sind immer gleich (true)
            return true;
        }
        if (o == null || getClass() != o.getClass()){   // Klassen vergleichen, wenn Klassen nicht gleich, dann nicht identisch
            return false;
        }

        Trace trace = (Trace) o;
        return traceEvents.equals(trace.traceEvents);
    }


    @Override
    public int hashCode() {
        return Objects.hash(traceEvents);
        //return this.traceEvents.hashCode();
    }


    /**
     * Method for permutations of SortedSet transitions
     * used / needed for method independence() in class Log
     * @param transitions Set of unique Events of given traces
     * @return set of all permutations of set transitions
     */
    public static Set<Pair<Event, Event>> permutations(SortedSet<Event> transitions){
        Set<Pair<Event, Event>> allPermutations = new HashSet<>();

        for (Event a: transitions){
            for (Event b: transitions){
                allPermutations.add(new Pair<Event, Event>(a,b));
            }
        }
        return allPermutations;
    }


//    /* *****************Test Area ************************* */
//    public static void main(String[] args) {
//        Trace t1 = new Trace(new String[] {"a", "c", "d"});
//
//        t1.addEvent(new Event("g"));
//
//        Trace t2 = new Trace();
//        t2.addEvent(new Event("f"), new Event("r"), new Event("t"));
//
//        Trace t3 = new Trace(new String[] {"b", "c", "d"});
//        Trace t4 = new Trace(new String[] {"a", "c", "e"});
//        Trace t5 = new Trace(new String[] {"b", "c", "e"});
//        Trace t6 = new Trace(new String[] {"b", "c", "e"});
//
//        System.out.println(t1);
//        System.out.println(t2);
//
//        System.out.println(t1.getFirstEvent());
//        System.out.println(t1.getLastEvent());
//
//        System.out.println(t1.getTrace());
//
//
//
//        System.out.println(transitions);
//        System.out.println(transitions.size());
//        System.out.println(permutations(transitions));
//
//        // hash and equals
//        System.out.println("check hashcode and equal for traces: ");
//        System.out.println(t1.equals(t2));
//        System.out.println(t5.equals(t6));
//        System.out.println(t5.hashCode());
//        System.out.println(t6.hashCode());
//        System.out.println(t6.hashCode());
//
//    }
//
//
}
