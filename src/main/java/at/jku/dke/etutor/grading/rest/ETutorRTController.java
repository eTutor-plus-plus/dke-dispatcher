package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.RTResourceService;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/rt")
public class ETutorRTController {

    private final RTResourceService resourceService;

    public ETutorRTController(RTResourceService resourceService){
        this.resourceService = resourceService;
    }

    @PostMapping("/task/getEntry")
    public boolean getEntry(@RequestBody String name, int id) {
        RTAnalysis elem = new RTAnalysis(name);
        return elem.checkSyntax();
    }

    @PostMapping("/task/addTask")
    public Long getTask(@RequestBody String solution) throws SQLException {
        return this.resourceService.insertTask(solution);
    }
}
