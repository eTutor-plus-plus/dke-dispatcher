package at.jku.dke.etutor.modules.nf.specification;

public class KeysDeterminationSpecification extends NFSpecification {
    private int penaltyPerMissingKey;

    private int penaltyPerIncorrectKey;

    public KeysDeterminationSpecification() {
        super();
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
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
