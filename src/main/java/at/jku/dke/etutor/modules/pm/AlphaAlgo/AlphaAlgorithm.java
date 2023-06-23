package at.jku.dke.etutor.modules.pm.AlphaAlgo;

import java.util.*;

/**
 * The alpha Algorithm constructs a net (Pw, Tw, Fw)
 * Pw => Places to construct the PetriNet
 * Tw => Transitions the construct the PetriNet
 * Fw => Arcs to connect Places and Transitions
 * and is used to tackle the rediscovery problem (Aalst2004)
 * the algorithm takes as input a complete workflow log
 * and derives a corresponding workflow process model
 */

public class AlphaAlgorithm {

    // OBJECT FIELD
    private Log log;

    // CONSTRUCTOR
    public AlphaAlgorithm(Log log){
        this.log = log;
    }

    // METHODS


    // note: HashMap added on 03.10.2022
    public Map<String, Object> run(){
        // HashMap to export results:
        Map<String, Object> resultMap = new HashMap<>();

        // print Ordering Relations:
        printOrderingRelations();
        resultMap.put("orI1", log.directSuccession());
        resultMap.put("orI2", log.causality(log.directSuccession()));
        resultMap.put("orI3", log.parallel(log.directSuccession()));
        resultMap.put("orI4", log.independence(log.directSuccession()));

        // print Footprint Matrix:
        log.generateFootprintMatrix();

        // AlphaAlgorithm Steps (8steps):
        /* ------------------------------ */
        System.out.println("-------------------------");
        System.out.println("AlphaAlgorithm: ");
        System.out.println("-------------------------");

        // Step 1, 2, 3: print Tl={...}, Ti = {...}, To= {...} on console
        printTransitionSets();
        resultMap.put("aaI1", log.getTransitions());
        resultMap.put("aaI2", log.getTiSet());
        resultMap.put("aaI3", log.getToSet());

        // Step 4: construct and print Xl
        List<Pair<List<Event>, List<Event>>> XlSet = deriveXlSet();
        resultMap.put("aaI4", XlSet);

        // Step 5: construct and print Yl
        List<Pair<List<Event>, List<Event>>> YlSet = deriveYlSet(XlSet);
        resultMap.put("aaI5", YlSet);

        // Step 6: construct Pl (Places)
        List<Place> PlSet = getPlSet(YlSet);
        resultMap.put("aaI6", PlSet);

        // Step 7: construct Fl (Arcs between Place - Transition)
        List<Arc<?,?>> FlSet = deriveFlSet(PlSet);
        resultMap.put("aaI7", FlSet);

        // Step 8: overall Result
        System.out.println("α(L) = (Pl, Tl, Fl)");
        return resultMap;

    }

    public void printOrderingRelations(){
        System.out.println("Ordering Relations: ");
        // set of direct Successors
        System.out.println(">: " + log.directSuccession());

        // set of causal relations
        System.out.println("->: " + log.causality(log.directSuccession()));

        //set of parallel relations
        System.out.println("||: " + log.parallel(log.directSuccession()));

        // set of independent relations
        System.out.println("#: " + log.independence(log.directSuccession()));

        // blank line
        System.out.println();
    }

    public void printTransitionSets(){
        // Set of unique transitions Tl
        System.out.println("Transitions Tl= {" + log.getTransitions() + "}");

        // Set Ti => initial transitions
        System.out.println("Initial Transitions Ti= {" + log.getTiSet() + "}");

        // Set To => final transitions
        System.out.println("Final Transitions To= {" + log.getToSet() + "}");

    }

