package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.web.bind.annotation.*;

@RestController(value="/xquery")
public class ETutorXQueryController {
    private ApplicationProperties properties;

    public ETutorXQueryController(ApplicationProperties properties){
        this.properties = properties;
    }

    @PostMapping(value = "/xml/taskgroup/{taskGroupUUID}")
    public void addXML(@PathVariable String taskGroupUUID, @RequestBody String XMLFILES){

    }


}
