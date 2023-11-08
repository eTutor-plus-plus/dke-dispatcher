package at.jku.dke.etutor.modules.fd;

import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.modules.fd.services.TaskService;
import at.jku.dke.etutor.modules.fd.utilities.FDSolve;
import at.jku.dke.etutor.modules.fd.utilities.FDTaskSolve;
import at.jku.dke.etutor.modules.fd.utilities.FDTaskSolveResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class FDEvaluator implements Evaluator, Serializable {
    transient TaskService taskService;
    FDTaskSolve fdTaskSolve;
    FDTaskSolveResponse fdTaskSolveResponse;


    public FDEvaluator(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        fdTaskSolve = new FDTaskSolve();
        fdTaskSolve.setId(String.valueOf(exerciseID));
        fdTaskSolve.setType(passedAttributes.get("type"));
        fdTaskSolve.setSolution(passedAttributes.get("solution"));
        List<FDSolve> closureSolutions = new ArrayList<>();
        fdTaskSolve.setClosureSolutions(closureSolutions);
        if (passedAttributes.get("closureSolution") != null) {
            closureSolutions.add(new FDSolve(Long.valueOf(fdTaskSolve.getId()), passedAttributes.get("closureSolution")));
        }
        if (passedAttributes.get("normalFormSolutions") != null) {
            ObjectMapper mapper = new ObjectMapper();
            List<FDSolve> normalFormSolutions = mapper.readValue(passedAttributes.get("normalFormSolutions").replace("FDSolve",""), new TypeReference<List<FDSolve>>(){});
            fdTaskSolve.setNormalFormSolutions(normalFormSolutions);
        }
        try {
            fdTaskSolve.setMaxPoints(Double.parseDouble(passedAttributes.get("maxPoints")));
        } catch (NumberFormatException e) {
            fdTaskSolve.setMaxPoints(0);
        }
        fdTaskSolveResponse = taskService.fdTaskGrade(fdTaskSolve);
        String submission = "id='"+fdTaskSolve.getId()+'\'' +",";
        submission += "type='"+fdTaskSolve.getType()+'\'' +",";
        submission += "maxPoints="+fdTaskSolve.getMaxPoints()+'\'' +",";
        if (fdTaskSolve.getSolution() != null) {
            submission += "solution=";
            submission += fdTaskSolve.getSolution();
            submission += ",";
        }
        if (!fdTaskSolve.getClosureSolutions().isEmpty()) {
            FDSolve fdSolve = fdTaskSolve.getClosureSolutions().get(0);
            submission += "closureSolution{";
            submission += "id="+ fdSolve.getId().toString()+",";
            submission += "solution='"+ fdSolve.getSolution()+ '\'' + '}';
        }
        if (fdTaskSolve.getNormalFormSolutions()!=null) {
            for (FDSolve fdSolve:fdTaskSolve.getNormalFormSolutions()) {
                submission += "normalformSolution{";
                submission += "id="+ fdSolve.getId().toString()+",";
                submission += "solution='"+ fdSolve.getSolution()+'\'' + '}';
            }
        }
        DefaultAnalysis analysis = new DefaultAnalysis();
        analysis.setSubmission(submission);
        analysis.setSubmissionSuitsSolution(fdTaskSolveResponse.isSolved());
        return analysis;
    }


    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        DefaultGrading defaultGrading = new DefaultGrading();
        defaultGrading.setMaxPoints(Double.parseDouble(passedAttributes.get("maxPoints")));
        defaultGrading.setPoints(fdTaskSolveResponse.getPoints());
        return defaultGrading;
    }

    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        DefaultReport report = new DefaultReport();
        report.setHint(fdTaskSolveResponse.getHints().toString());
        return report;
    }

    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }
}
