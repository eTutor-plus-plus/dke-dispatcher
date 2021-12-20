package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseBean;
import at.jku.dke.etutor.modules.dlg.exercise.DatalogExerciseManagerImpl;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParseException;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogParser;
import edu.harvard.seas.pl.abcdatalog.parser.DatalogTokenizer;
import org.springframework.stereotype.Service;

import java.io.StringReader;

@Service
public class DatalogResourceService {
    private ApplicationProperties properties;
    private DatalogExerciseManagerImpl exerciseManager;

    public DatalogResourceService(ApplicationProperties properties, DatalogExerciseManagerImpl exerciseManager){
        this.properties=properties;
        this.exerciseManager=exerciseManager;
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
}
