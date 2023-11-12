package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.Report;

import java.io.Serializable;

public class ErrorReport implements Report, Serializable{

	private String hint;
	private String error;
	private String description;
	
	private boolean showHint;
	private boolean showError;
	private boolean showErrorDescription;

	private ReportAtomType type;

	public ErrorReport() {
		super();
		
		this.hint = null;
		this.error = null;
		this.description = null;

		this.showHint = false;
		this.showError = false;
		this.showErrorDescription = false;
		
		this.type = ReportAtomType.ERROR;
	}

	public ReportAtomType getType(){
		return this.type;
	}
	
	public void setReportAtomType(ReportAtomType type){
		this.type = type;
	}

	public boolean showHint() {
		return this.showHint;
	}
	
	public boolean showError(){
		return this.showError;
	}

	
	public boolean showErrorDescription() {
		return this.showErrorDescription;
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
	}

	public void setShowError(boolean showError){
		this.showError = showError;
	}

	public void setShowErrorDescription(boolean showErrorDescription) {
		this.showErrorDescription = showErrorDescription;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getHint() {
		return this.hint;
	}

	public String getError() {
		return this.error;
	}

	public String getDescription() {
		return this.description;
	}
}
