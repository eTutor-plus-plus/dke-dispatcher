package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.IdWrapper;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/submission")
public class ETutorSubmissionController {

    @PostMapping("")
    public EntityModel<IdWrapper> dispatchSubmission(@RequestBody Submission submission){
        int submissionId = -1;

        Thread t = new Thread(new SubmissionDispatcher(submission,submissionId));
        t.start();

        IdWrapper wrapper = new IdWrapper(submissionId);
        return EntityModel.of(wrapper,
                linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId)).withRel("grading"));
    }
}
