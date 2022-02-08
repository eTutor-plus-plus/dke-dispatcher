package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.SQLExerciseDTO;
import at.jku.dke.etutor.grading.rest.dto.SQLSchemaInfoDTO;
import at.jku.dke.etutor.grading.rest.dto.SqlDataDefinitionDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.grading.service.StatementValidationException;
import ch.qos.logback.classic.Logger;
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
    public ResponseEntity<SQLSchemaInfoDTO> executeDDL(@RequestBody SqlDataDefinitionDTO ddl){
        logger.info("Enter executeDDL() for schema {} ",ddl.getSchemaName());
        // check if statements are null and abort if so
        if(ddl.getCreateStatements() == null){
            return ResponseEntity.status(500).body(null);
        }

        // If no diagnose/submission statements are provided, the one provided will be used
        var statementsSubmission = ddl.getInsertStatementsSubmission() != null ? ddl.getInsertStatementsSubmission() : ddl.getInsertStatementsDiagnose();
        var statementsDiagnose = ddl.getInsertStatementsDiagnose() != null ? ddl.getInsertStatementsDiagnose() : ddl.getInsertStatementsSubmission();

        var schemaInfo = new SQLSchemaInfoDTO();
        try {
            // delete (if exists) and recreate schema
            resourceService.deleteSchemas(ddl.getSchemaName());
            schemaInfo.setDiagnoseConnectionId(resourceService.createSchemas(ddl.getSchemaName()));

            // create tables
            var tableColumns = new HashMap<String, List<String>>();
            for(String stmt : ddl.getCreateStatements()){
                try {
                    tableColumns.putAll(resourceService.createTables(ddl.getSchemaName(), stmt.trim()));
                } catch (StatementValidationException e) {
                   logger.warn(e.getMessage());
                }
            }
            schemaInfo.setTableColumns(tableColumns);
            //add data to submission schema
            for(String stmt : statementsSubmission){
                try {
                    resourceService.insertDataSubmission(ddl.getSchemaName(), stmt.trim());
                } catch (StatementValidationException e) {
                   logger.warn(e.getMessage());
                }
            }
            //add data to diagnose schema
            for(String stmt : statementsDiagnose){
                try {
                    resourceService.insertDataDiagnose(ddl.getSchemaName(), stmt.trim());
                } catch (StatementValidationException e) {
                   logger.warn(e.getMessage());
                }
            }
            logger.info("Exit executeDDL() with Status 200");
            return ResponseEntity.ok(schemaInfo);
        } catch (DatabaseException e) {
            logger.error("Exit executeDDL() with Status 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(schemaInfo);
        }
    }

    /**
     * Deletes the diagnose and submission schemas with the specified prefix
     * @param schemaName the schema to be deleted
     */
    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<String> dropSchema(@PathVariable String schemaName){
        logger.info("Enter: dropSchema() {}",schemaName);
        try {
            resourceService.deleteSchemas(decode(schemaName));
            logger.info("Exit: dropSchema() with Status Code 200");
            return ResponseEntity.ok("Schema deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: dropSchema() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }

    }

    /**
     * Deletes a connection, the exercises referencing this connection, and the connection id from to table containing the diagnose id of all connections
     * @param schemaName the schemaName
     * @return an ResponseEntity containing a wrapped message
     */
    @DeleteMapping("/schema/{schemaName}/connection")
    public ResponseEntity<String> deleteConnection(@PathVariable String schemaName){
       logger.info("Enter: deleteConnection() {}",schemaName);
        try {
            resourceService.deleteConnection(decode(schemaName));
            logger.info("Exit: deleteConnection() with status 200");
            return ResponseEntity.ok("Connection deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: deleteConnection() with status 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Adds an exercise for the specified schema
     * @param  exerciseDTO the {@link SQLExerciseDTO} wrapping the schema-name and solution
     */
    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody SQLExerciseDTO exerciseDTO) {
        logger.info("Enter: createExercise() {}", "for schema "+exerciseDTO.getSchemaName());
        try {
            int id = resourceService.createExercise(exerciseDTO.getSchemaName(), exerciseDTO.getSolution());
            logger.info("Exit: createExercise() {} with Status Code 200", id);
            return ResponseEntity.ok(id);
        } catch (DatabaseException e) {
            logger.error("Exit: createExercise() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     */
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id)  {
        logger.info("Enter: deleteExercise(): {}", id);
        try {
            resourceService.deleteExercise(id);
            logger.info("Exit: deleteExercise() with Status Code 200");
            return ResponseEntity.ok("Exercise deleted");
        } catch (DatabaseException e) {
            logger.error("Exit: deleteExercise() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Updates the solution for an existing exercise
     * @param id the id of the exercise
     * @param newSolution the solution
     * @return a response entity
     */
    @PostMapping("/exercise/{id}/solution")
    public ResponseEntity<String> updateExerciseSolution(@PathVariable int id, @RequestBody String newSolution){
        logger.info("Enter: updateExerciseSolution(): {}",id);
        try{
            resourceService.updateExerciseSolution(id, newSolution);
            logger.info("Exit: updateExerciseSolution() with Status Code 200");
            return ResponseEntity.ok("Solution updated");
        }catch(DatabaseException e){
            logger.error("Exit: updateExerciseSolution() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Returns the persisted solution for a given exercise
     * @param id the id
     * @return the solution
     */
    @GetMapping("/exercise/{id}/solution")
    public ResponseEntity<String> getSolution(@PathVariable int id){
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
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
    public ResponseEntity<String> getHTMLTable(@PathVariable String tableName, @RequestParam(defaultValue = "") String taskGroup, @RequestParam(defaultValue="") String connId, @RequestParam(defaultValue = "-1") int exerciseId){
       logger.info("Enter: getHTMLTable() for table {}", tableName);
       String table = "";
       tableName = decode(tableName);
       try{
            if(!connId.isBlank()){
                var id = Integer.parseInt(connId);
                table = resourceService.getHTMLTableByConnId(id, tableName);
            } else if(exerciseId != -1){
                table = resourceService.getHTMLTableByExerciseID(exerciseId, tableName);
            }else if(!taskGroup.equals("")){
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Endpoint for debugging: triggers the evaluation of an existing exercise, using the persisted solution as submission
     * @param exercise_id the id
     * @param action the desired action
     * @param diagnose_level the diagnose level
     * @return {@link GradingDTO} the grading
     */
    @GetMapping("/grading/{exercise_id}/{action}/{diagnose_level}")
    public ResponseEntity<GradingDTO> triggerEvaluation(@PathVariable int exercise_id, @PathVariable String action, @PathVariable String diagnose_level){
        try {
            GradingDTO grading = resourceService.getGradingForExercise(exercise_id, action, diagnose_level);
            return ResponseEntity.ok(grading);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
