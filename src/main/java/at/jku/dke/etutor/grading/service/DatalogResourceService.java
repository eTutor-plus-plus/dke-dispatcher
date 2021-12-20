package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
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

    public void deleteTaskGroup(int id) throws ExerciseManagementException {
        exerciseManager.deleteTaskGroup(id);
    }
}
