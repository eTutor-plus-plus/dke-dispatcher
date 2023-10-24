package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.objects.dispatcher.sql.SQLExerciseDTO;
import at.jku.dke.etutor.objects.dispatcher.sql.SQLSchemaInfoDTO;
import at.jku.dke.etutor.objects.dispatcher.sql.SqlDataDefinitionDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.ExerciseNotValidException;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.grading.service.StatementValidationException;
import ch.qos.logback.classic.Logger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;


/**
 * Controller that handles requests to manage SQL exercises
 */
@RestController
@RequestMapping("/sql")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorSQLController {
    private final Logger logger;
    private final SQLResourceService resourceService;


    /**
     * The constructor
      * @param resourceService the injected SQLResourceManager providing methods for manipulating data related to the SQL module
     */
    public ETutorSQLController(SQLResourceService resourceService){

        this.logger= (Logger) LoggerFactory.getLogger(ETutorSQLController.class);
        this.resourceService = resourceService;
    }

    /**
     * Creates a schema, tables and inserts data
     * @param ddl the Object containing the statements
     * @return a {@link ResponseEntity<Integer>}wrapping the diagnose-connection id identifying the schema
     */
    @PostMapping("/schema")
    public ResponseEntity<SQLSchemaInfoDTO> executeDDL(@RequestBody SqlDataDefinitionDTO ddl) throws ApiException {
        logger.info("Enter executeDDL() for schema {} ",ddl.getSchemaName());
        // check if statements are null and abort if so
        if(ddl.getCreateStatements() == null){
            return ResponseEntity.status(412).body(null);
        }

        // If no diagnose/submission statements are provided, the one provided will be used
        var statementsSubmission = ddl.getInsertStatementsSubmission().size() == 1
                && StringUtils.isBlank(ddl.getInsertStatementsSubmission().stream().findFirst().get())
                ?
                ddl.getInsertStatementsDiagnose()
                :
                ddl.getInsertStatementsSubmission();
        var statementsDiagnose = ddl.getInsertStatementsDiagnose().size() == 1
                && StringUtils.isBlank(ddl.getInsertStatementsDiagnose().stream().findFirst().get())
                ?
                ddl.getInsertStatementsSubmission()
                :
                ddl.getInsertStatementsDiagnose();

        var schemaInfo = new SQLSchemaInfoDTO();
        try {
            // delete (if exists) and recreate schema
            resourceService.deleteSchemas(ddl.getSchemaName());

            schemaInfo.setDiagnoseConnectionId(resourceService.createSchemas(ddl.getSchemaName()));

            // create tables and set schema info
            var tableColumns = new HashMap<String, List<String>>();
            for(String stmt : ddl.getCreateStatements()){
                tableColumns.putAll(resourceService.createTables(ddl.getSchemaName(), stmt.trim()));
            }
            schemaInfo.setTableColumns(tableColumns);

            //add data to submission schema
            for(String stmt : statementsSubmission){
                resourceService.insertDataSubmission(ddl.getSchemaName(), stmt.trim());
            }

            //add data to diagnose schema
            for(String stmt : statementsDiagnose){
                resourceService.insertDataDiagnose(ddl.getSchemaName(), stmt.trim());
            }
            logger.info("Exit executeDDL() with Status 200");
            return ResponseEntity.ok(schemaInfo);
        } catch (DatabaseException | StatementValidationException e) {
            logger.error("Exit executeDDL() with Status 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Deletes the diagnose and submission schemas with the specified prefix
     * @param schemaName the schema to be deleted
     */
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



    /**
     * Deletes a connection, the exercises referencing this connection, and the connection id from to table containing the diagnose id of all connections
     * @param schemaName the schemaName
     * @return an ResponseEntity containing a wrapped message
     */
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

    /**
     * Adds an exercise for the specified schema
     * @param  exerciseDTO the {@link SQLExerciseDTO} wrapping the schema-name and solution
     */
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

    /**
     * Updates the solution for an existing exercise
     * @param id the id of the exercise
     * @param newSolution the solution
     * @return a response entity
     */
    @PostMapping("/exercise/{id}/solution")
    public ResponseEntity<String> updateExerciseSolution(@PathVariable int id, @RequestBody String newSolution) throws ApiException {
        logger.info("Enter: updateExerciseSolution(): {}",id);
        try{
            resourceService.updateExerciseSolution(id, newSolution);
            logger.info("Exit: updateExerciseSolution() with Status Code 200");
            return ResponseEntity.ok("Solution updated");
        }catch(DatabaseException e){
            logger.error("Exit: updateExerciseSolution() with Status Code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Returns the persisted solution for a given exercise
     * @param id the id
     * @return the solution
     */
    @GetMapping("/exercise/{id}/solution")
    public ResponseEntity<String> getSolution(@PathVariable int id) throws ApiException {
        logger.info("Enter: getSolution()");
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
     * Returns an HTML-Table for a given table in the database
     * @param tableName the name of the table
     * @param taskGroup optional taskGroup
     * @param exerciseId optional exerciseId
     * @return the HTML-table
     */
    @GetMapping("/table/{tableName}")
    public ResponseEntity<String> getHTMLTable(@PathVariable String tableName, @RequestParam(defaultValue = "") String taskGroup, @RequestParam(defaultValue="") String connId, @RequestParam(defaultValue = "-1") int exerciseId) throws ApiException {
       logger.info("Enter: getHTMLTable() for table {}", tableName);
       String table = "";
       tableName = decode(tableName);
       try{
            if(!connId.isBlank()){
                logger.info("Trying to fetch table according to connection-id {}", connId);
                var id = Integer.parseInt(connId);
                table = resourceService.getHTMLTableByConnId(id, tableName);
            } else if(exerciseId != -1){
                logger.info("Trying to fetch table according to exercise-id {}", exerciseId);
                table = resourceService.getHTMLTableByExerciseID(exerciseId, tableName);
            }else if(!taskGroup.equals("")){
                logger.info("Trying to fetch table according to taskgroup {}", taskGroup);
                table = resourceService.getHTMLTableByTaskGroup(taskGroup, tableName);
            }

            if (table.equals("")){
                table = resourceService.getHTMLTable(tableName);
            }

            if(!table.equals("")){
                logger.info("Exit: getHTMLTable() with status 200");
                return ResponseEntity.ok(table);
            }else{
                logger.info("Exit: getHTMLTable() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find table");
            }
       }catch(DatabaseException e){
           logger.error("Exit: getHTMLTable() with status 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Endpoint for debugging: triggers the evaluation of an existing exercise, using the persisted solution as submission
     * @param exercise_id the id
     * @param action the desired action
     * @param diagnose_level the diagnose level
     * @return {@link Grading} the grading
     */
    @GetMapping("/grading/{exercise_id}/{action}/{diagnose_level}")
    public ResponseEntity<Grading> triggerEvaluation(@PathVariable int exercise_id, @PathVariable String action, @PathVariable String diagnose_level) throws ApiException {
        try {
            Grading grading = resourceService.getGradingForExercise(exercise_id, action, diagnose_level);
            return ResponseEntity.ok(grading);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Decodes a string from an URL that has been encoded
     * @param value the value to decode
     * @return the string
     */
    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return  value;
        }
    }
}
