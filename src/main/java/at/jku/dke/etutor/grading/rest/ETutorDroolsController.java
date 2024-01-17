
package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.DroolsResourceService;

import at.jku.dke.etutor.objects.dispatcher.drools.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

@RestController
@RequestMapping("/drools")
public class ETutorDroolsController {

    private final DroolsResourceService resourceService;
    private final Logger logger;


    public ETutorDroolsController(DroolsResourceService resourceService){
        this.logger = LoggerFactory.getLogger(ETutorDroolsController.class);
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

    /**
     * Fetches the solution rules
     * @param id
     * @return
     * @throws RuntimeException
     */
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

    /**
     * Fetches additional properties for the selected task
     * @param id
     * @return
     * @throws RuntimeException
     */
    @GetMapping("/task/getTaskProperties/{id}")
    public ResponseEntity<String> getTaskProperties(@PathVariable int id) throws RuntimeException {
        logger.info("Enter: getTaskProperties()");
        try{
            String solution = resourceService.getTaskProperties(id);
            if(solution.equals("")){
                logger.info("Exit: getTaskProperties() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find properties for task "+id);
            }else{
                logger.info("Exit: getTaskProperties() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(solution);
            }
        } catch (Exception e) {
            logger.info("Enter: getTaskProperties() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches the facts for either submit or diagnose
     * @param id
     * @param isForDiagnose
     * @return
     * @throws RuntimeException
     */
    @GetMapping("/task/getFacts/{id}")
    public ResponseEntity<String> getFacts(
            @PathVariable int id,
            @RequestParam(value = "isForDiagnose") boolean isForDiagnose
    ) throws RuntimeException {
        logger.info("Enter: getFacts()");
        try{
            String facts = resourceService.getFacts(id, isForDiagnose);
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


    /**
     * Fetches the output facts for the selected task (submit/diagnose)
     * @param id
     * @param isForDiagnose
     * @return
     * @throws RuntimeException
     */
    @GetMapping("/task/getOutput/{id}")
    public ResponseEntity<String> getOutput(
            @PathVariable int id,
            @RequestParam(value = "isForDiagnose") boolean isForDiagnose
    ) throws RuntimeException {
        logger.info("Enter: getOutput()");
        try{
            String facts = resourceService.getOutput(id, isForDiagnose);
            if(facts.equals("")){
                logger.info("Exit: getOutput() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find output for task "+id);
            }else{
                logger.info("Exit: getOutput() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(facts);
            }
        } catch (Exception e) {
            logger.info("Exit: getOutput() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }


    /**
     * Fetches the information of a task
     * @param id
     * @return
     * @throws RuntimeException
     */
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

    /**
     * Creates a task in the database
     * @param taskDTO
     * @return
     * @throws ApiException
     */
    @PostMapping("/task/addTask")
    public ResponseEntity<Integer> addTask(@RequestBody DroolsTaskDTO taskDTO) throws ApiException {
        logger.info("Enter: addTask()");
        try {
            int id = resourceService.addTask(taskDTO);

            if(id > 0){
                logger.info("Exit: addTask() for id: {} with Status Code 200", id);
                return ResponseEntity.ok(id);
            }else{
                logger.info("Exit: addTask() for id: {} with Status Code 400", id);
                logger.info("rollback...");
                return ResponseEntity.badRequest().build();
            }

        } catch (DatabaseException e) {
            logger.error("Exit: addTask() with Status Code 500", e);
            logger.info("Rollback. Deleting task.");
            throw new ApiException(500, e.toString(), null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Edit/update a task
     * @param id
     * @param taskDTO
     * @return
     */
    @PutMapping("/task/updateTask/{id}")
    public ResponseEntity<String> updateTask(@PathVariable int id, @RequestBody DroolsTaskDTO taskDTO){
        logger.info("Enter: updateTask() task_id: {}", id);
        try{
            resourceService.editTask(id, taskDTO);
            logger.info("Exit: updateTask() with Status Code 200");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete a task
     * @param id
     * @throws SQLException
     */
    @DeleteMapping("/task/deleteTask/{id}")
    public void deleteTask(@PathVariable int id) throws SQLException {
        resourceService.deleteTask(id);
    }

}