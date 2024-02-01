package at.jku.dke.etutor.modules.nf.specification;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MinimalCoverSpecification extends NFSpecification {

    // Start of point deduction variables
    /**
     * Points deducted for every non-canonical functional dependency.
     * <br><br>
     * (a in the grading schema V3)
     */
    private int penaltyPerNonCanonicalDependency;

    /**
     * Points deducted for every trivial functional dependency.
     * <br><br>
     * (b in the grading schema V3)
     */
    private int penaltyPerTrivialDependency;

    /**
     * Points deducted for every extraneous attribute on the left-hand side of a functional dependency.
     * <br><br>
     * (c in the grading schema V3)
     */
    private int penaltyPerExtraneousAttribute;

    /**
     * Points deducted for every redundant functional dependency.
     * <br><br>
     * (d in the grading schema V3)
     */
    private int penaltyPerRedundantDependency;

    /**
     * Points deducted for every functional dependency that would have to be present in the correct solution but is not.
     * <br><br>
     * (e in the grading schema V3)
     */
    private int penaltyPerMissingDependencyVsSolution;

    /**
     * Points deducted for every functional dependency that exists the submission, even though it is not present in the
     * correct solution.
     * <br><br>
     * (f in the grading schema V3)
     */
    private int penaltyPerIncorrectDependencyVsSolution;
    // End of point deduction variables

    public MinimalCoverSpecification() {
        super();
    }

    public int getPenaltyPerNonCanonicalDependency() {
        return penaltyPerNonCanonicalDependency;
    }

    public void setPenaltyPerNonCanonicalDependency(int penaltyPerNonCanonicalDependency) {
        this.penaltyPerNonCanonicalDependency = penaltyPerNonCanonicalDependency;
    }

    public int getPenaltyPerTrivialDependency() {
        return penaltyPerTrivialDependency;
    }

    public void setPenaltyPerTrivialDependency(int penaltyPerTrivialDependency) {
        this.penaltyPerTrivialDependency = penaltyPerTrivialDependency;
    }

    public int getPenaltyPerExtraneousAttribute() {
        return penaltyPerExtraneousAttribute;
    }

    public void setPenaltyPerExtraneousAttribute(int penaltyPerExtraneousAttribute) {
        this.penaltyPerExtraneousAttribute = penaltyPerExtraneousAttribute;
    }

    public int getPenaltyPerRedundantDependency() {
        return penaltyPerRedundantDependency;
    }

    public void setPenaltyPerRedundantDependency(int penaltyPerRedundantDependency) {
        this.penaltyPerRedundantDependency = penaltyPerRedundantDependency;
    }

    public int getPenaltyPerMissingDependencyVsSolution() {
        return penaltyPerMissingDependencyVsSolution;
    }

    public void setPenaltyPerMissingDependencyVsSolution(int penaltyPerMissingDependencyVsSolution) {
        this.penaltyPerMissingDependencyVsSolution = penaltyPerMissingDependencyVsSolution;
    }

    public int getPenaltyPerIncorrectDependencyVsSolution() {
        return penaltyPerIncorrectDependencyVsSolution;
    }

    public void setPenaltyPerIncorrectDependencyVsSolution(int penaltyPerIncorrectDependencyVsSolution) {
        this.penaltyPerIncorrectDependencyVsSolution = penaltyPerIncorrectDependencyVsSolution;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MinimalCoverSpecification)) {
            return false;
        }

        MinimalCoverSpecification spec = (MinimalCoverSpecification) obj;

        if(spec.getPenaltyPerNonCanonicalDependency() != this.penaltyPerNonCanonicalDependency) {
            return false;
        }

        if(spec.getPenaltyPerTrivialDependency() != this.penaltyPerTrivialDependency) {
            return false;
        }

        if(spec.getPenaltyPerExtraneousAttribute() != this.penaltyPerExtraneousAttribute) {
            return false;
        }

        if(spec.getPenaltyPerRedundantDependency() != this.penaltyPerRedundantDependency) {
            return false;
        }

        if(spec.getPenaltyPerMissingDependencyVsSolution() != this.penaltyPerMissingDependencyVsSolution) {
            return false;
        }

        if(spec.getPenaltyPerIncorrectDependencyVsSolution() != this.penaltyPerIncorrectDependencyVsSolution) {
            return false;
        }

        return super.semanticallyEquals(spec);
    }
}
