package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.Exercise;
import at.jku.dke.etutor.modules.fd.services.ExerciseService;
import org.springframework.web.bind.annotation.*;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosure;

@RestController
@RequestMapping(path="/fd")
public class ExerciseController {
    ExerciseService exerciseService;
    ExerciseController(ExerciseService exerciseService) {
        this.exerciseService=exerciseService;
    }
    @PostMapping("/new_exercise")
    public Exercise newExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
        return exercise;
    }

    @GetMapping("/exercise")
    public Exercise getExerciseById(@RequestParam Long id) {
        return exerciseService.getExerciseById(id);
    }
    @GetMapping("/closure")
    public void generateClosure(@RequestParam Long id) {
        calculateClosure(exerciseService.getExerciseById(id));
    }
}
