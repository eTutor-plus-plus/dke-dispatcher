package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.DatalogEvaluatorImpl;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.xquery.XQEvaluatorImpl;
import org.springframework.stereotype.Service;

/**
 * Maps the task-types to the module's implementation of the {@link at.jku.dke.etutor.core.evaluation.Evaluator}
 */
@Service
public class ModuleService {
    private ApplicationProperties properties;
    private SQLConstants sqlConstants;

    /**
     * The constructor
     */
    public ModuleService(
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
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#RATask", "ra" -> new RAEvaluator(new SQLEvaluator(sqlConstants), new SQLReporter());
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#XQTask", "xq" -> new XQEvaluatorImpl(properties);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#DLGTask", "dlg" -> new DatalogEvaluatorImpl(properties);
            default -> null;
        };
    }
}