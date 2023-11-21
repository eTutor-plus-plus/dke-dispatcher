package at.jku.dke.etutor.modules.ddl.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.ddl.analysis.ErrorTupel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DDLReport extends DefaultReport implements Report, Serializable {
    //region Fields
    private List<DDLErrorReport> errorReports;
    private List<String> missingTables;
    private List<String> surplusTables;
    private List<ErrorTupel> missingColumns;
    private List<ErrorTupel> surplusColumns;
    private List<ErrorTupel> wrongColumns;
    private List<ErrorTupel> missingUniqueConstraints;
    private List<ErrorTupel> surplusUniqueConstraints;
    private List<String> wrongInsertStatements;
    private List<ErrorTupel> missingPrimaryKeys;
    private List<ErrorTupel> surplusPrimaryKeys;
    private List<ErrorTupel> missingForeignKeys;
    private List<ErrorTupel> surplusForeignKeys;


    private String prologue;
    private String exceptionText;

    private boolean showHints;
    private boolean showPrologue;
    private boolean showException;
    private boolean showQueryResult;
    private boolean showErrorReports;
    private boolean showErrorDescriptions;
    //endregion

    public DDLReport() {
        this.showHints = false;
        this.showPrologue = false;
        this.showException = false;
        this.showQueryResult = false;
        this.showErrorReports = false;
        this.showErrorDescriptions = false;

        this.prologue = "";
        this.exceptionText = "";

        this.errorReports = new ArrayList<>();
        this.wrongColumns = new ArrayList<>();
    }

    //region Getter/Setter
    public List<DDLErrorReport> getErrorReports() {
        return errorReports;
    }

    public void setErrorReports(List<DDLErrorReport> errorReports) {
        this.errorReports = errorReports;
    }

    public void addErrorReport(DDLErrorReport errorReport) {
        this.errorReports.add(errorReport);
    }

    public List<String> getMissingTables() {
        return missingTables;
    }

    public void setMissingTables(List<String> missingTables) {
        this.missingTables = missingTables;
    }

    public List<String> getSurplusTables() {
        return surplusTables;
    }

    public void setSurplusTables(List<String> surplusTables) {
        this.surplusTables = surplusTables;
    }

    public List<ErrorTupel> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(List<ErrorTupel> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public List<ErrorTupel> getSurplusColumns() {
        return surplusColumns;
    }

    public void setSurplusColumns(List<ErrorTupel> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    public List<ErrorTupel> getWrongColumns() {
        return wrongColumns;
    }

    public void setWrongColumns(List<ErrorTupel> wrongColumns) {
        this.wrongColumns = wrongColumns;
    }

    public void addWrongColumns(List<ErrorTupel> wrongColumns) {
        this.wrongColumns.addAll(wrongColumns);
    }

    public List<ErrorTupel> getMissingUniqueConstraints() {
        return missingUniqueConstraints;
    }

    public void setMissingUniqueConstraints(List<ErrorTupel> missingUniqueConstraints) {
        this.missingUniqueConstraints = missingUniqueConstraints;
    }

    public List<ErrorTupel> getSurplusUniqueConstraints() {
        return surplusUniqueConstraints;
    }

    public void setSurplusUniqueConstraints(List<ErrorTupel> surplusUniqueConstraints) {
        this.surplusUniqueConstraints = surplusUniqueConstraints;
    }

    public List<String> getWrongInsertStatements() {
        return wrongInsertStatements;
    }

    public void setWrongInsertStatements(List<String> wrongInsertStatements) {
        this.wrongInsertStatements = wrongInsertStatements;
    }

    public List<ErrorTupel> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    public void setMissingPrimaryKeys(List<ErrorTupel> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    public List<ErrorTupel> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    public void setSurplusPrimaryKeys(List<ErrorTupel> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }

    public List<ErrorTupel> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    public void setMissingForeignKeys(List<ErrorTupel> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    public List<ErrorTupel> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    public void setSurplusForeignKeys(List<ErrorTupel> surplusForeignKeys) {
        this.surplusForeignKeys = surplusForeignKeys;
    }

    public String getPrologue() {
        return prologue;
    }

    public void setPrologue(String prologue) {
        this.prologue = prologue;
    }

    public String getExceptionText() {
        return exceptionText;
    }

    public void setExceptionText(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    public boolean isShowHints() {
        return showHints;
    }

    public void setShowHints(boolean showHints) {
        this.showHints = showHints;
    }

    public boolean isShowPrologue() {
        return showPrologue;
    }

    public void setShowPrologue(boolean showPrologue) {
        this.showPrologue = showPrologue;
    }

    public boolean isShowException() {
        return showException;
    }

    public void setShowException(boolean showException) {
        this.showException = showException;
    }

    public boolean isShowQueryResult() {
        return showQueryResult;
    }

    public void setShowQueryResult(boolean showQueryResult) {
        this.showQueryResult = showQueryResult;
    }

    public boolean isShowErrorReports() {
        return showErrorReports;
    }

    public void setShowErrorReports(boolean showErrorReports) {
        this.showErrorReports = showErrorReports;
    }

    public boolean isShowErrorDescriptions() {
        return showErrorDescriptions;
    }

    public void setShowErrorDescriptions(boolean showErrorDescriptions) {
        this.showErrorDescriptions = showErrorDescriptions;
    }
    //endregion
}
