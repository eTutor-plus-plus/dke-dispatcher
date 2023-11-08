package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.model.repositories.SubmissionRepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import at.jku.dke.etutor.modules.fd.repositories.TaskRepository;
import at.jku.dke.etutor.modules.fd.services.TaskService;
import at.jku.dke.etutor.modules.fd.utilities.FDSolve;
import at.jku.dke.etutor.modules.fd.utilities.FDTaskSolve;
import at.jku.dke.etutor.objects.dispatcher.SubmissionDTO;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/fd")
public class ETutorFDController {
    private final Logger logger;
    private final SubmissionDispatcherService submissionDispatcherService;
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final GradingDTORepository gradingDTORepository;
    private final SubmissionRepository submissionRepository;

    public ETutorFDController(SubmissionDispatcherService submissionDispatcherService, TaskService taskService,
                              TaskRepository taskRepository, GradingDTORepository gradingDTORepository,
                              SubmissionRepository submissionRepository) {
        this.logger = (Logger) LoggerFactory.getLogger(ETutorSubmissionController.class);
        this.submissionDispatcherService = submissionDispatcherService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.gradingDTORepository = gradingDTORepository;
        this.submissionRepository = submissionRepository;
    }

    @PostMapping("/submission")
    public ResponseEntity<List<String>> dispatchSubmission(@RequestBody FDTaskSolve fdTaskSolve, @RequestHeader(value = "Accept-Language", defaultValue = "de") String language) throws JsonProcessingException {
        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setTaskType("fd");
        List<String> uuids = new ArrayList<>();
        if (fdTaskSolve.getMaxPoints() == 0) {
            fdTaskSolve.setMaxPoints(1);
        }

        submissionDTO.setMaxPoints((int)fdTaskSolve.getMaxPoints());

        if (fdTaskSolve.getType().equals("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#Closure")) {
            Long groupId = Long.parseLong(fdTaskSolve.getId().replace("Closure-",""));
            int numberOfTasks = taskRepository.countByClosureGroupId(groupId);
            for (FDSolve closureSolution : fdTaskSolve.getClosureSolutions()) {
                submissionDTO.setExerciseId(closureSolution.getId().intValue());
                Map<String, String> passedAttributes = new HashMap<>();
                submissionDTO.setPassedAttributes(passedAttributes);
                passedAttributes.put("id", fdTaskSolve.getId());
                passedAttributes.put("type", fdTaskSolve.getType());
                passedAttributes.put("closureSolution", closureSolution.getSolution());
                passedAttributes.put("maxPoints", String.valueOf((fdTaskSolve.getMaxPoints()/Float.valueOf(numberOfTasks))));
                Submission submission = new Submission(submissionDTO);
                logger.info("Submission received");
                SubmissionId submissionId;
                logger.info("Calculating submission-ID");
                submissionId = SubmissionId.createId(submission);
                uuids.add(submissionId.getSubmissionId());
                logger.info("Finished calculating submission-ID: {}", submissionId.getSubmissionId());
                submissionDispatcherService.run(submission, mapLangToLocale(language), true);
            }
        } else {
            submissionDTO.setExerciseId(Integer.parseInt(fdTaskSolve.getId()));
            Map<String, String> passedAttributes = new HashMap<>();
            submissionDTO.setPassedAttributes(passedAttributes);
            passedAttributes.put("id", fdTaskSolve.getId());
            passedAttributes.put("type", fdTaskSolve.getType());
            passedAttributes.put("solution", fdTaskSolve.getSolution());
            passedAttributes.put("maxPoints", String.valueOf((fdTaskSolve.getMaxPoints())));
            if (fdTaskSolve.getNormalFormSolutions() != null) {
                ObjectMapper mapper = new ObjectMapper();
                passedAttributes.put("normalFormSolutions", mapper.writeValueAsString(fdTaskSolve.getNormalFormSolutions()));
            }
            passedAttributes.put("maxPoints", String.valueOf(fdTaskSolve.getMaxPoints()));
            Submission submission = new Submission(submissionDTO);
            logger.info("Submission received");
            SubmissionId submissionId;
            logger.info("Calculating submission-ID");
            submissionId = SubmissionId.createId(submission);
            uuids.add(submissionId.getSubmissionId());
            logger.info("Finished calculating submission-ID: {}", submissionId.getSubmissionId());
            submissionDispatcherService.run(submission, mapLangToLocale(language), true);
        }
        return ResponseEntity.ok(uuids);
    }
    /**
     * Duplicate look at src/main/java/at/jku/dke/etutor/grading/rest/ETutorSubmissionController.java
     * @param language the language
     * @return the Locale
     */
    private Locale mapLangToLocale(String language){
        if(language.equalsIgnoreCase("de")) return Locale.GERMAN;
        else return Locale.ENGLISH;
    }

    @GetMapping("/points")
    public ResponseEntity<Double> getPoints(@RequestParam String uuid) {
        Optional<Grading> optionalGrading = gradingDTORepository.findBySubmissionId(uuid);
        if (optionalGrading.isPresent()) {
            return ResponseEntity.ok(optionalGrading.get().getPoints());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
