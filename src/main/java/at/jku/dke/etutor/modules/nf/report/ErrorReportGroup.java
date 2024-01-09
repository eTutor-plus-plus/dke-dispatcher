package at.jku.dke.etutor.modules.nf.report;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ErrorReportGroup implements Serializable {

	private String header;
	private final List<ErrorReport> errorReports;
	private final List<ErrorReportGroup> subErrorReportGroups;
	
	public ErrorReportGroup() {
		super();
		
		this.header = null;
		this.errorReports = new LinkedList<>();
		this.subErrorReportGroups = new LinkedList<>();
	}
	
	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public List<ErrorReport> getErrorReports() {
		return new LinkedList<>(this.errorReports);
	}

	public void setErrorReports(Collection<ErrorReport> reports) {
		this.errorReports.clear();
		this.errorReports.addAll(reports);
	}
	
	public void addErrorReport(ErrorReport report){
		this.errorReports.add(report);
	}

	public List<ErrorReportGroup> getSubErrorReportGroups() {
		return new LinkedList<>(this.subErrorReportGroups);
	}

	public void setSubErrorReportGroups(Collection<ErrorReportGroup> reportGroups) {
		this.subErrorReportGroups.clear();
		this.subErrorReportGroups.addAll(reportGroups);
	}
	
	public void addSubErrorReportGroup(ErrorReportGroup reportGroup){
		this.subErrorReportGroups.add(reportGroup);
	}
}
