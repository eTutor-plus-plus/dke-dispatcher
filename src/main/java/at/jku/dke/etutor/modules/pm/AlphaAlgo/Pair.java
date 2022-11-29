package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.Objects;

/**
 * This is the second Version of Pair compared to
 * First Version of class Pair
 * In the context of planning method getXlSet() in class AlphaAlgorithm it has become clear that class Pair
 * with its only 2 possible events (a,b) is not efficient enough
 * in the case of set Xl we need the chance of pairs with more than 2 possible events, e.g. (a, (b,c))
 * therefore, class pair needs to be changed in terms of generality
 *
 * IDEA:
 * class should be as general as possible -> Generics -> SW2, Chapter 6, Slide 15
 * Possibilities that should be covered:
 * either just a pair of two events, or a pair of event, set of events and v.v. or a pair of two sets of events
 *
 *
 */

// SECOND version
public class Pair <E, S> {          // either Event or Set of Events or combination of both

    // OBJECT FIELDS:
    private E firstPosition;              // Example: Event x or v.v.
    private S secondPosition;              // Example: Set<Event> y or v.v


    // CONSTRUCTOR
    public Pair(E first, S second) {
        this.firstPosition = first;
        this.secondPosition = second;
    }

    // note: added 23.09.2022
    public Pair(){
        this.firstPosition = null;
        this.secondPosition = null;
    }


    // METHODS
    public E getFirstPosition() {
        return firstPosition;
    }       // Example: either returns Event x or set of events

    public S getSecondPosition() {
        return secondPosition;
    }

    // note: added 23.09.2022
    public void setFirstPosition(E firstPosition){
        this.firstPosition = firstPosition;
    }
    public void setSecondPosition(S secondPosition) {
        this.secondPosition = secondPosition;
    }
    public boolean firstPositionSet(){
        return this.firstPosition != null;
    }
    public boolean secondPositionSet(){
        return this.secondPosition != null;
    }

    /**
     * Method returns pair that has flipped events compared to original pair
     * Example: (a,b) = (b,a)
     * used in methods for log based ordering relations
     * @return pair with flipped events
     *
     *
     * In Case of the SECOND version:
     * this method should only work for pair(Event, Event)
     */
    public Pair<S, E> flipEvents(){
        // only usable for Event, Event
        // flip position of event
        return new Pair<S, E>(this.secondPosition, this.firstPosition);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(firstPosition, pair.firstPosition) && Objects.equals(secondPosition, pair.secondPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPosition, secondPosition);
    }


    @Override
    public String toString() {
        return "(" + firstPosition + "," + secondPosition + ")";

    }


    /* ********TestArea******** */
    public static void main(String[] args) {
        Event t1 = new Event("a");
        Event t2 = new Event("b");

        Pair<Event, Event> p = new Pair<Event, Event>(t1, t2);

        System.out.println(p);
        System.out.println(p.flipEvents());
    }


}





/*

// FIRST version
public class Pair {

    // OBJECT FIELDS:
    private final Event x;
    private final Event y;


    // CONSTRUCTOR
    public Pair(Event x, Event y) {
        this.x = x;
        this.y = y;
    }


    // METHODS
    public Event getX() {
        return x;
    }

    public Event getY() {
        return y;
    }


     * Method returns pair that has flipped events compared to original pair
     * Example: (a,b) = (b,a)
     * used in methods for log based ordering relations
     * @return pair with flipped events

    public Pair flipEvents(){
        return new Pair(this.y, this.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Pair pair = (Pair) o;
        return Objects.equals(x, pair.x) && Objects.equals(y, pair.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }


    // Place to test
    public static void main(String[] args) {
        Event t1 = new Event("a");
        Event t2 = new Event("b");

        Pair p = new Pair(t1, t2);

        System.out.println(p);
        System.out.println(p.flipEvents());
    }


}
*/



