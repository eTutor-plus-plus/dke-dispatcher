package at.jku.dke.etutor.modules.pm.AlphaAlgo;

/**
 * Enumeration class that provides respective ordering relation signs
 * Consider workflow log W -> There are 4 relations possible (Aalst2004)
 * >w: relation describes which tasks appear in sequence (one directly following the other)
 * ->w: relation can be computed from >w and is referred to as the (direct) causal relation
 * ||w: suggests potential parallelism => (x || y) & (y||x)
 * #w: relation gives pairs of transitions that never follow each other directly
 *
 * FootprintMatrix only consists of ->, <-, #, ||
 * Therefore, initialize enum class for ordering relation types that occur in matrix
 *
 *
 * Enum Class consists of:
 * 1 object field: relation
 * 1 constructor
 * 1 getterMethod
 */

public enum OrderingRelation {
    CAUSAL('>'),        // -> causal relation
    FOLLOW('<'),        // <- follows from causal relation
    PARALLEL('|'),      // || parallelism
    NEVERFOLLOW('#');   // #

    // object field
    private char relation;

    // constructor
    OrderingRelation(char relation){
        this.relation = relation;
    }

    // methods
    public char getRelation() {
        return relation;
    }
}
