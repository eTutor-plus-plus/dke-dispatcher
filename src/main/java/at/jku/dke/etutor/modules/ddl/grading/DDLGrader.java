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

    public DefaultGrading grade(DDLAnalysis analysis, DDLGraderConfig graderConfig) {
        String msg;
        int points = 0;
        boolean correctTables = false;
        boolean correctColumns = false;
        Iterator<DDLEvaluationCriterion> evaluationCriterionIterator = graderConfig.iterCriterionsToGrade();
        DDLEvaluationCriterion criterion;
        DDLCriterionAnalysis criterionAnalysis;
        DefaultGrading grading = new DefaultGrading();

        // Set max points
        grading.setMaxPoints(graderConfig.getMaxPoints());

        while (evaluationCriterionIterator.hasNext()) {
            criterion = evaluationCriterionIterator.next();

            if(analysis.isCriterionAnalyzed(criterion)) {
                criterionAnalysis = analysis.getCriterionAnalysis(criterion);

                // Check that there was no exception analysing the criterion and also the criterion is satisfied
                if(criterionAnalysis.getAnalysisException() == null && criterionAnalysis.isCriterionSatisfied()) {
                    // Add the points for this criterion to the total points
                    if(criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                        points += graderConfig.getTablePoints();
                        correctTables = true;
                    } else if(criterion.equals(DDLEvaluationCriterion.CORRECT_COLUMNS)) {
                        points += graderConfig.getColumnPoints();
                        correctColumns = true;
                    } else if(criterion.equals(DDLEvaluationCriterion.CORRECT_PRIMARY_KEYS)) {
                        points += graderConfig.getPrimaryKeyPoints();
                    } else if(criterion.equals(DDLEvaluationCriterion.CORRECT_FOREIGN_KEYS)) {
                        points += graderConfig.getForeignKeyPoints();
                    }
                } else  {
                    // Check if the criterion is Syntax -> return with 0 points and log
                    if(criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                        msg = "SQL DDL Syntax not correct.";
                        this.logger.info(msg);

                        grading.setPoints(0);
                        return grading;
                    } else if(criterion.equals(DDLEvaluationCriterion.CORRECT_TABLES)) {
                        // Set 0 points - because columns, primary and foreign keys are only check on available tables
                        grading.setPoints(0);
                        return grading;
                    }
                }
            } else {
                msg = "";
                msg = msg.concat("Could not grade criterion '"  + criterion +  "'. ");
                this.logger.info(msg);

                grading.setPoints(0);
                return grading;
            }
        }

        // Check again for the constraint criterion - constraints are only fully tested if tables and column criterions are correct
        evaluationCriterionIterator = graderConfig.iterCriterionsToGrade();
        while (evaluationCriterionIterator.hasNext()) {
            criterion = evaluationCriterionIterator.next();

            if(analysis.isCriterionAnalyzed(criterion)) {
                criterionAnalysis = analysis.getCriterionAnalysis(criterion);

                // Check that there was no exception analysing the criterion and also the criterion is satisfied
                if(criterionAnalysis.getAnalysisException() == null && criterionAnalysis.isCriterionSatisfied()) {
                  // Add the points for this criterion to the total points
                  if(criterion.equals(DDLEvaluationCriterion.CORRECT_CONSTRAINTS) && correctTables && correctColumns) {
                      points += graderConfig.getConstraintPoints();
                  }
                } else  {
                    // Check if the criterion is Syntax -> return with 0 points and log
                    if(criterion.equals(DDLEvaluationCriterion.CORRECT_SYNTAX)) {
                        msg = "SQL DDL Syntax not correct.";
                        this.logger.info(msg);

                        grading.setPoints(0);
                        return grading;
                    }
                }
            } else {
                msg = "";
                msg = msg.concat("Could not grade criterion '"  + criterion +  "'. ");
                this.logger.info(msg);

                grading.setPoints(0);
                return grading;
            }
        }

        // Set the reached points
        grading.setPoints(points);

        return grading;
    }
}
