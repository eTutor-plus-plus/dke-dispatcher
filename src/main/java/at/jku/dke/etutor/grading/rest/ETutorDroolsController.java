
package at.jku.dke.etutor.grading.rest;

        import at.jku.dke.etutor.grading.rest.model.entities.Grading;
        import at.jku.dke.etutor.grading.service.DatabaseException;
        import at.jku.dke.etutor.grading.service.DroolsResourceService;
        import at.jku.dke.etutor.grading.service.SQLResourceService;
        import at.jku.dke.etutor.modules.pm.plg.utils.Logger;
        import net.minidev.json.JSONArray;
        import net.minidev.json.JSONObject;
        import net.minidev.json.parser.ParseException;
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
        logger.info("getClasses()");
        try{
            String classes = resourceService.getClasses(id);
            if(classes.isEmpty()){
                logger.info("Exit --> getClasses() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find classes for task "+id);
            }else{
                logger.info("Exit --> getClasses() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(classes);
            }
        } catch (Exception e) {
            logger.info("Exit --> getClasses() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/task/getTestData/{id}")
    public ResponseEntity<String> getTestData(@PathVariable int id) throws RuntimeException {
        logger.info("getTestData()");
        try{
            String testData = resourceService.getTestData(id);
            if(testData.isEmpty()){
                logger.info("Exit --> getTestData() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find test-data for task "+id);
            }else{
                logger.info("Exit --> getTestData() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(testData);
            }
        } catch (Exception e) {
            logger.info("Exit --> getTestData() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getEvents/{id}")
    public ResponseEntity<String> getEvents(@PathVariable int id) throws RuntimeException {
        logger.info("getEvents()");
        try{
            String events = resourceService.getEvents(id);
            if(events.isEmpty()){
                logger.info("Exit --> getEvents() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find events for task "+id);
            }else{
                logger.info("Exit --> getEvents() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(events);
            }
        } catch (Exception e) {
            logger.info("Exit --> getEvents() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getSolution/{id}")
    public ResponseEntity<String> getSolution(@PathVariable int id) throws RuntimeException {
        logger.info("getSolution()");
        try{
            String solution = resourceService.getSolution(id);
            if(solution.isEmpty()){
                logger.info("Exit --> getSolution() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find solution for task "+id);
            }else{
                logger.info("Exit --> getSolution() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(solution);
            }
        } catch (Exception e) {
            logger.info("Exit --> getSolution() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/task/getFacts/{id}")
    public ResponseEntity<String> getFacts(@PathVariable int id) throws RuntimeException {
        logger.info("getFacts()");
        try{
            String facts = resourceService.getFacts(id);
            if(facts.isEmpty()){
                logger.info("Exit --> getFacts() with status 404");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find facts for task "+id);
            }else{
                logger.info("Exit --> getFacts() with status 200");
                return ResponseEntity.status(HttpStatus.OK).body(facts);
            }
        } catch (Exception e) {
            logger.info("Exit --> getFacts() - Internal Server Error - Code 500");
            throw new RuntimeException(e);
        }
    }


//    @PostMapping("/task/addTask")
//    public Long getTask(@RequestBody String elem) throws SQLException, ParseException {
//        RTObject rtObject = resourceService.getRTObject(elem);
//        String solution = rtObject.getSolution().toString();
//        solution = solution = solution.substring(1, solution.length() - 1);
//        return this.resourceService.insertTask(solution, rtObject.getMaxPoints());
//    }
//
//    @PutMapping("/task/editTask")
//    public void editTask(@RequestBody String elem) throws SQLException, ParseException {
//        RTObject rtObject = resourceService.getRTObject(elem);
//        String solution = rtObject.getSolution().toString();
//        solution = solution = solution.substring(1, solution.length() - 1);
//        this.resourceService.editTask(solution, rtObject.getMaxPoints(), rtObject.getId());
//    }
//
//    @DeleteMapping("/task/deleteTask/{id}")
//    public void deleteTask(@PathVariable Integer id) throws SQLException {
//        resourceService.deleteTask(id);
//    }
//
//    @GetMapping("/task/getTask/{id}")
//    public RTObject getTask(@PathVariable Integer id) throws SQLException {
//        return resourceService.getTask(id);
//    }


}