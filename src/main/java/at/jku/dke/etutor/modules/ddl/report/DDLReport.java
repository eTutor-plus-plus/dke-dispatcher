package at.jku.dke.etutor.modules.ddl.report;

import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DDLReport extends DefaultReport implements Report, Serializable {
    //region Fields
    private List<DDLErrorReport> errorReports;
    private List<String> missingTables;
    private List<String> surplusTables;
    private List<String> missingColumns;
    private List<String> surplusColumns;
    private List<String> wrongColumns;
    private List<String> missingUniqueConstraints;
    private List<String> surplusUniqueConstraints;
    private List<String> missingPrimaryKeys;
    private List<String> surplusPrimaryKeys;
    private List<String> missingForeignKeys;
    private List<String> surplusForeignKeys;


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
    }

    //region Getter/Setter
    public List<DDLErrorReport> getErrorReports() {
        return errorReports;
    }

    public void setErrorReports(List<DDLErrorReport> errorReports) {
        this.errorReports = errorReports;
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

    public List<String> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(List<String> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public List<String> getSurplusColumns() {
        return surplusColumns;
    }

    public void setSurplusColumns(List<String> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    public List<String> getWrongColumns() {
        return wrongColumns;
    }

    public void setWrongColumns(List<String> wrongColumns) {
        this.wrongColumns = wrongColumns;
    }

    public List<String> getMissingUniqueConstraints() {
        return missingUniqueConstraints;
    }

    public void setMissingUniqueConstraints(List<String> missingUniqueConstraints) {
        this.missingUniqueConstraints = missingUniqueConstraints;
    }

    public List<String> getSurplusUniqueConstraints() {
        return surplusUniqueConstraints;
    }

    public void setSurplusUniqueConstraints(List<String> surplusUniqueConstraints) {
        this.surplusUniqueConstraints = surplusUniqueConstraints;
    }

    public List<String> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    public void setMissingPrimaryKeys(List<String> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    public List<String> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    public void setSurplusPrimaryKeys(List<String> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }

    public List<String> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    public void setMissingForeignKeys(List<String> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    public List<String> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    public void setSurplusForeignKeys(List<String> surplusForeignKeys) {
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
