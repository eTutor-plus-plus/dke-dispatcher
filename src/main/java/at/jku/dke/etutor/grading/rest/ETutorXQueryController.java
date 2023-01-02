package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.objects.dispatcher.xq.XMLDefinitionDTO;
import at.jku.dke.etutor.objects.dispatcher.xq.XQExerciseDTO;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import ch.qos.logback.classic.Logger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

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
     * @param taskGroup the name of the task group
     * @param xmls wrapper dto for the diagnose-xml and submission-xml
     * @return the url identifying the task group
     */
    @PostMapping(value = "/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> addXMLForTaskGroup(@PathVariable String taskGroup, @RequestBody XMLDefinitionDTO xmls) throws ApiException {
        Objects.requireNonNull(taskGroup);
        Objects.requireNonNull(xmls);

        taskGroup = decode(taskGroup);
        int diagnoseFileId;
        int submissionFileId;

        // Return if both xml-strings are blank
        if(StringUtils.isBlank(xmls.getDiagnoseXML()) && StringUtils.isBlank(xmls.getSubmissionXML())){
            return ResponseEntity.status(412).build();
        }

        // if one of the xml-strings is blank, use the other one
        if(StringUtils.isBlank(xmls.getDiagnoseXML())) xmls.setDiagnoseXML(xmls.getSubmissionXML());
        if(StringUtils.isBlank(xmls.getSubmissionXML())) xmls.setSubmissionXML(xmls.getDiagnoseXML());


        int[] fileIds;
        try{
            // check for valid xml
            xQueryResourceService.checkXMLStrings(xmls);

            // add xml to database
            fileIds = xQueryResourceService.addXMLToDatabase(taskGroup, xmls);
            diagnoseFileId = fileIds[0];
            submissionFileId = fileIds[1];

            // add xml to file system
            xQueryResourceService.addXMLToFileSystem(xmls, diagnoseFileId, submissionFileId);

            return ResponseEntity.ok(properties.getXquery().getXmlFileURLPrefix()+diagnoseFileId);

        } catch (SQLException | SAXException | IOException e) {
            LOGGER.error(e.getMessage());
            try {
                xQueryResourceService.deleteTaskGroup(taskGroup);
            } catch (SQLException ex) {
                LOGGER.info(ex.getMessage());
            }
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Deletes a task group (namely the xml files belonging to the task group and the entries in the database)
     * @param taskGroup the task group
     * @return an ResponseEntity
     */
    @DeleteMapping("/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> deleteXMLOfTaskGroup(@PathVariable String taskGroup) throws ApiException {
        taskGroup = decode(taskGroup);
        try{
            xQueryResourceService.deleteTaskGroup(taskGroup);
            return ResponseEntity.ok("XML for taskGroup deleted");
        } catch (SQLException throwables) {
            LOGGER.error(throwables.getMessage());
            throw new ApiException(500, throwables.toString(), null);
        }
    }

    /**
     * Returns an XML according to the file id
     * @param id the id of the file
     * @return a String representation of the xml
     */
    @GetMapping("/xml/fileid/{id}")
    public ResponseEntity<String> getXMLById(@PathVariable int id) throws ApiException {
        try{
            String xml = xQueryResourceService.getXMLById(id);
            return ResponseEntity.ok(xml);
        } catch (Exception throwables) {
            LOGGER.error(throwables.getMessage());
            throw new ApiException(500, throwables.toString(), null);
        }
    }

    /**
     * Creates an exercise for a task group
     * @param taskGroup the task group
     * @param dto the dto
     * @return a ResponseEntity
     */
    @PostMapping("/exercise/taskGroup/{taskGroup}")
    public ResponseEntity<Integer> createExercise(@PathVariable String taskGroup, @RequestBody XQExerciseDTO dto, @RequestParam(required = false, defaultValue = "false") boolean checkSyntax) throws ApiException {
        taskGroup = decode(taskGroup);
        var id = -1;
        try {
            id = xQueryResourceService.createExercise(taskGroup, dto);
            xQueryResourceService.testNewlyCreatedExercise(id);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            try {
                xQueryResourceService.deleteExercise(id);
            } catch (Exception ex) {
                LOGGER.warn(e.getMessage());
            }
            throw new ApiException(500, e.toString(), null);
        }
        return ResponseEntity.ok(id);
    }

    /**
     * Returns the exercise definition for a given id
     * @param id the id of the exercise
     * @return an XQExerciseDTO
     */
    @GetMapping("/exercise/solution/id/{id}")
    public ResponseEntity<XQExerciseDTO> getSolutionAndSorting(@PathVariable int id) throws ApiException {
        try {
            XQExerciseDTO exercise = xQueryResourceService.fetchExercise(id);
            return ResponseEntity.ok(exercise);
        } catch (Exception e) {
           LOGGER.error(e.getMessage());
           throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Updates an exercise
     * @param dto the XQExerciseDTO
     * @param id the id of the exercise
     * @return a ResponseEntity
     */
    @PostMapping("exercise/id/{id}")
    public ResponseEntity<String> updateExercise(@RequestBody XQExerciseDTO dto, @PathVariable int id, HttpServletRequest request) throws ApiException {
        LOGGER.info(dto.getQuery());
        LOGGER.info(dto.getSortedNodes().toString());
        try {
            return ResponseEntity.ok(xQueryResourceService.updateExercise(dto, id));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Deletes an exercise
     * @param id the id of the exercise
     * @return a ResponseEntity
     */
    @DeleteMapping("exercise/id/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id) throws ApiException {
        var response = "Exercise with id "+id +" deleted";
        try {
            xQueryResourceService.deleteExercise(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApiException(500, e.toString(), null);
        }
    }

    @GetMapping("/grading/{exercise_id}/{action}/{diagnose_level}")
    public ResponseEntity<Grading> triggerEvaluation(@PathVariable int exercise_id, @PathVariable String action, @PathVariable String diagnose_level) throws ApiException {
        try {
            Grading grading = xQueryResourceService.getGradingForExercise(exercise_id, action, diagnose_level);
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
