package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import at.jku.dke.etutor.modules.fd.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            exerciseRepository.save(exercise);
//            calculateClosure(exercise);
        } catch (Exception e) {

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

//    public boolean calculateClosure(Exercise exercise) {
//        System.out.println(exercise.getId());
//        return true;
//    }

}
