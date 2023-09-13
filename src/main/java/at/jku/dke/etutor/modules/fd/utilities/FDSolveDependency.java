package at.jku.dke.etutor.modules.fd.utilities;

import java.util.Set;

public class FDSolveDependency {
    Set<String> leftSide;
    Set<String> rightSide;

    public FDSolveDependency(Set<String> leftSide, Set<String> rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public Set<String> getLeftSide() {
        return leftSide;
    }

    public Set<String> getRightSide() {
        return rightSide;
    }
}
