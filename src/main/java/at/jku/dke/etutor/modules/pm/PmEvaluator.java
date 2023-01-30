package at.jku.dke.etutor.modules.pm;

import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Arc;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Event;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Pair;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Place;
import at.jku.dke.etutor.modules.pm.analysis.PmAnalysis;
import at.jku.dke.etutor.modules.pm.analysis.PmAnalyzerConfig;
import at.jku.dke.etutor.modules.pm.analysis.PmPartialSubmissionAnalysis;
import at.jku.dke.etutor.modules.pm.grading.PmCriterionGradingConfig;
import at.jku.dke.etutor.modules.pm.grading.PmGrader;
import at.jku.dke.etutor.modules.pm.grading.PmGraderConfig;
import at.jku.dke.etutor.modules.pm.grading.PmPartialSubmissionGrading;
import at.jku.dke.etutor.modules.pm.report.PmReporter;
import at.jku.dke.etutor.modules.pm.report.PmReporterConfig;
import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Process Mining Task:
 * Implementation of the Evaluator Interface for PM Submissions
 * EVALUATOR of a process mining task consisting of:
 * analysis module
 * grading module
 * report module
 * @author Falk Görner
 * @version 1.0
 */

@Service
public class PmEvaluator implements Evaluator {

    // OBJECT FIELDS
    private Logger logger;

//    //Only for offline test settings:
//    private Map<String, Object> resultMap;
//    public void setResultMap(Map<String, Object> resultMap){
//        this.resultMap = resultMap;
//    }

