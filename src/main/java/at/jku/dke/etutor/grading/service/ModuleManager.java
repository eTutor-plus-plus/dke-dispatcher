package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;


import java.util.HashMap;

/**
 * Maps the tasktypes to the modules
 */


public class ModuleManager{
    public static HashMap<String, Evaluator> evaluatorMap;

    public static HashMap<String, Evaluator> getEvaluatorMap() {
        return evaluatorMap;
    }

    public static void setEvaluatorMap(HashMap<String, Evaluator> evaluatorList) {
        evaluatorList = evaluatorList;
    }

}
