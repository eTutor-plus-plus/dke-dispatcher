package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.xquery.XQEvaluatorImpl;
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
     * @param sqlEvaluator the inejcted SQLEvaluator
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
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask" -> new SQLEvaluator(sqlConstants);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#RATask" -> new RAEvaluator(new SQLEvaluator(sqlConstants), new SQLReporter());
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#XQueryTask" -> new XQEvaluatorImpl(properties);
            case "sql" -> new SQLEvaluator(sqlConstants);
            default -> null;
        };
    }
}
