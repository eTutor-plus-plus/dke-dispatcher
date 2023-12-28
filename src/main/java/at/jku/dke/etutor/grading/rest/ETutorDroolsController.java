
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

//    @GetMapping("/task/getTestData/{id}")
//    public ResponseEntity<String> getTestData(@PathVariable int id) throws RuntimeException {
//        logger.info("Enter: getTestData()");
//        try{
//            String testData = resourceService.getTestData(id);
//            if(testData.equals("")){
//                logger.info("Exit: getTestData() with status 404");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find test-data for task " + id);
//            }else{
//                logger.info("Exit: getTestData() with status 200");
//                return ResponseEntity.status(HttpStatus.OK).body(testData);
//            }
//        } catch (Exception e) {
//            logger.info("Exit: getTestData() - Internal Server Error - Code 500");
//            throw new RuntimeException(e);
//        }
//    }
//    @GetMapping("/task/getEvents/{id}")
//    public ResponseEntity<String> getEvents(@PathVariable int id) throws RuntimeException {
//        logger.info("Enter: getEvents()");
//        try{
//            String events = resourceService.getEvents(id);
//            if(events.equals("")){
//                logger.info("Exit: getEvents() with status 404");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find events for task "+id);
//            }else{
//                logger.info("Exit: getEvents() with status 200");
//                return ResponseEntity.status(HttpStatus.OK).body(events);
//            }
//        } catch (Exception e) {
//            logger.info("Exit: getEvents() - Internal Server Error - Code 500");
//            throw new RuntimeException(e);
//        }
//    }
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

    @PostMapping("/task/createOutput")
    public ResponseEntity<String> createOutput(@RequestBody DroolsObjectDTO objectDTO) throws ApiException, DatabaseException {
        logger.info("Enter: createOutput()");
        try{
            int insertedRows = resourceService.createOutput(objectDTO);
            if(insertedRows == -1){
                logger.info("Exit: createOutput() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find task");
            }else{
                logger.info("Exit: createOutput() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body("Created " + insertedRows + " new output-object.");
            }
        } catch (Exception e){
            logger.info("Exit: createOutput() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

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
            logger.error("Exit: addTask() with Status Code 500", e);
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

//    @PostMapping("/task/addClass")
//    public ResponseEntity<String> addClass(@RequestBody DroolsClassDTO classDTO) throws ApiException {
//        logger.info("Enter: addClass()");
//        try {
//            String className = resourceService.addTask(classDTO);
//
//            //TODO: Eventuell Syntax vor dem erstellen Prüfen? 20231203
//
//            logger.info("Exit: addClass() for className: {} with Status Code 200", className);
//            return ResponseEntity.ok(className);
//        } catch (DatabaseException e) {
//            logger.error("Exit: addClass() with Status Code 500", e);
//            logger.info("Rollback. Deleting class.");
//            throw new ApiException(500, e.toString(), null);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @PostMapping("/task/addEvent")
//    public ResponseEntity<String> addEvent(@RequestBody DroolsEventDTO eventDTO) throws ApiException {
//        logger.info("Enter: addEvent()");
//        try {
//            String eventName = resourceService.addEvent(eventDTO);
//
//            //TODO: Eventuell Syntax vor dem erstellen Prüfen? 20231203
//
//            logger.info("Exit: addEvent() for eventName: {} with Status Code 200", eventName);
//            return ResponseEntity.ok(eventName);
//        } catch (DatabaseException e) {
//            logger.error("Exit: addEvent() with Status Code 500", e);
//            logger.info("Rollback. Deleting event.");
//            throw new ApiException(500, e.toString(), null);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    @PostMapping("/task/addFact")
//    public ResponseEntity<String> addFact(@RequestBody DroolsFactDTO factDTO) throws ApiException {
//        logger.info("Enter: addFact()");
//        try {
//            String factName = resourceService.addFact(factDTO);
//
//            //TODO: Eventuell Syntax vor dem erstellen Prüfen? 20231203
//
//            logger.info("Exit: addFact() for factName: {} with Status Code 200", factName);
//            return ResponseEntity.ok(factName);
//        } catch (DatabaseException e) {
//            logger.error("Exit: addFact() with Status Code 500", e);
//            logger.info("Rollback. Deleting fact.");
//            throw new ApiException(500, e.toString(), null);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @PostMapping("/task/addTestdata")
//    public ResponseEntity<Integer> addTestdata(@RequestBody DroolsTestDTO testDTO) throws ApiException {
//        logger.info("Enter: addTestdata()");
//        try {
//            int taskId = resourceService.addTestdata(testDTO);
//
//            //TODO: Eventuell Syntax vor dem erstellen Prüfen? 20231203
//
//            logger.info("Exit: addTestdata() for taskId: {} with Status Code 200", taskId);
//            return ResponseEntity.ok(taskId);
//        } catch (DatabaseException e) {
//            logger.error("Exit: addTestdata() with Status Code 500", e);
//            logger.info("Rollback. Deleting testdata.");
//            throw new ApiException(500, e.toString(), null);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }





}