package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorGradingConstants;
import at.jku.dke.etutor.grading.rest.dto.HTTPResponseDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.SQLResourceManager;
import io.swagger.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

/**
 * Controller that handles requests regarding SQL exercise management
 */
@RestController
@RequestMapping("/sql")
public class ETutorSQLController {
    private Logger logger;
    private SQLResourceManager resourceManager;

    public ETutorSQLController(SQLResourceManager resourceManager){
        this.logger= Logger.getLogger("at.jku.dke.etutor.sqlcontroller");
        this.resourceManager=resourceManager;
    }
    /**
     * Creates to schemas with the specified name and the submission-suffix and diagnose-suffix respectively
     * @param schemaName the schema to be created
     * @return
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PutMapping("/schema/{schemaName}")
    public ResponseEntity<HTTPResponseDTO> createSchema(@PathVariable String schemaName){
        logger.info("Enter: createSchema()"+schemaName);
        try {
            new SQLResourceManager().createSchemas(schemaName);
            logger.info("Exit: createSchema() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Schema created"));
        } catch (DatabaseException | ClassNotFoundException e) {
            logger.info("Exit: createSchema() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not create Schema"));
        }
    }

    /**
     * Deletes the diagnose and submission schemas with the specified prefix
     * @param schemaName the schema to be deleted
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<HTTPResponseDTO> dropSchema(@PathVariable String schemaName){
        logger.info("Enter: dropSchema()"+schemaName);
        try {
            resourceManager.deleteSchemas(schemaName);
            logger.info("Exit: dropSchema() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Schema deleted"));
        } catch (DatabaseException e) {
            logger.info("Exit: dropSchema() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not delete schema"));
        }

    }

    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/schema/{schemaName}/connection")
    public ResponseEntity<HTTPResponseDTO> deleteConnection(@PathVariable String schemaName){
       logger.info("Enter: deleteConnection() "+schemaName);
        try {
            resourceManager.deleteConnection(schemaName);
            logger.info("Exit: deleteConnection() with status 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Connection deleted"));
        } catch (DatabaseException e) {
            e.printStackTrace();
            logger.info("Exit: deleteConnection() with status 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not delete connection"));
        }
    }
    /**
     * Creates a table in the submission- and diagnose version of the  specified schema
     * @param schemaName the name of the schema where a table has to be created
     * @param queries the create table query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PutMapping("/schema/{schemaName}/table")
    public ResponseEntity<HTTPResponseDTO> createTables(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter: createTable() "+schemaName);
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.createTables(schemaName, s);
            }
            logger.info("Exit: createTable() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Tables Created"));
        } catch (DatabaseException e) {
            logger.info("Exit: createTable() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not create tables"));
        }
    }

    /**
     * Deletes a table in the specified schmema
     * @param schemaName the name of the schema where a table has to be deleted
     * @param tableName the name of the table to be deleted
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/schema/{schemaName}/table/{tableName}")
    public ResponseEntity<String> dropTable(@PathVariable String schemaName, @PathVariable String tableName){
        logger.info("Enter: dropTable() "+tableName);
        try {
            resourceManager.deleteTables(schemaName, tableName);
            logger.info("Exit: dropTable() with Status Code 200");
            return ResponseEntity.ok("Table deleted");
        } catch (DatabaseException e) {
            logger.info("Exit: dropTable() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete table");
        }
    }

    /**
     * Inserts data in the specified table in the submission-version of the specified schema
     * @param schemaName the name of the schema
     * @param queries the insert query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/schema/{schemaName}/submission/data")
    public ResponseEntity<HTTPResponseDTO> insertDataSubmission(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter: insertDataSubmission()");
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.insertDataSubmission(schemaName, s);
            }
            logger.info("Exit: insertDataSubmission() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Data inserted"));
        } catch (DatabaseException e) {
            logger.info("Exit: insertDataSubmission() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not insert data"));
        }
    }
    /**
     * Inserts data in the specified table in the diagnose-version of the specified schema
     * @param schemaName the name of the schame
     * @param queries the insert query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/schema/{schemaName}/diagnose/data")
    public ResponseEntity<HTTPResponseDTO> insertDataDiagnose(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter: insertDataDiagnose()");
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.insertDataDiagnose(schemaName, s);
            }
            logger.info("Exit: insertDataDiagnose() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Insert completed"));
        } catch (DatabaseException e) {
            logger.info("Exit: insertDataDiagnose() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not insert data"));
        }
    }

    /**
     * Adds an exercise for the specified schema
     * @param schemaName the name of the schema for which the exercise has to be created
     * @param id the id of the exercise to be created
     * @param solution the solution for the exercise
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PutMapping("/exercise/{schemaName}/{id}")
    public ResponseEntity<HTTPResponseDTO> createExercise(@PathVariable int id, @PathVariable String schemaName, @RequestBody String solution) {
        logger.info("Enter: createExercise() "+id);
        try {
            resourceManager.createExercise(id, schemaName, solution);
            logger.info("Exit: createExercise() "+id+" with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Exercise created"));
        } catch (DatabaseException e) {
            logger.info("Exit: createExercise() "+id+" with Status Code 500");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not create exercise"));
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<HTTPResponseDTO> deleteExercise(@PathVariable int id)  {
        logger.info("Enter: deleteExercise(): "+id);
        try {
            resourceManager.deleteExercise(id);
            logger.info("Exit: deleteExercise() with Status Code 200");
            return ResponseEntity.ok(new HTTPResponseDTO("Exercise deleted"));
        } catch (DatabaseException e) {
            e.printStackTrace();
            logger.info("Exit: deleteExercise() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not delete exercise"));
        }
    }

    /**
     * Fetches an available exercise id
     * @return the exercise id
     */

    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @GetMapping("/exercise/reservation")
    public ResponseEntity<HTTPResponseDTO> reserveExerciseID(){
        logger.info("Enter: reserveExercise() ");
        try {
            int id = resourceManager.reserveExerciseID();
            logger.info("Exit: reserveExercise() with status 200 and id "+id);
            return ResponseEntity.ok(new HTTPResponseDTO(""+id));
        } catch (DatabaseException e) {
            e.printStackTrace();
            logger.info("Exit: reserveExercise() with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HTTPResponseDTO("Could not reserve exercise"));
        }
    }
}
