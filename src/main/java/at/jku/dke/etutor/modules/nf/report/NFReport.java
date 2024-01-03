package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.Report;

import java.util.List;
import java.util.Vector;

public class NFReport implements Report {

	private String prologue;

	private boolean showPrologue;

	private final Vector<ErrorReport> errorReports;
	private final Vector<ErrorReportGroup> errorReportGroups;
	
	public NFReport() {
		super();
		this.prologue = null;
		this.showPrologue = true;
		
		this.errorReports = new Vector<>();
		this.errorReportGroups = new Vector<>();
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

	public List<ErrorReport> getErrorReports(){
		return (Vector<ErrorReport>)this.errorReports.clone();
	}

	public void addErrorReport(ErrorReport report){
		this.errorReports.add(report);
	}

	public List<ErrorReportGroup> getErrorReportGroups(){
		return (Vector<ErrorReportGroup>)this.errorReportGroups.clone();
	}

	public void addErrorReportGroup(ErrorReportGroup reportGroup){
		this.errorReportGroups.add(reportGroup);
	}
}
