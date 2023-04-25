package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import at.jku.dke.etutor.modules.fd.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.TreeSet;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosures;

@Service
public class ExerciseService {
    ExerciseRepository exerciseRepository;
    DependencyRepository dependencyRepository;

    ExerciseService (ExerciseRepository exerciseRepository, DependencyRepository dependencyRepository) {
        this.exerciseRepository = exerciseRepository;
        this.dependencyRepository = dependencyRepository;
    }

    public boolean createExercise(Exercise exercise) {
        try {
            if (exercise.getRelation().length==0) {
                exercise.setRelation(calculateRelation(exercise));
            }
            exercise.setClosures(calculateClosures(exercise));
            exerciseRepository.save(exercise);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Exercise getExerciseById(long id) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        Exercise exercise;
        if (optionalExercise.isPresent()) {
            exercise = optionalExercise.get();
            return exercise;
        }
        return null;
    }

    private String [] calculateRelation(Exercise exercise) {
        TreeSet<String> result = new TreeSet<>();
        for (Dependency dependency: exercise.getDependencies()) {
            for (String left: dependency.getLeftSide()) {
                result.add(left);
            }
            for (String right: dependency.getRightSide()) {
                result.add(right);
            }
        }
        return result.toArray(new String[0]);
    }

}