    /**
     * Definition of Xl:
     * Xl = {(A,B) | A subset of Tl AND B subset of Tl
     *               AND for all a element of A, for all b element of B: a -> b
     *               AND for all a1, a2 element of A    a1 # a2
     *               AND for all b1, b2 element of B    b1 # b2
     *               }
     *
     *
     * (A,B) element of Xl if (Aalst2004):
     * there is a causal relation (->) from each member of A to each member of B
     * and the members of A and B never occur next to another
     * Note:
     * if a -> b, b -> a or a||b, then a and b cannot be both in A (or B)
     *
     *
     * 2 potential APPROACHES to solve the problem:
     * FIRST APPROACH:
     * 1. takes as basis for operations result set log.causality()
     * 2. operations:
     *      2.1 identify a transition on firstPosition in the set, that occurs multiple times on firstPosition
     *          if there is a transition occurring multiple times in firstPosition (a element of A)
     *          store set of following transitions (on secondPosition) (b element of B) and check if b1#b2?
     *          e.g. {(a,b), (b,c), (b,d), (e, g), (e,f) }
     *          => ({b}, {c,d})     => check if c,d is in independent set:
     *          if yes => store pair ({b}, {c,d}) in Xl
     *          if not => check next Transition in firstPosition
     *          => potential method: public boolean checkIndependence()
     *
     *      2.2 same procedure for equal transitions in secondPosition
     * 3. add pairs of result set log.causality() to Xl set
     *
     * SECOND APPROACH:
     * 1. derive all possible combinations of transitions in range of 1 to len(transition)
     * 2. eliminate all combinations which are not in result set independent (NOT IN #)
     * 3. create product of remaining combinations
     * 4. check if result of the product (e.g. (a, (c,d)) fulfills causality condition
     *      4.1 check causality for each possible combination: a -> c and a -> d
     *      4.2 if combination fulfills condition -> add to result set Xl
     *
     * SECOND APPROACH is USED!
     *
     *
     * Method deriveXlSet() returns List of Pairs (List of Events, List of Events)
     * Uses methods from Class Combination: for more details check class Combination
     */
    public List<Pair<List<Event>, List<Event>>> deriveXlSet(){
        // result set
        //List<Pair<?, ?>> xl = new ArrayList<>();
        List<Pair<List<Event>, List<Event>>> xl = new ArrayList<>();

        // convert Set of Transitions to List of Transitions ->
        List<Event> eventList = new ArrayList<>(log.getTransitions());
        // derive all possible combinations for list of transitions in every possible length of combination (j)
        // stores result in static resultList in Class combination
        for (int j= 1; j< eventList.size() +1; j++){
            Combination.deriveCombinations(eventList, eventList.size(), j);
        }

        // to store original List of all possible combinations, clone list
        // needed because method deriveCombination is called in checkIndependence() and overrides static result list
        List<List<Event>> clonedResultList = new ArrayList<>(Combination.resultList);
        // list is needed to store intermediate result list -> list with all possible combinations that fulfill a1#a2 condition
        List<List<Event>> checkedIndependence = new ArrayList<>();
        // check each possible combination for condition a1#a2 and add to checkedIndependence list if it fulfills condition
        for(List<Event> list: clonedResultList){
            if(Combination.checkIndependence(list, log.independence(log.directSuccession()))){
                checkedIndependence.add(list);
            }
        }

        // check if product of all possible combinations fulfills causality condition (a -> b):
        // if yes: add to result list xl
        for(List<Event> l1: checkedIndependence){
            for(List<Event> l2: checkedIndependence){
                if(Combination.checkCausality(l1, l2, log.causality(log.directSuccession()))){
                    xl.add(new Pair<>(l1, l2));
                }
            }
        }

        System.out.println("Xl Set: " + xl);
        return xl;
    }


    /**
     * Relation Yl is derived from Xl
     * by taking only the largest elements w.r.t. set inclusion
     *
     * Definition of Yl Set (Aalst2004):
     * Yl = {(A,B) element of Xl | for all (A',B')element of Xl A subset of A' AND B subset of B'
     *      following (A,B) = (A',B')}

     *
     * Explanation:
     * For all (A,B) in Xl, check if there is a pair (A',B') in Xl for which A is subset of A' and B is subset of B'.
     *
     * If A is a subset of A' and B is a subset of B', then it follows,
     * that (A,B) = (A',B') and
     * that means then again => Yl = {(A', B')} instead of Yl = {(A, B)}
     * (is thus replaced)
     *
     *
     * APPROACH to solve the problem:
     *
     * checks for each pair (A,B) in Xl if there is a pair (A',B') in Xl ((A,B) ≠ (A',B'))
     * for which A is subset of A' and B is subset of B'
     * if A is subset of A' and B is subset of B' do not include (A,B) in Yl
     *
     *
     * AB = (A,B)
     * ab = (A',B')
     */
    public List<Pair<List<Event>, List<Event>>> deriveYlSet(List<Pair<List<Event>, List<Event>>> XlSet){
        List<Pair<List<Event>, List<Event>>> yl = new ArrayList<>();

        for(Pair<List<Event>, List<Event>> AB: XlSet){
            boolean x = false;
            for (Pair<List<Event>, List<Event>> ab: XlSet){
                // check if pairs are the same -> if yes, dont check for subset because its always true
                if(!AB.equals(ab)){
                    // check if AB is a subset of ab and store the result
                    // check method subset() for further details
                    x = subsetOf(AB, ab);
                    // if A and B is a subset of A'(a) and B'(b) (method returned true) -> break the inner loop -> x stays true
                    if(x){
                        break;
                    }
                }
            }
            // if x == false => neither A nor B is a subset of A' and B' => since they are not in a subset => add to yl
            if (!x){
                yl.add(AB);
            }
        }

        System.out.println("Yl Set: " + yl);
        return yl;
    }


