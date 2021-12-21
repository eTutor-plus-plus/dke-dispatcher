package at.jku.dke.etutor.modules.dlg.analysis;

import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;
import org.apache.logging.log4j.util.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents the predicates of the result of a Datalog query as it is returned by a Datalog query
 * processor. The predicates are customized, wrapped and transformed into an internal representation
 * so that analysis, grading and reporting can be carried out.
 * 
 * @author Georg Nitsche, Christian Eichinger
 * @version 1.1
 * @since 1.0
 */
public class WrappedPredicate implements Serializable {

    private String name;

    private int arity;

    private WrappedFact[] facts;

    private WrappedModel model;

    /**
     * @deprecated
     * Creates a new Wrapper for a Datalog predicate. Most important information is extracted, like
     * information about the facts, predicate name or number of terms.
     * 
     * @param factSet The facts.
     * @param model The model which this instance will be assigned to.
     * 
     */
    public WrappedPredicate(
            Set<PositiveAtom> factSet, WrappedModel model) {
        var predicate = factSet.stream().findFirst().get();
        this.model = model;
        this.arity = predicate.getPred().getArity();
        this.name = predicate.getPred().getSym();
        ArrayList factList = new ArrayList();

        // ce: 26.5.2008
        // ATTENTION: Do not use predicate.getLiterals() as the returned 
        // enumeration is buggy in version 3 of the predicate class
        // the enumeration causes an endless loop
        for(PositiveAtom p : factSet) {
            factList.add(new WrappedFact(p, this));
        }
        this.facts = (WrappedFact[])factList.toArray(new WrappedFact[] {});
    }

    /**
     * Initializes a wrapped predicate
     * @param predicateName the name of the predicate
     * @param facts a list of facts for this predicate
     * @param model the model reference
     */
    public WrappedPredicate(String predicateName, List<String> facts, WrappedModel model){
        if(!facts.isEmpty()){
            this.model = model;
            this.name = predicateName;
            this.arity = facts.stream().findFirst().get().split(",").length;
            var wrappedFactList = new ArrayList<>();
            for(String s : facts){
                if(Strings.isNotBlank(s)) wrappedFactList.add(new WrappedFact(s, this));
            }
            this.facts = wrappedFactList.toArray(new WrappedFact[] {});
        }
    }

    /**
     * Represents the Datalog fact which belongs to a predicate.
     * 
     * @author Georg Nitsche
     * @version 1.0
     * @since 1.0
     */
    public class WrappedFact implements Serializable {

        private String[] terms;

        private boolean positive;

        private WrappedPredicate predicate;

        private int arity;

        private String name;

        /**
         * @deprecated
         * Creates a new instance which represents the fact of a certain predicate.
         * 
         * @param fact The fact to be wrapped.
         * @param predicate The predicate which this instance will be assigned to.
         */
        private WrappedFact(
            PositiveAtom fact, WrappedPredicate predicate) {
            this.predicate = predicate;
            this.positive = true;
            this.arity = fact.getPred().getArity();
            this.terms = new String[arity];
            this.name = fact.getPred().getSym();
            for (int i = 0; i < arity; i++) {
                this.terms[i] = fact.getArgs()[i].toString();
            }

        }

        /**
         * Initializes a wrapped fact
         * @param fact the terms
         * @param predicate the predicate
         */
        private WrappedFact(String fact, WrappedPredicate predicate){
            this.predicate = predicate;
            this.positive = true;
            this.arity = predicate.getArity();
            this.terms = fact.split(",");
            this.name = predicate.getName();
        }

        /**
         * Returns the predicate, which this <code>WrappedFact</code> is assigned to.
         * 
         * @return The "parent" predicate.
         */
        public WrappedPredicate getPredicate() {
            return predicate;
        }

        /**
         * Checks if this fact is negated or not negated.
         * 
         * @return true if this fact is positive, false if it is negated.
         */
        public boolean isPositive() {
            return positive;
        }

        /**
         * Returns all terms, that this fact contains, in the exact order.
         * 
         * @return Returns the terms of this fact.
         */
        public String[] getTerms() {
            return terms;
        }

