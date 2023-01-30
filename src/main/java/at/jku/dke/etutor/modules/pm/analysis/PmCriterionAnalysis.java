package at.jku.dke.etutor.modules.pm.analysis;


import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

/**
 * Interface defining methods for an analysis in regard to an PmEvaluationCriterion
 */
public interface PmCriterionAnalysis {

    public boolean isCriterionSatisfied();

    public void setCriterionIsSatisfied(boolean b);

    public PmEvaluationCriterion getEvaluationCriterion();

    public void setAnalysisException(AnalysisException e);

    public AnalysisException getAnalysisException();
}
