package at.jku.dke.etutor.modules.ddl.grading;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.util.HashMap;
import java.util.Iterator;

public class DDLGraderConfig {
    //region Fields
    private int maxPoints;
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
    //endregion
}
