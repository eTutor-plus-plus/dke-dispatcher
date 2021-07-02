package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.rest.dto.SchemaDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.SQLResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.logging.Logger;

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
    @CrossOrigin(origins="*")
    @PutMapping("/schema/{schemaName}")
    public ResponseEntity<String> createSchema(@PathVariable String schemaName){
        logger.info("Enter createSchema/"+schemaName);
        try {
            new SQLResourceManager().createSchemas(schemaName);
            logger.info("Exit createSchema with Status Code 200");
            return ResponseEntity.ok("Schema created");
        } catch (DatabaseException | ClassNotFoundException e) {
            logger.info("Exit createSchema with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create schema");
        }
    }

    /**
     * Deletes the diagnose and submission schemas with the specified prefix
     * @param schemaName the schema to be deleted
     */
    @CrossOrigin(origins="*")
    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<String> deleteSchema(@PathVariable String schemaName){
        logger.info("Enter deleteSchema/"+schemaName);
        try {
            resourceManager.deleteSchemas(schemaName);
            logger.info("Exit deleteSchema with Status Code 200");
            return ResponseEntity.ok("Schema deleted");
        } catch (DatabaseException e) {
            logger.info("Exit createSchema with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete schema");
        }

    }


    /**
     * Creates a table in the submission- and diagnose version of the  specified schema
     * @param schemaName the name of the schema where a table has to be created
     * @param query the create table query
     */
    @CrossOrigin(origins="*")
    @PutMapping("/schema/{schemaName}/table")
    public ResponseEntity<String> createTables(@PathVariable String schemaName, @RequestBody String queries){
        logger.info("Enter createTable/"+schemaName);
        try {
            String[] queryArray = queries.split(";");
            for(String s: queryArray){
                resourceManager.createTables(schemaName, s);
            }
            logger.info("Exit createTable with Status Code 200");
            return ResponseEntity.ok("Table created");
        } catch (DatabaseException e) {
            logger.info("Exit createTable with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create table");
        }
    }

    /**
     * Deletes a table in the specified schmema
     * @param schemaName the name of the schema where a table has to be deleted
     * @param tableName the name of the table to be deleted
     */
    @CrossOrigin(origins="*")
    @DeleteMapping("/schema/{schemaName}/table/{tableName}")
    public ResponseEntity<String> deleteTable(@PathVariable String schemaName, @PathVariable String tableName){
        logger.info("Enter "+schemaName+"/deleteTable/"+tableName);
        try {
            resourceManager.deleteTables(schemaName, tableName);
            logger.info("Exit deleteTable with Status Code 200");
            return ResponseEntity.ok("Table deleted");
        } catch (DatabaseException e) {
            logger.info("Exit delete Table with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete table");
        }
    }

    /**
     * Inserts data in the specified table in the submission-version of the specified schema
     * @param schemaName the name of the schema
     * @param query the insert query
     */
    @CrossOrigin(origins="*")
    @PostMapping("/schema/{schemaName}/submission/data")
    public ResponseEntity<String> insertDataSubmission(@PathVariable String schemaName, @RequestParam String query){
        logger.info("Enter insertData");
        try {
            resourceManager.insertDataSubmission(schemaName, query);
            logger.info("Exit insertData with Status Code 200");
            return ResponseEntity.ok("Insert completed");
        } catch (DatabaseException e) {
            logger.info("Exit insertData with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not insert data");
        }
    }
    /**
     * Inserts data in the specified table in the diagnose-version of the specified schema
     * @param schemaName the name of the schame
     * @param query the insert query
     */
    @CrossOrigin(origins="*")
    @PostMapping("/schema/{schemaName}/diagnose/data")
    public ResponseEntity<String> insertDataDiagnose(@PathVariable String schemaName, @RequestParam String query){
        logger.info("Enter insertData");
        try {
            resourceManager.insertDataDiagnose(schemaName, query);
            logger.info("Exit insertData with Status Code 200");
            return ResponseEntity.ok("Insert completed");
        } catch (DatabaseException e) {
            logger.info("Exit insertData with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not insert data");
        }
    }

    /**
     * Deletes the data in the specified table in the specified schema
     */
    @CrossOrigin(origins="*")
    @DeleteMapping("/schema/{schemaName}/table/{tableName}/data")
    public void deleteData(@PathVariable String schemaName, @PathVariable String tableName){

    }


    /**
     * Adds an exercise for the specified schema
     * @param schemaName the name of the schema for which the exercise has to be created
     * @param id the id of the exercise to be created
     * @param solution the solution for the exercise
     */
    @CrossOrigin(origins="*")
    @PutMapping("/exercise/{schemaName}/{id}")
    public ResponseEntity<String> createExercise(@PathVariable int id, @PathVariable String schemaName, @RequestParam String solution) {
        logger.info("Enter createExercise/"+id);
        try {
            resourceManager.createExercise(id, schemaName, solution);
            logger.info("Exit createExercise/"+id+" with Status Code 200");
            return ResponseEntity.ok("Exercise created");
        } catch (DatabaseException e) {
            logger.info("Exit createExercise/"+id+" with Status Code 500");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create data");
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     */
    @CrossOrigin(origins="*")
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id)  {
        logger.info("Enter deleteExercise/"+id);
        try {
            resourceManager.deleteExercise(id);
            logger.info("Exit deleteExercise with Status Code 200");
            return ResponseEntity.ok("Exercise deleted");
        } catch (DatabaseException e) {
            e.printStackTrace();
            logger.info("Exit deleteExercise with Status Code 200");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not delete exercise");
        }
    }




}
