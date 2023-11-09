package at.jku.dke.etutor.modules.ddl.grading;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;
import at.jku.dke.etutor.modules.ddl.analysis.DDLAnalysis;
import at.jku.dke.etutor.modules.ddl.analysis.DDLCriterionAnalysis;
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

    //todo Check function
    public DefaultGrading grade(DDLAnalysis analysis, DDLGraderConfig graderConfig) throws MissingGradingCriterionConfigException {
        String msg;
        int points = 0;
        boolean everyCriterionOk = true;
        Iterator<DDLEvaluationCriterion> evaluationCriterionIterator = graderConfig.iterCriterionsToGrade();
        Iterator<DDLCriterionAnalysis> criterionAnalysisIterator = analysis.iterCriterionAnalysis();
        DDLEvaluationCriterion criterion;
        DDLCriterionGradingConfig criterionGradingConfig;
        DDLCriterionAnalysis criterionAnalysis;
        DefaultGrading grading = new DefaultGrading();

        // Set max points
        grading.setMaxPoints(graderConfig.getMaxPoints());

        while (evaluationCriterionIterator.hasNext()) {
            criterion = evaluationCriterionIterator.next();
            criterionGradingConfig = graderConfig.getCriterionGradingConfig(criterion);

            if(criterionGradingConfig != null) {
                if(analysis.isCriterionAnalyzed(criterion)) {
                    criterionAnalysis = analysis.getCriterionAnalysis(criterion);

                    // Check if there was an exception analysing the criterion
                    if(criterionAnalysis.getAnalysisException() == null) {
                        // Check if the criterion is satisfied
                        if(criterionAnalysis.isCriterionSatisfied()) {
                            points += criterionGradingConfig.getPositivePoints();
                        } else {
                            points -= criterionGradingConfig.getNegativePoints();
                        }
                    } else  {
                        //todo Implment exception handling?
                    }
                } else {
                    msg = "";
                    msg = msg.concat("Could not grade criterion '"  + criterion +  "'. ");

                    this.logger.info(msg);
                    return grading;
                }
            } else {
                msg = "";
                msg = msg.concat("Stopped grading due to errors. ");
                msg = msg.concat("No config for grading criterion' " + criterion + "' available. ");
                msg = msg.concat("This is an internal system error. ");
                msg = msg.concat("Please inform the system administrator.");

                this.logger.error(msg);
                throw new MissingGradingCriterionConfigException(criterion, msg);
            }
        }

        // Check if every criterion is satisfied
        while (criterionAnalysisIterator.hasNext()) {
            criterionAnalysis = criterionAnalysisIterator.next();
            if(criterionAnalysis != null && !criterionAnalysis.isCriterionSatisfied()) {
                everyCriterionOk = false;
            }
        }

        // If every criterion is satisfied = full points; otherwise no points
        if(everyCriterionOk) {
            grading.setPoints(grading.getMaxPoints());
        } else {
            grading.setPoints(0);
        }

        return grading;
    }
}
