package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.*;
import java.util.*;

public class CalculateNormalForm {
    private CalculateNormalForm() {
        throw new IllegalStateException("Utility class");
    }
    public static NF calculateNormalForm(Relation relation) {
        if (!is2NF(relation)) {
            return NF.FIRST;
        } else if (!is3NF(relation)) {
            return NF.SECOND;
        } else if (!isBCNF(relation)) {
            return NF.THIRD;
        } else {
            return NF.BCNF;
        }
    }
    public static boolean isBCNF (Relation relation) {
        boolean isOK = true;
        /** Trivial oder Superschlüssel */
        for (FunctionalDependency dependency: relation.getFunctionalDependencies()) {
            if (!isTrivial(dependency) && !isSuperKey(relation, dependency)) {
                dependency.setViolates(NF.BCNF);
                isOK = false;
            }
        }
        return isOK;
    }
    public static boolean is3NF (Relation relation) {
        boolean isOK = true;
        /** Trivial, Superschlüssel oder Prime */
        for (FunctionalDependency dependency: relation.getFunctionalDependencies()) {
            if (!isTrivial(dependency) && !areAllPrimeAttributes(relation, dependency)
                    && !isSuperKey(relation, dependency)) {
                dependency.setViolates(NF.THIRD);
                isOK = false;
            }
        }
        return isOK;
    }
    public static boolean is2NF(Relation relation) {
        boolean isOK = true;
        /** None Prime sind nicht von einem Teilschlüssel abhängig */
        Set<String> primeAttributes = getPrimeAttributes(relation);
        for (FunctionalDependency dependency: relation.getFunctionalDependencies()) {
            /** check ob alle abhängigen Attribute prime sind */
            if (primeAttributes.containsAll(Set.of(dependency.getRightSide()))) {
                continue;
            }
            /** Check ob die rechte Seite kein Schlüsselkandidat ist, aber vollständig in einem Schlüsselkandidaten
              * enthalten ist */
            for (Key key: relation.getKeys()) {
                if (!Arrays.equals(dependency.getLeftSide(), key.getLeftSide())
                        && Set.of(key.getLeftSide()).containsAll(Set.of(dependency.getLeftSide()))) {
                    dependency.setViolates(NF.SECOND);
                    isOK = false;
                }
            }

        }
        return isOK;
    }

    public static boolean isTrivial(Dependency dependency) {
        return new HashSet<>(List.of(dependency.getLeftSide())).containsAll(List.of(dependency.getRightSide()));
    }

    private static boolean areAllPrimeAttributes(Relation relation, Dependency dependency) {
        Set<String> primeAttributes = getPrimeAttributes(relation);
        for (String attribute: dependency.getRightSide()) {
            if (!primeAttributes.contains(attribute)) {
                return false;
            }
        }
        return true;
    }
    private static Set<String> getPrimeAttributes (Relation relation) {
        Set<String> primeAttributes = new TreeSet<>();
        for (Key key: relation.getKeys()) {
            primeAttributes.addAll(Arrays.asList(key.getLeftSide()));
        }
        return primeAttributes;
    }

    public static boolean isSuperKey(Relation relation, Dependency dependency) {
        Set<Closure> closures = relation.getClosures();
        Set<String[]> superKeys = new HashSet<>();
        for (Closure closure: closures) {
            if (Arrays.equals(closure.getRightSide(), relation.getAttributes())) {
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
