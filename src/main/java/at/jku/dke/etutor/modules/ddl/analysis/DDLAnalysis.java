package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.HashMap;

public class DDLAnalysis extends DefaultAnalysis implements Analysis {
    //region Fields
    private AnalysisException exception;
    private HashMap<DDLEvaluationCriterion, DDLCriterionAnalysis> criterionAnalysis;
    //endregion

    public DDLAnalysis() {
        this.exception = null;
        this.criterionAnalysis = new HashMap<>();
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
     * @param criterion
     * @param analysis
     */
    public void addCriterionAnalysis(DDLEvaluationCriterion criterion, DDLCriterionAnalysis analysis) {
        this.criterionAnalysis.put(criterion, analysis);
    }
}
