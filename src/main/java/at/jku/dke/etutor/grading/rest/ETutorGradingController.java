package at.jku.dke.etutor.grading.rest;



import at.jku.dke.etutor.core.evaluation.Grading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/grading")
public class ETutorGradingController {

    @GetMapping("/{submissionId}")
    public ResponseEntity<Grading> getGrading(@PathVariable int submissionId){

        return null;
    }
}
