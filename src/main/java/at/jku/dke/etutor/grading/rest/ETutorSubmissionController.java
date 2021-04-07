package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.EntityModel;

import java.io.IOException;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for submissions.
 */

@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/submission")
public class ETutorSubmissionController {
    /**
     * Takes a submission as parameter, calculates a unique submissionId (tbd) and dispatches the
     *  submission to the corresponding module (in a seperate thread).
     * @param submission : the submission from the student
     * @return : EntityModel containing the generated submissionId and a link under which the grading can be requested
     */

    @PostMapping("")
    public ResponseEntity<EntityModel<SubmissionId>> dispatchSubmission(@RequestBody Submission submission) {
        SubmissionId submissionId = new SubmissionId("-1");
        try{
            submissionId = SubmissionId.createId(submission);
            Thread t = new Thread(new SubmissionDispatcher(submission,submissionId));
            t.start();
            return new ResponseEntity<>(EntityModel.of(submissionId,
                    linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString())).withRel("grading")), HttpStatus.ACCEPTED);
        } catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity<>(EntityModel.of(submissionId), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
