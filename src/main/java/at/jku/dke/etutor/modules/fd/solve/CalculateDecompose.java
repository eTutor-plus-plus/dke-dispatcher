package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.*;
import at.jku.dke.etutor.modules.fd.utilities.Comparators.ArrayComparator;

import java.util.*;


import static at.jku.dke.etutor.modules.fd.entities.Relation.calculateRelation;
import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosures;
import static at.jku.dke.etutor.modules.fd.solve.CalculateKeys.calculateKeys;
import static at.jku.dke.etutor.modules.fd.solve.CalculateMinimalCover.calculateMinimalCover;
import static at.jku.dke.etutor.modules.fd.solve.CalculateNormalForm.*;

public class CalculateDecompose {
    private boolean isLossLessJoinable(Relation relation, Set<Dependency> dependencies) {
        return true;
    }

    private boolean isEquivalent(Relation relation, Relation relation1) {
        for (Dependency dependency: relation1.getFunctionalDependencies()) {
            if (!isFD(relation, dependency)) {
                return false;
            }
        }
        for (Dependency dependency: relation.getFunctionalDependencies()) {
            if (!isFD(relation1, dependency)) {
                return false;
            }
        }
        //TODO Vergleich der beiden
        return true;
    }
    private boolean isFD(Relation relation, Dependency dependency) {
        for (Closure closure: relation.getClosures()) {
            if (Arrays.equals(closure.getLeftSide(), dependency.getLeftSide())
                    && List.of(closure.getRightSide()).containsAll(List.of(dependency.getRightSide()))) {
                return true;
            }
        }
        return false;
    }
        static Set<Dependency> foldRightSides(Set<FunctionalDependency> dependencies) {
        Relation relation = null;
        Set<Dependency> toCombine = new HashSet<>();
        Map<String [], List<String>> combined = new HashMap<>();
        Set<Dependency> result = new TreeSet<>(new CalculateMinimalCover.DependencyComparator());
        /**
         * Veränderte und unveränderte Abhängigkeiten in einem gemeinsamen Set speichern
         */
        for (FunctionalDependency dependency: dependencies) {
            if (dependency.getMinimalCover().isEmpty()) {
                toCombine.add(dependency);
                if (relation == null) {
                    relation = dependency.getRelation();
                }
            } else {
                toCombine.addAll(dependency.getMinimalCover());
            }
        }
        /**
         * Da diese einzeln gespeichert werden, müssen die selben linken Seiten zusammengeführt werden
         */
        for (Dependency dependency: toCombine) {
            if (combined.isEmpty()) {
                combined.put(dependency.getLeftSide(), new ArrayList<>());
                for (String s : dependency.getRightSide()) {
                    combined.get(dependency.getLeftSide()).add(s);
                }
            } else {
                boolean found = false;
                for (String [] key : combined.keySet()) {
                    if (Arrays.equals(key, dependency.getLeftSide())) {
                        for (String s : dependency.getRightSide()) {
                            combined.get(key).add(s);
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    combined.put(dependency.getLeftSide(), new ArrayList<>());
                    for (String s : dependency.getRightSide()) {
                        combined.get(dependency.getLeftSide()).add(s);
                    }
                }
            }
        }
        //TODO durch die Änderung brauch ich die Information welche Funktionale Abhängigkeit BCNF oder 3NF verletzt, aber brauch ich vll gar nicht

        for (String [] key : combined.keySet()) {
            result.add(new FunctionalDependency(key, combined.get(key).toArray(new String[0]), null));
        }

        return result;
    }

    static Set<FunctionalDependency> convert(Set<Dependency> input, Relation relation) {
        Set <FunctionalDependency> returnSet = new HashSet<>();
        for (Dependency item: input) {
            if (item.getClass()==FunctionalDependency.class) {
                returnSet.add((FunctionalDependency) item);
            } else {
                returnSet.add(new FunctionalDependency(item.getLeftSide(), item.getRightSide(), relation));
            }
        }
        return returnSet;
    }


    public static boolean BNCFable (Relation relation) {
        if (relation.getNormalForm() == NF.BCNF) {
            return false;
        }

        List<Relation> decomposed = decomposeKemper(relation);
        return true;
    }

    /**
     * Algorithm DecompositionBCNF
     * This algorithm decomposes a relation schema in BCNF
     * @param input
     * @return set of relational schemas
     */

    public static List<Relation> decomposeKemper(Relation input) {
        List<Relation> decompositions = new ArrayList<>();
        List<Dependency> restDependencies = new ArrayList<>();
        restDependencies.addAll(input.getFunctionalDependencies());
        Relation startRelation = new Relation();
        startRelation.setFunctionalDependencies(convert(foldRightSides(input.getFunctionalDependencies()), input));
        startRelation.setClosures(input.getClosures());
        startRelation.setKeys(input.getKeys());
        startRelation.setMinimalCovers(input.getMinimalCovers());
        startRelation.setNormalForm(input.getNormalForm());
        decompositions.add(startRelation);


        while (!allBCNF(decompositions)) {

            for (Relation relation : decompositions) {
                Relation candidate = null;
                for (FunctionalDependency dependency : relation.getFunctionalDependencies()) {
//                    System.out.println("Trivial: "+isTrivial(dependency));
//                    System.out.println("Superkey: "+isSuperKey(relation, dependency));
//                    System.out.println("Intersect: "+hasIntersection(dependency));
                    if (!isTrivial(dependency) && !isSuperKey(relation, dependency)
                            && !hasIntersection(dependency)
                    ) {
                        candidate = new Relation();
                        candidate.setFunctionalDependencies(Set.of(dependency));
                        candidate.setAttributes(calculateRelation(candidate));
                        candidate.setClosures(calculateClosures(candidate));
                        candidate.setKeys(calculateKeys(candidate));
                        candidate.setMinimalCovers(calculateMinimalCover(candidate));
                        candidate.setNormalForm(calculateNormalForm(candidate));
                        relation.getFunctionalDependencies().remove(dependency);
                        relation.setAttributes(calculateRelation(relation));
                        relation.setClosures(calculateClosures(relation));
                        relation.setKeys(calculateKeys(relation));
                        relation.setMinimalCovers(calculateMinimalCover(relation));
                        relation.setNormalForm(calculateNormalForm(relation));

//                        System.out.println(candidate);
                        break;
                    }
                }
                if (candidate != null) {
                    decompositions.add(candidate);

                    break;
                }
            }
            System.out.println(decompositions);
            System.out.println("BCNF: "+ allBCNF(decompositions));

        }
        return decompositions;
    }

    public static List<Relation> decomposeFolien(Relation input) {
        List<Relation> decompositions = new ArrayList<>();
        Set<String []> attribute = new TreeSet<>(new ArrayComparator());
        while (!allBCNF(decompositions)) {
            /**
             * Gibt es eine bösartige Abhängigkeit X -> Y in F, dann splitten wir
             * R in zwei Teile R 1 und R 2 mit:
             * R 1 = R – Y und R 2 = XY
             */

            for (FunctionalDependency dependency: input.getFunctionalDependencies()) {
                if (dependency.getViolates()!=null) {

                }
            }
        }
        return decompositions;
    }

    private static boolean hasIntersection(Dependency dependency) {
        for (String leftAttribute : dependency.getLeftSide()) {
            for (String rightAttribute: dependency.getRightSide()) {
                if (leftAttribute.equals(rightAttribute)) {
                    System.out.println(leftAttribute+":"+rightAttribute);
                    return true;
                }
            }
        }
        return false;
    }
    private static boolean allBCNF(List<Relation> decompositions) {
        if (decompositions.isEmpty()) {
            return false;
        }
        for (Relation decomposition: decompositions) {
            if (decomposition.getClosures() == null) {
                decomposition.setClosures(calculateClosures(decomposition));
            }
            if(!isBCNF(decomposition)) {
                return false;
            }
        }
        return true;
    }

}
