package at.jku.dke.etutor.modules.nf.report;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

public class Report implements etutor.core.evaluation.Report, Serializable {

	private String prologue;

	private boolean showPrologue;

	private Vector errorReports;
	private Vector errorReportGroups;
	
	public Report() {
		super();
		this.prologue = null;
		this.showPrologue = true;
		
		this.errorReports = new Vector();
		this.errorReportGroups = new Vector();
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

	public Vector getErrorReports(){
		return (Vector)this.errorReports.clone();
	}
	
	public Iterator iterErrorReports(){
		return this.errorReports.iterator();
	}
	
	public void addErrorReport(ErrorReport report){
		this.errorReports.add(report);
	}

	public Vector getErrorReportGroups(){
		return (Vector)this.errorReportGroups.clone();
	}
	
	public Iterator iterErrorReportGroups(){
		return this.errorReportGroups.iterator();
	}
	
	public void addErrorReportGroup(ErrorReportGroup reportGroup){
		this.errorReportGroups.add(reportGroup);
	}
}
