package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorGradingConstants;
import at.jku.dke.etutor.grading.rest.dto.DataDefinitionDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.SQLResourceManager;
import ch.qos.logback.classic.Logger;
import io.swagger.models.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;


/**
 * Controller that handles requests to manage SQL exercises
 */
@RestController
@RequestMapping("/sql")
public class ETutorSQLController {
    private final Logger logger;
    private final SQLResourceManager resourceManager;


    /**
     * The constructor
      * @param resourceManager the injected SQLResourceManager providing methods for manipulating data related to the SQL module
     */
    public ETutorSQLController(SQLResourceManager resourceManager){

        this.logger= (Logger) LoggerFactory.getLogger(ETutorSQLController.class);
        this.resourceManager=resourceManager;
    }

    /**
     * Creates a schema, tables and inserts data
     * @param ddl the Object containing the statements
     * @return ResponseEntity
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/schema")
    public ResponseEntity<String> executeDDL(@RequestBody DataDefinitionDTO ddl){
        logger.info("Enter executeDDL() for schema {} ",ddl.getSchemaName());
        try {
            resourceManager.deleteSchemas(ddl.getSchemaName());
            resourceManager.createSchemas(ddl.getSchemaName());
            for(String stmt : ddl.getCreateStatements()){
                resourceManager.createTables(ddl.getSchemaName(), stmt.trim());
            }
            for(String stmt : ddl.getInsertStatementsSubmission()){
                resourceManager.insertDataSubmission(ddl.getSchemaName(), stmt.trim());
            }
            for(String stmt : ddl.getInsertStatementsDiagnose()){
                resourceManager.insertDataDiagnose(ddl.getSchemaName(), stmt.trim());
            }
            logger.info("Exit executeDDL() with Status 200");
            return ResponseEntity.ok("DDL Executed");
        } catch (DatabaseException e) {
            logger.info("Exit executeDDL() with Status 500");
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Creates two schemas with the specified name and the submission-suffix and diagnose-suffix respectively
     * @param schemaName the schema to be created
     * @return ResponseEntity
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PutMapping("/schema/{schemaName}")
    public ResponseEntity<String> createSchema(@PathVariable String schemaName){
        logger.info("Enter: createSchema() {}",schemaName);
        try {
           resourceManager.createSchemas(schemaName);
            logger.info("Exit: createSchema() with Status Code 200");
            return ResponseEntity.ok("Schema created");
        } catch (DatabaseException  e) {
            logger.info("Exit: createSchema() with Status Code 500 ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Deletes the diagnose and submission schemas with the specified prefix
     * @param schemaName the schema to be deleted
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<String> dropSchema(@PathVariable String schemaName){
        logger.info("Enter: dropSchema() {}",schemaName);
        try {
            resourceManager.deleteSchemas(schemaName);
            logger.info("Exit: dropSchema() with Status Code 200");
            return ResponseEntity.ok("Schema deleted");
        } catch (DatabaseException e) {
            logger.info("Exit: dropSchema() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }

    }

    /**
     * Deletes a connection
     * @param schemaName the schemaName
     * @return an ResponseEntity containing a wrapped message
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/schema/{schemaName}/connection")
    public ResponseEntity<String> deleteConnection(@PathVariable String schemaName){
       logger.info("Enter: deleteConnection() {}",schemaName);
        try {
            resourceManager.deleteConnection(schemaName);
            logger.info("Exit: deleteConnection() with status 200");
            return ResponseEntity.ok("Connection deleted");
        } catch (DatabaseException e) {
            logger.info("Exit: deleteConnection() with status 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
    /**
     * Creates a table in the submission- and diagnose version of the  specified schema
     * @param schemaName the name of the schema where a table has to be created
     * @param queries the create table query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PutMapping("/schema/{schemaName}/table")
    public ResponseEntity<String> createTables(@PathVariable String schemaName, @RequestBody String queries){
        logger.info(MessageFormat.format("Enter: createTable() {}", schemaName));
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.createTables(schemaName, s);
            }
            logger.info("Exit: createTable() with Status Code 200");
            return ResponseEntity.ok("Tables Created");
        } catch (DatabaseException e) {
            logger.info("Exit: createTable() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
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
            logger.info("Exit: dropTable() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Inserts data in the specified table in the submission-version of the specified schema
     * @param schemaName the name of the schema
     * @param queries the insert query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/schema/{schemaName}/submission/data")
    public ResponseEntity<String> insertDataSubmission(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter: insertDataSubmission()");
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.insertDataSubmission(schemaName, s);
            }
            logger.info("Exit: insertDataSubmission() with Status Code 200");
            return ResponseEntity.ok("Data inserted");
        } catch (DatabaseException e) {
            logger.info("Exit: insertDataSubmission() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
    /**
     * Inserts data in the specified table in the diagnose-version of the specified schema
     * @param schemaName the name of the schame
     * @param queries the insert query
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/schema/{schemaName}/diagnose/data")
    public ResponseEntity<String> insertDataDiagnose(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter: insertDataDiagnose()");
        try {
            String[] queryArray = queries.trim().split(";");
            for(String s: queryArray){
                resourceManager.insertDataDiagnose(schemaName, s);
            }
            logger.info("Exit: insertDataDiagnose() with Status Code 200");
            return ResponseEntity.ok("Insert completed");
        } catch (DatabaseException e) {
            logger.info("Exit: insertDataDiagnose() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
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
    public ResponseEntity<String> createExercise(@PathVariable int id, @PathVariable String schemaName, @RequestBody String solution) {
        logger.info("Enter: createExercise() {}", id);
        try {
            resourceManager.createExercise(id, schemaName, solution);
            logger.info("Exit: createExercise() {} with Status Code 200", id);
            return ResponseEntity.ok("Exercise created");
        } catch (DatabaseException e) {
            logger.error(e.getMessage(), e);
            logger.info("Exit: createExercise() {} with Status Code 500", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id)  {
        logger.info("Enter: deleteExercise(): {}", id);
        try {
            resourceManager.deleteExercise(id);
            logger.info("Exit: deleteExercise() with Status Code 200");
            return ResponseEntity.ok("Exercise deleted");
        } catch (DatabaseException e) {
            logger.info("Exit: deleteExercise() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Updates the solution for an existing exercise
     * @param id the id of the exercise
     * @param newSolution the solution
     * @return a response entity
     */
    @CrossOrigin(ETutorGradingConstants.CORS_POLICY)
    @PostMapping("/exercise/{id}/solution")
    public ResponseEntity<String> updateExerciseSolution(@PathVariable int id, @RequestBody String newSolution){
        logger.info("Enter: updateExerciseSolution(): {}",id);
        try{
            resourceManager.updateExerciseSolution(id, newSolution);
            logger.info("Exit: updateExerciseSolution() with Status Code 200");
            return ResponseEntity.ok("Solution updated");
        }catch(DatabaseException e){
            logger.info("Exit: updateExerciseSolution() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Fetches an available exercise id
     * @return the exercise id
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @GetMapping("/exercise/reservation")
    public ResponseEntity<String> reserveExerciseID(){
        logger.info("Enter: reserveExercise() ");
        try {
            int id = resourceManager.reserveExerciseID();
            logger.info("Exit: reserveExercise() with status 200 and id {}",id);
            return ResponseEntity.ok(""+id);
        } catch (DatabaseException e) {
            logger.info("Exit: reserveExercise() with Status Code 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }

    /**
     * Returns the persisted solution for a given exercise
     * @param id the id
     * @return the solution
     */
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @GetMapping("/exercise/{id}/solution")
    public ResponseEntity<String> getSolution(@PathVariable int id){
        logger.info("Enter: getSolution()");
        try{
            String solution = resourceManager.getSolution(id);
            if(!solution.equals("")){
                logger.info("Exit: getSolution() with status 200");
                return ResponseEntity.ok(solution);
            }else{
                logger.info("Exit: getSolution() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find solution for exercise "+id);
            }
        }catch(DatabaseException e){
            logger.info("Exit: getSolution() with status 500", e);
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
    @CrossOrigin(origins= ETutorGradingConstants.CORS_POLICY)
    @GetMapping("/table/{tableName}")
    public ResponseEntity<String> getHTMLTable(@PathVariable String tableName, @RequestParam(defaultValue = "") String taskGroup, @RequestParam(defaultValue = "-1") int exerciseId){
       logger.info("Enter: getHTMLTable() for table {}", tableName);
       String table = "";
       try{
            if(exerciseId != -1){
                table = resourceManager.getHTMLTableByExerciseID(exerciseId, tableName);
            }else if(!taskGroup.equals("")){
                // To be implemented
            }else{
                table = resourceManager.getHTMLTable(tableName, taskGroup);
            }

            if(!table.equals("")){
                logger.info("Exit: getHTMLTable() with status 200");
                return ResponseEntity.ok(table);
            }else{
                logger.info("Exit: getHTMLTable() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find table");
            }
       }catch(DatabaseException e){
           logger.info("Exit: getHTMLTable() with status 500", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
    }
}
