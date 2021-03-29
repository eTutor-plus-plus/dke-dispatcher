package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;


import java.util.HashMap;

/**
 * Maps the tasktypes to the modules
 */


public class ModuleManager{
    public static HashMap<String, Evaluator> evaluatorList;

    public HashMap<String, Evaluator> getEvaluatorList() {
        return evaluatorList;
    }

    public void setEvaluatorList(HashMap<String, Evaluator> evaluatorList) {
        this.evaluatorList = evaluatorList;
    }

}
