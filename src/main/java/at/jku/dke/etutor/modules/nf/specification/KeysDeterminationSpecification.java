package at.jku.dke.etutor.modules.nf.specification;

public class KeysDeterminationSpecification extends NFSpecification {
    private int pointsDeductedPerMissingKey;

    private int pointsDeductedPerIncorrectKey;

    public KeysDeterminationSpecification() {
        super();
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }

    public int getPointsDeductedPerMissingKey() {
        return pointsDeductedPerMissingKey;
    }

    public void setPointsDeductedPerMissingKey(int pointsDeductedPerMissingKey) {
        this.pointsDeductedPerMissingKey = pointsDeductedPerMissingKey;
    }

    public int getPointsDeductedPerIncorrectKey() {
        return pointsDeductedPerIncorrectKey;
    }

    public void setPointsDeductedPerIncorrectKey(int pointsDeductedPerIncorrectKey) {
        this.pointsDeductedPerIncorrectKey = pointsDeductedPerIncorrectKey;
    }
}