    /**
     * CONSTRUCTOR
     * added 18.11.2022
     */
    public PmEvaluator(){
        super();

        try{
            this.logger = (Logger) LoggerFactory.getLogger("at.jku.dke.etutor.modules.pm");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // METHODS
    /**
     * Method that covers the analysis of the user input
     * @param exerciseID the exercise id
     * @param userID the user id
     * @param passedAttributes a map containing different attributes -> MAIN USER INPUT (submitted answers)
     * @param passedParameters a map containing different parameters
     * @param locale
     * @return Analysis object (Submission, SubmissionSuitsSolution)
     * @throws Exception
     */
    @Override
    public Analysis analyze(int exerciseID, int userID, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        /**
         * Logger content
         * added 18.11
         */
        logger.info("exerciseID: {}", exerciseID);
        //logPassedAttributes(passedAttributes, passedParameters);

        // DECLARATIONS
        String action;
        String message;

        // data from input stream
        int diagnoseLevel;
        int dispatcherExerciseId;
        String action_Param;
        Map<String, String> completeSubmission_Param;
        String diagnoseLevel_Param;
        String dispatcherExerciseId_Param;

        // get value of key "action"
        action_Param = passedAttributes.get("action");
        // get values of submitted fields => actual user input
        completeSubmission_Param = new HashMap<>();
        completeSubmission_Param.put("orI1", passedAttributes.get("orI1"));
        completeSubmission_Param.put("orI2", passedAttributes.get("orI2"));
        completeSubmission_Param.put("orI3", passedAttributes.get("orI3"));
        completeSubmission_Param.put("aaI1", passedAttributes.get("aaI1"));
        completeSubmission_Param.put("aaI2", passedAttributes.get("aaI2"));
        completeSubmission_Param.put("aaI3", passedAttributes.get("aaI3"));
        completeSubmission_Param.put("aaI4", passedAttributes.get("aaI4"));
        completeSubmission_Param.put("aaI5", passedAttributes.get("aaI5"));
        completeSubmission_Param.put("aaI6", passedAttributes.get("aaI6"));
        completeSubmission_Param.put("aaI7", passedAttributes.get("aaI7"));
        // get value of key "action"
        diagnoseLevel_Param = passedAttributes.get("diagnoseLevel");
        // get value of key "exerciseId"
        dispatcherExerciseId_Param = passedAttributes.get("exerciseId");

        // reassign values
        action = action_Param;
        diagnoseLevel = Integer.parseInt(diagnoseLevel_Param);
        dispatcherExerciseId = Integer.parseInt(dispatcherExerciseId_Param);

        // CONFIGURING ANALYZER
        PmAnalyzerConfig pmAnalyzerConfig = new PmAnalyzerConfig();


        // ESTABLISHING CONNECTION TO PROCESS MINING DATABASE
        // note: status as of 18.11
        try(Connection conn = PmDataSource.getConnection()){
            conn.setAutoCommit(false);

            String query = "SELECT * FROM randomexercises WHERE exercise_id = ?;";

            try(PreparedStatement queryStmt = conn.prepareStatement(query)){
                queryStmt.setInt(1, dispatcherExerciseId);
                ResultSet rs = queryStmt.executeQuery();
                ObjectMapper mapper = new ObjectMapper();

                if(rs.next()){
                    pmAnalyzerConfig.setCorrectAnswer("orI1", mapper.readValue(rs.getString("or_one"), new TypeReference<Set<Pair<Event, Event>>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("orI2", mapper.readValue(rs.getString("or_two"), new TypeReference<Set<Pair<Event, Event>>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("orI3", mapper.readValue(rs.getString("or_three"), new TypeReference<Set<Pair<Event, Event>>>(){}));
                    //pmAnalyzerConfig.setCorrectAnswer("orI4", mapper.readValue(rs.getString("or_four"), new TypeReference<Set<Pair<Event, Event>>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI1", mapper.readValue(rs.getString("aa_one"), new TypeReference<SortedSet<Event>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI2", mapper.readValue(rs.getString("aa_two"), new TypeReference<SortedSet<Event>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI3", mapper.readValue(rs.getString("aa_three"), new TypeReference<SortedSet<Event>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI4", mapper.readValue(rs.getString("aa_four"), new TypeReference<List<Pair<List<Event>, List<Event>>>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI5", mapper.readValue(rs.getString("aa_five"), new TypeReference<List<Pair<List<Event>, List<Event>>>>(){}));
                    pmAnalyzerConfig.setCorrectAnswer("aaI6", mapper.readValue(rs.getString("aa_six"), new TypeReference<List<Place>>(){}));
                    // convert aaI7 with special method, since mapper.readValue() does not work for this part of the solution:
                    pmAnalyzerConfig.setCorrectAnswer("aaI7", convertSubmissionSeven(rs.getString("aa_seven")));
                }
                conn.commit();
            }catch (SQLException e){
                this.logger.warn("Could not fetch Solution", e);
                try{
                    conn.rollback();
                }catch (SQLException ex){
                    logger.error(ex.getMessage(), ex);
                }
            }
        }catch(SQLException e){
            message = "";
            message = message.concat("Stopped analysis due to errors. ");
            this.logger.error(message, e);
            throw e;
        }

//        // only for offline test setting:
//        pmAnalyzerConfig.setCorrectAnswers(this.resultMap);

        if(action.toUpperCase().equals(PmEvaluationAction.DIAGNOSE.toString())){
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_EVENT);
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS);
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA);

            pmAnalyzerConfig.setDiagnoseLevel(3);
        } else{ // in this version only submit
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_EVENT);
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS);
            pmAnalyzerConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA);

            if(action.toUpperCase().equals(PmEvaluationAction.SUBMIT.toString())){
                pmAnalyzerConfig.setDiagnoseLevel(1);
            }else{
                pmAnalyzerConfig.setDiagnoseLevel(diagnoseLevel);
            }
        }

        // Start analyzing
        PmAnalysis analysis = new PmAnalysis();
        PmPartialSubmissionAnalysis pmPartialSubmissionAnalysis;
        // note: maybe change Serializable to object?
        analysis.setSubmission((Serializable) completeSubmission_Param);
        analysis.setSubmissionSuitsSolution(true);

        // analyze each and every submitted answers one by one and store result
        for(var entry: completeSubmission_Param.entrySet()){
            PmPartialSubmissionAnalysis partialSubmissionAnalyzer = new PmPartialSubmissionAnalysis(entry.getKey());
            partialSubmissionAnalyzer.analyze(entry.getValue(), pmAnalyzerConfig);
            analysis.addPartialSubmissionAnalysis(entry.getKey(), partialSubmissionAnalyzer);
        }

        // iterate through partial submission analysis results to define status of submissionSuitsSolution
        Iterator<PmPartialSubmissionAnalysis> partialSubmissionIterator = analysis.iterSubmissionAnalysis();
        while(partialSubmissionIterator.hasNext()){
            pmPartialSubmissionAnalysis = partialSubmissionIterator.next();
            if(!pmPartialSubmissionAnalysis.isPartialSubmissionCorrect()){
                analysis.setSubmissionSuitsSolution(false);
            }
        }

        return analysis;
    }


