package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

public interface DDLCriterionAnalysis {
    boolean isCriterionSatisfied();

    void setCriterionIsSatisfied(boolean b);

    DDLEvaluationCriterion getEvaluationCriterion();

    void setAnalysisException(AnalysisException e);

    AnalysisException getAnalysisException();
}
