package at.jku.dke.etutor.modules.sql.analysis;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public interface SQLCriterionAnalysis {

	public boolean isCriterionSatisfied();
	
	public void setCriterionIsSatisfied(boolean b);
	
	public SQLEvaluationCriterion getEvaluationCriterion();
	
	public void setAnalysisException(AnalysisException e);
	
	public AnalysisException getAnalysisException();
}
