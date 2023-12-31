package at.jku.dke.etutor.modules.nf.specification;

public class MinimalCoverSpecification extends NFSpecification {

    private int pointsDeductedPerMissingDependency;

    private int pointsDeductedPerIncorrectDependency;

    public MinimalCoverSpecification() {
        super();
    }

    public int getPointsDeductedPerMissingDependency() {
        return pointsDeductedPerMissingDependency;
    }

    public void setPointsDeductedPerMissingDependency(int pointsDeductedPerMissingDependency) {
        this.pointsDeductedPerMissingDependency = pointsDeductedPerMissingDependency;
    }

    public int getPointsDeductedPerIncorrectDependency() {
        return pointsDeductedPerIncorrectDependency;
    }

    public void setPointsDeductedPerIncorrectDependency(int pointsDeductedPerIncorrectDependency) {
        this.pointsDeductedPerIncorrectDependency = pointsDeductedPerIncorrectDependency;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
