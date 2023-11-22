package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.service.DDLResourceService;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.objects.dispatcher.ddl.DDLExerciseDTO;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller that handles requests to manage SQL DDL exercises
@RestController
@RequestMapping("/ddl")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorDDLController {
    //region Fields
    private final Logger logger;
    private final DDLResourceService resourceService;
    //endregion

    public ETutorDDLController(DDLResourceService resourceService) {
        this.logger = (Logger) LoggerFactory.getLogger(ETutorDDLController.class);
        this.resourceService = resourceService;
    }

    /**
     * Function to get the current solution for the specified exercise
     * @param id Specifies the exercise
     * @return
     */
    @GetMapping("/exercise/{id}/solution")
    public ResponseEntity<String> getSolution(@PathVariable int id) throws ApiException {
        logger.info("Enter: getSolution(): {}", id);
        try{
            String solution = resourceService.getSolution(id);
            if(!solution.equals("")){
                logger.info("Exit: getSolution() with status 200");
                return ResponseEntity.ok(solution);
            }else{
                logger.info("Exit: getSolution() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find solution for exercise "+id);
            }
        }catch(DatabaseException e){
            logger.error("Exit: getSolution() with status 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Function to create a new exercise
     * @param exerciseDTO Specifies the {@link DDLExerciseDTO} wrapping the schema-name and solution
     * @return
     * @throws ApiException
     */
    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody DDLExerciseDTO exerciseDTO) throws ApiException {
        logger.info("Enter: createExercise()");
        try {
            int id = resourceService.createExercise(exerciseDTO);

            logger.info("Exit: createExercise() {} with Status Code 200", id);
            return ResponseEntity.ok(id);
        } catch (DatabaseException e) {
            logger.error("Exit: createExercise() with Status Code 500", e);
            logger.info("Deleting exercise");
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Function to delete an existing exercise
     * @param id Specifies the exercise
     * @return
     */
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable int id) throws ApiException {
        logger.info("Enter: deleteExercise(): {}", id);
        try {
            resourceService.deleteExercise(id);
            logger.info("Exit: deleteExercise() with Status Code 200");
            return ResponseEntity.ok().build();
        } catch (DatabaseException e) {
            logger.error("Exit: deleteExercise() with Status Code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Function to change the solution for an exercise
     * @param id Specifies the exercise
     * @param exerciseDTO Specifies the new exercise element
     * @return
     */
    @PostMapping("/exercise/{id}")
    public ResponseEntity<Void> updateExerciseSolution(@PathVariable int id, @RequestBody DDLExerciseDTO exerciseDTO) throws ApiException {
        logger.info("Enter: updateExerciseSolution(): {}",id);
        try{
            resourceService.updateExercise(id, exerciseDTO);
            logger.info("Exit: updateExerciseSolution() with Status Code 200");
            return ResponseEntity.ok().build();
        }catch(DatabaseException e){
            logger.error("Exit: updateExerciseSolution() with Status Code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }
}
