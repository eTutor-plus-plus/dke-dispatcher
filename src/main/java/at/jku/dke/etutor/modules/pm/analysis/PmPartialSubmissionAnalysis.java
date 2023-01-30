package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.modules.pm.AlphaAlgo.Arc;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Event;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Pair;
import at.jku.dke.etutor.modules.pm.AlphaAlgo.Place;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.io.Serializable;
import java.util.*;

/**
 * ...
 * @author Falk Görner
 * @version 1.0
 */


public class PmPartialSubmissionAnalysis {
    // OBJECT FIELDS
    private final String INTERNAL_ERROR = "This is an internal system error. ";
    private final String CONTACT_ADMIN = "Please contact the system administrator. ";

    private boolean partialSubmissionCorrect;
    // stores detailed analysis based on multiple possible evaluation Criteria
    private final HashMap<PmEvaluationCriterion, PmCriterionAnalysis> criterionAnalysis;
    private final String submissionID;
    private AnalysisException exception;    // individual exception to the respective partial submission


    // LOGGER STUFF

    // CONSTRUCTOR
    public PmPartialSubmissionAnalysis(String subID){
        this.submissionID = subID;
        this.exception = null;
        this.partialSubmissionCorrect = false;
        this.criterionAnalysis = new HashMap<>();
    }


    // METHODS
    public void analyze(Serializable partialSubmission, PmAnalyzerConfig analyzerConfig){
        String loggerMessage;
        String submittedAnswer;
        PmCriterionAnalysis pmCriterionAnalysis;

        if(partialSubmission == null){
            loggerMessage = "";
            loggerMessage = loggerMessage.concat("Analysis of partial submission "+ submissionID + " stopped! ");
            loggerMessage = loggerMessage.concat("Submitted answer is empty. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);

            // logger
            exception = new AnalysisException(loggerMessage);
            return;
        }

        if(partialSubmission instanceof String){
            submittedAnswer = (String) partialSubmission;
        }else{
            loggerMessage = "";
            loggerMessage = loggerMessage.concat("Analysis of partial submission " + submissionID + " stopped with errors. ");
            loggerMessage = loggerMessage.concat("Submission is not utilizable. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);

            //logger
            exception = new AnalysisException(loggerMessage);
            return;
        }

        if(analyzerConfig == null){
            loggerMessage = "";
            loggerMessage = loggerMessage.concat("Analysis of partial submission " + submissionID + " stopped with errors. ");
            loggerMessage = loggerMessage.concat("No configuration found. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);

            //logger
            exception = new AnalysisException(loggerMessage);
            return;
        }

        if(submissionID.equals("orI1") || submissionID.equals("orI2") || submissionID.equals("orI3")){
            if(analyzerConfig.isCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS)){
                pmCriterionAnalysis = this.analyzePairs(analyzerConfig, submittedAnswer);
                addCriterionAnalysis(PmEvaluationCriterion.CORRECT_PAIRS, pmCriterionAnalysis);

                if(pmCriterionAnalysis.getAnalysisException() != null || !pmCriterionAnalysis.isCriterionSatisfied()){
                    exception = pmCriterionAnalysis.getAnalysisException();
                    // return; in case of multiple criteria
                }else{
                    this.partialSubmissionCorrect = true;
                }
            }

            // more criteria are possible but in this case not necessary

        }else if(submissionID.equals("aaI1") || submissionID.equals("aaI2") || submissionID.equals("aaI3")){
            if(analyzerConfig.isCriterionToAnalyze(PmEvaluationCriterion.CORRECT_EVENT)){
                pmCriterionAnalysis = this.analyzeEvents(analyzerConfig, submittedAnswer);
                addCriterionAnalysis(PmEvaluationCriterion.CORRECT_EVENT, pmCriterionAnalysis);

                if(pmCriterionAnalysis.getAnalysisException() != null || !pmCriterionAnalysis.isCriterionSatisfied()){
                    exception = pmCriterionAnalysis.getAnalysisException();
                    //return; in case of multiple criteria
                }else{
                    this.partialSubmissionCorrect = true;
                }
            }



        }else if(submissionID.equals("aaI4") || submissionID.equals("aaI5") || submissionID.equals("aaI6") ){
            if(analyzerConfig.isCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA)){
                pmCriterionAnalysis = this.analyzeAlphaPairs(analyzerConfig, submittedAnswer);
                addCriterionAnalysis(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA, pmCriterionAnalysis);

                if(pmCriterionAnalysis.getAnalysisException() != null || !pmCriterionAnalysis.isCriterionSatisfied()){
                    exception = pmCriterionAnalysis.getAnalysisException();
                    // return;
                }else{
                    this.partialSubmissionCorrect = true;
                }
            }



        }else{  // submissionID.equals("aaI7")
            if(analyzerConfig.isCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA)){
                pmCriterionAnalysis = this.analyzeAlphaArcs(analyzerConfig, submittedAnswer);
                addCriterionAnalysis(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA, pmCriterionAnalysis);

                if(pmCriterionAnalysis.getAnalysisException() != null || !pmCriterionAnalysis.isCriterionSatisfied()){
                    exception = pmCriterionAnalysis.getAnalysisException();
                    // return;
                }else{
                    this.partialSubmissionCorrect = true;
                }
            }
        }

    }

