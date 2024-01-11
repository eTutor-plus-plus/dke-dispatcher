package at.jku.dke.etutor.modules.nf.specification;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KeysDeterminationSpecification extends NFSpecification {
    /**
     * Points deducted for every missing attribute, as compared to the correct solution.
     * <br><br>
     * (a in the grading schema V3)
     */
    private int penaltyPerMissingKey;

    /**
     * Points deducted for every incorrect attribute, as compared to the correct solution.
     * <br><br>
     * (b in the grading schema V3)
     */
    private int penaltyPerIncorrectKey;

    public KeysDeterminationSpecification() {
        super();
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof KeysDeterminationSpecification)) {
            return false;
        }

        KeysDeterminationSpecification spec = (KeysDeterminationSpecification) obj;

        if(spec.getPenaltyPerMissingKey() != this.penaltyPerMissingKey) {
            return false;
        }

        if(spec.getPenaltyPerIncorrectKey() != this.penaltyPerIncorrectKey) {
            return false;
        }

        return super.semanticallyEquals(spec);
    }

    public int getPenaltyPerMissingKey() {
        return penaltyPerMissingKey;
    }

    public void setPenaltyPerMissingKey(int penaltyPerMissingKey) {
        this.penaltyPerMissingKey = penaltyPerMissingKey;
    }

    public int getPenaltyPerIncorrectKey() {
        return penaltyPerIncorrectKey;
    }

    public void setPenaltyPerIncorrectKey(int penaltyPerIncorrectKey) {
        this.penaltyPerIncorrectKey = penaltyPerIncorrectKey;
    }
}
