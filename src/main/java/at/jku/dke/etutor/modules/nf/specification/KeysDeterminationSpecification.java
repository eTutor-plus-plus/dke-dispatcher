package at.jku.dke.etutor.modules.nf.specification;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class KeysDeterminationSpecification extends NFSpecification {
    private int penaltyPerMissingKey;

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
