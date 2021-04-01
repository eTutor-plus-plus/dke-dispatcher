package at.jku.dke.etutor.grading.service;
import at.jku.dke.etutor.evaluation.Evaluator;

import java.util.HashMap;


/**
 * Maps the tasktypes to the modules
 */


public class ModuleManager{
    private static HashMap<String, Evaluator> evaluatorMap;

    public ModuleManager(HashMap<String, Evaluator> evaluatorMap){
        this.evaluatorMap = evaluatorMap;
    }


    public  static HashMap<String, Evaluator> getEvaluatorMap() {
        return evaluatorMap;
    }

    public  void setEvaluatorMap(HashMap<String, Evaluator> evaluatorMap) {
        evaluatorMap = evaluatorMap;
    }

}
