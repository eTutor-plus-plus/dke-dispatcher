package at.jku.dke.etutor.modules.pm.grading;

import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;
import at.jku.dke.etutor.modules.pm.analysis.PmCriterionAnalysis;
import at.jku.dke.etutor.modules.pm.analysis.PmPartialSubmissionAnalysis;

import java.util.HashMap;

/**
 * Class follows same idea as pmPartialSubmissionAnalysis class
 * store for each used criteria achieved points and sum those points to get totalAchievedPoints
 * for this partial submission
 *
 * @author Falk Görner
 * @version 1.0
 */
public class PmPartialSubmissionGrading {

    // OBJECT FIELD
    private int totalAchievedPoints; // total achieved points per submittedAnswer over all criteria (might be multiple in the future, not in version 1.0)
    PmPartialSubmissionAnalysis pmPartialSubmissionAnalysis;
    PmGraderConfig pmGraderConfig;
    HashMap<PmEvaluationCriterion, Integer> pointsPerCriterion;

    // CONSTRUCTOR
    public PmPartialSubmissionGrading(PmPartialSubmissionAnalysis partialSubmissionAnalysis, PmGraderConfig graderConfig){
        this.totalAchievedPoints = 0;
        this.pmPartialSubmissionAnalysis = partialSubmissionAnalysis;
        this.pmGraderConfig = graderConfig;
        this.pointsPerCriterion = new HashMap<PmEvaluationCriterion, Integer>();
    }

    // METHODS

    /**
     * Method grade: Main method for grading a partial submission
     * checks for each criterion that is used if criterion is satisfied and if yes, grades points.
     * Code is designed for multiple, potential criteria per submission,
     * but in version 1.0 each submission is evaluated on only one criterion (keep possibilities open for the future)
     *
     * Points for a correct criterion can be given, even if partial mission is not correct in total
     * (e.g. potential second criterion is wrong => makes the partial submission false)
     *
     * @param partialPoints Setting option to grade partial points or not
     */
    public void grade(boolean partialPoints){
        // declaration
        String message;
        int totalPoints;
        Integer criterionPoints;
        PmEvaluationCriterion evaluationCriterion;
        PmCriterionAnalysis criterionAnalysis;
        PmCriterionGradingConfig criterionGradingConfig;

        totalPoints = 0;
        try{
            // review each criterion applied to partial submission
            for(var entry: pmPartialSubmissionAnalysis.getCriterionAnalysis().entrySet()){
                // initialization
                criterionPoints = 0;
                evaluationCriterion = entry.getKey();
                criterionGradingConfig = pmGraderConfig.getCriterionGradingConfig(evaluationCriterion);
                criterionAnalysis = entry.getValue();

                if(criterionGradingConfig != null){
                    if(criterionAnalysis.getAnalysisException() == null && criterionAnalysis.isCriterionSatisfied()){
                        criterionPoints += criterionGradingConfig.getPositivePoints();
                    }else{
                        criterionPoints += criterionGradingConfig.getNegativePoints();
                    }
                }else{
                    message = "";
                    message = message.concat("Stopped grading due to errors. ");
                    message = message.concat("No config for grading criterion' " + evaluationCriterion + "' available. ");
                    message = message.concat("This is an internal system error. ");
                    message = message.concat("Please inform the system administrator.");

                    //this.logger.error(message);
                    throw new Exception(message);
                }

                addPointsPerCriterion(evaluationCriterion, criterionPoints);
                totalPoints+= criterionPoints;
            }

            setTotalAchievedPoints(partialPoints, totalPoints);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void setTotalAchievedPoints(boolean partialPoints, int totalAchievedPoints) {
        if(partialPoints){
            // grade points for partial submission even if partial submission is not completely correct
            // (only single criteria are correct)
            this.totalAchievedPoints = totalAchievedPoints;
        }else{  // partialPoints == false
            // grade points only if partial submission is completely correct
            if(pmPartialSubmissionAnalysis.isPartialSubmissionCorrect()){
                this.totalAchievedPoints = totalAchievedPoints;
            } // else: totalAchievedPoints == 0
        }
    }

    //GETTER/SETTER
    public int getTotalAchievedPoints() {
        return totalAchievedPoints;
    }
    public Integer getPointsPerCriterion(PmEvaluationCriterion criterion) {
        return this.pointsPerCriterion.get(criterion);
    }
    public HashMap<PmEvaluationCriterion, Integer> getPointsForCriteria() {
        return pointsPerCriterion;
    }
    public void addPointsPerCriterion(PmEvaluationCriterion criterion, Integer points){
        this.pointsPerCriterion.put(criterion, points);
    }

}
