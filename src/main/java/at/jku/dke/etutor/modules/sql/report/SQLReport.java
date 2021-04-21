package at.jku.dke.etutor.modules.sql.report;


import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;



public class SQLReport extends DefaultReport implements Report, Serializable{

	private String prologue;
	private String exceptionText;

	private Vector queryResultTuples;
	private Vector errorReports;
	private Vector queryResultColumnLabels;
	private Vector missingTuples;
	private Vector surplusTuples;

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

		this.prologue = new String();
		this.exceptionText = new String();

		this.errorReports = new Vector();
		this.queryResultTuples = new Vector();
		this.queryResultColumnLabels = new Vector();
		this.missingTuples = new Vector();
		this.surplusTuples=new Vector();
	}

	public Vector getMissingTuples() {
		return missingTuples;
	}

	public void setMissingTuples(Vector missingTuples) {
		this.missingTuples = missingTuples;
	}

	public Vector getSurplusTuples() {
		return surplusTuples;
	}

	public void setSurplusTuples(Vector surplusTuples) {
		this.surplusTuples = surplusTuples;
	}

	public boolean hasErrorReports(){
		return this.errorReports.size() > 0;
	}

	public void addQueryResultColumnLabel(String columnLabel){
		this.queryResultColumnLabels.add(columnLabel);
	}
	
	public void addQueryResultTuple(Collection tuple) {
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
	
	public Iterator iterErrorReports(){
		return this.errorReports.iterator();
	}

	public Iterator iterQueryResultTuples(){
		return this.queryResultTuples.iterator();
	}

	public Vector getQueryResultTuples() {
		return (Vector)this.queryResultTuples.clone();
	}

	public Vector getQueryResultColumnLabels() {
		return (Vector)this.queryResultColumnLabels.clone();
	}

	public void setQueryResultTuples(Vector queryResultTuples) {
		this.queryResultTuples = queryResultTuples;
	}

	public Iterator iterQueryResultColumnLabels(){
		return this.queryResultColumnLabels.iterator();
	}

	public void setQueryResultColumnLabels(Vector queryResultColumnLabels) {
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
