package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.entities.MinimalCover;
import at.jku.dke.etutor.modules.fd.services.RelationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static at.jku.dke.etutor.modules.fd.solve.CalculateClosure.calculateClosures;
import static at.jku.dke.etutor.modules.fd.solve.CalculateMinimalCover.calculateMinimalCover;

@RestController
@RequestMapping(path="/fd")
public class RelationController {
    RelationService relationService;
    RelationController(RelationService relationService) {
        this.relationService = relationService;
    }
    @PostMapping("/new_exercise")
    public Long newExercise(@RequestBody Relation relation) {
//        System.out.println(relation);
        relationService.createExercise(relation);
        return relation.getId();
    }
    @GetMapping("/next_id")
    public Long getNextId() {return relationService.getNextId();}
    @GetMapping("/exercise")
    public Relation getExerciseById(@RequestParam Long id) {
        return relationService.getExerciseById(id);
    }
    @GetMapping("/exercises")
    public List<Relation> getExerciseById() {
        return relationService.getAll();
    }
    @DeleteMapping("/exercise")
    public void deleteExerciseById(@RequestParam Long id) {
        relationService.deleteExerciseById(id);
    }
    @DeleteMapping("/exercises")
    public void deleteExerciseById() {
        relationService.deleteAll();
    }
    @GetMapping("/closure")
    public void generateClosure(@RequestParam Long id) {
        calculateClosures(relationService.getExerciseById(id));
    }

    @GetMapping("/cover")
    public Set<MinimalCover> generateMinimalCover(@RequestParam Long id) {
        return calculateMinimalCover(relationService.getExerciseById(id));
    }


}
