package at.jku.dke.etutor.modules.pm;

import at.jku.dke.etutor.core.evaluation.Report;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.AlphaAlgorithm;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Log;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Trace;
import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.pm.grading.PmGrader;
import at.jku.dke.etutor.modules.pm.grading.PmPartialSubmissionGrading;
import at.jku.dke.etutor.modules.pm.plg.application.SimulationApplication;
import at.jku.dke.etutor.modules.pm.report.PmReport;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TestApplication {

    public static void main(String[] args) throws Exception{

        // TEST LOG 1
        // Initialize Traces (Aalst2004)
        // note: LOG
        Trace t1 = new Trace(new String[] {"A", "B", "C", "D"});
        Trace t2 = new Trace(new String[] {"A", "C", "B", "D"});
        Trace t3 = new Trace(new String[] {"A", "E", "D"});
        // generate Log
        Log l1 = new Log(t1, t2, t3);


        // note: Alpha Algorithm
        AlphaAlgorithm a1 = new AlphaAlgorithm(l1);
        Map<String, Object> resultMap;
        System.out.println("Solution of Alpha Algorithm:\n ");
        resultMap = a1.run();



        // note: user input => INPUT OF USER INTERFACE (front-end)
        Map<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("action", "diagnose");
        passedAttributes.put("diagnoseLevel", "3");
        passedAttributes.put("exerciseId", "5");

        passedAttributes.put("orI1", "(E,D), (C,B), (A,B), (B,C), (C,D), (A,C), (B,D), (A,E)");
        passedAttributes.put("orI2", "(E,D), (A,B), (C,D), (A,C), (B,D), (A,E)");
        passedAttributes.put("orI3", "(C,B), (B,C)");

        passedAttributes.put("aaI1", "A, B, C, T, E");
        passedAttributes.put("aaI2", "A");
        passedAttributes.put("aaI3", "D");
        passedAttributes.put("aaI4", "({G},{B}), ({A},{C}), ({A},{E}), ({A},{B, E}), ({A},{C, E}), ({B},{D}), ({C},{D}), ({E},{D}), ({B, E},{D}), ({C, E},{D})");
        passedAttributes.put("aaI5", "({A},{B, E}), ({A},{C, E}), ({B, E},{D}), ({C, E},{D})");
        passedAttributes.put("aaI6", "i, o, p({A},{B, E}), p({A},{C, E}), p({B, E},{D}), p({C, E},{D})");
        passedAttributes.put("aaI7", "(i,A), (D,o), (A,p({A},{B, E})), (p({A},{B, E}),B), (p({A},{B, E}),E), (A,p({A},{C, E})), (p({A},{C, E}),C), (p({A},{C, E}),E), (B,p({B, E},{D})), (E,p({B, E},{D})), (p({B, E},{D}),D), (C,p({C, E},{D})), (E,p({C, E},{D})), (p({C, E},{D}),D)");

        int exerciseID = 100;
        int userID = 101;

        // note: back-end => Analysis, Grading, Report
        PmEvaluator testEval = new PmEvaluator();
        //testEval.setResultMap(resultMap);
        try{
            // note: analysis module
            System.out.println("\nDetailed Analysis: " );
            Analysis testA = testEval.analyze(exerciseID, userID, passedAttributes, null, null);
            System.out.println("\nSubmission of Analysis:\n Submission suits solution?: " + testA.submissionSuitsSolution());

            // note: grading module
            Grading testG = testEval.grade(testA, 14, passedAttributes, null);
            System.out.println("\nYou achieved " + testG.getPoints() + " Points from  "+ testG.getMaxPoints() + " Points in total.\n");

            PmGrader testG_advanced = (PmGrader) testG;
            for(var entry: testG_advanced.getPartialSubmissionGrading().entrySet()){
                String subId = entry.getKey();
                PmPartialSubmissionGrading grad = entry.getValue();
                int points = grad.getTotalAchievedPoints();
                System.out.println("For submission: " + subId + " you received "+ points + " points.");
            }

            // note: report module
            Report testR = testEval.report(testA, testG, passedAttributes, null, Locale.GERMAN);
            PmReport pmReport = (PmReport) testR;
            System.out.println(pmReport.getDescription());
            System.out.println(pmReport.getError());


        }catch (Exception e){
            System.out.println("Error: " + e);
        }

    }

}