    // note: grading object fragt der Student ab-> end result

    /**
     * Important to mention, that grading is not just based on if partial submission is true or false
     * but the individual criteria are graded -> in this version, a partial submission is only analyzed by one criterion
     * which means that if the criteria is wrong the partial submission is graded zero points
     *
     * But the idea of this method (and method analyze) is to maximize the flexibility and to prepare the case, if one
     * partial submission is analyzed by multiple criteria
     * @param analysis the Analysis
     * @param maxPoints the maxPoints for this submission
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @return
     * @throws Exception
     */
    @Override
    public Grading grade(Analysis analysis, int maxPoints, Map<String, String> passedAttributes, Map<String, String> passedParameters) throws Exception {
        logger.info("analysis: {}", analysis);
        logger.info("maxPoints: {}", maxPoints);
        //logPassedAttributes(passedAttributes, passedParameters);


        // cast analysis object to PmAnalysis
        PmAnalysis pmAnalysis;
        if(analysis instanceof PmAnalysis) {
            pmAnalysis = (PmAnalysis) analysis;
        }else {
            return null; // or Exception
        }

        // declaration
        PmGrader pmGrader;      //actual grading is happening here
        PmCriterionGradingConfig pmCriterionGradingConfig;
        PmGraderConfig pmGraderConfig;
        String action;
        String actionParam;
        Iterator<PmPartialSubmissionAnalysis> pmPartialSubmissionAnalysisIterator;
        PmPartialSubmissionAnalysis partialSubmissionAnalysis;
        PmPartialSubmissionGrading partialSubmissionGrading;

        // initialization
        pmGrader = new PmGrader();
        actionParam = passedAttributes.get("action");
        action = actionParam;
        pmPartialSubmissionAnalysisIterator = pmAnalysis.iterSubmissionAnalysis();
        pmGraderConfig = new PmGraderConfig();

        /*configure criteria (in this case, configure all criteria, since all criteria are used in diagnose and submit
        * configuring in terms of settings available points per criteria */

        // CORRECT PAIRS - Criterion
        pmCriterionGradingConfig = new PmCriterionGradingConfig();
        pmCriterionGradingConfig.setPositivePoints(1);
        pmCriterionGradingConfig.setNegativePoints(0);
        pmGraderConfig.addCriterionGradingConfig(PmEvaluationCriterion.CORRECT_PAIRS, pmCriterionGradingConfig);

        // CORRECT EVENT
        pmCriterionGradingConfig = new PmCriterionGradingConfig();
        pmCriterionGradingConfig.setPositivePoints(1);
        pmCriterionGradingConfig.setNegativePoints(0);
        pmGraderConfig.addCriterionGradingConfig(PmEvaluationCriterion.CORRECT_EVENT, pmCriterionGradingConfig);

        // CORRECT PAIRS ALPHA
        pmCriterionGradingConfig = new PmCriterionGradingConfig();
        pmCriterionGradingConfig.setPositivePoints(2);
        pmCriterionGradingConfig.setNegativePoints(0);
        pmGraderConfig.addCriterionGradingConfig(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA, pmCriterionGradingConfig);

        // do the grading for each submitted answer and its corresponding criterion
        while(pmPartialSubmissionAnalysisIterator.hasNext()){
            partialSubmissionAnalysis = pmPartialSubmissionAnalysisIterator.next();
            partialSubmissionGrading = new PmPartialSubmissionGrading(partialSubmissionAnalysis, pmGraderConfig);
            partialSubmissionGrading.grade(true);

            pmGrader.addPmPartialSubmissionGrading(partialSubmissionAnalysis.getSubmissionID(), partialSubmissionGrading);
        }

        // sum up to get total achieved points
        pmGrader.setMaxPoints(maxPoints);
        pmGrader.sumUpTotalPoints();

        return pmGrader;
    }


    /**
     * Method Report:
     *
     * @param analysis the Analysis
     * @param grading the grading
     * @param passedAttributes the passed attributes
     * @param passedParameters the passed parameters
     * @param locale the locale
     * @return
     * @throws Exception
     */

