package at.jku.dke.etutor.modules.pm.analysis;


import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * The configuration for the PmAnalysis
 */
public class PmAnalyzerConfig {

    //OBJECT FIELDS
    private int diagnoseLevel;
    // map submissionID to corresponding correct Answer with respective Answer Type (Set, List, ...)
    private HashMap<String, Object> correctAnswers;
    // might be multiple
    private HashSet<PmEvaluationCriterion> criteriaToAnalyze;

    // CONSTRUCTOR
    public PmAnalyzerConfig() {
        super();
        this.diagnoseLevel = 0;
        this.correctAnswers = new HashMap<>();
        this.criteriaToAnalyze = new HashSet<>();
    }

    // METHODS
    public boolean isCriterionToAnalyze(PmEvaluationCriterion criterion){
        return this.criteriaToAnalyze.contains(criterion);
    }
    public Iterator<PmEvaluationCriterion> iterCriteriaToAnalyze(){
        return this.criteriaToAnalyze.iterator();       // returns Iterator object
    }
    public void addCriterionToAnalyze(PmEvaluationCriterion criterion){
        this.criteriaToAnalyze.add(criterion);
    }
    public void removeCriterionToAnalyze(PmEvaluationCriterion criterion){
        this.criteriaToAnalyze.remove(criterion);
    }
    public int getDiagnoseLevel() {
        return this.diagnoseLevel;
    }
    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }
    public HashMap<String, Object> getCorrectAnswers() {
        return this.correctAnswers;
    }
    public Object getCorrectAnswer(String submissionID){
        return this.correctAnswers.get(submissionID);
    }


    public void setCorrectAnswer(String submissionID, Object correctAnswer) {
        this.correctAnswers.put(submissionID, correctAnswer);
    }
    public void setCorrectAnswers(Map<String, Object> correctAnswers){
        this.correctAnswers.putAll(correctAnswers);
    }

}
