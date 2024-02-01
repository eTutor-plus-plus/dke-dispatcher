package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;

import java.util.LinkedList;
import java.util.List;

public class NFReport extends ErrorReport {

	private String prologue;

	private boolean showPrologue;

	private final List<ErrorReport> errorReports;
	private final List<ErrorReportGroup> errorReportGroups;
	
	public NFReport() {
		super();
		this.prologue = null;
		this.showPrologue = true;
		
		this.errorReports = new LinkedList<>();
		this.errorReportGroups = new LinkedList<>();
	}

	public boolean showPrologue() {
		return this.showPrologue;
	}

	public void setShowPrologue(boolean showPrologue){
		this.showPrologue = showPrologue;
	}

	public void setPrologue(String prologue){
		this.prologue = prologue;
	}

	public String getPrologue(){
		return this.prologue;
	}

	public List<ErrorReport> getErrorReports() {
		return new LinkedList<>(this.errorReports);
	}

	public void addErrorReport(ErrorReport report){
		this.errorReports.add(report);
	}

	public List<ErrorReportGroup> getErrorReportGroups() {
		return new LinkedList<>(this.errorReportGroups);
	}

	public void addErrorReportGroup(ErrorReportGroup reportGroup){
		this.errorReportGroups.add(reportGroup);
	}
}
