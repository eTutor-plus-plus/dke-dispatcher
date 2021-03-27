package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.evaluation.Analysis;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Grading;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Report;

import java.util.HashMap;

/**
 * Maps the tasktypes to the modules and provides methodes for calling
 * the corresponding interfaces as seen in evaluation package
 */


public class ModuleManager {
    private HashMap<String, Evaluator> evaluatorList;
    private HashMap<String, Analysis> analysisList;
    private HashMap<String, Report> reportList;
    private HashMap<String, Grading> gradingList;





}
