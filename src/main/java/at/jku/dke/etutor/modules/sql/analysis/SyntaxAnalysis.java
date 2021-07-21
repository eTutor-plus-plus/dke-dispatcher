package at.jku.dke.etutor.modules.sql.analysis;

import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;

public class SyntaxAnalysis extends AbstractSQLCriterionAnalysis implements SQLCriterionAnalysis{

	private boolean foundSyntaxError;
	private String syntaxErrorDescription;

	public SyntaxAnalysis() {
		super();
		this.foundSyntaxError = false;
		this.syntaxErrorDescription = "";
	}

	public boolean foundSyntaxError(){
		return this.foundSyntaxError;
	}
	
	public void setFoundSyntaxError(boolean foundSyntaxError){
		this.foundSyntaxError = foundSyntaxError;
	}
	
	public void setSyntaxErrorDescription(String syntaxErrorDescription){
		this.syntaxErrorDescription = syntaxErrorDescription;
	}
	
	public String getSyntaxErrorDescription(){
		return this.syntaxErrorDescription;
	}

	public SQLEvaluationCriterion getEvaluationCriterion(){
		return SQLEvaluationCriterion.CORRECT_SYNTAX;
	}
}
