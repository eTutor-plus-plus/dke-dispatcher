
package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.DroolsResourceService;

import at.jku.dke.etutor.grading.service.ExerciseNotValidException;
import at.jku.dke.etutor.objects.dispatcher.drools.DroolsTaskDTO;
import at.jku.dke.etutor.objects.dispatcher.sql.SQLExerciseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/drools")
public class ETutorDroolsController {

    private final DroolsResourceService resourceService;
    private final Logger logger;


    public ETutorDroolsController(DroolsResourceService resourceService){
        this.logger = (Logger) LoggerFactory.getLogger(ETutorDroolsController.class);
        this.resourceService = resourceService;
    }

    /**
     * Get required Java-Classes from database for selected task.
     *
     * @param id Task-ID
     * @return Java-Class content as String in body
     * @throws RuntimeException Internal Server Error 500
     */
    @GetMapping("/task/getClasses/{id}")
    public ResponseEntity<String> getClasses(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getClasses()");
        try{
            String classes = resourceService.getClasses(id);
            if(classes.equals("")){
                logger.info("Exit: getClasses() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find classes for task " + id);
            }else{
                logger.info("Exit: getClasses() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(classes);
            }
        } catch (Exception e) {
            logger.info("Exit: getClasses() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/task/getTestData/{id}")
    public ResponseEntity<String> getTestData(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getTestData()");
        try{
            String testData = resourceService.getTestData(id);
            if(testData.equals("")){
                logger.info("Exit: getTestData() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find test-data for task " + id);
            }else{
                logger.info("Exit: getTestData() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(testData);
            }
        } catch (Exception e) {
            logger.info("Exit: getTestData() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getEvents/{id}")
    public ResponseEntity<String> getEvents(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getEvents()");
        try{
            String events = resourceService.getEvents(id);
            if(events.equals("")){
                logger.info("Exit: getEvents() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find events for task "+id);
            }else{
                logger.info("Exit: getEvents() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(events);
            }
        } catch (Exception e) {
            logger.info("Exit: getEvents() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getSolution/{id}")
    public ResponseEntity<String> getSolution(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getSolution()");
        try{
            String solution = resourceService.getSolution(id);
            if(solution.equals("")){
                logger.info("Exit: getSolution() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find solution for task "+id);
            }else{
                logger.info("Exit: getSolution() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(solution);
            }
        } catch (Exception e) {
            logger.info("Enter: getSolution() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getFacts/{id}")
    public ResponseEntity<String> getFacts(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getFacts()");
        try{
            String facts = resourceService.getFacts(id);
            if(facts.equals("")){
                logger.info("Exit: getFacts() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find facts for task "+id);
            }else{
                logger.info("Exit: getFacts() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(facts);
            }
        } catch (Exception e) {
            logger.info("Exit: getFacts() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/task/getTask/{id}")
    public ResponseEntity<String> getTask(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getTask()");
        try{
            String task = resourceService.getTask(id);
            if(task.equals("")){
                logger.info("Exit: getTask() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find task "+id);
            }else{
                logger.info("Exit: getTask() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(task);
            }
        } catch (Exception e) {
            logger.info("Exit: getTask() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/task/addTask")
    public ResponseEntity<Integer> addTask(@RequestBody DroolsTaskDTO taskDTO) throws ApiException {
        logger.info("Enter: addTask()");
        try {
            int id = resourceService.addTask(taskDTO);

            //TODO: Eventuell Syntax der Regeln vor dem erstellen Prüfen? 20231202

            logger.info("Exit: addTask() for id: {} with Status Code 200", id);
            return ResponseEntity.ok(id);
        } catch (DatabaseException e) {
            logger.error("Exit: assTask() with Status Code 500", e);
            logger.info("Rollback. Deleting task.");
            throw new ApiException(500, e.toString(), null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @PutMapping("/task/editTask/{id}")
    public ResponseEntity<String> editTask(@PathVariable int id, @RequestBody DroolsTaskDTO taskDTO){
        logger.info("Enter: editTask() task_id: {}", id);
        try{
            resourceService.editTask(id, taskDTO);
            logger.info("Exit: editTask() with Status Code 200");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/task/deleteTask/{id}")
    public void deleteTask(@PathVariable int id) throws SQLException {
        resourceService.deleteTask(id);
    }




}