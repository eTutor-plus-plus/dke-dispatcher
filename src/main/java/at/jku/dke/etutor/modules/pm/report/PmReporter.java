package at.jku.dke.etutor.modules.pm.report;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.pm.analysis.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/**
 * Creates the report to a PM Task
 * @author Falk Görner
 * @version 1.0
 */
@Service
public class PmReporter {

    public PmReporter(){
        super();
    }

    public PmReport createReport(PmAnalysis pmAnalysis, DefaultGrading grading, PmReporterConfig pmReporterConfig, Locale locale){
        final String LS = "<br>";
        PmReport pmReport;
        PmErrorReport errorReport;

        PmPartialSubmissionAnalysis partialSubmissionAnalysis;
        PmCriterionAnalysis pmCriterionAnalysis;
        PairsAnalysis pairsAnalysis;
        EventAnalysis eventAnalysis;
        AlphaPairsAnalysis alphaPairsAnalysis;
        AlphaArcsAnalysis arcsAnalysis;

        Iterator<PmPartialSubmissionAnalysis> partialSubmissionIterator;
        Iterator<PmCriterionAnalysis> criterionAnalysisIterator;

        StringBuilder hint;
        StringBuilder error;
        StringBuilder description;

        String inputFieldName;

        pmReport = new PmReport();

        // Create Error Report:
        // check each partial submission
        // for every partial submission:
        // check each used criterion
        partialSubmissionIterator = pmAnalysis.iterSubmissionAnalysis();
        while(partialSubmissionIterator.hasNext()){
            partialSubmissionAnalysis = partialSubmissionIterator.next();

            // if partial submission is correct, no report needed
            // else:
            if(!partialSubmissionAnalysis.isPartialSubmissionCorrect()){
                hint = new StringBuilder();
                error = new StringBuilder();
                description = new StringBuilder();
                errorReport = new PmErrorReport();
                inputFieldName = matchSubmissionIdToUIName(partialSubmissionAnalysis.getSubmissionID());

                // iterate the PmCriterionAnalysis for each Pm partial submission
                // especially useful for the case if a submission is analysed by multiple criteria
                // in version 1.0 the analysis is only based on one criterion
                criterionAnalysisIterator = partialSubmissionAnalysis.iterPmCriterionAnalysis();
                while(criterionAnalysisIterator.hasNext()){
                    pmCriterionAnalysis = criterionAnalysisIterator.next();

                    // this if clause is especially important, if partial submission is analysed by multiple criterion
                    // if partial submission is only analysed by one criterion as in version 1.0
                    // this is always true, since !partialSubmissionAnalysis.isPartialSubmissionCorrect() is true
                    if(!pmCriterionAnalysis.isCriterionSatisfied()){

                        if((pmCriterionAnalysis instanceof PairsAnalysis) && (pmReporterConfig.getDiagnoseLevel() > 0)){
                            pairsAnalysis = (PairsAnalysis) pmCriterionAnalysis;
                            if(isGermanLocale(locale)){
                                error.append("<strong> Die Paare des Inputs ").append(inputFieldName).append("sind nicht richtig. </strong>").append(LS);
                            }else{
                                error.append("<strong> The pairs of input ").append(inputFieldName).append("are not correct. </strong>").append(LS);
                            }

                            int missingPairsCount = pairsAnalysis.getMissingPairs().size();
                            int surplusPairsCount = pairsAnalysis.getSurplusPairs().size();

                            // react to different diagnose levels [1, 2, 3]:
                            // 1 == diagnose level little
                            if(pmReporterConfig.getDiagnoseLevel() == 1){
                                if(pairsAnalysis.hasMissingPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append("fehlen Paare.").append(LS);
                                    }else{
                                        description.append("Pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((pairsAnalysis.hasSurplusPairs()) && pairsAnalysis.hasMissingPairs()){
                                    description.append(LS).append(LS);
                                }

                                if(pairsAnalysis.hasSurplusPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append("sind zu viele Paare.").append(LS);
                                    }else{
                                        description.append("Pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }


                            // 2 == diagnose level some
                            if(pmReporterConfig.getDiagnoseLevel() == 2){
                                if(pairsAnalysis.hasMissingPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es fehlen ").append(missingPairsCount).append(" Paare bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(missingPairsCount).append(" Pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((pairsAnalysis.hasMissingPairs()) && (pairsAnalysis.hasSurplusPairs())){
                                    description.append(LS).append(LS);
                                }

                                if(pairsAnalysis.hasSurplusPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es sind ").append(surplusPairsCount).append(" Paare zu viel bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(surplusPairsCount).append(" Pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 3 == diagnose level much
                            if(pmReporterConfig.getDiagnoseLevel() == 3){
                                if(pairsAnalysis.hasMissingPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(missingPairsCount).append(" Paare für den Input ").append(inputFieldName).append(" fehlen: ").append(LS);
                                    }else{
                                        description.append("The following ").append(missingPairsCount).append(" pairs are missing in input ").append(inputFieldName).append(LS);
                                    }

                                    // really necessary?
                                    // pmReport.addMissingPairs();

                                    description.append(pairsAnalysis.getMissingPairs().toString());
                                }

                                if((pairsAnalysis.hasMissingPairs()) && (pairsAnalysis.hasSurplusPairs())){
                                    description.append(LS).append(LS);
                                }

                                if(pairsAnalysis.hasSurplusPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(surplusPairsCount).append(" Paare für den Input ").append(inputFieldName).append(" sind zu viel: ").append(LS);
                                    }else{
                                        description.append("The following ").append(surplusPairsCount).append(" pairs are surplus in input ").append(inputFieldName).append(LS);
                                    }

                                    //report.addSurplusPairs();?
                                    description.append(pairsAnalysis.getSurplusPairs().toString());
                                }

                            }
                        }

                        if((pmCriterionAnalysis instanceof EventAnalysis) && (pmReporterConfig.getDiagnoseLevel() > 0)){
                            eventAnalysis = (EventAnalysis) pmCriterionAnalysis;

                            if(isGermanLocale(locale)){
                                error.append("<strong> Die Transitions des Inputs ").append(inputFieldName).append(" sind nicht richtig </strong>").append(LS);
                            }else{
                                error.append("<strong> The transitions of the input" ).append(inputFieldName).append(" are not correct. </strong>").append(LS);
                            }

                            int missingEventsCount = eventAnalysis.getMissingEvents().size();
                            int surplusEventsCount = eventAnalysis.getSurplusEvents().size();

                            // 1 == diagnose level little
                            if(pmReporterConfig.getDiagnoseLevel() == 1){
                                if(eventAnalysis.hasMissingEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es fehlen Transitions bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append("There are missing transitions for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((eventAnalysis.hasMissingEvents()) && eventAnalysis.hasSurplusEvents()){
                                    description.append(LS).append(LS);
                                }

                                if(eventAnalysis.hasSurplusEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es sind zu viele Transitions bei Input: ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append("There are surplus transitions for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 2 == diagnose level some
                            if(pmReporterConfig.getDiagnoseLevel() == 2){
                                if(eventAnalysis.hasMissingEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es fehlen ").append(missingEventsCount).append(" Transitions bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append("There are ").append(missingEventsCount).append(" missing transitions for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((eventAnalysis.hasMissingEvents()) && (eventAnalysis.hasSurplusEvents())){
                                    description.append(LS).append(LS);
                                }

                                if(eventAnalysis.hasSurplusEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es sind ").append(surplusEventsCount).append(" Transitions zu viel bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append("There are ").append(surplusEventsCount).append(" transitions to much for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 3 == diagnose level much
                            if(pmReporterConfig.getDiagnoseLevel() == 3){
                                if(eventAnalysis.hasMissingEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden Transitions fehlen bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append("The following transitions are missing for input ").append(inputFieldName).append(LS);
                                    }

                                    description.append("<strong> ").append(eventAnalysis.getMissingEvents().toString()).append(" </strong>").append(LS);

                                }

                                if((eventAnalysis.hasMissingEvents()) && (eventAnalysis.hasSurplusEvents())){
                                    description.append(LS).append(LS);
                                }

                                if(eventAnalysis.hasSurplusEvents()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden Transitions bei Input ").append(inputFieldName).append(" sind zu viel: ").append(LS);
                                    }else{
                                        description.append("The following transitions are too much for input ").append(inputFieldName).append(LS);
                                    }

                                    description.append("<strong> ").append(eventAnalysis.getSurplusEvents().toString()).append("</strong>").append(LS);
                                }
                            }
                        }

                        if((pmCriterionAnalysis instanceof AlphaPairsAnalysis) && pmReporterConfig.getDiagnoseLevel() > 0){
                            alphaPairsAnalysis = (AlphaPairsAnalysis) pmCriterionAnalysis;

                            if(isGermanLocale(locale)){
                                error.append("<strong> Die Paare des Inputs ").append(inputFieldName).append(" sind nicht richtig </strong>").append(LS);
                            }else{
                                error.append("<strong> The pairs for input ").append(inputFieldName).append(" are not correct </strong>.").append(LS);
                            }

                            int missingAlphaPairsCount = alphaPairsAnalysis.getMissingAlphaPairs().size();
                            int surplusAlphaPairsCount = alphaPairsAnalysis.getSurplusAlphaPairs().size();

                            // 1 == diagnose level little
                            if(pmReporterConfig.getDiagnoseLevel() == 1){
                                if(alphaPairsAnalysis.hasMissingAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append(" fehlen Paare.").append(LS);
                                    }else{
                                        description.append("Pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((alphaPairsAnalysis.hasMissingAlphaPairs()) && (alphaPairsAnalysis.hasSurplusAlphaPairs())){
                                    description.append(LS).append(LS);
                                }

                                if(alphaPairsAnalysis.hasSurplusAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append(" sind zu viele Paare.");
                                    }else{
                                        description.append("Pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 2 == diagnose level some
                            if(pmReporterConfig.getDiagnoseLevel() == 2){
                                if(alphaPairsAnalysis.hasMissingAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es fehlen ").append(missingAlphaPairsCount).append(" Paare bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(missingAlphaPairsCount).append(" Pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((alphaPairsAnalysis.hasMissingAlphaPairs()) && (alphaPairsAnalysis.hasSurplusAlphaPairs())){
                                    description.append(LS).append(LS);
                                }

                                if(alphaPairsAnalysis.hasSurplusAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es sind ").append(surplusAlphaPairsCount).append(" Paare zu viel bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(surplusAlphaPairsCount).append(" Pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 3 == diagnose level much
                            if(pmReporterConfig.getDiagnoseLevel() == 3){
                                if(alphaPairsAnalysis.hasMissingAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(missingAlphaPairsCount).append(" Paare für den Input ").append(inputFieldName).append(" fehlen: ").append(LS);
                                    }else{
                                        description.append("The following ").append(missingAlphaPairsCount).append(" pairs are missing in input ").append(inputFieldName).append(LS);
                                    }

                                    description.append(alphaPairsAnalysis.getMissingAlphaPairs().toString()).append(LS);
                                }

                                if((alphaPairsAnalysis.hasMissingAlphaPairs()) && (alphaPairsAnalysis.hasSurplusAlphaPairs())){
                                    description.append(LS).append(LS);
                                }

                                if(alphaPairsAnalysis.hasSurplusAlphaPairs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(surplusAlphaPairsCount).append(" Paare für den Input ").append(inputFieldName).append(" sind zu viel: ").append(LS);
                                    }else{
                                        description.append("The following ").append(surplusAlphaPairsCount).append(" pairs are surplus in input ").append(inputFieldName).append(LS);
                                    }

                                    description.append(alphaPairsAnalysis.getSurplusAlphaPairs().toString()).append(LS);
                                }
                            }
                        }

                        if((pmCriterionAnalysis instanceof AlphaArcsAnalysis) && pmReporterConfig.getDiagnoseLevel() > 0){
                            arcsAnalysis = (AlphaArcsAnalysis) pmCriterionAnalysis;

                            if(isGermanLocale(locale)){
                                error.append("<strong> Die Arc-Paare des Inputs ").append(inputFieldName).append(" sind nicht richtig. </strong>");
                            }else{
                                error.append("<strong> The arc- pairs of the input ").append(inputFieldName).append(" are not correct. </strong>");
                            }

                            int missingArcsCount = arcsAnalysis.getMissingAlphaArcs().size();
                            int surplusArcsCount = arcsAnalysis.getSurplusAlphaArcs().size();

                            // 1 == diagnose level little
                            if(pmReporterConfig.getDiagnoseLevel() == 1){
                                if(arcsAnalysis.hasMissingAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append(" fehlen Arc-Paare.").append(LS);
                                    }else{
                                        description.append("Arc-pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((arcsAnalysis.hasMissingAlphaArcs()) && (arcsAnalysis.hasSurplusAlphaArcs())){
                                    description.append(LS).append(LS);
                                }

                                if(arcsAnalysis.hasSurplusAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Bei Input ").append(inputFieldName).append(" sind zu viele Arc-Paare.").append(LS);
                                    }else{
                                        description.append("Arc-pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 2 == diagnose level some
                            if(pmReporterConfig.getDiagnoseLevel() == 2){
                                if(arcsAnalysis.hasMissingAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es fehlen ").append(missingArcsCount).append(" Arc-Paare bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(missingArcsCount).append(" Arc-pairs are missing for input ").append(inputFieldName).append(LS);
                                    }
                                }

                                if((arcsAnalysis.hasMissingAlphaArcs()) && (arcsAnalysis.hasSurplusAlphaArcs())){
                                    description.append(LS).append(LS);
                                }

                                if(arcsAnalysis.hasSurplusAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Es sind ").append(surplusArcsCount).append(" Arc-Paare zu viel bei Input ").append(inputFieldName).append(LS);
                                    }else{
                                        description.append(surplusArcsCount).append(" Arc-pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }
                                }
                            }

                            // 3 == diagnose level much
                            if(pmReporterConfig.getDiagnoseLevel() == 3){
                                if(arcsAnalysis.hasMissingAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(missingArcsCount).append(" Arc-Paare für den Input ").append(inputFieldName).append(" fehlen: ").append(LS);
                                    }else{
                                        description.append("The following ").append(missingArcsCount).append(" arc-pairs are missing for input ").append(inputFieldName).append(": ").append(LS);
                                    }

                                    description.append(arcsAnalysis.getMissingAlphaArcs().toString());
                                }

                                if((arcsAnalysis.hasMissingAlphaArcs()) && (arcsAnalysis.hasSurplusAlphaArcs())){
                                    description.append(LS).append(LS);
                                }

                                if(arcsAnalysis.hasSurplusAlphaArcs()){
                                    if(isGermanLocale(locale)){
                                        description.append("Die folgenden ").append(surplusArcsCount).append(" Arc-Paare für den Input ").append(inputFieldName).append(" sind zu viel.").append(LS);
                                    }else{
                                        description.append("The following ").append(surplusArcsCount).append(" arc-pairs are surplus for input ").append(inputFieldName).append(LS);
                                    }

                                    description.append(arcsAnalysis.getSurplusAlphaArcs().toString());
                                }
                            }
                        }
                    }
                }

                // each partial submission has its own error report
                if((hint.toString().length() != 0) || (error.toString().length() != 0) || (description.toString().length() !=0)){
                    errorReport.setHint(hint.toString());
                    errorReport.setError(error.toString());
                    errorReport.setDescription(description.toString());

                    // DESCRIPTION: combine the single strings to a total string
                    String tempDescription = pmReport.getDescription();
                    tempDescription = tempDescription + description.toString() + LS;
                    pmReport.setDescription(tempDescription);

                    // ERROR: combine the single strings to a total string
                    String tempError = pmReport.getError();
                    tempError = tempError + error.toString() + LS;
                    pmReport.setError(tempError);

                    pmReport.addErrorReport(errorReport);
                }
            }
        }

        // configuring report printer
        if(pmReporterConfig.getDiagnoseLevel() >= 0){
            pmReport.setShowErrorReports(true);
        }
        if(pmReporterConfig.getDiagnoseLevel() >= 1){
            pmReport.setShowErrorDescriptions(true);
        }
        if(pmReporterConfig.getDiagnoseLevel() >= 2){
            pmReport.setShowHints(true);
        }
        // note: status as of 20.11.2022 Night
        return pmReport;
    }

    /**
     * Help Method to display the right name of the incorrect submitted field
     * and not the internal submission id
     * @param submissionId the internal submission id
     * @return the corresponding name of the input field as stated in the UI
     */
    private String matchSubmissionIdToUIName(String submissionId){
        switch(submissionId){
            case "orI1":
                return "Ordering Relation 1 ";
            case "orI2":
                return "Ordering Relation 2 ";
            case "orI3":
                return "Ordering Relation 3 ";
            case "aaI1":
                return "Tw ";
            case "aaI2":
                return "Ti ";
            case "aaI3":
                return "To ";
            case "aaI4":
                return "Xw ";
            case "aaI5":
                return "Yw ";
            case "aaI6":
                return "Pw ";
            case "aaI7":
                return "Fw ";
            default:
                return " ";
        }
    }

    /**
     * Returns whether locale equals Locale.GERMAN
     * @param locale the locale
     * @return a boolean
     */
    private boolean isGermanLocale(Locale locale){
        return locale ==Locale.GERMAN;
    }


}
