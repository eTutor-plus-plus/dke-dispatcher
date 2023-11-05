package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller that handles requests to manage SQL DDL exercises
@RestController
@RequestMapping("/ddl")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorDDLController {
    //region Fields
    private final Logger logger;
    //endregion

    public ETutorDDLController() {
        this.logger = (Logger) LoggerFactory.getLogger(ETutorDDLController.class);
    }

    /**
     * Function to get the current solution for the specified exercise
     * @param id Specifies the exercise
     * @return
     */
    @GetMapping("/exercise/{id}/solution")
    public ResponseEntity<String> getSolution(@PathVariable int id) {
        logger.info("Enter: getSolution(): {}", id);
        return ResponseEntity.ok("");
    }

    // Function to create a new exercise
    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise() {
        logger.info("Enter: createExercise()");
        return ResponseEntity.ok(1);
    }

    /**
     * Function to delete an existing exercise
     * @param id Specifies the exercise
     * @return
     */
    @PostMapping("/exercise/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int id) {
        logger.info("Enter: deleteExercise(): {}", id);
        return ResponseEntity.ok().build();
    }

    /**
     * Function to change the solution for an exercise
     * @param id Specifies the exercise
     * @param newSolution Specifies the new solution for the exercise
     * @return
     */
    @PostMapping("/exercise/{id}/solution")
    public ResponseEntity<Void> updateExerciseSolution(@PathVariable int id, @RequestBody String newSolution) {
        logger.info("Enter: updateExerciseSolution(): {}",id);
        return ResponseEntity.ok().build();
    }
}
