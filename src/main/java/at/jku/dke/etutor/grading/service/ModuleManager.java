package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;


/**
 * Maps the tasktypes to the modules
 */
@Component
public class ModuleManager{
    private static SQLEvaluator sqlEvaluator;

    public ModuleManager(){
        sqlEvaluator = new SQLEvaluator();
    }

    public static Evaluator getEvaluator(String tasktype){
        switch (tasktype) {
            case  "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask":
                return sqlEvaluator;
            case  "sql":
                    return sqlEvaluator;
        }
        return null;
    }

}
