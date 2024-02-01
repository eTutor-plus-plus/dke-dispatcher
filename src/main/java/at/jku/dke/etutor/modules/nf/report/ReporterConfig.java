package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.NFConstants;

public class ReporterConfig {

	private NFConstants.EvalAction evalAction;
	private int diagnoseLevel;

	public ReporterConfig() {
		super();
		this.evalAction = NFConstants.EvalAction.DIAGNOSE;
		this.diagnoseLevel = 0;
	}

	public int getDiagnoseLevel() {
		return this.diagnoseLevel;
	}

	public void setDiagnoseLevel(int diagnoseLevel) {
		this.diagnoseLevel = diagnoseLevel;
	}
	
	
	public NFConstants.EvalAction getEvalAction() {
		return this.evalAction;
	}

	public void setEvalAction(NFConstants.EvalAction evalAction) {
		this.evalAction = evalAction;
	}

}