    @Override
    public Report report(Analysis analysis, Grading grading, Map<String, String> passedAttributes, Map<String, String> passedParameters, Locale locale) throws Exception {
        logger.info("analysis: {}", analysis);
        logger.info("grading: {}", grading);
        //logPassedAttributes(passedAttributes, passedParameters);

        PmReporter pmReporter = new PmReporter();
        PmReporterConfig pmReporterConfig = new PmReporterConfig();

        String action;
        String diagnoseLevel;
        String actionParam = passedAttributes.get("action");
        String diagnoseLevelParam = passedAttributes.get("diagnoseLevel");

        action = actionParam;
        diagnoseLevel = diagnoseLevelParam;

        if(action.toUpperCase().equals(PmEvaluationAction.SUBMIT.toString())){
            pmReporterConfig.setAction(PmEvaluationAction.SUBMIT);
            pmReporterConfig.setDiagnoseLevel(2);
        }
        if(action.toUpperCase().equals(PmEvaluationAction.DIAGNOSE.toString())){
            pmReporterConfig.setAction(PmEvaluationAction.DIAGNOSE);
            pmReporterConfig.setDiagnoseLevel(Integer.parseInt(diagnoseLevel));
        }


        return pmReporter.createReport((PmAnalysis) analysis, (DefaultGrading) grading, pmReporterConfig, locale);
    }


    /**
     * Logs the passedAttributes and passedParameters
     * @param passedAttributes the passedAttributes
     * @param passedParameters the passedParameters
     */
    public void logPassedAttributes(Map<String, String> passedAttributes, Map<String, String> passedParameters){
        logger.info("passedAttributes (" + passedAttributes.size() + ")");
        for (String key: passedAttributes.keySet()) {
            logger.info("  key: "+key+" value: " + passedAttributes.get(key));
        }
        logger.info("passedParameters (" + passedParameters.size() + ")");
        for (String key: passedParameters.keySet()) {
            logger.info("  key: "+key+" value: " + passedParameters.get(key));
        }
    }


    @Override
    public String generateHTMLResult(Analysis analysis, Map<String, String> passedAttributes, Locale locale) {
        return null;
    }


