package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.entities.MinimalCover;

import java.util.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosure;

public class CalculateMinimalCover {
    private CalculateMinimalCover() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<MinimalCover> calculateMinimalCover(Exercise exercise) {
        Set<MinimalCover> returnSet = new HashSet<>();
        Exercise temp = new Exercise();
        Set<Dependency> leftSideReducedDependencies = new HashSet<>();
        temp.setDependencies(leftSideReducedDependencies);
        temp.setRelation(exercise.getRelation());
        leftSideReducedDependencies.addAll(exercise.getDependencies());
        /** Entfernen überflüssiger Attribute auf der linken Seite*/
        for (Dependency dependency: exercise.getDependencies()) {
            if (dependency.getLeftSide().length>1) {
                Closure original = calculateClosure(exercise, dependency.getLeftSide());
                List<String> reduced = null;
                Closure modified = null;
                for (String attribute: dependency.getLeftSide()) {
                    List<String> toReduce = new ArrayList<>();
                    /** Wenn noch kein Attribut auf der linken Seite der Abhängigkeit entfernt wurde */
                    if (reduced == null) {
                        toReduce.addAll(Arrays.asList(dependency.getLeftSide()));
                    } else {
                        toReduce.addAll(reduced);
                    }
                    toReduce.remove(attribute);
                    modified = calculateClosure(exercise, toReduce.toArray(new String[0]));
                    if (Arrays.equals(original.getRightSide(), modified.getRightSide())) {
                        reduced = toReduce;
                    }

                }
                if (reduced != null) {
                    leftSideReducedDependencies.remove(dependency);
                    Dependency replacement = new Dependency(reduced.toArray(new String[0]), dependency.getRightSide(), exercise);
                    returnSet.add(new MinimalCover(replacement.getLeftSide(), replacement.getRightSide(), Set.of(dependency)));
                    leftSideReducedDependencies.add(replacement);

                }
            }
        }
        Set<Dependency> rightSideReducedDependencies = new HashSet<>();
        rightSideReducedDependencies.addAll(leftSideReducedDependencies);
        /** Entfernen überflüssiger Attribute auf der rechten Seite */
        for (Dependency dependency: leftSideReducedDependencies) {
            temp.setDependencies(leftSideReducedDependencies);
            Closure original = calculateClosure(temp, dependency.getLeftSide());
            Dependency changedDependency = null;
            List<String> reduced = new ArrayList<>();
            for (String attribute: dependency.getRightSide()) {
                List<String> toReduce = new ArrayList<>();
                if (changedDependency == null) {
                    rightSideReducedDependencies.remove(dependency);
                    toReduce.addAll(Arrays.asList(dependency.getRightSide()));
                }
                else {
                    rightSideReducedDependencies.remove(changedDependency);
                    toReduce.addAll(reduced);
                }
                toReduce.remove(attribute);
                Dependency dependencyToCheck = new Dependency(dependency.getLeftSide(), toReduce.toArray(new String[0]), exercise);
                rightSideReducedDependencies.add(dependencyToCheck);
                temp.setDependencies(rightSideReducedDependencies);
                Closure modified = calculateClosure(temp, dependencyToCheck.getLeftSide());
                if (Arrays.equals(original.getRightSide(), modified.getRightSide())) {
                    changedDependency = dependencyToCheck;
                    reduced = toReduce;
                }
                else {
                    rightSideReducedDependencies.remove(dependencyToCheck);
                    if (changedDependency == null) {
                        rightSideReducedDependencies.add(dependency);
                    }
                    else {
                        rightSideReducedDependencies.add(changedDependency);
                    }
                }
            }
        }
        /** Entferne Abhängigkeiten ohne rechte Seite */
        for (Dependency dependency: rightSideReducedDependencies) {
            if (dependency.getRightSide().length == 0) {
                rightSideReducedDependencies.remove(dependency);
            }
        }
        Set<Dependency> result = new TreeSet<>(new DependencyComparator());
        result.addAll(rightSideReducedDependencies);
        /** Zusammenfassen der rechten Seiten, wenn die linken gleich sind */
        for (Dependency dependency: rightSideReducedDependencies) {
            for (Dependency dependency1: rightSideReducedDependencies) {
                if (Arrays.equals(dependency.getLeftSide(),dependency1.getLeftSide()) && !dependency.equals(dependency1)) {
                    result.remove(dependency);
                    Set<String> combineArray = new HashSet<>();
                    combineArray.addAll(Arrays.asList(dependency.getRightSide()));
                    combineArray.addAll(Arrays.asList(dependency1.getRightSide()));
                    result.add(new Dependency(dependency.getLeftSide(), combineArray.toArray(new String[0]), exercise));
                }
            }
        }
        return transformToMinimalCovers(result);
    }
    private static Set<MinimalCover> transformToMinimalCovers (Set<Dependency> dependencies) {
        Set<MinimalCover> minimalCovers = new HashSet<>();
        for (Dependency dependency: dependencies) {
            minimalCovers.add(new MinimalCover(dependency.getLeftSide(),dependency.getRightSide(), Set.of(dependency)));
        }
        return minimalCovers;
    }


    static class MinimalCoverComparator implements Comparator<MinimalCover> {
        @Override public int compare(MinimalCover e1, MinimalCover e2) {
            return MinimalCover.compare(e1, e2);
        }
    }
    static class DependencyComparator implements Comparator<Dependency> {
        @Override public int compare(Dependency e1, Dependency e2) {
            return Dependency.compare(e1, e2);
        }
    }


}
