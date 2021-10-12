package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.XMLDefinitionDTO;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController(value="/xquery")
public class ETutorXQueryController {
    private ApplicationProperties properties;
    private XQueryResourceService xQueryResourceService;
    private final Logger LOGGER;

    public ETutorXQueryController(ApplicationProperties properties, XQueryResourceService xQueryResourceService){
        this.properties = properties;
        this.xQueryResourceService = xQueryResourceService;
        LOGGER = (Logger) LoggerFactory.getLogger(ETutorXQueryController.class);
    }

    @PostMapping(value = "/xml/taskgroup/{taskGroupUUID}")
    public ResponseEntity<Integer> addXML(@PathVariable String taskGroupUUID, @RequestBody XMLDefinitionDTO xmls){
        Objects.requireNonNull(taskGroupUUID);
        Objects.requireNonNull(xmls);
        int diagnoseFileId;
        int submissionFileId;

        // TODO:1. find available/ existent ids for diagnose and submission
        diagnoseFileId = 22;
        submissionFileId = 23;
        // 2. Save the files at the path specified in the properties: path/id.xml
        try {
            xQueryResourceService.createXMLFiles(xmls, diagnoseFileId, submissionFileId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. map the UUID to the found url and hidden url - ids in the database
        // 4. return the id of the diagnose schema for XML retrieval
        // 5. when creating a task, fetch the ids according to the taskgroup uuid, append them to the url prefix according to properties and save them as url/hidden url for the task
        // 6. persist data in xmldocs table
        return ResponseEntity.ok(0);
    }
}
