package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

public class FootprintMatrix {

    // OBJECT FIELDS
    // different result sets
    private final SortedSet<Event> transitions;
    private final Set<Pair<Event, Event>> independent;
    private final Set<Pair<Event, Event>> causal;
    private final Set<Pair<Event, Event>> parallel;
    // actual FootprintMatrix
    private final FootprintMatrixCell[][] cells;
    // Dictionary to Map index of columns to transition size
    // Key must be String -> Reason: use method get(Object key) from class HashMap -> we know the key (transition) and want to know the value (index of the 2D Array)
    private final Map<Event, Integer> transitionIndex = new HashMap<>();

    // CONSTRUCTOR

    /**
     * Heart of the class
     * if new Footprint matrix is declared and initialized it is directly printed without calling any further methods
     * for this one need to provide certain parameters to the constructor
     * @param transitions is the sorted set of unique transitions
     * @param independent is the set of independent pairs (never follow each other)
     * @param causal is the set of pairs with causality
     * @param parallel is the set of potential parallel pairs
     *
     * Operations:
     * creates new matrix and assigns each cell of the matrix the FootprintMatrixCell constructor
     * maps the events in the transition set to indices which are used to call a matrix cell [][]
     *  => Reason Map -> HashMap: https://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html
     * assigns the correct ordering relations to the respective cells
     * prints the matrix
     */
    public FootprintMatrix(SortedSet<Event> transitions, Set<Pair<Event, Event>> independent, Set<Pair<Event, Event>> causal, Set<Pair<Event, Event>> parallel){

        // initialize fields
        this.transitions = transitions;
        this.independent = independent;
        this.causal = causal;
        this.parallel = parallel;

        int numberOfTransitions  = transitions.size();

        // symmetric matrix => multidimensional Array of size number Transitions
        cells = new FootprintMatrixCell[numberOfTransitions][numberOfTransitions];

        // initialize Transition-name -- index map
        int startIndex = 0;
        for(Event e: transitions){
            transitionIndex.put(e, startIndex++);
        }

        // assign each fieldcell f.m.-cell constructor
        for (int row = 0; row < numberOfTransitions; row++) {
            for (int col = 0; col < numberOfTransitions; col++) {
                cells[row][col] = new FootprintMatrixCell();
            }
        }

        // assign each cell the appropriate relation type
        assignRelations();

        // print the matrix
        print();
    }

    // METHODS
    /**
     * Method assignRelations()
     * calls the different placing methods one after another
     * called in constructor of Footprint Matrix
     */
    public void assignRelations(){
        //place -> && place <-
        placeCausal();
        //place ||
        placeParallel();
        // place #
        placeNeverFollow();
    }

    /**
     * Method places the causal (>) and follow (<) signs in respective cell
     * Procedure:
     * iterate over pairs in causal set
     * for each event in pair get key in transitionIndex dict
     * provide this key to cells array to assign ordering relation to respective cell
     *
     * in the same process and with the same method assign ordering relations for follow sign in the respective cells
     * only difference: flip events of a causal pair
     * reason: if pair (a,b) has causal relation (->), pair with flipped events (b,a) has a following relation (<-)
     */
    public void placeCausal(){
        for(Pair<Event, Event> p: causal){
            // causal >
            cells[transitionIndex.get(p.getFirstPosition())][transitionIndex.get(p.getSecondPosition())].setSign(OrderingRelation.CAUSAL);

            // follow <
            Pair<Event, Event> x = p.flipEvents();
            cells[transitionIndex.get(x.getFirstPosition())][transitionIndex.get(x.getSecondPosition())].setSign(OrderingRelation.FOLLOW);
        }
    }

    /**
     * Method places the parallel sign (|) in the respective sign
     * Procedure:
     * iterate over pairs in parallel set
     * for each event in pair get key in transitionIndex dict
     * provide this key to cells array to assign ordering relation to respective cell
     */
    public void placeParallel(){
        for(Pair<Event, Event> p: parallel){
            cells[transitionIndex.get(p.getFirstPosition())][transitionIndex.get(p.getSecondPosition())].setSign(OrderingRelation.PARALLEL);
        }
    }

    /**
     * Method places the neverFollow sign (#) in the respective sign
     * Procedure:
     * iterate over pairs in independent set
     * for each event in pair get key in transitionIndex dict
     * provide this key to cells array to assign ordering relation to respective cell
     */
    public void placeNeverFollow(){
        for(Pair<Event, Event> p: independent){
            cells[transitionIndex.get(p.getFirstPosition())][transitionIndex.get(p.getSecondPosition())].setSign(OrderingRelation.NEVERFOLLOW);
        }

    }

    /**
     * Print method of FootprintMatrix
     * is called in FootprintMatrix constructor
     * therefore no extra call necessary
     */

    public void print(){
        // String.format(String format):
        int padding = (int) (Math.log10(cells[0].length - 1) + 1);
        String frameFormat = "%" + padding + "s";
        String fieldFormat = "%" + padding + "s";

        System.out.println("FootprintMatrix: ");

        // print the COLUMN HEADER padded
        paddingCol(padding);
        for(Event e: transitions){
            System.out.printf(frameFormat, e);
            System.out.print(" ");
        }
        System.out.println();

        // print EACH ROW padded
        int row = 0;
        for(Event e: transitions){
            // print transition name
            System.out.printf(frameFormat, e);
            System.out.print("|");

            // print the cell contents
            for (int col = 0; col < cells.length; col++) {
                System.out.printf(fieldFormat, cells[row][col]);
                System.out.print(" ");
            }
            System.out.println();
            row++;
        }

        System.out.println();
        System.out.println();
    }

    /**
     * method for padding
     * @param padding initialized in method print()
     */

    // add some padding before printing column header
    public static void paddingCol(int padding){
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.print(" ");

    }

}
