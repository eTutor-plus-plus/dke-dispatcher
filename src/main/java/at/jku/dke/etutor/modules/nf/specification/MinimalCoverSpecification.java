package at.jku.dke.etutor.modules.nf.specification;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MinimalCoverSpecification extends NFSpecification {

    private int penaltyPerNonCanonicalDependency;

    private int penaltyPerTrivialDependency;

    private int penaltyPerExtraneousAttribute;

    private int penaltyPerRedundantDependency;

    private int penaltyPerMissingDependencyVsSolution;

    private int penaltyPerIncorrectDependencyVsSolution;

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
