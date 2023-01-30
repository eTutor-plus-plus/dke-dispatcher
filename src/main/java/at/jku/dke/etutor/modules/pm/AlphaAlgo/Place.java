package at.jku.dke.etutor.modules.pm.AlphaAlgo;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Objects;

/**
 * Class Place
 * Places are part of a Workflow net and connect the transitions'
 * workflow nets are a subclass of Petri Nets (Aalst1997)
 *
 * and have two special places: i and o
 * used to mark begin and end of a procedure
 *
 * Places are presented by circles (Aalst1997)
 * There are input and output places of transitions
 *
 * Tasks are modeled by transitions and precedence relations are modeled by places
 *
 * For the purpose of this work, places are defined as follows:
 * a place is defined by a pair of events p(A,B)
 * the list of pairs which is used to define those pairs is derived by method deriveYlSet()
 * additionally the class place always has a source i and sink o node
 */

public class Place {

    // CLASS FIELDS
    /**
     *  as described above, workflow nets have two special places: source and sink places
     *  a source place is a place but only with outgoing arcs and therefore output transitions
     *  a sink place is a place but only with ingoing arcs and therefore input transitions
     *
     *  IDEA:
     *  define source and sink as a pair but with a null value for missing in- or output transitions
     *
     *  SOLVED in other way:
     *
     *  no need for static class fields
     *  in class AlphaAlgorithm, method getPlSet() we just add those to places to the list before the other, regular places
     *  to do so, a new place is initialized with the respective values
     *  for place i: null ingoing transitions and Set Ti as outgoing transitions
     *  for place o: Set To as ingoing transitions and null as outgoing transitions
     *
     */

    // OBJECT FIELDS
    //each place is defined by a pair of input and output transitions
    Pair<List<Event>, List<Event>> place;

    // CONSTRUCTOR
    public Place (Pair<List<Event>, List<Event>> inAndOutputEventsFromYl){
        this.place = inAndOutputEventsFromYl;
    }
    // note: added 18.10
    public Place(){

    }


    // METHODS
    // getter Methods
    public Pair<List<Event>, List<Event>> getPlace() {
        return place;
    }
    @JsonIgnore
    public List<Event> getInputTransition(){
        return place.getFirstPosition();
    }
    @JsonIgnore
    public List<Event> getOutputTransition(){
        return place.getSecondPosition();
    }


    @Override
    public String toString() {
        return "p" + place;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Place place = (Place) o;
        return Objects.equals(this.place, place.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place);
    }
}
