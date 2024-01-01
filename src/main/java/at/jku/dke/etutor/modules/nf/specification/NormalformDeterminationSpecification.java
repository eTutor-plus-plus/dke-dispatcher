package at.jku.dke.etutor.modules.nf.specification;

public class NormalformDeterminationSpecification extends NFSpecification {

    private int penaltyPerIncorrectNFOverall;

    private int penaltyPerIncorrectNFDependency;

    public NormalformDeterminationSpecification() {
        super();
    }

    public int getPenaltyPerIncorrectNFOverall() {
        return penaltyPerIncorrectNFOverall;
    }

    public void setPenaltyPerIncorrectNFOverall(int penaltyPerIncorrectNFOverall) {
        this.penaltyPerIncorrectNFOverall = penaltyPerIncorrectNFOverall;
    }

    public int getPenaltyPerIncorrectNFDependency() {
        return penaltyPerIncorrectNFDependency;
    }

    public void setPenaltyPerIncorrectNFDependency(int penaltyPerIncorrectNFDependency) {
        this.penaltyPerIncorrectNFDependency = penaltyPerIncorrectNFDependency;
    }

    @Override
    public boolean semanticallyEquals(Object obj) {
        return false; // TODO: implement
    }
}
