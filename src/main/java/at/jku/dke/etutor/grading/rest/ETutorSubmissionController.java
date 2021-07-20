package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.hateoas.EntityModel;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for posting submissions.
 */
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/submission")
public class ETutorSubmissionController {
    private final Logger logger;
    private final SubmissionDispatcher submissionDispatcherService;

    public ETutorSubmissionController(SubmissionDispatcher submissionDispatcherService){
        this.logger = Logger.getLogger("at.jku.dke.etutor.grading");
        this.submissionDispatcherService = submissionDispatcherService;
    }

    /**
     * Takes a submission as parameter, calculates a unique submissionId, initializes SubmissionDispatcher and returns the generated ID
     * @param submission : the submission from the student
     * @return : ResponseEntity containing EntityModel with the generated submissionId and a link under which the grading can be requested
     *          - HttpStatus.INTERNAL_SERVER_ERROR if exception occurs
     *          - HttpStatus.ACCEPTED if submission is accepted for processing
     */
    @CrossOrigin(origins="*")
    @PostMapping("")
    public ResponseEntity<EntityModel<SubmissionId>> dispatchSubmission(@RequestBody Submission submission) throws JsonProcessingException {
        logger.info("Submission received");
        SubmissionId submissionId = new SubmissionId("-1");
        try{
            logger.info("Calculating submission-ID");
            submissionId = SubmissionId.createId(submission);
            logger.info("Finished calculating submission-ID: " + submissionId.getSubmissionId());

           submissionDispatcherService.run(submission);

            return new ResponseEntity<>(EntityModel.of(submissionId,
                    linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString())).withRel("grading")),
                    HttpStatus.ACCEPTED);
        } catch (IOException e){
            logger.log(Level.SEVERE, "Request processing stopped due to errors");
            e.printStackTrace();
            return new ResponseEntity<>(EntityModel.of(submissionId),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
