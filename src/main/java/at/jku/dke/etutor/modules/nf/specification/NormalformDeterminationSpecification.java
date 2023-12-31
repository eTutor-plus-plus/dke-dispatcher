package at.jku.dke.etutor.modules.nf.specification;

public class NormalformDeterminationSpecification extends NFSpecification {

    private int pointsDeductedForIncorrectNFOverall;

    private int pointsDeductedPerIncorrectNFDependency;

    public NormalformDeterminationSpecification() {
        super();
    }

    public int getPointsDeductedForIncorrectNFOverall() {
        return pointsDeductedForIncorrectNFOverall;
    }

    public void setPointsDeductedForIncorrectNFOverall(int pointsDeductedForIncorrectNFOverall) {
        this.pointsDeductedForIncorrectNFOverall = pointsDeductedForIncorrectNFOverall;
    }

    public int getPointsDeductedPerIncorrectNFDependency() {
        return pointsDeductedPerIncorrectNFDependency;
    }

    public void setPointsDeductedPerIncorrectNFDependency(int pointsDeductedPerIncorrectNFDependency) {
        this.pointsDeductedPerIncorrectNFDependency = pointsDeductedPerIncorrectNFDependency;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
