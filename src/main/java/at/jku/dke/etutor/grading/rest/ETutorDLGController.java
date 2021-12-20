package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
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
    public ResponseEntity<String> deleteTaskGroup(@PathVariable int id, @RequestParam(required = false, value = "withTasks", defaultValue = "true") boolean deleteTasks){
       try{
           service.deleteTaskGroup(id, deleteTasks);
           return ResponseEntity.ok("Deleted task group with id "+ id);
       }catch(ExerciseManagementException e){
           return ResponseEntity.status(500).body("Could not delete task group with id "+ id);
       }
    }

    @PostMapping("/taskgroup/{id}")
    public void updateTaskGroup(@PathVariable int id, @RequestBody String newFacts){
        try {
            service.updateTaskGroup(id, newFacts);
        } catch (DatalogParseException | ExerciseManagementException e) {
            e.printStackTrace();
        }
    }


    public void createExercise(int factsId){
        // exercise anlegen
        // exercise id zurückgeben
    }

    public void modifyExercise(DatalogExerciseBean exerciseBean){

    }

    public void deleteExercise(int id){

    }


}
