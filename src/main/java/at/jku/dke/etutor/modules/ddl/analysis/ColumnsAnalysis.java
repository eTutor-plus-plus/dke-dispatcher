package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ColumnsAnalysis extends AbstractDDLCriterionAnalysis implements DDLCriterionAnalysis{
    //region Fields
    private List<ErrorTupel> missingColumns;
    private List<ErrorTupel> surplusColumns;
    private List<ErrorTupel> wrongDatatypeColumns;
    private List<ErrorTupel> wrongDefaultColumns;
    private List<ErrorTupel> wrongNullColumns;
    //endregion

    public ColumnsAnalysis() {
        this.missingColumns = new ArrayList<>();
        this.surplusColumns = new ArrayList<>();
        this.wrongDatatypeColumns = new ArrayList<>();
        this.wrongDefaultColumns = new ArrayList<>();
        this.wrongNullColumns = new ArrayList<>();
    }

    public boolean isMissingColumnsEmpty(){
        return this.missingColumns.isEmpty();
    }

    public boolean isSurplusColumnsEmpty(){
        return this.surplusColumns.isEmpty();
    }

    public boolean isWrongDatatypeColumnsEmpty(){
        return this.wrongDatatypeColumns.isEmpty();
    }

    public boolean isWrongDefaultColumnsEmpty(){
        return this.wrongDefaultColumns.isEmpty();
    }

    public boolean isWrongNullColumnsEmpty(){
        return this.wrongNullColumns.isEmpty();
    }

    public Iterator<ErrorTupel> iterMissingColumns() {
        return this.missingColumns.iterator();
    }

    public Iterator<ErrorTupel> iterSurplusColumns() {
        return this.surplusColumns.iterator();
    }

    public Iterator<ErrorTupel> iterWrongDatatypeColumns() {
        return this.wrongDatatypeColumns.iterator();
    }

    public Iterator<ErrorTupel> iterWrongDefaultColumns() {
        return this.wrongDefaultColumns.iterator();
    }

    public Iterator<ErrorTupel> iterWrongNullColumns() {
        return this.wrongNullColumns.iterator();
    }

    @Override
    public DDLEvaluationCriterion getEvaluationCriterion() {
        return null;
    }

    //region Getter/Setter
    public List<ErrorTupel> getMissingColumns() {
        return missingColumns;
    }

    public void setMissingColumns(List<ErrorTupel> missingColumns) {
        this.missingColumns = missingColumns;
    }

    public void removeMissingColumns(List<ErrorTupel> columns) {
        this.missingColumns.remove(columns);
    }

    public void addMissingColumn(ErrorTupel column) {
        this.missingColumns.add(column);
    }

    public List<ErrorTupel> getSurplusColumns() {
        return surplusColumns;
    }

    public void setSurplusColumns(List<ErrorTupel> surplusColumns) {
        this.surplusColumns = surplusColumns;
    }

    public void removeSurplusColumns(List<ErrorTupel> columns) {
        this.surplusColumns.remove(columns);
    }

    public void addSurplusColumn(ErrorTupel column) {
        this.surplusColumns.add(column);
    }

    public List<ErrorTupel> getWrongDatatypeColumns() {
        return wrongDatatypeColumns;
    }

    public void setWrongDatatypeColumns(List<ErrorTupel> wrongDatatypeColumns) {
        this.wrongDatatypeColumns = wrongDatatypeColumns;
    }

    public void addWrongDatatypeColumn(ErrorTupel column) {
        this.wrongDatatypeColumns.add(column);
    }

    public List<ErrorTupel> getWrongDefaultColumns() {
        return wrongDefaultColumns;
    }

    public void setWrongDefaultColumns(List<ErrorTupel> wrongDefaultColumns) {
        this.wrongDefaultColumns = wrongDefaultColumns;
    }

    public void addWrongDefaultColumn(ErrorTupel column) {
        this.wrongDefaultColumns.add(column);
    }

    public List<ErrorTupel> getWrongNullColumns() {
        return wrongNullColumns;
    }

    public void setWrongNullColumns(List<ErrorTupel> wrongNullColumns) {
        this.wrongNullColumns = wrongNullColumns;
    }

    public void addWrongNullColumn(ErrorTupel column) {
        this.wrongNullColumns.add(column);
    }
    //endregion
}
