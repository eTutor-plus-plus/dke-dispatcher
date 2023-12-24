package at.jku.dke.etutor.modules.nf.specification;

public class NormalformDeterminationSpecification extends NFSpecification {

    private int pointsDeductedForWrongNFOverall;

    private int pointsDeductedForWrongNFDependency;

    public NormalformDeterminationSpecification() {
        super();
    }

    public int getPointsDeductedForWrongNFOverall() {
        return pointsDeductedForWrongNFOverall;
    }

    public void setPointsDeductedForWrongNFOverall(int pointsDeductedForWrongNFOverall) {
        this.pointsDeductedForWrongNFOverall = pointsDeductedForWrongNFOverall;
    }

    public int getPointsDeductedForWrongNFDependency() {
        return pointsDeductedForWrongNFDependency;
    }

    public void setPointsDeductedForWrongNFDependency(int pointsDeductedForWrongNFDependency) {
        this.pointsDeductedForWrongNFDependency = pointsDeductedForWrongNFDependency;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
