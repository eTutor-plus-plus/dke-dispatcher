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
    @PostMapping("/new_group")
    public Long newGroup(@RequestBody Relation relation) {
        relationService.createGroup(relation);
        return relation.getId();
    }
    @GetMapping("/next_id")
    public Long getNextId() {return relationService.getNextId();}
    @GetMapping("/exercise")
    public Relation getExerciseById(@RequestParam Long id) {
        return relationService.getRelationById(id);
    }
    @GetMapping("/exercises")
    public List<Relation> getExerciseById() {
        return relationService.getAll();
    }
    @PutMapping("/exercise")
    public Long changeExercise(@RequestBody Relation relation){
        if (relationService.getRelationById(relation.getId()) != null) {
            relationService.createGroup(relation);
            return relation.getId();
        }
        else {
            return -1L;
        }
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
        calculateClosures(relationService.getRelationById(id));
    }

    @GetMapping("/cover")
    public Set<MinimalCover> generateMinimalCover(@RequestParam Long id) {
        return calculateMinimalCover(relationService.getRelationById(id));
    }


}
