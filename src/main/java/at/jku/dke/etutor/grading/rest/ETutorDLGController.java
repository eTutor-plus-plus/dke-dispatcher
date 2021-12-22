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

/**
 * Controller for handling resources of the datalog module
 */
@RestController
@RequestMapping("/datalog")
public class ETutorDLGController {
    private final DatalogResourceService service;
    private final Logger logger;

    /**
     * Initializes the controller
     * @param service the service for handling the resources
     */
    public ETutorDLGController(DatalogResourceService service){
        this.service = service;
        logger = (Logger) LoggerFactory.getLogger(ETutorDLGController.class);
    }

    /**
     * Creates a task group by persisting the facts
     * @param facts the facts
     * @return the id of the facts
     */
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

    /**
     * Deletes a task group
     * @param id the id of the task group
     * @param deleteTasks flag indicating if exercises that reference this group should be deleted as well (default=true)
     * @return a {@link ResponseEntity} indicating if deleting has been successful
     */
    @DeleteMapping("/taskgroup/{id}")
    public ResponseEntity<Void> deleteTaskGroup(@PathVariable int id, @RequestParam(required = false, value = "withTasks", defaultValue = "true") boolean deleteTasks){
       try{
           service.deleteTaskGroup(id, deleteTasks);
           return ResponseEntity.ok().build();
       }catch(ExerciseManagementException e){
           return ResponseEntity.status(500).build();
       }
    }

    /**
     * Updates the facts of a task group
     * @param id the id of the facts
     * @param newFacts the new facts
     * @return a {@link ResponseEntity} indicating if updating has been successful
     */
    @PostMapping("/taskgroup/{id}")
    public ResponseEntity<Void> updateTaskGroup(@PathVariable int id, @RequestBody String newFacts){
        try {
            service.updateTaskGroup(id, newFacts);
        } catch (DatalogParseException | ExerciseManagementException e) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a datalog exercise
     * @param exerciseBean an {@link DatalogExerciseBean} wrapping the required information
     * @return the id of the created exercise
     */
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

    /**
     * Updates an exercise
     * @param exerciseBean a {@link DatalogExerciseBean} wrapping the required attributes
     * @param id the id of the exercise
     * @return a {@link ResponseEntity} indicating if updating has been successful
     */
    @PostMapping("/exercise/{id}")
    public ResponseEntity<Void> modifyExercise(@RequestBody DatalogExerciseBean exerciseBean, @PathVariable int id){
        try{
            if(service.modifyExercise(id, exerciseBean)) return ResponseEntity.ok().build();
            else return ResponseEntity.status(500).build();
        }catch(ExerciseManagementException e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     * @return a {@link ResponseEntity} indicating if deletion has been successful
     */
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int id){
        try{
            if(service.deleteExercise(id))return ResponseEntity.ok().build();
            else return ResponseEntity.status(500).build();
        }catch(ExerciseManagementException e){
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Returns an exercise
     * @param id the id of the exercise
     * @return a {@link DatalogExerciseBean} representing the exercise
     */
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

    /**
     * Returns the facts for a task group
     * @param id the id if the group
     * @return a String with the facts
     */
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

    /**
     * Debug method that triggers evaluation of an exercise, using the solution as submission
     * @param exercise_id the id of the exercise
     * @param action the action
     * @param diagnose_level the diagnose level
     * @return the grading {@link GradingDTO}
     */
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
