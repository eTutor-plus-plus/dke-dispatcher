package at.jku.dke.etutor.modules.nf.specification;

public class NormalformDeterminationSpecification extends NFSpecification {

    private boolean wrongOverallLevelInvalidatesEverything;

    private int pointsDeductedForWrongNFLevel;

    public NormalformDeterminationSpecification() {
        super(null);
    }

    public boolean isWrongOverallLevelInvalidatesEverything() {
        return wrongOverallLevelInvalidatesEverything;
    }

    public void setWrongOverallLevelInvalidatesEverything(boolean wrongOverallLevelInvalidatesEverything) {
        this.wrongOverallLevelInvalidatesEverything = wrongOverallLevelInvalidatesEverything;
    }

    public int getPointsDeductedForWrongNFLevel() {
        return pointsDeductedForWrongNFLevel;
    }

    public void setPointsDeductedForWrongNFLevel(int pointsDeductedForWrongNFLevel) {
        this.pointsDeductedForWrongNFLevel = pointsDeductedForWrongNFLevel;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
