package at.jku.dke.etutor.modules.ddl.grading;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

//todo Necessary??
public class MissingGradingCriterionConfigException extends Exception {
    //region Fields
    private final DDLEvaluationCriterion criterion;
    //endregion

    public MissingGradingCriterionConfigException(DDLEvaluationCriterion evaluationCriterion) {
        super();
        this.criterion = evaluationCriterion;
    }

    public MissingGradingCriterionConfigException(DDLEvaluationCriterion evaluationCriterion, String msg) {
        super(msg);
        this.criterion = evaluationCriterion;
    }

    //region Getter
    public DDLEvaluationCriterion getDDLEvaluationCriterion() {
        return criterion;
    }
    //endregion
}
