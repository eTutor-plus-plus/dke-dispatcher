package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseBean;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseManagerImpl;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParser;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogTokenizer;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class DatalogResourceService {
    private ApplicationProperties properties;
    private DatalogExerciseManagerImpl exerciseManager;
    private SubmissionDispatcherService dispatcherService;
    private GradingDTORepository gradingDTORepository;
    private final String TASK_TYPE = "dlg";

    public DatalogResourceService(SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository, ApplicationProperties properties, DatalogExerciseManagerImpl exerciseManager){
        this.properties=properties;
        this.exerciseManager=exerciseManager;
        this.dispatcherService=dispatcherService;
        this.gradingDTORepository=gradingDTORepository;
    }


    public int createTaskGroup(DatalogTaskGroupDTO facts) throws DatalogParseException, ExerciseManagementException {
        if(!facts.getFacts().contains("-")){
            validate(facts.getFacts());
        }
        return exerciseManager.createTaskGroup(facts);
    }

    private void validate(String facts) throws DatalogParseException {
        var r = new StringReader(facts);
        DatalogTokenizer t = new DatalogTokenizer(r);
        DatalogParser.parseProgram(t);
    }

    public void deleteTaskGroup(int id, boolean deleteTasks) throws ExerciseManagementException {
        exerciseManager.deleteTaskGroup(id, deleteTasks);
    }

    public void updateTaskGroup(int id, String newFacts) throws DatalogParseException, ExerciseManagementException {
        if(!newFacts.contains("-")){
            validate(newFacts);
        }

        exerciseManager.updateTaskGroup(id, newFacts);

    }

    public int createExercise(DatalogExerciseBean exerciseBean) throws ExerciseManagementException {
        return exerciseManager.createExercise(exerciseBean);
    }

    public boolean modifyExercise(int id, DatalogExerciseBean exerciseBean) throws ExerciseManagementException {
        return exerciseManager.modifyExercise(id, exerciseBean);
    }

    public boolean deleteExercise(int id) throws ExerciseManagementException {
        return exerciseManager.deleteExercise(id);
    }

    public DatalogExerciseBean fetchExercise(int id) throws ExerciseManagementException {
        return exerciseManager.fetchExercise(id);
    }

    public String fetchFacts(int id) throws ExerciseManagementException {
        return exerciseManager.fetchFacts(id);
    }

    public GradingDTO getGradingForExercise(int exercise_id, String action, String diagnose_level) throws ExerciseManagementException, InterruptedException {
        Submission submission = new Submission();
        String id = UUID.randomUUID().toString();
        submission.setSubmissionId(id);
        submission.setExerciseId(exercise_id);
        submission.setTaskType(TASK_TYPE);
        submission.setMaxPoints(1);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("diagnoseLevel", diagnose_level);
        attributes.put("action", action);

        var exercise = exerciseManager.fetchExercise(exercise_id);

        if (exercise==null) return null;

        attributes.put("submission", exercise.getQuery());

        submission.setPassedAttributes(attributes);
        submission.setPassedParameters(new HashMap<>());
        dispatcherService.run(submission, Locale.GERMAN);
        Thread.sleep(10000);
        return gradingDTORepository.findById(id).isPresent() ? gradingDTORepository.findById(id).get() : null;
    }
}
