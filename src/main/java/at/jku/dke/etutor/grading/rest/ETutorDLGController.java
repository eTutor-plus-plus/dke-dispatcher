package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.service.DatalogResourceService;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseBean;
import ch.qos.logback.classic.Logger;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/datalog")
public class ETutorDLGController {
    private final DatalogResourceService service;
    private final Logger logger;

    public ETutorDLGController(DatalogResourceService service){
        this.service = service;
        logger = (Logger) LoggerFactory.getLogger(ETutorDLGController.class);
    }

    @PostMapping("/taskgroup")
    public ResponseEntity<Integer> createTaskGroup(@RequestBody DatalogTaskGroupDTO facts){
        int id;
        try {
            id = service.createTaskGroup(facts);
        } catch (DatalogParseException | ExerciseManagementException e) {
            return ResponseEntity.status(500).body(-1);
        }
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/taskgroup/{id}")
    public ResponseEntity<Void> deleteTaskGroup(@PathVariable int id, @RequestParam(required = false, value = "withTasks", defaultValue = "true") boolean deleteTasks){
       try{
           service.deleteTaskGroup(id, deleteTasks);
           return ResponseEntity.ok().build();
       }catch(ExerciseManagementException e){
           return ResponseEntity.status(500).build();
       }
    }

    @PostMapping("/taskgroup/{id}")
    public ResponseEntity<Void> updateTaskGroup(@PathVariable int id, @RequestBody String newFacts){
        try {
            service.updateTaskGroup(id, newFacts);
        } catch (DatalogParseException | ExerciseManagementException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody DatalogExerciseBean exerciseBean){
        int id;
        try {
            id = service.createExercise(exerciseBean);
        } catch (ExerciseManagementException e) {
            return ResponseEntity.status(500).body(-1);
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping("/exercise/{id}")
    public ResponseEntity<Void> modifyExercise(@RequestBody DatalogExerciseBean exerciseBean, @PathVariable int id){
        try{
            if(service.modifyExercise(id, exerciseBean)) return ResponseEntity.ok().build();
            else return ResponseEntity.status(500).build();
        }catch(ExerciseManagementException e){
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int id){
        try{
            if(service.deleteExercise(id))return ResponseEntity.ok().build();
            else return ResponseEntity.status(500).build();
        }catch(ExerciseManagementException e){
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/exercise/{id}")
    public ResponseEntity<DatalogExerciseBean> getExercise(@PathVariable int id){
        DatalogExerciseBean exercise = null;
        try {
            exercise = service.fetchExercise(id);
            return ResponseEntity.ok(exercise);
        } catch (ExerciseManagementException e) {
            return ResponseEntity.status(500).body(exercise);
        }
    }

    @GetMapping("/taskgroup/{id}")
    public ResponseEntity<String> getFacts(@PathVariable int id){
        String facts = null;
        try {
            facts = service.fetchFacts(id);
        } catch (ExerciseManagementException e) {
            return ResponseEntity.status(500).body(facts);
        }
        return ResponseEntity.ok(facts);
    }

    @GetMapping("/grading/{exercise_id}/{action}/{diagnose_level}")
    public ResponseEntity<GradingDTO> triggerEvaluation(@PathVariable int exercise_id, @PathVariable String action, @PathVariable String diagnose_level){
        try {
            GradingDTO grading = service.getGradingForExercise(exercise_id, action, diagnose_level);
            return ResponseEntity.ok(grading);
        } catch (ExerciseManagementException | InterruptedException e) {
            return null;
        }
    }
}
