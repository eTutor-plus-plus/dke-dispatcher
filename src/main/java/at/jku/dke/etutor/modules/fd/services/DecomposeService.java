package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.repositories.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateNormalForm.*;

@Service
public class DecomposeService {
    ExerciseRepository exerciseRepository;
    DecomposeService(ExerciseRepository exerciseRepository) {this.exerciseRepository=exerciseRepository;}

    public List<Map<String,String>> getIsBCNF() {
        List<Exercise> exercises = exerciseRepository.findAll();
        List<Map<String,String>> returnList = new ArrayList<>();
        for (Exercise exercise: exercises) {
            Map<String, String> map = new HashMap<>();
            map.put("id", exercise.getId().toString());
            map.put("BCNF", Boolean.toString(isBCNF(exercise)));
            map.put("3NF", Boolean.toString(is3NF(exercise)));
            map.put("2NF", Boolean.toString(is2NF(exercise)));
            returnList.add(map);
        }
        return returnList;
    }

    public Map<String, String> getIsBCNF(Long id) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        Exercise exercise;
        Map<String, String> map = new HashMap<>();
        if (optionalExercise.isPresent()) {
            exercise = optionalExercise.get();
            map.put("id", exercise.getId().toString());
            map.put("BCNF", Boolean.toString(isBCNF(exercise)));
            map.put("3NF", Boolean.toString(is3NF(exercise)));
        }
        return map;
    }
}
