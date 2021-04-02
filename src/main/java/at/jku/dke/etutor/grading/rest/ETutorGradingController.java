package at.jku.dke.etutor.grading.rest;



import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.service.GradingManager;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/grading")
public class ETutorGradingController {

    @GetMapping("/{submissionId}")
    public EntityModel<RestGrading> getGrading(@PathVariable String submissionId){
       return EntityModel.of(GradingManager.gradingMap.get(submissionId),
                linkTo(methodOn(ETutorGradingController.class).getGrading(submissionId.toString())).withRel("self"));
    }
}
