package at.jku.dke.etutor.grading;

import at.jku.dke.etutor.grading.service.ModuleManager;


import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.evaluation.Evaluator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;

@SpringBootApplication
public class ETutorGradingApplication {

    public static void main(String[] args) {
        HashMap<String, Evaluator> evaluatorMap = new HashMap<>();
        Evaluator sqlEvaluator = new SQLEvaluator();
        evaluatorMap.put("sql", sqlEvaluator);
        ModuleManager myModuleManager = new ModuleManager(evaluatorMap);
        System.out.println(myModuleManager.getEvaluatorMap().get("sql"));
        SpringApplication.run(ETutorGradingApplication.class, args);
    }

}
