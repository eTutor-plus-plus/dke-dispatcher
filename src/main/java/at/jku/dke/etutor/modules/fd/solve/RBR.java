package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
import at.jku.dke.etutor.modules.fd.entities.Relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static at.jku.dke.etutor.modules.fd.solve.CalculateDecompose.foldRightSides;
import static at.jku.dke.etutor.modules.fd.solve.CalculateDecompose.convert;
import static at.jku.dke.etutor.modules.fd.solve.CalculateNormalForm.isTrivial;


public class RBR {
    private RBR() {
        throw new IllegalStateException("Utility class");
    }
    /** This algorithm computes an embedded cover of FDs for F+(x) */
    public static Set<FunctionalDependency> reductionByResolution(Relation relation, Set<FunctionalDependency> inputDependencies,
                                                                  String [] subschema) {
        Set<FunctionalDependency> resultDependencies = new HashSet<>(inputDependencies);
        ArrayList<String> attributes = difference(relation.getAttributes(), subschema);
        while (!attributes.isEmpty()) {
            String attribute = attributes.get(0);
            attributes.remove(attribute);
            resultDependencies.addAll(resolvent(resultDependencies,new String []{attribute}));
        }
        return convert(foldRightSides(resultDependencies), relation);
    }

    static ArrayList<String> difference (String [] s1, String [] s2) {
        ArrayList<String> result = new ArrayList<>();
        for (String item: s1) {
            result.add(item);
        }
        for (String item1: s1) {
            for (String item2: s2) {
                if(item1.equals(item2)) {
                    result.remove(item1);
                }
            }
        }
        return result;
    }

    static Set<FunctionalDependency> resolvent (Set<FunctionalDependency> dependencies, String [] attribute) {
        Set<FunctionalDependency> directDependencies = new HashSet<>();
        Set<FunctionalDependency> result = new HashSet<>();
        for (FunctionalDependency dependency: dependencies) {
            if (!isTrivial(dependency) && Arrays.equals(dependency.getLeftSide(), attribute)) {
                directDependencies.add(dependency);
            }
        }
        for (FunctionalDependency dependency: dependencies) {
            for (FunctionalDependency directDependency : directDependencies) {
                if (!isTrivial(dependency) && Arrays.equals(dependency.getLeftSide(), directDependency.getRightSide())) {
                    result.add(new FunctionalDependency(dependency.getLeftSide(), dependency.getRightSide(), dependency.getRelation()));
                }
            }
        }
        return result;
    }

}
