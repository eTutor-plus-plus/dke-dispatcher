package at.jku.dke.etutor.grading.rest.repositories;

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
     * Creates a new schema with the specified name
     * @param schemaName the schema to be created
     * @return
     */
    @CrossOrigin(origins="*")
    @PutMapping("/schema/{schemaName}")
    public ResponseEntity<String> createSchema(@PathVariable String schemaName){
        logger.info("Enter createSchema/"+schemaName);
        try {
            resourceManager.createSchema(schemaName);
            logger.info("Exit createSchema with Status Code 200");
            return ResponseEntity.ok("Schema created");
        } catch (DatabaseException e) {
            logger.info("Exit createSchema with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    /**
     * Deletes the schema with the specified name
     * @param schemaName the schema to be deleted
     * @return
     */
    @CrossOrigin(origins="*")
    @DeleteMapping("/schema/{schemaName}")
    public ResponseEntity<String> deleteSchema(@PathVariable String schemaName){
        logger.info("Enter deleteSchema"+schemaName);
        try {
            resourceManager.deleteSchema(schemaName);
            logger.info("Exit deleteSchema with Status Code 200");
            return ResponseEntity.ok("Schema deleted");
        } catch (DatabaseException e) {
            logger.info("Exit createSchema with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }

    }


    /**
     * Creates a table for the specified schema
     */
    @PutMapping("/schema/{schemaName}/table")
    public ResponseEntity<String> createTable(@PathVariable String schemaName, @RequestParam String query){
        logger.info("Enter createTable/"+schemaName);
        try {
            resourceManager.createTable(schemaName, query);
            logger.info("Exit createTable with Status Code 200");
            return ResponseEntity.ok("Table created");
        } catch (DatabaseException e) {
            logger.info("Exit createTable with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }

    }

    /**
     * Deletes a table in the specified schmema
     */
    @DeleteMapping("/schema/{schemaName}/table/{tableName}")
    public ResponseEntity<String> deleteTable(@PathVariable String schemaName, @PathVariable String tableName){
        logger.info("Enter "+schemaName+"/deleteTable/"+tableName);
        try {
            resourceManager.deleteTable(schemaName, tableName);
            logger.info("Exit deleteTable with Status Code 200");
            return ResponseEntity.ok("Table deleted");
        } catch (DatabaseException e) {
            logger.info("Exit delete Table with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    /**
     * Inserts data in the specified table in the specified schema
     */
    @PostMapping("/schema/{schemaName}/data")
    public ResponseEntity<String> insertData(@PathVariable String schemaName, @RequestParam String query){
        logger.info("Enter insertData");
        try {
            resourceManager.insertData(schemaName, query);
            logger.info("Exit insertData with Status Code 200");
            return ResponseEntity.ok("Insert completed");
        } catch (DatabaseException e) {
            logger.info("Exit insertData with Status Code 500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error");
        }
    }

    /**
     * Deletes the data in the specified table in the specified schema
     */
    @DeleteMapping("/schema/{schemaName}/table/{tableName}/data")
    public void deleteData(@PathVariable String schemaName, @PathVariable String tableName){

    }


    /**
     * Adds an exercise for the specified schema
     */
    @PutMapping("/exercise/{schemaName}/{id}")
    public void createExercise(@PathVariable int id, @PathVariable String schemaName, @RequestParam String solution) {
        try {
            resourceManager.createExercise(id, schemaName, solution);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an exercise
     */
    @DeleteMapping("/exercise/{id}")
    public void deleteExercise(@PathVariable String id) {

    }




}
