package at.jku.dke.etutor.modules.dlg.analysis;

import edu.harvard.seas.pl.abcdatalog.ast.PositiveAtom;

import java.io.Serializable;
import java.util.*;


/**
 * Represents a model which is part of the result of a Datalog query.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class WrappedModel implements Serializable {

    private String query;

    private String database;

    private String[] requPredicates;

    private int maxInt;

    private int exerciseID;

    private boolean consistentModel;

    private WrappedPredicate[] predicates;

    private String[] predicateNames;


    /**
     * Constructs a new <code>WrappedModel</code> from a set of PositiveAtoms as returned by the ABCDatalogProcessor.
     * @param result The model object, as it is returned by the processor.
     */
    public WrappedModel(Set<PositiveAtom> result) {
        this.consistentModel = true;
        this.predicates = getWrappedPredicates(result);
        this.predicateNames = getPredicateNames(this.predicates);
    }

    /**
     * Constructs a new <code>WrappedModel</code> from a Map as returned by the DlvCliDatalogProcessor.
     * @param model The model object, as it is returned by the processor. Maps predicates to lists of facts
     */
    public WrappedModel(Map<String, List<String>> model){
        this.consistentModel = true;
        this.predicates = getWrappedPredicates(model);
        this.predicateNames = getPredicateNames(this.predicates);
    }
    
    /**
     * Returns the consistence of this <code>DatalogModel</code>, which is part of the result of
     * a datalog query.
     * 
     * @return true, if the query is not contradictory in any sense, so that there is a model in
     *         which the query is satisfied, else false.
     * @see ModelConsistency
     */
    public boolean isConsistent() {
        return consistentModel;
    }

    /**
     * Returns all predicates that this model contains.
     * 
     * @return The predicates of this model. This might not only be an empty array if there are
     *         simply no predicates in the result, but also if the model is infact no model due
     * 		   to inconsistence.
     */
    public WrappedPredicate[] getPredicates() {
        return predicates;
    }

    /**
     * Customizes a model by transforming its predicates into an internal representation of these
     * predicates, which provides all necessary information for analysis, grading and reporting
     * within this module.
     * 
     * @param model The model which is the original result of the query processor.
     * @return An array of customized, wrapped objects representing the predicates of the model.
     */
    private WrappedPredicate[] getWrappedPredicates(Set<PositiveAtom> model) {
        if (model == null || model.isEmpty()) {
            return new WrappedPredicate[0];
        }
        WrappedPredicate[] predicates = new WrappedPredicate[1];
        predicates[0] = new WrappedPredicate(model, this);
        return predicates;
    }

    private WrappedPredicate[] getWrappedPredicates(Map<String, List<String>> model){
        if (model == null || model.isEmpty()) {
            return new WrappedPredicate[0];
        }

        var p = new ArrayList<WrappedPredicate>();
        var iterator = model.entrySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            var entry = iterator.next();
            var key = entry.getKey();
            var value = entry.getValue();
            if (!value.isEmpty()) p.add(new WrappedPredicate(key, value, this));
        }
        return p.toArray(new WrappedPredicate[]{});
    }

    /**
     * Returns the names of the specified wrapped predicates.
     * 
     * @param predicates The predicates whose names are to be returned.
     * @return An array of the predicate names or null, if the parameter was null.
     */
    private String[] getPredicateNames(WrappedPredicate[] predicates) {
        if (predicates == null) {
            return null;
        }
        String[] names = new String[predicates.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = predicates[i].getName();
        }
        return names;
    }

    /**
     * Returns the names of all predicates which this <code>DatalogResult</code> contains.
     * 
     * @return An array of the predicate names.
     */
    public String[] getPredicateNames() {
        return predicateNames;
    }

    /**
     * Returns a predicate which this <code>DatalogModel</code> contains, identified by its name.
     * 
     * @param name Name of the requested predicate.
     * @return The predicate, if contained, otherwise <code>null</code>.
     */
    public WrappedPredicate getPredicate(String name) {
        for (int i = 0; i < predicates.length; i++) {
            if (predicates[i].getName().equals(name)) {
                return predicates[i];
            }
        }
        return null;
    }

    /**
     * Returns the result of the query with regard to this model.
     * 
     * @param filters A set of predicate names indicating which predicates should be included in the
     *            returned result. If this parameter is <code>null</code>, all available
     *            predicates are selected by default.
     * @return The evaluation result of the query. This might not only be an empty String if there
     *         are no facts returned, but also if the query contains syntax errors or evaluation was
     *         stopped after reaching a time limit.
     */
    public String getRawResult(String[] filters) {
        String result = "";
        if (filters == null) {
            filters = getPredicateNames();
        }
        if (this.consistentModel && filters != null) {
            List filterList = Arrays.asList(filters);
            for (int i = 0; i < this.predicates.length; i++) {
                WrappedPredicate pred = this.predicates[i];
                if (filterList.contains(pred.getName())) {
                    result += pred.toString();
                }
            }
        }
        return result;
    }

    /**
     * Returns the complete, unfiltered evaluation result of the query. This is equivalent to
     * invoking {@link #getRawResult(String[]) getRawResult(null)}.
     * 
     * @return The evaluation result of the query. This might not only be an empty String if there
     *         are no facts returned, but also if the query contains syntax errors or evaluation was
     *         stopped after reaching a time limit.
     */
    public String getRawResult() {
        return getRawResult(getPredicateNames());
    }

}
