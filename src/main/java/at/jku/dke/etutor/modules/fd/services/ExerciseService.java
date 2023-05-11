package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.entities.FunctionalDependency;
import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import at.jku.dke.etutor.modules.fd.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosures;
import static at.jku.dke.etutor.modules.fd.solve.CalculateKeys.calculateKeys;
import static at.jku.dke.etutor.modules.fd.solve.CalculateMinimalCover.calculateMinimalCover;
import static at.jku.dke.etutor.modules.fd.solve.CalculateNormalForm.calculateNormalForm;

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
            exercise.setKeys(calculateKeys(exercise));
            exercise.setMinimalCovers(calculateMinimalCover(exercise));
            exercise.setNormalForm(calculateNormalForm(exercise));
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
    public List<Exercise> getAll() {
        return exerciseRepository.findAll();
    }
    public void deleteExerciseById(Long id){
        exerciseRepository.deleteById(id);
    }
    public void deleteAll() {
        exerciseRepository.deleteAll();
    }

    private String [] calculateRelation(Exercise exercise) {
        TreeSet<String> result = new TreeSet<>();
        for (Dependency functionalDependency : exercise.getDependencies()) {
            for (String left: functionalDependency.getLeftSide()) {
                result.add(left);
            }
            for (String right: functionalDependency.getRightSide()) {
                result.add(right);
            }
        }
        return result.toArray(new String[0]);
    }

}
