package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.ExerciseNotValidException;
import at.jku.dke.etutor.grading.service.JDBCResourceService;
import at.jku.dke.etutor.objects.dispatcher.sql.SQLExerciseDTO;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jdbc")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorJDBCController {

    private final Logger logger;
    private final JDBCResourceService resourceService;

    /**
     * The constructor
     * @param resourceService the injected SQLResourceManager providing methods for manipulating data related to the SQL module
     */
    public ETutorJDBCController(JDBCResourceService resourceService){

        this.logger= (Logger) LoggerFactory.getLogger(ETutorJDBCController.class);
        this.resourceService = resourceService;
    }

    @PostMapping("/schema/{schemaName}")
    public ResponseEntity<String> executeFile(@PathVariable String schemaName) throws ApiException {

        return null;
    }

    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<String> dropSchema(@PathVariable String schemaName) throws ApiException {
        logger.info("Enter: dropSchema() {}",schemaName);
        try {
            resourceService.deleteSchemas(decode(schemaName));
            logger.info("Exit: dropSchema() with Status Code 200");
            return ResponseEntity.ok("Schema deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: dropSchema() with Status Code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    @DeleteMapping("/schema/{schemaName}/connection")
    public ResponseEntity<String> deleteConnection(@PathVariable String schemaName) throws ApiException {
        logger.info("Enter: deleteConnection() {}",schemaName);
        try {
            resourceService.deleteConnection(decode(schemaName));
            logger.info("Exit: deleteConnection() with status 200");
            return ResponseEntity.ok("Connection deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: deleteConnection() with status 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody SQLExerciseDTO exerciseDTO, @RequestParam(required = false, defaultValue = "false") boolean checkSyntax) throws ApiException {
        logger.info("Enter: createExercise() {}", "for schema "+exerciseDTO.getSchemaName());
        try {
            int id = resourceService.createExercise(exerciseDTO.getSchemaName(), exerciseDTO.getSolution());

            if(checkSyntax) resourceService.testExercise(id);

            logger.info("Exit: createExercise() {} with Status Code 200", id);
            return ResponseEntity.ok(id);
        } catch (DatabaseException | ExerciseNotValidException e) {
            logger.error("Exit: createExercise() with Status Code 500", e);
            logger.info("Deleting exercise");
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     */
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id) throws ApiException {
        logger.info("Enter: deleteExercise(): {}", id);
        try {
            resourceService.deleteExercise(id);
            logger.info("Exit: deleteExercise() with Status Code 200");
            return ResponseEntity.ok("Exercise deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: deleteExercise() with Status Code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

}
