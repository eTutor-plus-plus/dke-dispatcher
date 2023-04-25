package at.jku.dke.etutor.modules.fd.solve;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;

import java.util.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosure;

public class CalculateMinimalCover {
    public static void calculateMinimalCover(Exercise exercise) {
        Exercise temp = new Exercise();
        Set dependencies = new HashSet();
        temp.setDependencies(dependencies);
        temp.setRelation(exercise.getRelation());
        dependencies.addAll(exercise.getDependencies());
        for (Dependency dependency: exercise.getDependencies()) {
            if (dependency.getLeftSide().length>1) {
                Closure original = calculateClosure(exercise, dependency.getLeftSide());
                for (String attribute: dependency.getLeftSide()) {
                    List<String> toReduce = new ArrayList<>(Arrays.asList(dependency.getLeftSide()));
                    toReduce.remove(attribute);
                    Closure modified = calculateClosure(exercise, toReduce.toArray(new String[0]));
                    if (Arrays.equals(original.getRightSide(), modified.getRightSide())) {
                        System.out.println(attribute + " in " + dependency);
                        dependencies.remove(dependency);
                        Dependency replacement = new Dependency(modified.getLeftSide(), dependency.getRightSide());
                        System.out.println(replacement);
                        dependencies.add(replacement);
                    }
                }
            }

//            TreeSet<String[]> attributeCombinations = new TreeSet<>(new CalculateClosure.ArrayComparator());



        }
    }
}
