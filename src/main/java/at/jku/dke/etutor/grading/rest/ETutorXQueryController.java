package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@RestController
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
     * Adds the XML files for a specific taskGroup to to filesystem and adds the necessary data to the database,
     * namely the mapping of the taskGroup-UUID to the file-ids and the XML's to the xmldocs table.
     * @param taskGroup the UUID identifying the task
     * @param xmls wrapper dto for the diagnose-xml and submission-xml
     * @return
     */
    @PostMapping(value = "/xml/taskGroup/{taskGroup}")
    public ResponseEntity<Integer> addXMLForTaskGroup(@PathVariable String taskGroup, @RequestBody XMLDefinitionDTO xmls){
        Objects.requireNonNull(taskGroup);
        Objects.requireNonNull(xmls);
        int diagnoseFileId;
        int submissionFileId;

        int[] fileIds;
        try {
            fileIds = xQueryResourceService.getFileIds(taskGroup, xmls);
            diagnoseFileId = fileIds[0];
            submissionFileId = fileIds[1];
            try {
                xQueryResourceService.createXMLFiles(xmls, diagnoseFileId, submissionFileId);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
            }
            return ResponseEntity.ok(diagnoseFileId);
        } catch (SQLException throwables) {
            LOGGER.error(throwables.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1);
        }
    }

    /**
     * Deletes a task group (namely the xml files belonging to the task group and the entries in the database)
     * @param taskGroup the task group
     * @return an ResponseEntity
     */
    @DeleteMapping("/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> deleteXMLOfTaskGroup(@PathVariable String taskGroup){
        try{
            xQueryResourceService.deleteXML(taskGroup);
            return ResponseEntity.ok("XML for taskGroup deleted");
        } catch (SQLException throwables) {
            LOGGER.error(throwables.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwables.getMessage());
        }
    }
    /**
     * Returns the diagnose-xml for a task group
     * @param taskGroup the UUID of the task group
     * @return a String containing the xml
     */
    @GetMapping("/xml/taskGroup/{taskGroup}")
    public ResponseEntity<String> getXML(@PathVariable String taskGroup){
        Objects.requireNonNull(taskGroup);
        String xml = null;
        try {
            xml = xQueryResourceService.getXML(taskGroup);
            return ResponseEntity.ok(xml);
        } catch (SQLException throwables) {
           LOGGER.error(throwables.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwables.getMessage());
        }
    }
}
