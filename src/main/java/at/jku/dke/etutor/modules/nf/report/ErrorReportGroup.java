package at.jku.dke.etutor.modules.nf.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

public class ErrorReportGroup implements Serializable {

	private String header;
	private final Vector<ErrorReport> errorReports;
	private final Vector<ErrorReportGroup> subErrorReportGroups;
	
	public ErrorReportGroup() {
		super();
		
		this.header = null;
		this.errorReports = new Vector<>();
		this.subErrorReportGroups = new Vector<>();
	}
	
	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Vector<ErrorReport> getErrorReports() {
		return (Vector<ErrorReport>)this.errorReports.clone();
	}

	public void setErrorReports(Collection<ErrorReport> reports) {
		this.errorReports.clear();
		this.errorReports.addAll(reports);
	}
	
	public void addErrorReport(ErrorReport report){
		this.errorReports.add(report);
	}

	public Vector<ErrorReportGroup> getSubErrorReportGroups() {
		return (Vector<ErrorReportGroup>)this.subErrorReportGroups.clone();
	}

	public void setSubErrorReportGroups(Collection<ErrorReportGroup> reportGroups) {
		this.subErrorReportGroups.clear();
		this.subErrorReportGroups.addAll(reportGroups);
	}
	
	public void addSubErrorReportGroup(ErrorReportGroup reportGroup){
		this.subErrorReportGroups.add(reportGroup);
	}
}