    /**
     * Method checked for correctness => works
     *
     * Comment: it is assumed that answer is submitted as follows:
     * OR: (), (), (), ....e.g: (a,b)
     *
     * @param config Analyzer Configuration to get correct answer
     * @param submittedAnswer corresponding partial submission (String)
     * @return returns PairsAnalysis object, with missing and surplus tuples and information if criterion is satisfied
     */
    private PairsAnalysis analyzePairs(PmAnalyzerConfig config, String submittedAnswer){
        String loggerMessage;
        PairsAnalysis pairsAnalysis = new PairsAnalysis();

        StringBuilder workSB = new StringBuilder(submittedAnswer.trim());

        // converted result set
        Set<Pair<Event, Event>> submittedPairs = new HashSet<>();
        List<Pair<Event, Event>> missingPairs = new ArrayList<>();
        List<Pair<Event, Event>> surplusPairs = new ArrayList<>();

        // get correct answer to corresponding submission with original type
        Set<Pair<Event, Event>> correctPairs = (Set<Pair<Event, Event>>) config.getCorrectAnswer(submissionID);

        // convert input string to a set of pairs (of events)
        // condition:  if char does not occur as a substring, -1 is returned
        // loop until this condition is met
        try {
            while (workSB.indexOf("(") != -1) {
                // get first occurrence
                int bracketOpen = workSB.indexOf("(");
                int bracketClose = workSB.indexOf(")");

                // returns string with events
                String tempSubstring = workSB.substring(bracketOpen, bracketClose + 1);
                Pair<Event, Event> tempPair = new Pair<>();

                // start at 1, since openBracket is at index 0
                for (int i = 1; i < tempSubstring.length(); i++) {
                    if (isLetter(tempSubstring.charAt(i))) {
                        // convert to upper case since log events are upper case
                        char tempChar = Character.toUpperCase(tempSubstring.charAt(i));

                        // check char at next index:
                        if (!isLetter(tempSubstring.charAt(i + 1))) {
                            if (!tempPair.firstPositionSet()) {
                                tempPair.setFirstPosition(new Event(Character.toString(tempChar)));
                            } else if(!tempPair.secondPositionSet()){
                                tempPair.setSecondPosition(new Event(Character.toString(tempChar)));
                            }
                        } else {  // should never be the case, except for exceptionally large traces/ logs
                            String concatenateChars = tempChar + "" + Character.toUpperCase(tempSubstring.charAt(i + 1));
                            if (!tempPair.firstPositionSet()) {
                                tempPair.setFirstPosition(new Event(concatenateChars));
                            }else if(!tempPair.secondPositionSet()){
                                tempPair.setSecondPosition(new Event(concatenateChars));
                            }
                            i += 1; // skips one index since this corresponding index is already added
                        }
                    }
                }

                // add resulting pair (converted on basis of submitted answer) to set
                submittedPairs.add(tempPair);

                // delete analyzed substring from string builder
                workSB.delete(bracketOpen, bracketClose + 1);
            }

            // compare submittedPairs to correctPairs:
            for (var pair : submittedPairs) {
                if (!correctPairs.contains(pair)) {
                    surplusPairs.add(pair);
                }
            }

            for (var pair : correctPairs) {
                if (!submittedPairs.contains(pair)) {
                    missingPairs.add(pair);
                }
            }

            pairsAnalysis.addMissingPairs(missingPairs);
            pairsAnalysis.addSurplusPairs(surplusPairs);

            // update Exception and Satisfaction
            if (pairsAnalysis.hasSurplusPairs() || pairsAnalysis.hasMissingPairs()) {
                pairsAnalysis.setCriterionIsSatisfied(false);
                pairsAnalysis.setAnalysisException(new AnalysisException("Submitted answer has missing pairs: "
                        + pairsAnalysis.hasMissingPairs() + " and/ or surplus pairs: " + pairsAnalysis.hasSurplusPairs()));
            }
        }catch (Exception e){

            pairsAnalysis.setCriterionIsSatisfied(false);
            pairsAnalysis.setAnalysisException(new AnalysisException("Error: " + e));

            loggerMessage ="";
            loggerMessage = loggerMessage.concat("Something went wrong. ");
            loggerMessage = loggerMessage.concat("Check that your spelling matches the information provided. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);
            // add logger content
            System.out.println(loggerMessage+ "\nError message: " + e);
        }

        System.out.println(pairsAnalysis.isCriterionSatisfied() + ", surplus pairs: " + pairsAnalysis.getSurplusPairs()
        + " missing: " + pairsAnalysis.getMissingPairs());
        return pairsAnalysis;
    }

    /**
     * METHOD DESCRIPTION
     *
     * Comment: it is assumed that answer is submitted as follows:
     * T = A, B, C, D, ...
     * @param config
     * @param submittedAnswer
     * @return
     */
    private EventAnalysis analyzeEvents(PmAnalyzerConfig config, String submittedAnswer){
        String loggerMessage;
        EventAnalysis eventAnalysis = new EventAnalysis();


        // converted Result Sets
        Set<Event> submittedEvents = new TreeSet<>();
        Set<Event> missingEvents = new TreeSet<>();
        Set<Event> surplusEvents = new TreeSet<>();

        // get correct answer
        Set<Event> correctEvents = (Set<Event>) config.getCorrectAnswer(submissionID);

        try{
            for(int i= 0; i< submittedAnswer.length(); i++){
                Event tempEvent;

                // iterate over submitted answer (String):
                if (isLetter(submittedAnswer.charAt(i))) {
                    // convert to upper case since log events are upper case
                    char tempChar = Character.toUpperCase(submittedAnswer.charAt(i));

                    // check char at next index:
                    if(i < submittedAnswer.length()-1) {
                        if (!isLetter(submittedAnswer.charAt(i + 1))) { //e.g. ','
                            tempEvent = new Event(Character.toString(tempChar));
                        } else {  // should never be the case, except for exceptionally large traces/ logs
                            String concatenateChars = tempChar + "" + Character.toUpperCase(submittedAnswer.charAt(i + 1));
                            tempEvent = new Event(concatenateChars);
                            i += 1; // skips one index since this corresponding index is already added
                        }
                    }else{
                        tempEvent = new Event(Character.toString(tempChar));
                    }
                    submittedEvents.add(tempEvent);
                }
            }

            // compare submittedEvents to correctEvents:
            for (var event : submittedEvents) {
                if (!correctEvents.contains(event)) {
                    surplusEvents.add(event);
                }
            }

            for (var event : correctEvents) {
                if (!submittedEvents.contains(event)) {
                    missingEvents.add(event);
                }
            }

            eventAnalysis.addMissingEvents(missingEvents);
            eventAnalysis.addSurplusEvents(surplusEvents);

            if(eventAnalysis.hasSurplusEvents() || eventAnalysis.hasMissingEvents()){
                eventAnalysis.setCriterionIsSatisfied(false);
                eventAnalysis.setAnalysisException(new AnalysisException("Submitted answer has missing " +
                        "events: " + eventAnalysis.hasMissingEvents() + " and/ or surplus events: "
                + eventAnalysis.hasSurplusEvents()));
            }

        }catch(Exception e){

            eventAnalysis.setCriterionIsSatisfied(false);
            eventAnalysis.setAnalysisException(new AnalysisException("Error: " + e));

            loggerMessage = "";
            loggerMessage = loggerMessage.concat("Something went wrong. ");
            loggerMessage = loggerMessage.concat("Check that your spelling matches the information provided. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);
            // add logger content
            System.out.println(loggerMessage+ "\nError message: " + e);
        }

        System.out.println(eventAnalysis.isCriterionSatisfied() + ", missing events: " + eventAnalysis.getMissingEvents() + ", surplus events: " + eventAnalysis.getSurplusEvents());
        return eventAnalysis;
    }


    /**
     * METHOD DESCRIPTION
     *
     * Comment: it is assumed that answer to "aaI4" & "aaI5" is submitted as follows:
     * Xl/ Yl set: ({x},{x,y}), ({...},{...}),... Notation follows notation in paper: vanAalst2004 p.1137
     * () == Pair, {} == Set of events
     *
     * Comment: it is assumed that answer to "aaI6" is submitted as follows:
     * Pl set: i,o, p({},{}), p({},{}), ...
     * additional comment: letter p in front of pair (...) can also be ignored for the sake of simplicity and for better clarity
     *
     *
     *
     * @param config Analyzer Configuration that which contains the correct answer
     * @param submittedAnswer submitted answer of student of type String
     * @return returns AlphaPairsAnalysis that contains missing and surplus pairs and stores if criterion is satisfied
     */
    private AlphaPairsAnalysis analyzeAlphaPairs(PmAnalyzerConfig config, String submittedAnswer){
        AlphaPairsAnalysis alphaPairsAnalysis = new AlphaPairsAnalysis();
        String loggerMessage;
        StringBuilder workSB = new StringBuilder(submittedAnswer.trim());

        // converted result set
        List<Pair<List<Event>, List<Event>>> submittedAlphaPairs = new ArrayList<>();
        List<Pair<List<Event>, List<Event>>> missingAlphaPairs = new ArrayList<>();
        List<Pair<List<Event>, List<Event>>> surplusAlphaPairs = new ArrayList<>();


        if(submissionID.equals("aaI4") || submissionID.equals("aaI5")){ // X-Set, Y-Set
            // get correct answer to corresponding submission with original type
            List<Pair<List<Event>, List<Event>>> correctAlphaPairs = (List<Pair<List<Event>, List<Event>>>) config.getCorrectAnswer(submissionID);

            try{
                while(workSB.indexOf("(") != -1){
                    Pair<List<Event>, List<Event>> tempAlphaPair = new Pair<>();

                    int bracketOpen = workSB.indexOf("(");  // first occurrence of "("
                    int bracketClose = workSB.indexOf(")"); // first occurrence of ")"

                    StringBuilder tempWorkSB = new StringBuilder(workSB.substring(bracketOpen, bracketClose+1));    //({xx},{xx})

                    while(tempWorkSB.indexOf("{") != -1){

                        int curlyBracketOpen = tempWorkSB.indexOf("{");
                        int curlyBracketClose = tempWorkSB.indexOf("}");
                        String tempSubstring = tempWorkSB.substring(curlyBracketOpen, curlyBracketClose+1);     //{xxx}

                        List<Event> tempList = new ArrayList<>();
                        for(int i=1; i< tempSubstring.length(); i++){
                            if(isLetter(tempSubstring.charAt(i))){
                                // convert to upper case since log events are upper case
                                char tempChar = Character.toUpperCase(tempSubstring.charAt(i));

                                // check char at next index:
                                if(!isLetter(tempSubstring.charAt(i+1))){
                                    tempList.add(new Event(Character.toString(tempChar)));
                                }else{  // should never be the case except for exceptionally large traces/ log
                                    String concatenateChars = tempChar + "" + Character.toUpperCase(tempSubstring.charAt(i+1));
                                    tempList.add(new Event(concatenateChars));
                                    i+=1;  // skips one index since this corresponding index is already added
                                }
                            }
                        }

                        if(!tempAlphaPair.firstPositionSet()){
                            tempAlphaPair.setFirstPosition(tempList);
                        }else if(!tempAlphaPair.secondPositionSet()){
                            tempAlphaPair.setSecondPosition(tempList);
                        }

                        tempWorkSB.delete(curlyBracketOpen, curlyBracketClose+1);
                    }

                    // add resulting pair to set
                    submittedAlphaPairs.add(tempAlphaPair);

                    // delete analyzed substring from string builder
                    workSB.delete(bracketOpen, bracketClose+1);
                }

                // Analysis:
                for(var alphaPair: submittedAlphaPairs){
                    if(!correctAlphaPairs.contains(alphaPair)){
                        surplusAlphaPairs.add(alphaPair);
                    }
                }

                for(var alphaPair: correctAlphaPairs){
                    if(!submittedAlphaPairs.contains(alphaPair)){
                        missingAlphaPairs.add(alphaPair);
                    }
                }

                alphaPairsAnalysis.addMissingAlphaPairs(missingAlphaPairs);
                alphaPairsAnalysis.addSurplusAlphaPairs(surplusAlphaPairs);


                // update Exception and Satisfaction
                if(alphaPairsAnalysis.hasSurplusAlphaPairs() || alphaPairsAnalysis.hasMissingAlphaPairs()){
                    alphaPairsAnalysis.setCriterionIsSatisfied(false);
                    alphaPairsAnalysis.setAnalysisException(new AnalysisException("Submitted answer has missing alpha pairs: " +
                            alphaPairsAnalysis.hasMissingAlphaPairs() + " and/or surplus alpha pairs: " +
                            alphaPairsAnalysis.hasSurplusAlphaPairs()
                    ));
                }

            }catch(Exception e){

                alphaPairsAnalysis.setCriterionIsSatisfied(false);
                alphaPairsAnalysis.setAnalysisException(new AnalysisException("Error: " + e));

                loggerMessage = "";
                loggerMessage = loggerMessage.concat("Something went wrong. ");
                loggerMessage = loggerMessage.concat("Check that your spelling matches the information provided. ");
                loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
                loggerMessage = loggerMessage.concat(CONTACT_ADMIN);
                //logger content
                System.out.println(loggerMessage + "\nError message: " + e);
            }


        }else if(submissionID.equals("aaI6")){  // P-Set (places)
            // get correct answer to corresponding submission with original type
            List<Place> correctAlphaPairs = (List<Place>) config.getCorrectAnswer(submissionID);

            boolean hasSinkPlace = false;
            boolean hasSourcePlace = false;


            try{
                while (workSB.indexOf("(") != -1) {
                    Pair<List<Event>, List<Event>> tempAlphaPair = new Pair<>();

                    int bracketOpen = workSB.indexOf("(");  // first occurrence of "("
                    int bracketClose = workSB.indexOf(")"); // first occurrence of ")"

                    StringBuilder tempWorkSB = new StringBuilder(workSB.substring(bracketOpen, bracketClose+1));    //({xx},{xx})

                    while(tempWorkSB.indexOf("{") != -1) {

                        int curlyBracketOpen = tempWorkSB.indexOf("{");
                        int curlyBracketClose = tempWorkSB.indexOf("}");
                        String tempSubstring = tempWorkSB.substring(curlyBracketOpen, curlyBracketClose + 1);     //{xxx}

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

                        if (!tempAlphaPair.firstPositionSet()) {
                            tempAlphaPair.setFirstPosition(tempList);
                        }else if(!tempAlphaPair.secondPositionSet()) {
                            tempAlphaPair.setSecondPosition(tempList);
                        }

                        tempWorkSB.delete(curlyBracketOpen, curlyBracketClose + 1);

                    }
                    // add resulting pair to set
                    submittedAlphaPairs.add(tempAlphaPair);

                    // delete analyzed substring from string builder
                    workSB.delete(bracketOpen, bracketClose+1);
                }

                // check for source and sink place
                // only valid, if student submits answer with standalone "i" or "o"
                // every appendix to "i" or "o" is not a valid submission
                if(workSB.indexOf("i") != -1){  // i exists
                    int sourceIndex = workSB.indexOf("i");
                    if(sourceIndex != 0){
                        if(sourceIndex < workSB.length()-1) {
                            if (!isLetter(workSB.charAt(sourceIndex - 1)) && !isLetter(workSB.charAt(sourceIndex + 1))) {
                                hasSourcePlace = true;
                            }
                        }else{
                            if(!isLetter(workSB.charAt(sourceIndex - 1))){
                                hasSourcePlace = true;
                            }
                        }
                    }else{
                        if(!isLetter(workSB.charAt(sourceIndex+1))){
                            hasSourcePlace = true;
                        }
                    }
                }

                if(workSB.indexOf("o") != -1){  // o exists
                    int sinkIndex = workSB.indexOf("o");
                    if(sinkIndex != 0){
                        if(sinkIndex < workSB.length()-1){
                            if(!isLetter(workSB.charAt(sinkIndex-1)) && !isLetter(workSB.charAt(sinkIndex+1))){
                                hasSinkPlace = true;
                            }
                        }else{
                            if(!isLetter(workSB.charAt(sinkIndex -1))){
                                hasSinkPlace = true;
                            }
                        }
                    }else{
                        if(!isLetter(workSB.charAt(sinkIndex+1))){
                            hasSinkPlace = true;
                        }
                    }
                }


                // ANALYSIS:
                for(var alphaPair: submittedAlphaPairs){
                    if(!correctAlphaPairs.contains(new Place(alphaPair))){
                        surplusAlphaPairs.add(alphaPair);
                    }
                }

                for(var alphaPair: correctAlphaPairs){
                    // check first place (source)
                    if(alphaPair.getInputTransition() == null){
                        if(!hasSourcePlace){
                            missingAlphaPairs.add(alphaPair.getPlace());
                        }

                    // check last place (sink)
                    }else if(alphaPair.getOutputTransition() == null){
                        if(!hasSinkPlace){
                            missingAlphaPairs.add(alphaPair.getPlace());
                        }

                    }else{
                        if(!submittedAlphaPairs.contains(alphaPair.getPlace())){
                            missingAlphaPairs.add(alphaPair.getPlace());
                        }
                    }
                }

                alphaPairsAnalysis.addSurplusAlphaPairs(surplusAlphaPairs);
                alphaPairsAnalysis.addMissingAlphaPairs(missingAlphaPairs);

                // update Exception and Satisfaction
                if(alphaPairsAnalysis.hasSurplusAlphaPairs() || alphaPairsAnalysis.hasMissingAlphaPairs()){
                    alphaPairsAnalysis.setCriterionIsSatisfied(false);
                    alphaPairsAnalysis.setAnalysisException(new AnalysisException("Submitted answer has missing alpha pairs: " +
                            alphaPairsAnalysis.hasMissingAlphaPairs() + " and/or surplus alpha pairs: " +
                            alphaPairsAnalysis.hasSurplusAlphaPairs()
                    ));
                }



            }catch(Exception e){

                alphaPairsAnalysis.setCriterionIsSatisfied(false);
                alphaPairsAnalysis.setAnalysisException(new AnalysisException("Error: " + e));

                loggerMessage = "";
                loggerMessage = loggerMessage.concat("Something went wrong. ");
                loggerMessage = loggerMessage.concat("Check that your spelling matches the information provided. ");
                loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
                loggerMessage = loggerMessage.concat(CONTACT_ADMIN);
                //logger content
                System.out.println(loggerMessage + "\nError message: " + e);
            }
        }


        System.out.println(alphaPairsAnalysis.isCriterionSatisfied()+ ", surplus pairs: " + alphaPairsAnalysis.getSurplusAlphaPairs()
        + ", missing pairs: " + alphaPairsAnalysis.getMissingAlphaPairs());
        return alphaPairsAnalysis;
    }


    /**
     * submissionID.equals("aaI7") => F-Set (arcs)
     * Comment: it is assumed that answer to aaI7 is submitted as follows:
     * there are multiple (i,Transition) or (Transition, o) Arcs possible
     * Fl set: (i,Transition), (Transition,o), (Transition, p({},{})), (p({},{}), Transition), ...
     *
     * @param config
     * @param submittedAnswer
     * @return
     */
    private AlphaArcsAnalysis analyzeAlphaArcs(PmAnalyzerConfig config, String submittedAnswer){
        AlphaArcsAnalysis alphaArcsAnalysis = new AlphaArcsAnalysis();
        String loggerMessage;
        StringBuilder workSB = new StringBuilder(submittedAnswer.trim());

        // get correct answer to corresponding submission with original type
        List<Arc<?, ?>> correctAlphaArcs = (List<Arc<?, ?>>) config.getCorrectAnswer(submissionID);

        // convert submitted answer (result set)
        List<Arc<?,?>> submittedAlphaArcs = new ArrayList<>();
        List<Arc<?,?>> surplusAlphaArcs = new ArrayList<>();
        List<Arc<?,?>> missingAlphaArcs = new ArrayList<>();

        try{
            // analyze and convert submitted arcs coming from source place (might be multiple)
            while(workSB.indexOf("i") != -1){
                Arc<String, Event> tempArc = new Arc<>();

                int indexI = workSB.indexOf("i");
                int bracketOpen = workSB.lastIndexOf("(", indexI);
                int bracketClose = workSB.indexOf(")", indexI);

                // no need for further checks -> if submitted syntax is not correct (=> (i,X) <=), exception is thrown
                String tempSubString = workSB.substring(bracketOpen, bracketClose+1);

                for(int i = 1; i < tempSubString.length(); i++){        // (i,X)
                    if(isLetter(tempSubString.charAt(i))){
                        if(tempSubString.charAt(i) == 'i'){
                            tempArc.setFirst("i");
                        }else{
                            char tempChar = Character.toUpperCase(tempSubString.charAt(i));
                            tempArc.setSecond(new Event(Character.toString(tempChar)));
                        }

                    }
                }
                submittedAlphaArcs.add(tempArc);
                workSB.delete(bracketOpen, bracketClose+1);
            }

            // analyze and convert submitted arcs going to sink place (might be multiple)
            while(workSB.indexOf("o") != -1){
                Arc<Event, String> tempArc = new Arc<>();

                int indexO = workSB.indexOf("o");
                int bracketClose = workSB.indexOf(")", indexO);
                int bracketOpen = workSB.lastIndexOf("(", indexO);

                String tempSubString = workSB.substring(bracketOpen, bracketClose+1);

                for(int i= 1; i < tempSubString.length(); i++){     // (X,o)
                    if(isLetter(tempSubString.charAt(i))){
                        if(tempSubString.charAt(i) == 'o'){
                            tempArc.setSecond("o");
                        }else{
                            char tempChar = Character.toUpperCase(tempSubString.charAt(i));
                            tempArc.setFirst(new Event(Character.toString(tempChar)));
                        }
                    }
                }

                submittedAlphaArcs.add(tempArc);
                workSB.delete(bracketOpen, bracketClose+1);
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

                StringBuilder tempWorkSB = new StringBuilder(workSB.substring(bracketOpen, bracketClose+1));    // (X, p({},{})) XOR (p({},{}), X)

                /*
                * If the first comma (",") inside the respective pair (either (X, p({},{})) OR (p({},{}), X)) comes before
                * the first "p" (start of a new place) indicates
                * that place comes at second position and
                * transition at first position
                */
                boolean placeOnFirst = true;
                if(tempWorkSB.indexOf(",") < tempWorkSB.indexOf("p")){
                    placeOnFirst = false;

                }

                // start to analyze/ convert place
                int placeOpen = tempWorkSB.indexOf("(", 1);
                int placeClose = tempWorkSB.indexOf(")");


                StringBuilder placeSB = new StringBuilder(tempWorkSB.substring(placeOpen, placeClose+1));   // ({},{})
                Pair<List<Event>, List<Event>> tempPair = new Pair<>(); // == place
                while(placeSB.indexOf("{") != -1) {

                    int curlyBracketOpen = placeSB.indexOf("{");
                    int curlyBracketClose = placeSB.indexOf("}");
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
                    }else if(!tempPair.secondPositionSet()) {
                        tempPair.setSecondPosition(tempList);
                    }

                    placeSB.delete(curlyBracketOpen, curlyBracketClose+1);
                }

                tempWorkSB.delete(placeOpen, placeClose+1); // removes place => resultSB either (X,p) or (p,X)

                // analyze the rest of the String: either (X,p) or (p,X)
                Event tempEvent = null;
                for(int i= 1; i < tempWorkSB.length(); i++){
                    if(isLetter(tempWorkSB.charAt(i)) && tempWorkSB.charAt(i) != 'p'){
                        // convert to upper case since log events are upper case
                        char tempChar = Character.toUpperCase(tempWorkSB.charAt(i));

                        // check char at next index
                        if(!isLetter(tempWorkSB.charAt(i+1))){
                            tempEvent = new Event(Character.toString(tempChar));
                        }else{
                            String concatenateChars = tempChar + "" + Character.toUpperCase(tempWorkSB.charAt(i+1));
                            tempEvent = new Event(concatenateChars);
                            i+=1;
                        }
                    }
                }

                if(placeOnFirst){
                    placeEventArc = new Arc<>();
                    placeEventArc.setFirst(new Place(tempPair));
                    placeEventArc.setSecond(tempEvent);

                    submittedAlphaArcs.add(placeEventArc);
                }else{
                    eventPlaceArc = new Arc<>();
                    eventPlaceArc.setFirst(tempEvent);
                    eventPlaceArc.setSecond(new Place(tempPair));

                    submittedAlphaArcs.add(eventPlaceArc);
                }

                workSB.delete(bracketOpen, bracketClose+1);
            }

            // ANALYSIS
            for(var arc: submittedAlphaArcs){
                if(!correctAlphaArcs.contains(arc)){
                    surplusAlphaArcs.add(arc);
                }
            }

            for(var arc: correctAlphaArcs){
                if(!submittedAlphaArcs.contains(arc)){
                    missingAlphaArcs.add(arc);
                }
            }

            alphaArcsAnalysis.addMissingAlphaArcs(missingAlphaArcs);
            alphaArcsAnalysis.addSurplusAlphaArcs(surplusAlphaArcs);

            // update Exception and Satisfaction
            if(alphaArcsAnalysis.hasSurplusAlphaArcs() || alphaArcsAnalysis.hasMissingAlphaArcs()){
                alphaArcsAnalysis.setCriterionIsSatisfied(false);
                alphaArcsAnalysis.setAnalysisException(new AnalysisException(
                        "Submitted answer has missing arcs: " + alphaArcsAnalysis.hasMissingAlphaArcs() +
                        " and/ or surplus arcs: " + alphaArcsAnalysis.hasSurplusAlphaArcs()
                ));
            }


        }catch(Exception e){

            alphaArcsAnalysis.setCriterionIsSatisfied(false);
            alphaArcsAnalysis.setAnalysisException(new AnalysisException("Error: " + e));

            loggerMessage = "";
            loggerMessage = loggerMessage.concat("Check that your spelling matches the information provided. ");
            loggerMessage = loggerMessage.concat(INTERNAL_ERROR);
            loggerMessage = loggerMessage.concat(CONTACT_ADMIN);
            // logger content
            System.out.println(loggerMessage + "\nError message: " +e);
        }





        System.out.println(alphaArcsAnalysis.isCriterionSatisfied()+ ", surplus arcs: " + alphaArcsAnalysis.getSurplusAlphaArcs()
                + ", missing arcs: " + alphaArcsAnalysis.getMissingAlphaArcs());
        return alphaArcsAnalysis;
    }




    private static boolean isLetter(char c){
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    // GETTER/ SETTER
    public boolean isPartialSubmissionCorrect() {
        return partialSubmissionCorrect;
    }
    public AnalysisException getException() {
        return exception;
    }
    public HashMap<PmEvaluationCriterion, PmCriterionAnalysis> getCriterionAnalysis() {
        return criterionAnalysis;
    }
    public void addCriterionAnalysis (PmEvaluationCriterion pmEvaluationCriterion, PmCriterionAnalysis pmCriterionAnalysis){
        this.criterionAnalysis.put(pmEvaluationCriterion, pmCriterionAnalysis);
    }
    public boolean isCriterionSatisfied(PmEvaluationCriterion evaluationCriterion){
       return this.criterionAnalysis.get(evaluationCriterion).isCriterionSatisfied();
    }
    public void setPartialSubmissionCorrect(boolean partialSubmissionCorrect) {
        this.partialSubmissionCorrect = partialSubmissionCorrect;
    }
    public String getSubmissionID(){
        return this.submissionID;
    }
    // added 19.11.2022:
    public Iterator<PmCriterionAnalysis> iterPmCriterionAnalysis(){
        return this.criterionAnalysis.values().iterator();
    }


    // GETTER/ SETTER FOR MISSING/ SURPLUS OBJECTS
    public List<Pair<Event, Event>> getMissingPairs(PmEvaluationCriterion criterion){
        PairsAnalysis pairsAnalysis =  (PairsAnalysis) this.criterionAnalysis.get(criterion);
        return pairsAnalysis.getMissingPairs();
    }
    public List<Pair<Event, Event>> getSurplusPairs(PmEvaluationCriterion criterion){
        PairsAnalysis pairsAnalysis = (PairsAnalysis) this.criterionAnalysis.get(criterion);
        return pairsAnalysis.getSurplusPairs();
    }
    public SortedSet<Event> getMissingEvents (PmEvaluationCriterion criterion){
        EventAnalysis eventAnalysis = (EventAnalysis) this.criterionAnalysis.get(criterion);
        return eventAnalysis.getMissingEvents();
    }
    public SortedSet<Event> getSurplusEvents (PmEvaluationCriterion criterion){
        EventAnalysis eventAnalysis = (EventAnalysis) this.criterionAnalysis.get(criterion);
        return eventAnalysis.getSurplusEvents();
    }
    public List<Pair<List<Event>, List<Event>>> getMissingAlphaPairs(PmEvaluationCriterion criterion){
        AlphaPairsAnalysis alphaPairsAnalysis = (AlphaPairsAnalysis) this.criterionAnalysis.get(criterion);
        return alphaPairsAnalysis.getMissingAlphaPairs();
    }
    public List<Pair<List<Event>, List<Event>>> getSurplusAlphaPairs(PmEvaluationCriterion criterion){
        AlphaPairsAnalysis alphaPairsAnalysis = (AlphaPairsAnalysis) this.criterionAnalysis.get(criterion);
        return alphaPairsAnalysis.getSurplusAlphaPairs();
    }
    public List<Arc<?,?>> getMissingAlphaArcs(PmEvaluationCriterion criterion){
        AlphaArcsAnalysis alphaArcsAnalysis = (AlphaArcsAnalysis) this.criterionAnalysis.get(criterion);
        return alphaArcsAnalysis.getMissingAlphaArcs();
    }
    public List<Arc<?,?>> getSurplusAlphaArcs (PmEvaluationCriterion criterion){
        AlphaArcsAnalysis alphaArcsAnalysis = (AlphaArcsAnalysis) this.criterionAnalysis.get(criterion);
        return alphaArcsAnalysis.getSurplusAlphaArcs();
    }






    public static void main(String[] args) {
        Event t1 = new Event("A");
        Event t2 = new Event("B");
        Pair<Event, Event> p1 = new Pair<Event, Event>(t1, t2);

        Event t3 = new Event("B");
        Event t4 = new Event("E");
        Pair<Event, Event> p2 = new Pair<Event, Event>(t3, t4);

        Event t5 = new Event("C");
        Event t6 = new Event("E");
        Pair<Event, Event> p3 = new Pair<Event, Event>(t5, t6);

        Event t7= new Event("D");
        Event t8 = new Event("E");
        Pair<Event, Event> p4 = new Pair<Event, Event>(t7, t8);


        Set<Pair<Event, Event>> correctAnswer1 = new HashSet<>();   //(A,B), (B,E), (C,E), (D,E)
        correctAnswer1.add(p1);
        correctAnswer1.add(p2);
        correctAnswer1.add(p3);
        correctAnswer1.add(p4);

        Set<Event> correctAnswer2 = new TreeSet<>();    //A, B, C, D, E
        correctAnswer2.add(t1); //A
        correctAnswer2.add(t2); //B
        correctAnswer2.add(t5); //C
        correctAnswer2.add(t7); //D
        correctAnswer2.add(t4); //E

        List<Pair<List<Event>, List<Event>>> correctAnswer3 = new ArrayList<>();

        Pair<List<Event>, List<Event>> pa1 = new Pair<>();
        List<Event> l1 = new ArrayList<>();
        l1.add(t1); l1.add(t2); pa1.setFirstPosition(l1);
        List<Event> l2 = new ArrayList<>();
        l2.add(t5); pa1.setSecondPosition(l2);
        correctAnswer3.add(pa1);

        Pair<List<Event>, List<Event>> pa2 = new Pair<>();
        List<Event> l3 = new ArrayList<>();
        l3.add(t5); l3.add(t6); pa2.setFirstPosition(l3);
        List<Event> l4 = new ArrayList<>();
        l4.add(t8); l4.add(t7); pa2.setSecondPosition(l4);
        correctAnswer3.add(pa2);

        Pair<List<Event>, List<Event>> pa3 = new Pair<>();
        List<Event> l5 = new ArrayList<>();
        l5.add(t4); pa3.setFirstPosition(l5);
        List<Event> l6 = new ArrayList<>();
        l6.add(t5); pa3.setSecondPosition(l6);
        correctAnswer3.add(pa3);

        List<Place> correctAnswer4 = new ArrayList<>();

        Pair<List<Event>, List<Event>> ip1 = new Pair<>();
        List<Event> i2 = new ArrayList<>();
        i2.add(t4);
        ip1.setFirstPosition(null);
        ip1.setSecondPosition(i2);
        correctAnswer4.add(new Place(ip1));

        Pair<List<Event>, List<Event>> ip2 = new Pair<>();
        List<Event> i3 = new ArrayList<>();
        i3.add(t4);
        ip2.setFirstPosition(i3);
        ip2.setSecondPosition(null);
        correctAnswer4.add(new Place(ip2));

        correctAnswer4.add(new Place(pa1)); // p({A,B},{C})
        correctAnswer4.add(new Place(pa2)); // p({C,E},{E,D})
        correctAnswer4.add(new Place(pa3)); // p({E},{C})

        //arcs:
        List<Arc<?,?>> correctAnswer5 = new ArrayList<>();
        Arc<String, Event> sourceArc = new Arc<>("i", t1); correctAnswer5.add(sourceArc); //(i,A)
        Arc<Event, String> sinkArc = new Arc<>(t4, "o"); correctAnswer5.add(sinkArc); //(E,o)
        //optional:
        //Arc<String, Event> sourceArc2 = new Arc<>("i", t2); correctAnswer5.add(sourceArc2); //(i,B)
        Arc<Event, Place> arc1 = new Arc<>(t1, new Place(pa1)); correctAnswer5.add(arc1);
        Arc<Event, Place> arc2 = new Arc<>(t2, new Place(pa1)); correctAnswer5.add(arc2);
        Arc<Place, Event> arc3 = new Arc<>(new Place(pa1),t5); correctAnswer5.add(arc3);

        Arc<Event, Place> arc4 = new Arc<>(t5, new Place(pa2)); correctAnswer5.add(arc4);
        Arc<Event, Place> arc5 = new Arc<>(t4, new Place(pa2)); correctAnswer5.add(arc5);
        Arc<Place, Event> arc6 = new Arc<>(new Place(pa2),t8); correctAnswer5.add(arc6);
        Arc<Place, Event> arc7 = new Arc<>(new Place(pa2),t7); correctAnswer5.add(arc7);

        Arc<Event, Place> arc8 = new Arc<>(t4, new Place(pa3)); correctAnswer5.add(arc8);
        Arc<Place, Event> arc9 = new Arc<>(new Place(pa3),t5); correctAnswer5.add(arc9);


        /*
        ############################################################################
        * Test Environment (DEBUGGED):
        * Case1: Empty String:  String emptyString
        * Case2: Wrong answer, syntax wrong: String bullshit/2
        * Case3: Correct Answer, syntax correct: corresponding String submittedAnswer#
        * Case4: Wrong answer, syntax correct: corresponding String submittedAnswer#
        * Case5: Correct answer, syntax wrong: corresponding String submittedAnswer#
                    => wrong syntax can/ should throw exception
        ############################################################################
        */
        PmAnalyzerConfig testConfig = new PmAnalyzerConfig();
        testConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS);
        testConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_PAIRS_ALPHA);
        testConfig.addCriterionToAnalyze(PmEvaluationCriterion.CORRECT_EVENT);

        String emptyString = "";    //case1
        String bullshit = "ksjdfkjsndf, weiuwieFB, WEIWIEF, (KBSDFK, (skdfjb) }";       //case2
        String bullshit2 = "hello {and} (ciao) (4829)";                                 //case2
        String bullshit3 = "((iusdfiuhs){ds}sif{adaosd} zasdj)";                        //case2
        String bullshit4 = "((iusdfiuhs)({ds}sif{)adaosd} zasdj)";                      //case2

        /* Method *analyzePairs * =>  "orI1-3"          */
        /* ############################################# */
        testConfig.setCorrectAnswer("orI1", correctAnswer1); 
        String submittedAnswer1 = "(A,   b)        , (b,E), (  c,e), (d   ,e   )";  //case3
        String submittedAnswer2 = "(A,B),(C,D),(D,E)";                              //case4
        String submittedAnswer3 = "(A,B) (B;E}, (C,e(, (D,E)";                      //case5

        PmPartialSubmissionAnalysis testRun1 = new PmPartialSubmissionAnalysis("orI1");
        /*testRun1.analyzePairs(testConfig, emptyString);         //case1
        testRun1.analyzePairs(testConfig, bullshit2);           //case2
        testRun1.analyzePairs(testConfig, submittedAnswer1);    //case3
        testRun1.analyzePairs(testConfig, submittedAnswer2);    //case4
        testRun1.analyzePairs(testConfig, submittedAnswer3);    //case5*/


        /* Method *analyzeEvents * => "aaI1-3"          */
        /* ############################################# */
        testConfig.setCorrectAnswer("aaI1", correctAnswer2);
        String submittedAnswer4 = "A,B,C,D,E";          //case3
        String submittedAnswer5 = "Z,G,o,h,A";          //case4
        String submittedAnswer6 = "A8; B,e, :c,-D";     //case5

        PmPartialSubmissionAnalysis testRun2 = new PmPartialSubmissionAnalysis("aaI1");
        /*testRun2.analyzeEvents(testConfig, emptyString);        //case1
        testRun2.analyzeEvents(testConfig, bullshit);           //case2
        testRun2.analyzeEvents(testConfig, submittedAnswer4);   //case3
        testRun2.analyzeEvents(testConfig, submittedAnswer5);   //case4
        testRun2.analyzeEvents(testConfig, submittedAnswer6);   //case5*/


        /* Method *analyzeAlphaPairs * => "aaI4-5"        */
        /* ############################################# */
        testConfig.setCorrectAnswer("aaI4", correctAnswer3);
        String submittedAnswer7 = "({A,B},{C}), ({C,E},{E,D}), ({E},{C})";      //case3
        String submittedAnswer8 = "({R,G},{C}), ({C,Z},{E,V}), ({f},{C})";      //case4
        String submittedAnswer9 = "((A,B),(C)), {{C,E},{E,D}), ({E),{C})";      //case5

        PmPartialSubmissionAnalysis testRun3 = new PmPartialSubmissionAnalysis("aaI4");
        /*testRun3.analyzeAlphaPairs(testConfig, emptyString);        //case1
        testRun3.analyzeAlphaPairs(testConfig, bullshit);           //case2
        testRun3.analyzeAlphaPairs(testConfig, submittedAnswer7);   //case3
        testRun3.analyzeAlphaPairs(testConfig, submittedAnswer8);   //case4
        testRun3.analyzeAlphaPairs(testConfig, submittedAnswer9);   //case5*/
        //testRun3.analyze(submittedAnswer9, testConfig);             //case5


        /* Method *analyzeAlphaPairs * => "aaI6"        */
        /* ############################################# */
        testConfig.setCorrectAnswer("aaI6", correctAnswer4);
        String submittedAnswer10 = "i,o, p({A,B},{C}), p({C,E},{E,D}), p({E},{C})";     //case3
        String submittedAnswer11 = "p({A,B},{C}),i,p({C,E},{E,D}),o,p({E},{C})";        //case3
        String submittedAnswer12 = "p({A,B},{C}), p({C,E},{E,D}), p({E},{C})";          //case4
        String submittedAnswer13 = "p({A,B},{C}), p({C,E},{E,D}), p({E},{C}), o,il";    //case5

        PmPartialSubmissionAnalysis testRun4 = new PmPartialSubmissionAnalysis("aaI6");
        /*testRun4.analyzeAlphaPairs(testConfig, emptyString);                //case1
        testRun4.analyzeAlphaPairs(testConfig, bullshit3);                  //case2
        testRun4.analyzeAlphaPairs(testConfig, submittedAnswer10);          //case3
        testRun4.analyzeAlphaPairs(testConfig, submittedAnswer11);          //case3
        testRun4.analyzeAlphaPairs(testConfig, submittedAnswer12);          //case4
        testRun4.analyzeAlphaPairs(testConfig, submittedAnswer13);          //case5*/


        /* Method *analyzeAlphaArcs * => "aaI7"        */
        /* ############################################# */
        testConfig.setCorrectAnswer("aaI7", correctAnswer5);
        String submittedAnswer14 = "(i,A), (E,o), (A, p({A,B},{C})), (B, p({A,B},{C}))," +
                "(p({A,B},{C}), C), (C, p({C,E},{E,D})), (E, p({C,E},{E,D})), (p({C,E},{E,D}), E), (p({C,E},{E,D}), D),  " +
                "(E, p({E},{C})), (p({E},{C}), C)";             // case3
        String submittedAnswer15 = "(i,P), (E,o), (A, p({A,B},{C})), (B, p({A,B},{C}))," +
                "(p({A,B},{C}), C), (C, p({G,H},{E,D})), (E, p({C,E},{E,D})), (p({C,Z},{E,D}), T), (p({C,E},{E,D}), D),  " +
                "(E, p({E},{C})), (p({E},{K}), C)";             // case4
        String submittedAnswer16 = "(A,i), (E,o), (A, p({A,B},{C}), (B, p({A,B},{C}))," +
                "(p({A,B},{C}), C), (C, p({C,E},E,D})), (E, p({C,E},{E,D})), (({C,E},{E,D}), E), (({C,E},{E,D}), D),  " +
                "E, p({E},{C})), (p({E},{C}), C)";               //case5


        PmPartialSubmissionAnalysis testRun5 = new PmPartialSubmissionAnalysis("aaI7");
        /*testRun5.analyzeAlphaArcs(testConfig, emptyString);                 //case1
        testRun5.analyzeAlphaArcs(testConfig, bullshit4);                   //case2
        testRun5.analyzeAlphaArcs(testConfig, submittedAnswer14);           //case3
        testRun5.analyzeAlphaArcs(testConfig, submittedAnswer15);           //case4
        testRun5.analyzeAlphaArcs(testConfig, submittedAnswer16);           //case5*/




        /* Method *analyze* -> using *analyzePairs*      */
        /* ############################################# */
        /*testRun1.analyze(emptyString, testConfig);         //case1
        testRun1.analyze(bullshit2, testConfig);           //case2
        testRun1.analyze(submittedAnswer1, testConfig);    //case3
        testRun1.analyze(submittedAnswer2, testConfig);    //case4
        testRun1.analyze(submittedAnswer3, testConfig);    //case5*/


        /* Method *analyze* -> using *analyzeEvents*      */
        /* ############################################# */
        /*testRun2.analyze(emptyString, testConfig);        //case1
        testRun2.analyze(bullshit, testConfig);           //case2
        testRun2.analyze(submittedAnswer4, testConfig);   //case3
        testRun2.analyze(submittedAnswer5, testConfig);   //case4
        testRun2.analyze(submittedAnswer6, testConfig);   //case5*/


        /* Method *analyze* -> using *analyzeAlphaPairs*   */
        /* ############################################# */
        /*testRun3.analyze(emptyString, testConfig);        //case1
        testRun3.analyze(bullshit, testConfig);           //case2
        testRun3.analyze(submittedAnswer7, testConfig);   //case3
        testRun3.analyze(submittedAnswer8, testConfig);   //case4
        testRun3.analyze(submittedAnswer9, testConfig);   //case5*/


        /* Method *analyze* -> using *analyzeAlphaPairs*   */
        /* ############################################# */
        /*testRun4.analyze(emptyString, testConfig);                //case1
        testRun4.analyze(bullshit3, testConfig);                  //case2
        testRun4.analyze(submittedAnswer10, testConfig);          //case3
        testRun4.analyze(submittedAnswer11, testConfig);          //case3
        testRun4.analyze(submittedAnswer12, testConfig);          //case4
        testRun4.analyze(submittedAnswer13, testConfig);          //case5*/


        /* Method *analyze* -> using *analyzeAlphaArcs*   */
        /* ############################################# */
        /*testRun5.analyze(emptyString, testConfig);                 //case1
        testRun5.analyze(bullshit4, testConfig);                   //case2
        testRun5.analyze(submittedAnswer14, testConfig);           //case3
        testRun5.analyze(submittedAnswer15, testConfig);           //case4
        testRun5.analyze(submittedAnswer16, testConfig);           //case5*/

        // note: Stand 02.10.2022 Lunch
    }
}
