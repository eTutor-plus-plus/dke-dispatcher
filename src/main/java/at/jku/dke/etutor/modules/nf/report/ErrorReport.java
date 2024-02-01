package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;

public class ErrorReport extends DefaultReport {

	private boolean showError;

	private ReportAtomType type;

	public ErrorReport() {
		super();

		this.showError = false;
		
		this.type = ReportAtomType.ERROR;
	}

	public ReportAtomType getType(){
		return this.type;
	}
	
	public void setReportAtomType(ReportAtomType type){
		this.type = type;
	}
	
	public boolean showError(){
		return this.showError;
	}

	public void setShowError(boolean showError){
		this.showError = showError;
	}
}
