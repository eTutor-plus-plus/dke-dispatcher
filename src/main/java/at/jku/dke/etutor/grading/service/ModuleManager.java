package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * Maps the tasktypes to the modules
 */
@Service
public class ModuleManager{
    private static SQLEvaluator sqlEvaluator;

    public ModuleManager(){
        sqlEvaluator = new SQLEvaluator();
    }

    public Evaluator getEvaluator(String tasktype) throws Exception{
        switch (tasktype) {
            case  "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask":
                return sqlEvaluator;
            case  "sql":
                    return sqlEvaluator;
        }
        throw new Exception("Could not find evaluator");
    }
}
