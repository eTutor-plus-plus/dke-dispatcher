package at.jku.dke.etutor.modules.pm.grading;

import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.HashMap;
import java.util.Iterator;

public class PmGraderConfig {

    // OBJECT FIELD
    private final HashMap<PmEvaluationCriterion, PmCriterionGradingConfig> criteriaGradingConfigs;

    // CONSTRUCTOR
    public PmGraderConfig(){
        super();

        criteriaGradingConfigs = new HashMap<>();
    }

    // METHODS
    public void addCriterionGradingConfig(PmEvaluationCriterion criterion, PmCriterionGradingConfig config){
        this.criteriaGradingConfigs.put(criterion, config);
    }
    public PmCriterionGradingConfig getCriterionGradingConfig(PmEvaluationCriterion criterion){
        return this.criteriaGradingConfigs.get(criterion);
    }
    public Iterator<PmEvaluationCriterion> iterCriterionToGrade(){
        return this.criteriaGradingConfigs.keySet().iterator();
    }
}
