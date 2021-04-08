package at.jku.dke.etutor.grading.rest;


import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.service.DatabaseManager;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/grading")
public class ETutorGradingController {
    private Logger logger;

    public ETutorGradingController() {
        this.logger = Logger.getLogger("at.jku.dke.etutor.grading");
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<EntityModel<RestGrading>> getGrading(@PathVariable String submissionId) {
        logger.info("Received request for Grading with Submission ID: " + submissionId);
        logger.info("Fetching Grading from database ");

        RestGrading grading = DatabaseManager.getGrading(submissionId);
        if (grading != null) {
            logger.info("Finished fetching Grading from database ");
            return ResponseEntity.ok(EntityModel.of(grading,
                    linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString())).withRel("self")));
        } else  {
            logger.info("No Grading found for Submission ID: " + submissionId);
            return new ResponseEntity<>(EntityModel.of(new RestGrading(0, 0),
                        linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString())).withRel("self")),
                        HttpStatus.NOT_FOUND);
        }
    }
}
