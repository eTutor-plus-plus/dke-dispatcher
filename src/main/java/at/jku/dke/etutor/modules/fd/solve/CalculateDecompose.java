package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CalculateDecompose {
    private boolean isLossLessJoinable(Exercise exercise, Set<Dependency> dependencies) {
        return true;
    }

    private boolean isEquivalent(Exercise exercise, Exercise exercise1) {
        for (Dependency dependency: exercise1.getDependencies()) {
            if (!isFD(exercise, dependency)) {
                return false;
            }
        }
        for (Dependency dependency: exercise.getDependencies()) {
            if (!isFD(exercise1, dependency)) {
                return false;
            }
        }
        return true;
    }
    private boolean isFD(Exercise exercise, Dependency dependency) {
        for (Closure closure: exercise.getClosures()) {
            if (Arrays.equals(closure.getLeftSide(), dependency.getLeftSide())
                    && List.of(closure.getRightSide()).containsAll(List.of(dependency.getRightSide()))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
