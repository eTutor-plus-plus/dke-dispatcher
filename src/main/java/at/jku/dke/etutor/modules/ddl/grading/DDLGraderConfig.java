package at.jku.dke.etutor.modules.ddl.grading;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.HashMap;
import java.util.Iterator;

public class DDLGraderConfig {
    //region Fields
    private int maxPoints;
    private int tablePoints;
    private int columnPoints;
    private int primaryKeyPoints;
    private int foreignKeyPoints;
    private int constraintPoints;
    private final HashMap<DDLEvaluationCriterion, DDLCriterionGradingConfig> criteriaGradingConfigs;
    //endregion

    public DDLGraderConfig() {
        this.criteriaGradingConfigs = new HashMap<>();
    }

    public void addCriteriaGradingConfig(DDLEvaluationCriterion criterion, DDLCriterionGradingConfig config){
        this.criteriaGradingConfigs.put(criterion, config);
    }

    public boolean isCriterionToGrade(DDLEvaluationCriterion criterion){
        return this.criteriaGradingConfigs.containsKey(criterion);
    }

    public Iterator<DDLEvaluationCriterion> iterCriterionsToGrade(){
        return this.criteriaGradingConfigs.keySet().iterator();
    }

    public DDLCriterionGradingConfig getCriterionGradingConfig(DDLEvaluationCriterion criterion){
        return this.criteriaGradingConfigs.get(criterion);
    }

    //region Getter/Setter
    public int getMaxPoints() {
        return this.maxPoints;
    }

    public void setMaxPoints(int maximumPoints) {
        this.maxPoints = maximumPoints;
    }

    public int getTablePoints() {
        return tablePoints;
    }

    public void setTablePoints(int tablePoints) {
        this.tablePoints = tablePoints;
    }

    public int getColumnPoints() {
        return columnPoints;
    }

    public void setColumnPoints(int columnPoints) {
        this.columnPoints = columnPoints;
    }

    public int getPrimaryKeyPoints() {
        return primaryKeyPoints;
    }

    public void setPrimaryKeyPoints(int primaryKeyPoints) {
        this.primaryKeyPoints = primaryKeyPoints;
    }

    public int getForeignKeyPoints() {
        return foreignKeyPoints;
    }

    public void setForeignKeyPoints(int foreignKeyPoints) {
        this.foreignKeyPoints = foreignKeyPoints;
    }

    public int getConstraintPoints() {
        return constraintPoints;
    }

    public void setConstraintPoints(int constraintPoints) {
        this.constraintPoints = constraintPoints;
    }

    //endregion
}
