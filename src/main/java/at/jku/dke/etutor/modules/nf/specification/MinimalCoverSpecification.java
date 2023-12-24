package at.jku.dke.etutor.modules.nf.specification;

public class MinimalCoverSpecification extends NFSpecification {

    private int pointsDeductedForMissingDependency;

    private int pointsDeductedForAdditionalDependency;

    public MinimalCoverSpecification() {
        super();
    }

    public int getPointsDeductedForMissingDependency() {
        return pointsDeductedForMissingDependency;
    }

    public void setPointsDeductedForMissingDependency(int pointsDeductedForMissingDependency) {
        this.pointsDeductedForMissingDependency = pointsDeductedForMissingDependency;
    }

    public int getPointsDeductedForAdditionalDependency() {
        return pointsDeductedForAdditionalDependency;
    }

    public void setPointsDeductedForAdditionalDependency(int pointsDeductedForAdditionalDependency) {
        this.pointsDeductedForAdditionalDependency = pointsDeductedForAdditionalDependency;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
