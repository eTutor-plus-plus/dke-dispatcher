package at.jku.dke.etutor.modules.sql.report;

import at.jku.dke.etutor.modules.sql.SQLEvaluationAction;

public class SQLReporterConfig {

	private int diagnoseLevel;
	private SQLEvaluationAction action;

	public SQLReporterConfig() {
		super();
		this.diagnoseLevel = 0;
		this.action = SQLEvaluationAction.RUN;
	}

	public int getDiagnoseLevel() {
		return this.diagnoseLevel;
	}

	public void setDiagnoseLevel(int diagnoseLevel) {
		this.diagnoseLevel = diagnoseLevel;
	}
	
	
	public SQLEvaluationAction getAction() {
		return this.action;
	}

	public void setAction(SQLEvaluationAction action) {
		this.action = action;
	}
}