        /**
         * Returns the number of terms which this fact contains. Note that this is equal to the
         * arity of the {@link WrappedPredicate}which this fact is assigned to.
         * 
         * @return Returns the arity.
         */
        public int getArity() {
            return arity;
        }

        /**
         * Returns the name of this fact. Note that this is equal to the name of the
         * {@link WrappedPredicate}which this fact is assigned to.
         * 
         * @return Returns the name.
         */
        public String getName() {
            return name;
        }

        /**
         * Generates a textual presentation of this fact. The following format is used: <br>
         * <code>[ '-' ] &lt;<i>fact name</i>&gt; [ '(' &lt;<i>term name</i>&gt; { ', ' &lt;<i>term name</i>&gt; } ')' ] '.'</code>
         * 
         * @return A String describing this fact.
         */
        public String toString() {
            String result = "";
            String prefix = "";
            if (!this.isPositive()) {
                prefix = "-";
            }
            String fact = "";
            String[] terms = this.getTerms();
            for (int i = 0; i < terms.length; i++) {
                if (i > 0) {
                    fact += ", ";
                }
                fact += terms[i];
            }
            if (fact.length() > 0) {
                fact = "(" + fact + ")";
            }
            result += prefix + getName() + fact + ".";
            return result;
        }

        /**
         * Equality is confirmed, if the <code>Object</code> is a <code>WrappedFact</code>, and
         * if it has the same name and arity as this <code>WrappedFact</code>. Moreover, each
         * term must be equal to term of this fact at the corresponding index.
         * 
         * @return true if equal, else false.
         * @see Object#equals(Object)
         */
        public boolean equals(Object obj) {
            if (!(obj instanceof WrappedFact)) {
                return false;
            }
            WrappedFact fact = (WrappedFact)obj;
            if (this.arity != fact.getArity() || !this.name.equals(fact.getName())) {
                return false;
            }
            for (int i = 0; i < this.arity; i++) {
                if (!this.getTerms()[i].equals(fact.getTerms()[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns the number of terms which this predicate contains. Note that this is equal to the
     * arity of all {@link WrappedFact}objects which this predicate contains.
     * 
     * @return Returns the arity.
     */
    public int getArity() {
        return arity;
    }

    /**
     * Returns all facts belonging to this predicate.
     * 
     * @return An array of wrapped facts.
     */
    public WrappedFact[] getFacts() {
        return facts;
    }


    /**
     * Returns the name of this predicate. Note that this is equal to the name of all
     * {@link WrappedFact}objects which this predicate contains.
     * 
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Generates a textual presentation of this predicate. Basically, this is a concatenation of
     * calling {@link WrappedFact#toString()}of all facts which this predicate contains.
     * 
     * @return A String describing this predicate.
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < facts.length; i++) {
            result += facts[i] + "\n";
        }
        return result;
    }

    /**
     * Equality is confirmed, if the <code>Object</code> is a <code>WrappedPredicate</code>,
     * and if it has the same name and arity as this <code>WrappedPredicate</code>.
     * 
     * @return true if equal, else false.
     * @see Object#equals(Object)
     */
    public boolean equals(Object obj) {
        return obj instanceof WrappedPredicate
                && ((WrappedPredicate)obj).getName().equals(this.name)
                && ((WrappedPredicate)obj).getArity() == this.arity;
    }

    /**
     * Tests if this predicate contains the specified fact.
     * 
     * @param fact The fact to search for.
     * @return true if the fact was found, otherwise false.
     */
    public boolean containsFact(WrappedFact fact) {
        return getFact(fact) != null;
    }

    /**
     * Gets a fact.
     * 
     * @param fact The fact to be searched for in this predicate.
     * @return The fact if found, else <code>null</code>.
     */
    public WrappedFact getFact(WrappedFact fact) {
        WrappedFact[] facts = this.getFacts();
        for (int i = 0; i < facts.length; i++) {
            if (facts[i].equals(fact)) {
                return facts[i];
            }
        }
        return null;
    }

    /**
     * Returns the model, which this <code>WrappedPredicate</code> is assigned to.
     * 
     * @return The "parent" model.
     */
    public WrappedModel getModel() {
        return model;
    }

}