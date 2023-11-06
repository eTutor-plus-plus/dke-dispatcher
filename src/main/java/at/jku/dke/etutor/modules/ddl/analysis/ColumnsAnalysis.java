package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColumnsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis{
    //region Fields
    private List<String> missingColumns;
    private List<String> surplusColumns;
    private List<String> wrongDatatypeColumns;
    private List<String> wrongDefaultColumns;
    private List<String> wrongNullColumns;
    //endregion

    public ColumnsAnalysis() {
        this.missingColumns = new ArrayList<>();
        this.surplusColumns = new ArrayList<>();
        this.wrongDatatypeColumns = new ArrayList<>();
        this.wrongDefaultColumns = new ArrayList<>();
        this.wrongNullColumns = new ArrayList<>();
    }

    public boolean hasMissingColumns(){
        return !this.missingColumns.isEmpty();
    }

    public boolean hasSurplusColumns(){
        return !this.surplusColumns.isEmpty();
    }

    public boolean hasWrongDatatypeColumns(){
        return !this.wrongDatatypeColumns.isEmpty();
    }

    public boolean hasWrongDefaultColumns(){
        return !this.wrongDefaultColumns.isEmpty();
    }

    public boolean hasWrongNullColumns(){
        return !this.wrongNullColumns.isEmpty();
    }

    public Iterator<String> iterMissingColumns() {
        return this.missingColumns.iterator();
    }

    public Iterator<String> iterSurplusColumns() {
        return this.surplusColumns.iterator();
    }

    public Iterator<String> iterWrongDatatypeColumns() {
        return this.wrongDatatypeColumns.iterator();
    }

    public Iterator<String> iterWrongDefaultColumns() {
        return this.wrongDefaultColumns.iterator();
    }

    public Iterator<String> iterWrongNullColumns() {
        return this.wrongNullColumns.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<String> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(List<String> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public void removeMissingTables(List<String> columns) {
        this.missingColumns.remove(columns);
    }

    public List<String> getSurplusColumns() {
        return surplusColumns;
    }

    public void setSurplusColumns(List<String> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    public void removeSurplusColumns(List<String> columns) {
        this.surplusColumns.remove(columns);
    }

    public List<String> getWrongDatatypeColumns() {
        return wrongDatatypeColumns;
    }

    public void setWrongDatatypeColumns(List<String> wrongDatatypeColumns) {
        this.wrongDatatypeColumns = wrongDatatypeColumns;
    }

    public List<String> getWrongDefaultColumns() {
        return wrongDefaultColumns;
    }

    public void setWrongDefaultColumns(List<String> wrongDefaultColumns) {
        this.wrongDefaultColumns = wrongDefaultColumns;
    }

    public List<String> getWrongNullColumns() {
        return wrongNullColumns;
    }

    public void setWrongNullColumns(List<String> wrongNullColumns) {
        this.wrongNullColumns = wrongNullColumns;
    }

    //endregion
}
