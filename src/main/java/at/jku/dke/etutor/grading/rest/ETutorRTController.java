package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rt")
public class ETutorRTController {

    @PostMapping("/task/getEntry")
    public boolean getEntry(@RequestBody String name, int id) {
        RTAnalysis elem = new RTAnalysis(name);
        return elem.checkSyntax();
    }
}
