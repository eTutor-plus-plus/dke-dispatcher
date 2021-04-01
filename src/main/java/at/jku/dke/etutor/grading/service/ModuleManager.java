package at.jku.dke.etutor.grading.service;




import java.util.HashMap;

/**
 * Maps the tasktypes to the modules
 */


public class ModuleManager{
    public static HashMap<String, etutor.core.evaluation.Evaluator> evaluatorMap;

    public static HashMap<String, etutor.core.evaluation.Evaluator> getEvaluatorMap() {
        return evaluatorMap;
    }

    public static void setEvaluatorMap(HashMap<String, etutor.core.evaluation.Evaluator> evaluatorList) {
        evaluatorList = evaluatorList;
    }

}