    /**
     * Method getPlSet()
     * This method is based on the Yl Set{} and returns the Places that connect the transitions
     * ++plus++
     * the source and sink places (il, ol)
     *
     * To return a set of places we must first define the class Place, since this is a new type of object
     * This means, before solving method getPlSet(), class Place must be defined first
     *
     *
     * First place i and o are added to the result list
     * than all other, regular places based on result list Yl are added
     */

    public List<Place> getPlSet(List<Pair<List<Event>, List<Event>>> YlSet){
        List<Place> pl = new ArrayList<>();

        // add source place ... new ArrayList<>() necessary because TiSet is SortedSet of Events
        pl.add(new Place(new Pair<>(null, new ArrayList<>(log.getTiSet()))));
        // add sink place ... new ArrayList<>() necessary because TiSet is SortedSet of Events
        pl.add(new Place(new Pair<>(new ArrayList<>(log.getToSet()), null)));

        // add other (regular) places to list
        for(Pair<List<Event>, List<Event>> p: YlSet){
            pl.add(new Place(p));
        }

        System.out.println("Pl Set: " + pl);
        return pl;
    }


    /**
     * Method getFlSet()
     *  to derive the set{} of arcs that connect places and transitions
     *
     * since sink and source places are defined as already described
     * it is necessary to check for null values -> use temporary boolean variables sink and source to check for this
     *
     * The Arc class and its associated methods were defined in the course of writing this method
     * for further information check Arc class
     *
     */

    public List<Arc<?,?>> deriveFlSet(List<Place> PlSet){
        List<Arc<?,?>> fl = new ArrayList<>();
        boolean source;
        boolean sink;

        for(Place place: PlSet){

            // check for each place if it is a sink or source place (short for if/else)
            source = place.getInputTransition() == null;
            sink = place.getOutputTransition() == null;

            if(!source){        //prevent null values
                for(Event event: place.getInputTransition()){
                    if(sink){   // special case: if sink place: print "o" instead of actual place definition
                        fl.add(new Arc<Event, String>(event, "o"));
                    }else{
                        fl.add(new Arc<Event, Place>(event, place));
                    }
                }
            }

            if(!sink){      // prevent null values
                for(Event event: place.getOutputTransition()){
                    if(source){
                        fl.add(new Arc<String, Event>("i", event));
                    }else{
                        fl.add(new Arc<Place, Event>(place, event));
                    }
                }
            }
        }

        System.out.println("Fl Set: " + fl);
        return fl;
    }



    /**
     * FIRST APPROACH:
     * AB ==  (A,B); ab == (A', B')
     * if(ab.getFirstPosition().containsAll(AB.getFirstPosition()) // A subset of A'
     *      &&
     *    ab.getSecondPosition().containsAll(AB.getSecondPosition())) // B subset of B' {
     *      return true;
     * }
     * return false;
     * ********************
     * Method returns true,
     *  if Pair(A,B) is subset of Pair(A',B')   => A subset of A' !AND! B subset of B'
     * Method returns false,
     *  otherwise
     *
     * @param AB Pair (A,B) that is part of Set Yl (original from set Xl)
     * @param ab Pair(a,b) that is part of Set Xl
     * @return returns true, if Pair(A,B) is subset of Pair(A',B')   => A subset of A' !AND! B subset of B'
     */

    public static boolean subsetOf(Pair<List<Event>,List<Event>> AB, Pair<List<Event>,List<Event>> ab){
        return ab.getFirstPosition().containsAll(AB.getFirstPosition()) &&
                ab.getSecondPosition().containsAll(AB.getSecondPosition());
    }



//    /* ********TestArea******** */
//    public static void main(String[] args) {
//        // Initialize Traces
//        Trace t1 = new Trace(new String[] {"a", "c", "d"});
//        Trace t2 = new Trace(new String[] {"b", "c", "d"});
//        Trace t3 = new Trace(new String[] {"a", "c", "e"});
//        Trace t4 = new Trace(new String[] {"b", "c", "e"});
//        // generate Log
//        Log l1 = new Log(t1, t2, t3, t4);
//
//        AlphaAlgorithm a = new AlphaAlgorithm(l1);
//
//        a.printTransitionSets();
//
//
//        List<Pair<List<Event>, List<Event>>> test = a.deriveXlSet();
//        /*
//        System.out.println(test);
//        for(Pair<List<Event>, List<Event>> p1: test){
//            boolean x = false;
//            for(Pair<List<Event>, List<Event>> p2: test){
//                if (!p1.equals(p2)){
//                    System.out.println(subsetOf(p1,p2));
//                    x = subsetOf(p1, p2);
//                    if(x){
//                        break;
//                    }
//                }
//            }
//            if(!x){
//                System.out.println(p1);
//            }
//        }
//
//         */
//
//        //a.deriveYlSet(test);
//
//        List<Place> test2= a.getPlSet(a.deriveYlSet(test));
//        a.deriveFlSet(test2);
//
//
//
//
//
//
//
//
//    }
}
