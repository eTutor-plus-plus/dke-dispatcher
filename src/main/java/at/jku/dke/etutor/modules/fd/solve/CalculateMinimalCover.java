package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.*;

import java.util.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosure;

public class CalculateMinimalCover {
    private CalculateMinimalCover() {
        throw new IllegalStateException("Utility class");
    }

    public static Set<MinimalCover> calculateMinimalCover(Exercise exercise) {
        Set<Dependency> leftSideReducedDependencies = reduceLeftSide(exercise.getDependencies(), exercise);
        Set<Dependency> rightSideReducedDependencies = reduceRightSide(leftSideReducedDependencies, exercise);
        Set<Dependency> onlyValidDependencies = new TreeSet<>(new DependencyComparator());
        /** Entferne Abhängigkeiten ohne rechte Seite */
        System.out.println(rightSideReducedDependencies);
        for (Dependency dependency : rightSideReducedDependencies) {
            if (dependency.getRightSide().length > 0) {
                onlyValidDependencies.add(dependency);
            }
        }
        System.out.println("Hallo: "+onlyValidDependencies);
        Set<Dependency> result = new TreeSet<>(new DependencyComparator());
        result.addAll(onlyValidDependencies);
        /** Aufsplitten wenn mehrere Attribute auf der rechten Seite vorhanden sind */
        for (Dependency dependency : rightSideReducedDependencies) {
            if (dependency.getClass()== FunctionalDependency.class) {
                if (dependency.getRightSide().length>1) {
                    for (String split : dependency.getRightSide()) {
                        result.remove(dependency);
                        result.add(new MinimalCover(dependency.getLeftSide(), new String[]{split}, new String[]{"Zerlegung"},
                                (FunctionalDependency) dependency, exercise));
                    }
                }
                else {
                    result.remove(dependency);
                }
            }
            else {
                if (dependency.getRightSide().length>1) {
                    for (String split : dependency.getRightSide()) {
                        MinimalCover minimalCover = (MinimalCover) dependency;
                        result.add(new MinimalCover(dependency.getLeftSide(), new String[]{split},
                                addToArray(minimalCover.getReasons(),"Zerlegung"),
                                minimalCover.getDependency(), exercise));
                    }
                }
            }
        }
        return convertToMinimalCover(result);
    }

    /** Entfernen überflüssiger Attribute auf der linken Seite*/
    private static Set<Dependency> reduceLeftSide(Set<? extends Dependency> dependencies, Exercise exercise) {
        Set<Dependency> leftSideReducedDependencies = new HashSet<>();
        leftSideReducedDependencies.addAll(dependencies);
        for (Dependency dependency : exercise.getDependencies()) {
            if (dependency.getLeftSide().length > 1) {
                Closure original = calculateClosure(exercise.getDependencies(), dependency.getLeftSide(),
                        exercise);
                List<String> reduced = null;
                Closure modified = null;
                /** für jedes Attribut wird überprüft ob es überflüssig ist */
                for (String attribute : dependency.getLeftSide()) {
                    List<String> toReduce = new ArrayList<>();
                    /** Wenn noch kein Attribut auf der linken Seite dieser Abhängigkeit entfernt wurde */
                    if (reduced == null) {
                        toReduce.addAll(Arrays.asList(dependency.getLeftSide()));
                    } else {
                        toReduce.addAll(reduced);
                    }
                    toReduce.remove(attribute);
                    modified = calculateClosure(exercise.getDependencies(), toReduce.toArray(new String[0]), exercise);
                    if (Arrays.equals(original.getRightSide(), modified.getRightSide())) {
                        reduced = toReduce;
                    }
                }
                if (reduced != null) {
                    leftSideReducedDependencies.remove(dependency);
                    Dependency replacement;
                    if (dependency.getClass() == FunctionalDependency.class) {
                        replacement = new MinimalCover(reduced.toArray(new String[0]),
                                dependency.getRightSide(), new String[]{"Linksreduktion"},
                                (FunctionalDependency) dependency, exercise);
                    } else {
                        MinimalCover minimalCover = (MinimalCover) dependency;
                        replacement = new MinimalCover(reduced.toArray(new String[0]),
                                dependency.getRightSide(), addToArray(minimalCover.getReasons(), "Linksreduktion"),
                                minimalCover.getDependency(), exercise);
                    }
                    leftSideReducedDependencies.add(replacement);
                }
            }
        }
        return leftSideReducedDependencies;
    }

