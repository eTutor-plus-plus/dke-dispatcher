package at.jku.dke.etutor.modules.dlg.analysis;

import java.io.Serializable;

/**
 * This class can be used to represent the characteristics of a query result with
 * regard to consistency, number of models and <i>emptyness</i>.
 * <p>
 * The result of a query is referred to as <i>model </i>. It must be distinguished between a valid
 * model and an inconsistent model. A valid model may contain facts but may also be empty.
 * Contrarily, an inconsistent model is the result of given or derived facts, which are
 * contradictory in some sense, as the following simple example shows:
 * 
 * <pre>
 *   a. 
 *   -a.
 * </pre>
 * 
 * There is no model in which <code>a</code> is true together with its negation, so the model is
 * inconsistent. <p>
 * Moreover a result may infact consist of a number of models, in which the query is true.
 */
public class ModelConsistency implements Serializable {

    /**
     * Represents a result which contains exactly one consistent and <i>non-empty</i> model, which
     * means it contains at least one predicate.
     */
    public static final int CONSISTENT = 0;

    /**
     * Represents a result which contains exactly one consistent, but <i>empty</i> model, which
     * means it contains no predicates.
     */
    public static final int CONSISTENT_EMPTY = 1;

    /**
     * Represents a result which contains exactly one model, which is nevertheless <i>inconsistent</i>.
     */
    public static final int INCONSISTENT = 2;

    /**
     * Represents a result which consists of <i>more than one model</i>, whether consistent or not.
     */
    public static final int MULTIPLE = 3;

    /**
     * A result is set to this flag by default. This means none of the other flags apply as there is 
     * not a single model in the result, whether empty or non-empty, consistent or inconsistent.
     * Basically, this is due to syntax or timeout errors with regard to a query. 
     */
    public static final int DEFAULT = 4;

    private int consistency;

    /**
     * Constructs a new instance of <code>ModelConsistency</code> with {@link #DEFAULT}.
     */
    public ModelConsistency() {
        this.consistency = DEFAULT;
    }

    /**
     * Constructs a new instance of <code>ModelConsistency</code> and depending on the consistency
     * of the result models assigns one of the flags of this class. If the result was built from
     * a query which contains syntax errors or which raised a {@link etutor.modules.datalog.TimeoutException}
     * before evaluation was finished, the {@link #DEFAULT} flag appplies.
     * 
     * @param result The object, which represents the result of a query.
     * @throws NullPointerException if the passed object is <code>null</code>.
     */
    public ModelConsistency (
            DatalogResult result) throws NullPointerException {
        this.setModel(result);
    }

    /**
     * Sets the flags of this class depending on the consistency of the result models.
     * 
     * @param result The object, which represents the result of a query.
     * @throws NullPointerException if the passed object is <code>null</code>.
     */
    public void setModel(DatalogResult result) throws NullPointerException {
       /*
        WrappedModel[] models = result.getWrappedModels();
        if (result.getSyntaxException() != null || result.getTimeoutException() != null || models.length == 0) {
            this.consistency = DEFAULT;
        } else if (models.length == 1){
            WrappedModel model = result.getWrappedModels()[0];
            if (model.isConsistent()) {
                String[] predicateNames = model.getPredicateNames();
                if (predicateNames != null && predicateNames.length > 0) {
                    this.consistency = CONSISTENT;    
                } else {
                    this.consistency = CONSISTENT_EMPTY;
                }
            } else {
                this.consistency = INCONSISTENT;
            }
        } else {
            this.consistency = MULTIPLE;
        }

        */
    }

    /**
     * Returns the consistency category of a result with regard to the models it contains.
     * 
     * @return One of the constants defined in this class.
     */
    public int getConsistency() {
        return consistency;
    }

}