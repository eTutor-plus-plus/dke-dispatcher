package at.jku.dke.etutor.modules.ddl.grading;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;
import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalysis;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class DDLGrader {
    //region Fields
    private Logger logger;
    //endregion

    public DDLGrader() {
        try {
            this.logger = (Logger) LoggerFactory.getLogger(DDLGrader.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefaultGrading grade(DDLAnalysis analysis, DDLGraderConfig graderConfig) {
        String msg;
        Iterator<DDLEvaluationCriterion> iterator = graderConfig.iterCriterionsToGrade();
        DDLEvaluationCriterion criterion;
        DDLCriterionGradingConfig gradingConfig;

        while (iterator.hasNext()) {
            criterion = iterator.next();
            gradingConfig = graderConfig.getCriterionGradingConfig(criterion);

            if(gradingConfig != null) {

            } else {
                msg = "";
                msg = msg.concat("Stopped grading due to errors. ");
                msg = msg.concat("No config for grading criterion' " + criterion + "' available. ");
                msg = msg.concat("This is an internal system error. ");
                msg = msg.concat("Please inform the system administrator.");

                this.logger.error(msg);
            }
        }

        return null;
    }
}
