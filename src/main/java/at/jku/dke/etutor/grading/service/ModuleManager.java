package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import org.springframework.stereotype.Component;


/**
 * Maps the tasktypes to the modules
 */
@Component
public class ModuleManager{
    private static Evaluator sqlEvaluator;

    public ModuleManager(){
        sqlEvaluator = new SQLEvaluator();

    }

    public static Evaluator getEvaluator(String tasktype){
        switch (tasktype) {
            case  "sql":
                    return sqlEvaluator;
        }
        return null;
    }

}
