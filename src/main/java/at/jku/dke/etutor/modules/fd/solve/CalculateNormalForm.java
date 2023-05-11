package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.*;
import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency.FunctionalDependencyComparator;
import at.jku.dke.etutor.modules.fd.entities.Exercise.NF;
import java.util.*;

public class CalculateNormalForm {



    public static NF calculateNormalForm(Exercise exercise) {
        if (!is2NF(exercise)) {
            return NF.FIRST;
        } else if (!is3NF(exercise)) {
            return NF.SECOND;
        } else if (!isBCNF(exercise)) {
            return NF.THIRD;
        } else {
            return NF.BCNF;
        }
    }


    public static boolean isBCNF (Exercise exercise) {
        /** Trivial oder Superschlüssel */
        for (Dependency dependency: exercise.getDependencies()) {
            if (!isTrivial(dependency) && !isSuperKey(exercise, dependency)) {
                return false;
            }
        }
        return true;
    }
    public static boolean is3NF (Exercise exercise) {
        /** Trivial, Superschlüssel oder Prime */
        for (Dependency dependency: exercise.getDependencies()) {
            if (!isTrivial(dependency) && !areAllPrimeAttributes(exercise, dependency)
                    && !isSuperKey(exercise, dependency)) {
                return false;
            }
        }
        return true;
    }
    public static boolean is2NF(Exercise exercise) {
        /** None Prime sind nicht von einem Teilschlüssel abhängig */
        Set<String> primeAttributes = getPrimeAttributes(exercise);
        for (Dependency dependency: exercise.getDependencies()) {
            boolean isKey = false;
            /** check ob alle abhängigen Attribute prime sind */
            if (primeAttributes.containsAll(Set.of(dependency.getRightSide()))) {
                continue;
            }

            /** Check ob die rechte seite ein Schlüsselkandidat ist */
            for (Key key: exercise.getKeys()) {
                if (!Arrays.equals(dependency.getLeftSide(), key.getLeftSide())
                        && Set.of(key.getLeftSide()).containsAll(Set.of(dependency.getLeftSide()))) {
                    return false;
                }
            }

        }
        return true;
    }

    private static boolean isTrivial(Dependency dependency) {
        return new HashSet<>(List.of(dependency.getLeftSide())).containsAll(List.of(dependency.getRightSide()));
    }

    private static boolean areAllPrimeAttributes(Exercise exercise, Dependency dependency) {
        Set<String> primeAttributes = getPrimeAttributes(exercise);
        for (String attribute: dependency.getRightSide()) {
            if (!primeAttributes.contains(attribute)) {
                return false;
            }
        }
        return true;
    }
    private static Set<String> getPrimeAttributes (Exercise exercise) {
        Set<String> primeAttributes = new TreeSet<>();
        for (Key key: exercise.getKeys()) {
            primeAttributes.addAll(Arrays.asList(key.getLeftSide()));
        }
        return primeAttributes;
    }

    public static boolean isSuperKey(Exercise exercise, Dependency dependency) {
        Set<Closure> closures = exercise.getClosures();
        Set<String[]> superKeys = new HashSet<>();
        for (Closure closure: closures) {
            if (Arrays.equals(closure.getRightSide(), exercise.getRelation())) {
                superKeys.add(closure.getLeftSide());
            }
        }
        for (String[] superKey: superKeys) {
            if (Arrays.equals(superKey, dependency.getLeftSide())) {
                return true;
            }
        }
        return false;
    }
}
