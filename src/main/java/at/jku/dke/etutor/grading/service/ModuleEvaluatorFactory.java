package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.dlg.DatalogEvaluatorImpl;
import at.jku.dke.etutor.modules.fd.FDEvaluator;
import at.jku.dke.etutor.modules.fd.services.TaskService;
import at.jku.dke.etutor.modules.pm.PmEvaluator;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.uml.UMLEvaluator;
import at.jku.dke.etutor.modules.xquery.XQEvaluatorImpl;
import org.springframework.stereotype.Service;


/**
 * Maps the task-types to the module's implementation of the {@link at.jku.dke.etutor.core.evaluation.Evaluator}
 */
@Service
public class ModuleEvaluatorFactory {
    private ApplicationProperties properties;
    private SQLConstants sqlConstants;
    private TaskService fDTaskService;

    /**
     * The constructor
     */
    public ModuleEvaluatorFactory(
            ApplicationProperties properties,
            SQLConstants sqlConstants, TaskService fDTaskService){
        this.properties = properties;
        this.sqlConstants = sqlConstants;
        this.fDTaskService = fDTaskService;
    }

    /**
     * Maps the task type to the evaluator
     * @param tasktype the task type
     * @return the evaluator
     */
    public Evaluator forTaskType(String tasktype) {
        return switch (tasktype) {
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#SQLTask", "sql" -> new SQLEvaluator(sqlConstants);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#RATask", "ra" -> new RAEvaluator(new SQLEvaluator(sqlConstants), new SQLReporter());
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#XQTask", "xq" -> new XQEvaluatorImpl(properties);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#DLGTask", "dlg" -> new DatalogEvaluatorImpl(properties);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#PmTask", "pm" -> new PmEvaluator();
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#FDTask", "fd" -> new FDEvaluator(fDTaskService);
            case "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#UMLTask", "uml" -> new UMLEvaluator();
            default -> null;
        };
    }

}
