package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.HashMap;
import java.util.Iterator;

public class DDLAnalysis extends DefaultAnalysis implements Analysis {
    //region Fields
    private int exerciseId;
    private AnalysisException exception;
    private HashMap<DDLEvaluationCriterion, DDLCriterionAnalysis> criterionAnalysis;
    //endregion

    public DDLAnalysis() {
        this.exerciseId = 0;
        this.exception = null;
        this.criterionAnalysis = new HashMap<>();
    }

    //region Getter/Setter

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    /**
     * Function to set the analysis exception
     * @param exception Specifies the exception
     */
    public void setAnalysisException(AnalysisException exception){
        this.exception = exception;
    }

    /**
     * Function to add a criterion analysis
     * @param criterion Specifies the evaluation criterion
     * @param analysis Specifies the analysis
     */
    public void addCriterionAnalysis(DDLEvaluationCriterion criterion, DDLCriterionAnalysis analysis) {
        this.criterionAnalysis.put(criterion, analysis);
    }

    /**
     * Function to create an iterator for the DDLCriterionAnalysis
     * @return Returns an iterator
     */
    public Iterator<DDLCriterionAnalysis> iterCriterionAnalysis() {
        return this.criterionAnalysis.values().iterator();
    }

    /**
     * Function to check if the criterion was analyzed
     * @param evaluationCriterion Specifies the criterion
     * @return Returns true if it was analyzed; otherwise false
     */
    public boolean isCriterionAnalyzed(DDLEvaluationCriterion evaluationCriterion) {
        return this.criterionAnalysis.containsKey(criterionAnalysis);
    }

    /**
     * Function to get the DDLCriterionAnalysis object for the specified criterion
     * @param criterion Specifies the criterion
     * @return Returns the analysis for this criterion
     */
    public DDLCriterionAnalysis getCriterionAnalysis(DDLEvaluationCriterion criterion) {
        return this.criterionAnalysis.get(criterion);
    }
    //endregion
}
