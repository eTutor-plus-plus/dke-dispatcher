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

/**
 * Service for handling datalog resources
 */
@Service
public class DatalogResourceService {
    private ApplicationProperties properties;
    private DatalogExerciseManagerImpl exerciseManager;
    private SubmissionDispatcherService dispatcherService;
    private GradingDTORepository gradingDTORepository;
    private final String TASK_TYPE = "dlg";

    /**
     * Initializes the bean
     * @param dispatcherService the {@link SubmissionDispatcherService} for evaluation of exercises
     * @param gradingDTORepository the {@link GradingDTORepository} for handling {@link GradingDTO} entities
     * @param properties the {@link ApplicationProperties} properties of the application
     * @param exerciseManager the {@link DatalogExerciseManagerImpl} for handling resources
     */
    public DatalogResourceService(SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository, ApplicationProperties properties, DatalogExerciseManagerImpl exerciseManager){
        this.properties=properties;
        this.exerciseManager=exerciseManager;
        this.dispatcherService=dispatcherService;
        this.gradingDTORepository=gradingDTORepository;
    }

    /**
     * Creates a task group by persisting the facts and returning the id.
     * Facts are checked for validity.
     * @param facts the facts
     * @return the id of the facts
     * @throws DatalogParseException if facts are not valid
     * @throws ExerciseManagementException if an error occurs while persisting the facts
     */
    public int createTaskGroup(DatalogTaskGroupDTO facts) throws DatalogParseException, ExerciseManagementException {
        if(!facts.getFacts().contains("-")){
            validate(facts.getFacts());
        }
        return exerciseManager.createTaskGroup(facts);
    }

    /**
     * Utility method that checks if facts are syntactically correct
     * @param facts the facts to be validated
     * @throws DatalogParseException if facts are not valid
     */
    private void validate(String facts) throws DatalogParseException {
        var r = new StringReader(facts);
        DatalogTokenizer t = new DatalogTokenizer(r);
        DatalogParser.parseProgram(t);
    }

    /**
     * Deletes a task group
     * @param id the id
     * @param deleteTasks indicates if exercises should be deleted as well
     * @throws ExerciseManagementException if an error occurs while deleting the group
     */
    public void deleteTaskGroup(int id, boolean deleteTasks) throws ExerciseManagementException {
        exerciseManager.deleteTaskGroup(id, deleteTasks);
    }

    /**
     * Updates a task group
     * @param id the id
     * @param newFacts the new facts
     * @throws DatalogParseException if facts are not valid
     * @throws ExerciseManagementException if error occurs while persisting the facts
     */
    public void updateTaskGroup(int id, String newFacts) throws DatalogParseException, ExerciseManagementException {
        if(!newFacts.contains("-")){
            validate(newFacts);
        }

        exerciseManager.updateTaskGroup(id, newFacts);

    }

    /**
     * Creates an exercise
     * @param exerciseBean the {@link DatalogExerciseBean} representing the exercise
     * @return the id of the created exercise
     * @throws ExerciseManagementException if an error occurs while persisting the exercise
     */
    public int createExercise(DatalogExerciseBean exerciseBean) throws ExerciseManagementException {
        return exerciseManager.createExercise(exerciseBean);
    }

    /**
     * Updates an exercise
     * @param id the id
     * @param exerciseBean the {@link DatalogExerciseBean} representing the exercise
     * @return a {@link Boolean} indicating if updating has been successful
     * @throws ExerciseManagementException if an error occurs while updating exercise
     */
    public boolean modifyExercise(int id, DatalogExerciseBean exerciseBean) throws ExerciseManagementException {
        return exerciseManager.modifyExercise(id, exerciseBean);
    }

    /**
     * Deletes an exercise
     * @param id the id
     * @return a {@link Boolean} indicating if deletion has been successful
     * @throws ExerciseManagementException if an error occurs while deleting
     */
    public boolean deleteExercise(int id) throws ExerciseManagementException {
        return exerciseManager.deleteExercise(id);
    }

    /**
     * Returns an exercise
     * @param id the id of the exercise
     * @return the exercise
     * @throws ExerciseManagementException if an error occurs
     */
    public DatalogExerciseBean fetchExercise(int id) throws ExerciseManagementException {
        return exerciseManager.fetchExercise(id);
    }

    /**
     * Fetches facts for a datalog group
     * @param id the id of the group
     * @return a String with the facts
     * @throws ExerciseManagementException if an error occurs while fetching the facts
     */
    public String fetchFacts(int id) throws ExerciseManagementException {
        return exerciseManager.fetchFacts(id);
    }

    /**
     * Gets the {@link GradingDTO} for an exercise, using the solution as submission.
     * @param exercise_id the id
     * @param action the action for the evaluation
     * @param diagnose_level the diagnose-level for the evaluation
     * @return the {@link GradingDTO}
     * @throws ExerciseManagementException if an error occurs while fetching exercise information
     * @throws InterruptedException if an error occurs while waiting for the evaluation to finish
     */
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

        String sub = "asd";
        //sub = exercise.getQuery();
        attributes.put("submission", sub);

        submission.setPassedAttributes(attributes);
        submission.setPassedParameters(new HashMap<>());
        dispatcherService.run(submission, Locale.GERMAN);
        Thread.sleep(10000);
        return gradingDTORepository.findById(id).isPresent() ? gradingDTORepository.findById(id).get() : null;
    }
}