    private List<Arc<?,?>> convertSubmissionSeven(String solutionSeven){
        StringBuilder workSB = new StringBuilder(solutionSeven.trim());
        // convert submitted answer (result set)
        List<Arc<?,?>> submittedAlphaArcs = new ArrayList<>();

        try {
            // analyze and convert submitted arcs coming from source place (might be multiple)
            while (workSB.indexOf("i") != -1) {
                Arc<String, Event> tempArc = new Arc<>();

                int indexI = workSB.indexOf("i");
                int bracketOpen = workSB.lastIndexOf("(", indexI);
                int bracketClose = workSB.indexOf(")", indexI);

                // no need for further checks -> if submitted syntax is not correct (=> (i,X) <=), exception is thrown
                String tempSubString = workSB.substring(bracketOpen, bracketClose + 1);

                for (int i = 1; i < tempSubString.length(); i++) {        // (i,X)
                    if (isLetter(tempSubString.charAt(i))) {
                        if (tempSubString.charAt(i) == 'i') {
                            tempArc.setFirst("i");
                        } else {
                            char tempChar = Character.toUpperCase(tempSubString.charAt(i));
                            tempArc.setSecond(new Event(Character.toString(tempChar)));
                        }

                    }
                }
                submittedAlphaArcs.add(tempArc);
                workSB.delete(bracketOpen, bracketClose + 1);
            }

            // analyze and convert submitted arcs going to sink place (might be multiple)
            while (workSB.indexOf("o") != -1) {
                Arc<Event, String> tempArc = new Arc<>();

                int indexO = workSB.indexOf("o");
                int bracketClose = workSB.indexOf(")", indexO);
                int bracketOpen = workSB.lastIndexOf("(", indexO);

                String tempSubString = workSB.substring(bracketOpen, bracketClose + 1);

                for (int i = 1; i < tempSubString.length(); i++) {     // (X,o)
                    if (isLetter(tempSubString.charAt(i))) {
                        if (tempSubString.charAt(i) == 'o') {
                            tempArc.setSecond("o");
                        } else {
                            char tempChar = Character.toUpperCase(tempSubString.charAt(i));
                            tempArc.setFirst(new Event(Character.toString(tempChar)));
                        }
                    }
                }

                submittedAlphaArcs.add(tempArc);
                workSB.delete(bracketOpen, bracketClose + 1);
            }

            // from here on there are no more arcs from source or to sink place, therefore change of analysis procedure
            while (workSB.indexOf("(") != -1) { // as long as there is an open bracket in submitted answer, DO:

                // EITHER:
                Arc<Event, Place> eventPlaceArc;
                // OR:
                Arc<Place, Event> placeEventArc;


                int bracketOpen = workSB.indexOf("(");
                int bracketClose1 = workSB.indexOf(")");
                int bracketClose = workSB.indexOf(")", bracketClose1 + 1);    // next close bracket after close-place bracket

                StringBuilder tempWorkSB = new StringBuilder(workSB.substring(bracketOpen, bracketClose + 1));    // (X, p({},{})) XOR (p({},{}), X)

                /*
                 * If the first comma (",") inside the respective pair (either (X, p({},{})) OR (p({},{}), X)) comes before
                 * the first "p" (start of a new place) indicates
                 * that place comes at second position and
                 * transition at first position
                 */
                boolean placeOnFirst = true;
                if (tempWorkSB.indexOf(",") < tempWorkSB.indexOf("p")) {
                    placeOnFirst = false;

                }

                // start to analyze/ convert place
                int placeOpen = tempWorkSB.indexOf("(", 1);
                int placeClose = tempWorkSB.indexOf(")");


                StringBuilder placeSB = new StringBuilder(tempWorkSB.substring(placeOpen, placeClose + 1));   // ({},{})
                Pair<List<Event>, List<Event>> tempPair = new Pair<>(); // == place
                while (placeSB.indexOf("[") != -1) {

                    int curlyBracketOpen = placeSB.indexOf("[");
                    int curlyBracketClose = placeSB.indexOf("]");
                    String tempSubstring = placeSB.substring(curlyBracketOpen, curlyBracketClose + 1);     //{x,x,xv}

                    List<Event> tempList = new ArrayList<>();
                    for (int i = 1; i < tempSubstring.length(); i++) {
                        if (isLetter(tempSubstring.charAt(i))) {
                            // convert to upper case since log events are upper case
                            char tempChar = Character.toUpperCase(tempSubstring.charAt(i));

                            // check char at next index:
                            if (!isLetter(tempSubstring.charAt(i + 1))) {
                                tempList.add(new Event(Character.toString(tempChar)));
                            } else {  // should never be the case except for exceptionally large traces/ log
                                String concatenateChars = tempChar + "" + Character.toUpperCase(tempSubstring.charAt(i + 1));
                                tempList.add(new Event(concatenateChars));
                                i += 1;  // skips one index since this corresponding index is already added
                            }
                        }
                    }

                    if (!tempPair.firstPositionSet()) {
                        tempPair.setFirstPosition(tempList);
                    } else if (!tempPair.secondPositionSet()) {
                        tempPair.setSecondPosition(tempList);
                    }

                    placeSB.delete(curlyBracketOpen, curlyBracketClose + 1);
                }

                tempWorkSB.delete(placeOpen, placeClose + 1); // removes place => resultSB either (X,p) or (p,X)

                // analyze the rest of the String: either (X,p) or (p,X)
                Event tempEvent = null;
                for (int i = 1; i < tempWorkSB.length(); i++) {
                    if (isLetter(tempWorkSB.charAt(i)) && tempWorkSB.charAt(i) != 'p') {
                        // convert to upper case since log events are upper case
                        char tempChar = Character.toUpperCase(tempWorkSB.charAt(i));

                        // check char at next index
                        if (!isLetter(tempWorkSB.charAt(i + 1))) {
                            tempEvent = new Event(Character.toString(tempChar));
                        } else {
                            String concatenateChars = tempChar + "" + Character.toUpperCase(tempWorkSB.charAt(i + 1));
                            tempEvent = new Event(concatenateChars);
                            i += 1;
                        }
                    }
                }

                if (placeOnFirst) {
                    placeEventArc = new Arc<>();
                    placeEventArc.setFirst(new Place(tempPair));
                    placeEventArc.setSecond(tempEvent);

                    submittedAlphaArcs.add(placeEventArc);
                } else {
                    eventPlaceArc = new Arc<>();
                    eventPlaceArc.setFirst(tempEvent);
                    eventPlaceArc.setSecond(new Place(tempPair));

                    submittedAlphaArcs.add(eventPlaceArc);
                }

                workSB.delete(bracketOpen, bracketClose + 1);
            }
        }catch(Exception e){
            this.logger.error("Could not convert aaI7 to respective Object", e);
        }

        return submittedAlphaArcs;
    }
    private static boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

}
