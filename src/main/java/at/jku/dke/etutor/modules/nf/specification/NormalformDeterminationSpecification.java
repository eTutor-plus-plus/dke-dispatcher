package at.jku.dke.etutor.modules.nf.specification;

public class NormalformDeterminationSpecification extends NFSpecification {

    private int penaltyForIncorrectNFOverall;

    private int penaltyPerIncorrectNFDependency;

    public NormalformDeterminationSpecification() {
        super();
    }

    public int getPenaltyForIncorrectNFOverall() {
        return penaltyForIncorrectNFOverall;
    }

    public void setPenaltyForIncorrectNFOverall(int penaltyForIncorrectNFOverall) {
        this.penaltyForIncorrectNFOverall = penaltyForIncorrectNFOverall;
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
