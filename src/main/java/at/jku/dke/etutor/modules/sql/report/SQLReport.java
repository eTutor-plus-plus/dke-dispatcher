package at.jku.dke.etutor.modules.sql.report;


import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.io.Serializable;
import java.util.*;


public class SQLReport extends DefaultReport implements Report, Serializable{

	private String prologue;
	private String exceptionText;

	private List<Collection<String>> queryResultTuples;
	private List<SQLErrorReport> errorReports;
	private List<String> queryResultColumnLabels;
	private List<Collection<String>> missingTuples;
	private List<Collection<String>> surplusTuples;

	private boolean showHints;
	private boolean showPrologue;
	private boolean showException;
	private boolean showQueryResult;
	private boolean showErrorReports;
	private boolean showErrorDescriptions;

	public SQLReport(){
		super();

		this.showHints = false;
		this.showPrologue = false;
		this.showException = false;
		this.showQueryResult = false;
		this.showErrorReports = false;
		this.showErrorDescriptions = false;

		this.prologue = "";
		this.exceptionText = "";

		this.errorReports = new ArrayList<>();
		this.queryResultTuples = new ArrayList<>();
		this.queryResultColumnLabels = new ArrayList<>();
		this.missingTuples = new ArrayList<>();
		this.surplusTuples=new ArrayList<>();
	}

	public List<Collection<String>> getMissingTuples() {
		return missingTuples;
	}

	public void setMissingTuples(List<Collection<String>> missingTuples) {
		this.missingTuples = missingTuples;
	}

	public List<Collection<String>> getSurplusTuples() {
		return surplusTuples;
	}

	public void setSurplusTuples(List<Collection<String>> surplusTuples) {
		this.surplusTuples = surplusTuples;
	}

	public boolean hasErrorReports(){
		return !this.errorReports.isEmpty();
	}

	public void addQueryResultColumnLabel(String columnLabel){
		this.queryResultColumnLabels.add(columnLabel);
	}
	
	public void addQueryResultTuple(Collection<String> tuple) {
		this.queryResultTuples.add(tuple);
	}

	public boolean showPrologue() {
		return this.showPrologue;
	}

	public void setShowPrologue(boolean b) {
		this.showPrologue = b;
	}
	
	public void addErrorReport(SQLErrorReport errorReport){
		this.errorReports.add(errorReport);
	}
	
	public Iterator<SQLErrorReport> iterErrorReports(){
		return this.errorReports.iterator();
	}

	public Iterator<Collection<String>> iterQueryResultTuples(){
		return this.queryResultTuples.iterator();
	}

	public List<Collection<String>> getQueryResultTuples() {
		return new ArrayList<>(queryResultTuples);
	}

	public List<String> getQueryResultColumnLabels() {
		return new ArrayList<>(queryResultColumnLabels);
	}

	public void setQueryResultTuples(List<Collection<String>> queryResultTuples) {
		this.queryResultTuples = queryResultTuples;
	}

	public Iterator<String> iterQueryResultColumnLabels(){
		return this.queryResultColumnLabels.iterator();
	}

	public void setQueryResultColumnLabels(List<String> queryResultColumnLabels) {
		this.queryResultColumnLabels = queryResultColumnLabels;
	}

	public String getExceptionText() {
		return this.exceptionText;
	}
	
	public void setExceptionText(String exceptionText) {
		this.exceptionText = exceptionText;
	}

	public boolean showErrorDescriptions() {
		return this.showErrorDescriptions;
	}

	public boolean showErrorReport() {
		return this.showErrorReports;
	}

	public boolean showHints() {
		return this.showHints;
	}

	public boolean showQueryResult() {
		return this.showQueryResult;
	}

	public void setShowErrorDescriptions(boolean b) {
		this.showErrorDescriptions = b;
	}

	public void setShowErrorReports(boolean b) {
		this.showErrorReports = b;
	}

	public void setShowHints(boolean b) {
		this.showHints = b;
	}

	public void setShowQueryResult(boolean b) {
		this.showQueryResult = b;
	}

	public boolean showException() {
		return this.showException;
	}

	public void setShowException(boolean b) {
		this.showException = b;
	}

	public String getPrologue() {
		return this.prologue;
	}

	public void setPrologue(String prologue) {
		this.prologue = prologue;
	}
}
