package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.objects.dispatcher.SubmissionDTO;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import at.jku.dke.etutor.grading.rest.model.repositories.SubmissionRepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;


import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * API for posting submissions.
 */
@org.springframework.web.bind.annotation.RestController
@org.springframework.web.bind.annotation.RequestMapping("/submission")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorSubmissionController {
    public static final Set<String> runningEvaluations = new ConcurrentSkipListSet<>();
    private final Logger logger;
    private final SubmissionDispatcherService submissionDispatcherService;
    private final SubmissionRepository submissionRepository;

    /**
     * The constructor
     * @param submissionDispatcherService the injected SubmissionDispatcher service that handles the evaluation of a submission
     */
    public ETutorSubmissionController(SubmissionDispatcherService submissionDispatcherService, SubmissionRepository submissionRepository){
        this.logger = (Logger) LoggerFactory.getLogger(ETutorSubmissionController.class);
        this.submissionDispatcherService = submissionDispatcherService;
        this.submissionRepository = submissionRepository;
    }

    /**
     * Takes a submission as parameter, calculates a unique submissionId, asynchronously initializes a SubmissionDispatcher and returns the generated ID
     * @param submissionDto : the submission from the student
     * @param persist : if true, the submission is persisted in the database
     * @return : ResponseEntity containing EntityModel with the generated submissionId and a link under which the grading can be requested
     *          - HttpStatus.INTERNAL_SERVER_ERROR if exception occurs
     */
    @PostMapping("")
    public ResponseEntity<EntityModel<SubmissionId>> dispatchSubmission(@RequestBody SubmissionDTO submissionDto, @RequestHeader(value = "Accept-Language", defaultValue = "de") String language, @RequestParam(required = false, defaultValue = "true") Boolean persist) {
        Submission submission = new Submission(submissionDto);
        logger.info("Submission received");
        SubmissionId submissionId;

        logger.info("Calculating submission-ID");
        submissionId = SubmissionId.createId(submission);
        logger.info("Finished calculating submission-ID: {}", submissionId.getSubmissionId());

        // note: start of evaluation
        runningEvaluations.add(submission.getSubmissionId());
        submissionDispatcherService.run(submission, mapLangToLocale(language), persist);

        return new ResponseEntity<>(EntityModel.of(submissionId,
                linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString(), false)).withRel("grading")),
                HttpStatus.ACCEPTED);
    }

    /**
     * Returns a submission
     * @param submissionUUID the UUID identifying the submission
     * @return a ResponseEntity containing the submission
     */
    @GetMapping("/{submissionUUID}")
    public ResponseEntity<SubmissionDTO> getSubmission(@PathVariable String submissionUUID){
        Optional<Submission> optionalSubmission = this.submissionRepository.findById(submissionUUID);
        if(optionalSubmission.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        SubmissionDTO submissionDTO = new SubmissionDTO();
        var submission = optionalSubmission.get();
        submissionDTO.setSubmissionId(submission.getSubmissionId());
        submissionDTO.setMaxPoints(submission.getMaxPoints());
        submissionDTO.setExerciseId(submission.getExerciseId());
        submissionDTO.setTaskType(submission.getTaskType());
        submissionDTO.setPassedParameters(submission.getPassedParameters());
        submissionDTO.setPassedAttributes(submission.getPassedAttributes());
        return ResponseEntity.ok(submissionDTO);
    }


    /**
     * Maps a String to a Locale
     * @param language the language
     * @return the Locale
     */
    private Locale mapLangToLocale(String language){
        if(language.equalsIgnoreCase("de")) return Locale.GERMAN;
        else return Locale.ENGLISH;
    }
}
