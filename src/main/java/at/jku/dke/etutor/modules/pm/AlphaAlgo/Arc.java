package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.Objects;


/**
 * Arc Class
 *
 * Generic class to have the flexibility to define an arc object as required in a given situation
 * parameter T and E are given to constructor and with those two parameters arc object of type Pair(T,E) is constructed
 * @param <T> used for types String, Event, Place
 * @param <E> used for types String, Event, Place
 *
 */

public class Arc <T, E>{

    // OBJECT FIELD
    private  T first;
    private  E second;
    private  Pair<T, E> arc;

    // CONSTRUCTOR
    public Arc(T first, E second){
        this.first = first;
        this.second = second;
        arc = new Pair<>(first, second);
    }

    // note: added 28.09.2022
    public Arc(){
        this.first = null;
        this.second = null;
        this.arc = new Pair<>();
    }


    // METHODS
    // getter methods
    public T getFirst() {
        return first;
    }
    public E getSecond() {
        return second;
    }

    // note: added 28.09.2022
    public void setFirst(T first){
        this.first = first;
        this.arc.setFirstPosition(this.first);
    }
    public void setSecond(E second){
        this.second = second;
        this.arc.setSecondPosition(this.second);
    }

    @Override
    public String toString() {
        return "" + arc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Arc<?, ?> arc1 = (Arc<?, ?>) o;
        return Objects.equals(first, arc1.first) && Objects.equals(second, arc1.second) && Objects.equals(arc, arc1.arc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, arc);
    }




    /*** test area*****/
    public static void main(String[] args) {
        Arc<String , String > testArc = new Arc<>();

        testArc.setFirst("A");
        testArc.setSecond("B");

        System.out.println(testArc);

    }
}



