package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import org.springframework.stereotype.Service;

/**
 * Maps the task-types to the modules implementation of the Evaluator interface
 */
@Service
public class ModuleManager{
    private final SQLEvaluator sqlEvaluator;
    private final RAEvaluator raEvaluator;
    private final ApplicationProperties properties;

    /**
     * The constructor
     * @param sqlEvaluator the inejcted SQLEvaluator
     */
    public ModuleManager(SQLEvaluator sqlEvaluator, RAEvaluator raEvaluator, ApplicationProperties properties){
        this.sqlEvaluator = sqlEvaluator;
        this.raEvaluator = raEvaluator;
        this.properties = properties;
    }

    /**
     * Maps the task type to the evaluator
     * @param tasktype the task type
     * @return the evaluator
     * @throws Exception if no evaluator for the given task type has been found
     */
    public Evaluator getEvaluator(String tasktype) {
        return switch (tasktype) {
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask" -> sqlEvaluator;
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#RATask" -> raEvaluator;
            case "sql" -> sqlEvaluator;
            default -> null;
        };
    }
}
