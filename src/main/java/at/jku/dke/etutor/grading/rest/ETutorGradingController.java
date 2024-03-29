package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.model.repositories.SubmissionRepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import at.jku.dke.etutor.objects.dispatcher.GradingDTO;
import at.jku.dke.etutor.objects.dispatcher.ReportDTO;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static at.jku.dke.etutor.grading.rest.ETutorSubmissionController.runningEvaluations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Api for handling GradingDTO resources
 */

@RestController
@RequestMapping("/grading")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorGradingController {
    public static final ConcurrentMap<String, Thread> waitingThreadMap = new ConcurrentHashMap<>();
    private final Logger logger;
    private final GradingDTORepository gradingDTORepository;
    private final ApplicationProperties properties;

    /**
     * The constructor
     * @param gradingDTORepository the injected repository
     */
    public ETutorGradingController(GradingDTORepository gradingDTORepository, ApplicationProperties properties) {
        this.logger = (Logger) LoggerFactory.getLogger(ETutorGradingController.class);
        this.gradingDTORepository = gradingDTORepository;
        this.properties = properties;
    }

    /**
     * Takes the submissionId and fetches and returns
     * the corresponding Grading. If no grading is found at first, a retry is attempted after the duration specified in application.properties.
     * @param submissionId the identifier for the GradingDTO as path variable
     * @param delete if true, the grading is deleted from the database after returned
     * @return ResponseEntity containing EntityModel<GradingDTO> and a self reference (link)
     *          - HttpStatus.NOT_FOUND if no Grading is available for the given id.
     *          - HttpStatus.OK if Grading is available
     */
    @GetMapping("/{submissionId}")
    public ResponseEntity<EntityModel<GradingDTO>> getGrading(@PathVariable String submissionId, @RequestParam(required = false, defaultValue = "false") Boolean delete)  {
        logger.info( "Received request for Grading with Submission ID:  {}",  submissionId);
        logger.info("Fetching Grading from database ");
        GradingDTO gradingDTO = new GradingDTO();

        waitingThreadMap.put(submissionId, Thread.currentThread());
        for(int i = 0; ((i * properties.getGrading().getSleepDuration()) < properties.getGrading().getMaxWaitTime())
                && runningEvaluations.contains(submissionId); i++){
            // wait till evaluation has finished but no longer than the defined max wait time
            try {
                Thread.sleep(properties.getGrading().getSleepDuration());
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
                // exit loop if thread is interrupted
                break;
            }
        }
        waitingThreadMap.remove(submissionId);

        Optional<Grading> optional = gradingDTORepository.findById(submissionId);
        if (optional.isPresent()) {
            logger.info("Finished fetching Grading from database ");
            Grading grading = optional.get();
            ReportDTO reportDTO = new ReportDTO();
            if(grading.getReport() != null){
                reportDTO.setDescription(grading.getReport().getDescription());
                reportDTO.setHint(grading.getReport().getHint());
                reportDTO.setError(grading.getReport().getError());
            }
            gradingDTO.setReport(reportDTO);
            gradingDTO.setPoints(grading.getPoints());
            gradingDTO.setMaxPoints(grading.getMaxPoints());
            gradingDTO.setResult(grading.getResult());
            gradingDTO.setSubmissionId(grading.getSubmissionId());
            gradingDTO.setSubmissionSuitsSolution(grading.isSubmissionSuitsSolution());

            if(Boolean.TRUE.equals(delete))
                gradingDTORepository.deleteById(submissionId);

            return ResponseEntity.ok(EntityModel.of(gradingDTO,
                    linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId, false)).withRel("self")));
        } else  {
            logger.info("No Grading found for Submission ID: {}", submissionId);
            return new ResponseEntity<>(EntityModel.of(gradingDTO,
                        linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId, false)).withRel("self")),
                        HttpStatus.NOT_FOUND);
        }
    }
}
