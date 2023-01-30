package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.Objects;

/**
 * Transitions T are part of a petri net tuple (P, T, F)
 * where T is a finite set of transitions such that P ∩ T = ∅ (empty set)
 * each @TRANSITION has input and output @PLACES connected by directed arcs
 *
 * Result of AlphaAlgorithm consists of:
 * #1 T_L= {...} => Set of transitions, derived by expecting log
 * #2 T_I = {...} => Set of all initial transitions
 * #3 T_O = {...} => Set of all final transitions
 *
 * it is sufficient to construct transitions with its name
 * no need for further constructors
 *
 * , sorted according to the natural ordering of its elements.
 *
 * class implements interface Comparable because class Trace constructs a new, empty tree set with Event objects inserted
 * and all elements inserted into the set must implement the Comparable interface
 *
 */

public class Event implements Comparable<Event>{

    // OBJECT FIELD
    private String name;

    // CONSTRUCTOR
    // note: changed to public on 23092022
    public Event(String name){
        this.name = name;
    }

    // note: added 18.10
    public Event(){

    }

    // METHODS
    public String getName() {
        return name;
    }

    // note: added 18.10
    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    /**
     * Compares this object with the specified object for order
     * compareTo method is referred to as its natural comparison method of interface Comparable
     * This method is needed because of the use of TreeSet in class Trace
     *
     * @param other
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Event other) {
         return name.compareTo(other.name);
    }


    /**
     * Reasons why we have to override equals() and hashCode():
     *
     * Because we use HashSet in Class Log we need to check if the elements of the HashSet (Traces) are equal
     * Therefore we first need to override equals and hashCode in class Traces
     * and because traces are differentiated by their events we also need to override
     * equals and hashcode in class Events
     *
     * @return true if two objects are equal
     * in this case here: if two events are equal based on their name (e.g. Event a == Event a ) method returns true
     * otherwiese false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o){     //Referenzgleichheit prüfen => referenzgleiche Objekte sind immer gleich (true)
            return true;
        }
        if (o == null || getClass() != o.getClass()){       // Klassen vergleichen, wenn Klassen nicht gleich, dann nicht identisch
            return false;
        }
        Event event = (Event) o;
        return Objects.equals(name, event.name);
    }

    /**
     * The general contract of hashCode is:
     *
     * Whenever it is invoked on the same object more than once during an execution of a Java application,
     * the hashCode method must consistently return the same integer, provided no information used in equals
     * comparisons on the object is modified. This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     *
     * If two objects are equal according to the equals(Object) method, then calling the hashCode method on each
     * of the two objects must produce the same integer result.
     *
     * It is not required that if two objects are unequal according to the equals(java.lang.Object) method, then calling
     * the hashCode method on each of the two objects must produce distinct integer results.
     * However, the programmer should be aware that producing distinct integer results for unequal objects may
     * improve the performance of hash tables.
     *
     * @return integer hashCode
     *
     * https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html
     */
    @Override
   public int hashCode(){
        return Objects.hash(name);  // SW2, Chapter 8, Slide 37
        //return this.name.hashCode();
   }

    /* ********TestArea******** */

    public static void main(String[] args) {
        Event t1 = new Event("a");
        Event t2 = new Event("a");
        Event t3 = new Event("b");
        System.out.println(t1.hashCode());
        System.out.println(t2.hashCode());
        System.out.println(t1.hashCode());
        System.out.println(t3.hashCode());

        System.out.println(t1);
    }


}