    /** Entfernen überflüssiger Attribute auf der rechten Seite */
    private static Set<Dependency> reduceRightSide(Set<? extends Dependency> dependencies, Exercise exercise) {
        Set<Dependency> rightSideReducedDependencies = new HashSet<>();
        rightSideReducedDependencies.addAll(dependencies);
        for (Dependency dependency : dependencies) {
            Closure original = calculateClosure(dependencies, dependency.getLeftSide(), exercise);
            Dependency changedDependency = null;
            List<String> reduced = new ArrayList<>();
            for (String attribute: dependency.getRightSide()) {
                List<String> toReduce = new ArrayList<>();
                if (changedDependency == null) {
                    rightSideReducedDependencies.remove(dependency);
                    toReduce.addAll(Arrays.asList(dependency.getRightSide()));
                } else {
                    rightSideReducedDependencies.remove(changedDependency);
                    toReduce.addAll(reduced);
                }
                toReduce.remove(attribute);
                MinimalCover dependencyToCheck;
                if (dependency.getClass() == FunctionalDependency.class) {
                    dependencyToCheck = new MinimalCover(dependency.getLeftSide(),
                            toReduce.toArray(new String[0]), new String[]{"Rechtssreduktion"},
                            (FunctionalDependency) dependency, exercise);
                } else {
                    MinimalCover minimalCover = (MinimalCover) dependency;
                    dependencyToCheck = new MinimalCover(dependency.getLeftSide(),
                            toReduce.toArray(new String[0]), addToArray(minimalCover.getReasons() ,"Rechtssreduktion"),
                            minimalCover.getDependency(), exercise);
                }
                rightSideReducedDependencies.add(dependencyToCheck);
                Closure modified = calculateClosure(rightSideReducedDependencies,
                        dependencyToCheck.getLeftSide(), exercise);
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
        return rightSideReducedDependencies;
    }
//    private Set<Dependency> combineRightSides (Set<? extends Dependency> dependencies, Exercise exercise) {
//        Set<Dependency> result = new TreeSet<>(new DependencyComparator());
//        result.addAll(dependencies);
//        for (Dependency dependency: dependencies) {
//            for (Dependency dependency1: dependencies) {
//                if (Arrays.equals(dependency.getLeftSide(),dependency1.getLeftSide()) && !dependency.equals(dependency1)) {
//                    result.remove(dependency);
//                    Set<String> combineArray = new HashSet<>();
//                    combineArray.addAll(Arrays.asList(dependency.getRightSide()));
//                    combineArray.addAll(Arrays.asList(dependency1.getRightSide()));
//                    result.add(new MinimalCover(dependency.getLeftSide(), combineArray.toArray(new String[0]), exercise));
//                }
//            }
//        }
//        return result;
//    }


    private static String[] addToArray (String[] oldArray, String toAdd) {
        String[] newArray = new String[oldArray.length+1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }
        newArray[newArray.length-1] = toAdd;
        return newArray;
    }
    private static Set<MinimalCover> convertToMinimalCover(Set<Dependency> toConvert) {
        TreeSet<MinimalCover> minimalCovers = new TreeSet<>(new MinimalCoverComparator());
        for (Dependency dependency: toConvert) {
            if (dependency.getClass() == MinimalCover.class) {
                minimalCovers.add((MinimalCover) dependency);
            }
        }
        return minimalCovers;
    }
    static class DependencyComparator implements Comparator<Dependency> {
        @Override public int compare(Dependency e1, Dependency e2) {
            if (!Arrays.equals(e1.getLeftSide(),e2.getLeftSide())) {
                return Arrays.compare(e1.getLeftSide(), e2.getLeftSide());
            }
            else if (!Arrays.equals(e1.getRightSide(),e2.getRightSide())) {
                return Arrays.compare(e1.getRightSide(), e2.getRightSide());
            }
            return 0;
        }
    }
    static class MinimalCoverComparator implements Comparator<MinimalCover> {
        @Override public int compare(MinimalCover e1, MinimalCover e2) {
            if (!Arrays.equals(e1.getLeftSide(),e2.getLeftSide())) {
                return Arrays.compare(e1.getLeftSide(),e2.getLeftSide());
            }
            else if (!Arrays.equals(e1.getRightSide(),e2.getRightSide())) {
                return Arrays.compare(e1.getRightSide(), e2.getRightSide());
            } else {
                return Arrays.compare(e1.getReasons(),e2.getReasons());
            }
        }
    }
}
