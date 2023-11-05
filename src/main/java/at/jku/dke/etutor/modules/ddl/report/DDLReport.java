package at.jku.dke.etutor.modules.ddl.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DDLReport {
    //region Fields
    private List<DDLErrorReport> errorReports;
    private List<Collection<String>> missingTables;
    private List<Collection<String>> surplusTables;
    private List<Collection<String>> missingColumns;
    private List<Collection<String>> surplusColumns;
    private List<Collection<String>> wrongColumns;
    private List<Collection<String>> missingUniqueConstraints;
    private List<Collection<String>> surplusUniqueConstraints;
    private List<Collection<String>> missingPrimaryKeys;
    private List<Collection<String>> surplusPrimaryKeys;
    private List<Collection<String>> missingForeignKeys;
    private List<Collection<String>> surplusForeignKeys;


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

    public List<Collection<String>> getMissingTables() {
        return missingTables;
    }

    public void setMissingTables(List<Collection<String>> missingTables) {
        this.missingTables = missingTables;
    }

    public List<Collection<String>> getSurplusTables() {
        return surplusTables;
    }

    public void setSurplusTables(List<Collection<String>> surplusTables) {
        this.surplusTables = surplusTables;
    }

    public List<Collection<String>> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(List<Collection<String>> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public List<Collection<String>> getSurplusColumns() {
        return surplusColumns;
    }

    public void setSurplusColumns(List<Collection<String>> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    public List<Collection<String>> getWrongColumns() {
        return wrongColumns;
    }

    public void setWrongColumns(List<Collection<String>> wrongColumns) {
        this.wrongColumns = wrongColumns;
    }

    public List<Collection<String>> getMissingUniqueConstraints() {
        return missingUniqueConstraints;
    }

    public void setMissingUniqueConstraints(List<Collection<String>> missingUniqueConstraints) {
        this.missingUniqueConstraints = missingUniqueConstraints;
    }

    public List<Collection<String>> getSurplusUniqueConstraints() {
        return surplusUniqueConstraints;
    }

    public void setSurplusUniqueConstraints(List<Collection<String>> surplusUniqueConstraints) {
        this.surplusUniqueConstraints = surplusUniqueConstraints;
    }

    public List<Collection<String>> getMissingPrimaryKeys() {
        return missingPrimaryKeys;
    }

    public void setMissingPrimaryKeys(List<Collection<String>> missingPrimaryKeys) {
        this.missingPrimaryKeys = missingPrimaryKeys;
    }

    public List<Collection<String>> getSurplusPrimaryKeys() {
        return surplusPrimaryKeys;
    }

    public void setSurplusPrimaryKeys(List<Collection<String>> surplusPrimaryKeys) {
        this.surplusPrimaryKeys = surplusPrimaryKeys;
    }

    public List<Collection<String>> getMissingForeignKeys() {
        return missingForeignKeys;
    }

    public void setMissingForeignKeys(List<Collection<String>> missingForeignKeys) {
        this.missingForeignKeys = missingForeignKeys;
    }

    public List<Collection<String>> getSurplusForeignKeys() {
        return surplusForeignKeys;
    }

    public void setSurplusForeignKeys(List<Collection<String>> surplusForeignKeys) {
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
