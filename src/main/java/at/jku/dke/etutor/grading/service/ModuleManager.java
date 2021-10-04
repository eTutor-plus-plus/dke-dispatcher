package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import org.springframework.stereotype.Service;

/**
 * Maps the task-types to the modules implementation of the Evaluator interface
 */
@Service
public class ModuleManager{
    private ApplicationProperties properties;
    private SQLConstants sqlConstants;

    /**
     * The constructor
     */
    public ModuleManager(
            ApplicationProperties properties,
            SQLConstants sqlConstants){
        this.properties = properties;
        this.sqlConstants = sqlConstants;
    }

    /**
     * Maps the task type to the evaluator
     * @param tasktype the task type
     * @return the evaluator
     */
    public Evaluator getEvaluator(String tasktype) {
        return switch (tasktype) {
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask", "sql" -> new SQLEvaluator(sqlConstants);
            default -> null;
        };
    }
}
