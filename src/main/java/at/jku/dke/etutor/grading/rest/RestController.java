package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Grading;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/")
public class RestController {


    @PostMapping("submission")
    public long dispatchSubmission(@RequestBody Submission submission){
        long submissionId = -1;


        return submissionId;
    }



    @GetMapping("grading")
    public Grading getGrading(@RequestParam long Id){


        return null;
    }





}
