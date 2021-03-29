package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.EntityModel;

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
    public EntityModel<SubmissionId> dispatchSubmission(@RequestBody Submission submission){
        int submissionId = -1;

        Thread t = new Thread(new SubmissionDispatcher(submission,submissionId));
        t.start();

        SubmissionId wrapper = new SubmissionId(submissionId);
        return EntityModel.of(wrapper,
                linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId)).withRel("grading"));
    }
}
