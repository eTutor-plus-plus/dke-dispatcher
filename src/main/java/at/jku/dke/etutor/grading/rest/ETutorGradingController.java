package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.ETutorCORSPolicy;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Api for handling GradingDTO resources
 */

@RestController
@RequestMapping("/grading")
@CrossOrigin(origins= ETutorCORSPolicy.CORS_POLICY)
public class ETutorGradingController {
    private Logger logger;
    private GradingDTORepository gradingDTORepository;
    private ApplicationProperties properties;

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
     * @return ResponseEntity containing EntityModel<GradingDTO> and a self reference (link)
     *          - HttpStatus.NOT_FOUND if no Grading is available for the given id.
     *          - HttpStatus.OK if Grading is available
     */
    @GetMapping("/{submissionId}")
    public ResponseEntity<EntityModel<GradingDTO>> getGrading(@PathVariable String submissionId)  {
        logger.info( "Received request for Grading with Submission ID:  {}",  submissionId);
        logger.info("Fetching Grading from database ");

        Optional<GradingDTO> optional = gradingDTORepository.findById(submissionId);
        if(optional.isEmpty()){
            try {
                Thread.sleep(properties.getGrading().getSleepDuration());
            } catch (InterruptedException e) {
               logger.info(e.getMessage());
            }
            optional = gradingDTORepository.findById(submissionId);
        }
        if (optional.isPresent()) {
            logger.info("Finished fetching Grading from database ");
            GradingDTO grading = optional.get();
            return ResponseEntity.ok(EntityModel.of(grading,
                    linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId)).withRel("self")));
        } else  {
            logger.info("No Grading found for Submission ID: {}", submissionId);
            return new ResponseEntity<>(EntityModel.of(new GradingDTO(),
                        linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId)).withRel("self")),
                        HttpStatus.NOT_FOUND);
        }
    }
}
