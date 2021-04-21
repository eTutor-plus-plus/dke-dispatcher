package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.ReportDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private Logger logger;
    private SubmissionRepository submissionRepository;
    private GradingDTORepository gradingDTORepository;
    private ReportDTORepository reportDTORepository;
    public ETutorSubmissionController(SubmissionRepository submissionRepository, GradingDTORepository gradingDTORepository, ReportDTORepository reportDTORepository){
        this.logger = Logger.getLogger("at.jku.dke.etutor.grading");
        this.submissionRepository=submissionRepository;
        this.gradingDTORepository = gradingDTORepository;
        this.reportDTORepository = reportDTORepository;
    }

    /**
     * Takes a submission as parameter, calculates a unique submissionId, initializes SubmissionDispatcher and returns the generated ID
     * @param submission : the submission from the student
     * @return : ResponseEntity containing EntityModel with the generated submissionId and a link under which the grading can be requested
     *          - HttpStatus.INTERNAL_SERVER_ERROR if exception occurs
     *          - HttpStatus.ACCEPTED if submission is accepted for processing
     */
    @PostMapping("")
    public ResponseEntity<EntityModel<SubmissionId>> dispatchSubmission(@RequestBody Submission submission) {
        logger.info("Submission received");
        SubmissionId submissionId = new SubmissionId("-1");
        try{
            logger.info("Calculating submission-ID");
            submissionId = SubmissionId.createId(submission);
            logger.info("Finished calculating submission-ID: " + submissionId.getId());

            Thread t = new Thread(new SubmissionDispatcher(submission,
                    submissionRepository, gradingDTORepository, reportDTORepository));
            t.start();

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
