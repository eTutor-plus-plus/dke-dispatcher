package at.jku.dke.etutor.grading.rest;


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
public class ETutorGradingController {
    private Logger logger;
    private GradingDTORepository gradingDTORepository;

    /**
     * The construcotr
     * @param gradingDTORepository the injected repository
     */
    public ETutorGradingController(GradingDTORepository gradingDTORepository) {
        this.logger = (Logger) LoggerFactory.getLogger(ETutorGradingController.class);
        this.gradingDTORepository = gradingDTORepository;
    }

    /**
     * Takes the submissionId and fetches and returns
     * the corresponding Grading.
     * @param submissionId the identifier for the GradingDTO as path variable
     * @return ResponseEntity containing EntityModel<GradingDTO> and a self reference (link)
     *          - HttpStatus.NOT_FOUND if no Grading is available for the given id.
     *          - HttpStatus.OK if Grading is available
     */
    @CrossOrigin(origins="*")
    @GetMapping("/{submissionId}")
    public ResponseEntity<EntityModel<GradingDTO>> getGrading(@PathVariable String submissionId) {
        logger.info( "Received request for Grading with Submission ID:  {}",  submissionId);
        logger.info("Fetching Grading from database ");

        Optional<GradingDTO> optional = gradingDTORepository.findById(submissionId);
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
