package at.jku.dke.etutor.modules.nf.specification;

public class KeysDeterminationSpecification extends NFSpecification {
    private int pointsDeductedForMissingKey;

    private int pointsDeductedForAdditionalKey;

    public KeysDeterminationSpecification() {
        super();
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }

    public int getPointsDeductedForMissingKey() {
        return pointsDeductedForMissingKey;
    }

    public void setPointsDeductedForMissingKey(int pointsDeductedForMissingKey) {
        this.pointsDeductedForMissingKey = pointsDeductedForMissingKey;
    }

    public int getPointsDeductedForAdditionalKey() {
        return pointsDeductedForAdditionalKey;
    }

    public void setPointsDeductedForAdditionalKey(int pointsDeductedForAdditionalKey) {
        this.pointsDeductedForAdditionalKey = pointsDeductedForAdditionalKey;
    }
}
