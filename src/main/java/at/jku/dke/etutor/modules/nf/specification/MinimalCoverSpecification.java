package at.jku.dke.etutor.modules.nf.specification;

public class MinimalCoverSpecification extends NFSpecification {

    private int pointsDeductedPerNonCanonicalDependency;

    private int pointsDeductedPerTrivialDependency;

    private int pointsDeductedPerExtraneousAttribute;

    private int pointsDeductedPerRedundantDependency;

    private int pointsDeductedPerMissingDependencyVsSolution;

    private int pointsDeductedPerIncorrectDependencyVsSolution;

    public MinimalCoverSpecification() {
        super();
    }

    public int getPointsDeductedPerNonCanonicalDependency() {
        return pointsDeductedPerNonCanonicalDependency;
    }

    public void setPointsDeductedPerNonCanonicalDependency(int pointsDeductedPerNonCanonicalDependency) {
        this.pointsDeductedPerNonCanonicalDependency = pointsDeductedPerNonCanonicalDependency;
    }

    public int getPointsDeductedPerTrivialDependency() {
        return pointsDeductedPerTrivialDependency;
    }

    public void setPointsDeductedPerTrivialDependency(int pointsDeductedPerTrivialDependency) {
        this.pointsDeductedPerTrivialDependency = pointsDeductedPerTrivialDependency;
    }

    public int getPointsDeductedPerExtraneousAttribute() {
        return pointsDeductedPerExtraneousAttribute;
    }

    public void setPointsDeductedPerExtraneousAttribute(int pointsDeductedPerExtraneousAttribute) {
        this.pointsDeductedPerExtraneousAttribute = pointsDeductedPerExtraneousAttribute;
    }

    public int getPointsDeductedPerRedundantDependency() {
        return pointsDeductedPerRedundantDependency;
    }

    public void setPointsDeductedPerRedundantDependency(int pointsDeductedPerRedundantDependency) {
        this.pointsDeductedPerRedundantDependency = pointsDeductedPerRedundantDependency;
    }

    public int getPointsDeductedPerMissingDependencyVsSolution() {
        return pointsDeductedPerMissingDependencyVsSolution;
    }

    public void setPointsDeductedPerMissingDependencyVsSolution(int pointsDeductedPerMissingDependencyVsSolution) {
        this.pointsDeductedPerMissingDependencyVsSolution = pointsDeductedPerMissingDependencyVsSolution;
    }

    public int getPointsDeductedPerIncorrectDependencyVsSolution() {
        return pointsDeductedPerIncorrectDependencyVsSolution;
    }

    public void setPointsDeductedPerIncorrectDependencyVsSolution(int pointsDeductedPerIncorrectDependencyVsSolution) {
        this.pointsDeductedPerIncorrectDependencyVsSolution = pointsDeductedPerIncorrectDependencyVsSolution;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
