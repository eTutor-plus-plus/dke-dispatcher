package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.grading.rest.dto.XQExerciseDTO;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;

@RestController
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
@RequestMapping("/xquery")
public class ETutorXQueryController {
    private ApplicationProperties properties;
    private XQueryResourceService xQueryResourceService;
    private final Logger LOGGER;

    public ETutorXQueryController(ApplicationProperties properties, XQueryResourceService xQueryResourceService){
        this.properties = properties;
        this.xQueryResourceService = xQueryResourceService;
        LOGGER = (Logger) LoggerFactory.getLogger(ETutorXQueryController.class);
    }

    /**
     * Adds the XML files for a specific taskGroup to the filesystem and adds the necessary data to the database,
     * namely the mapping of the taskGroup-UUID to the file-ids and the XML's to the xmldocs table.
     * @param taskGroup the UUID identifying the task
     * @param xmls wrapper dto for the diagnose-xml and submission-xml
     * @return
     */
    @PostMapping(value = "/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> addXMLForTaskGroup(@PathVariable String taskGroup, @RequestBody XMLDefinitionDTO xmls){
        Objects.requireNonNull(taskGroup);
        Objects.requireNonNull(xmls);
        int diagnoseFileId;
        int submissionFileId;
        taskGroup = decode(taskGroup);

        int[] fileIds;
        try {
            fileIds = xQueryResourceService.addXMLToDatabase(taskGroup, xmls);
            diagnoseFileId = fileIds[0];
            submissionFileId = fileIds[1];
            try {
                xQueryResourceService.addXMLToFileSystem(xmls, diagnoseFileId, submissionFileId);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                xQueryResourceService.deleteTaskGroup(taskGroup);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1+"");
            }
            return ResponseEntity.ok(properties.getXquery().getXmlFileURLPrefix()+diagnoseFileId);
        } catch (SQLException throwables) {
            LOGGER.error(throwables.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1+"");
        }
    }

    /**
     * Deletes a task group (namely the xml files belonging to the task group and the entries in the database)
     * @param taskGroup the task group
     * @return an ResponseEntity
     */
    @DeleteMapping("/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> deleteXMLOfTaskGroup(@PathVariable String taskGroup){
        taskGroup = decode(taskGroup);
        try{
            xQueryResourceService.deleteTaskGroup(taskGroup);
            return ResponseEntity.ok("XML for taskGroup deleted");
        } catch (SQLException throwables) {
            LOGGER.error(throwables.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwables.getMessage());
        }
    }

    /**
     * Returns an XML according to the file id
     * @param id the id of the file
     * @return a String representation of the xml
     */
    @GetMapping("/xml/fileid/{id}")
    public ResponseEntity<String> getXMLById(@PathVariable int id){
        try{
            String xml = xQueryResourceService.getXMLById(id);
            return ResponseEntity.ok(xml);
        } catch (Exception throwables) {
            LOGGER.error(throwables.getMessage());
            return ResponseEntity.status(500).body(throwables.getMessage());
        }
    }

    /**
     * Creates an exercise for a task group
     * @param taskGroup the task group
     * @param dto the dto
     * @return a ResponseEntity
     */
    @PostMapping("/exercise/taskGroup/{taskGroup}")
    public ResponseEntity<Integer> createExercise(@PathVariable String taskGroup, @RequestBody XQExerciseDTO dto){
        taskGroup = decode(taskGroup);
        var id = -1;
        try {
            id = xQueryResourceService.createExercise(taskGroup, dto);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(id);
        }
        return ResponseEntity.ok(id);
    }

    /**
     * Returns the exercise definition for a given id
     * @param id the id of the exercise
     * @return an XQExerciseDTO
     */
    @GetMapping("/exercise/solution/id/{id}")
    public ResponseEntity<XQExerciseDTO> getSolutionAndSorting(@PathVariable int id){
        try {
            XQExerciseDTO exercise = xQueryResourceService.fetchExercise(id);
            return ResponseEntity.ok(exercise);
        } catch (Exception e) {
           LOGGER.error(e.getMessage());
           return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Updates an exercise
     * @param dto the XQExerciseDTO
     * @param id the id of the exercise
     * @return a ResponseEntity
     */
    @PostMapping("exercise/id/{id}")
    public ResponseEntity<String> updateExercise(@RequestBody XQExerciseDTO dto, @PathVariable int id, HttpServletRequest request){
        LOGGER.info(dto.getQuery());
        LOGGER.info(dto.getSortedNodes().toString());
        try {
            return ResponseEntity.ok(xQueryResourceService.updateExercise(dto, id));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).body("Could not update exercise");
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     * @return a ResponseEntity
     */
    @DeleteMapping("exercise/id/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id){
        var response = "Exercise with id "+id +" deleted";
        try {
            xQueryResourceService.deleteExercise(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return ResponseEntity.status(500).body("Could not delete exercise with id "+id);
    }

    @GetMapping("/grading/{exercise_id}/{action}/{diagnose_level}")
    public ResponseEntity<GradingDTO> triggerEvaluation(@PathVariable int exercise_id, @PathVariable String action, @PathVariable String diagnose_level){
        try {
            GradingDTO grading = xQueryResourceService.getGradingForExercise(exercise_id, action, diagnose_level);
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
