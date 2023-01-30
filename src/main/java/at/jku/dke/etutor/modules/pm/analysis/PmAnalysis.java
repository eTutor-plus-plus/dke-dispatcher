package at.jku.dke.etutor.modules.pm.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.pm.PmEvaluationCriterion;

import java.util.*;

/**
 * ...
 * @author Falk Görner
 * @version 1.0
 */
public class PmAnalysis extends DefaultAnalysis implements Analysis {

    // OBJECT FIELDS
    // note: exception here not necessary anymore?
    private AnalysisException exception;
    // maps ID of submitted answer to detailed analysis of respective submission
    private final HashMap<String, PmPartialSubmissionAnalysis> partialSubmissionAnalysis;



    // CONSTRUCTOR
    public PmAnalysis(){
        super();    // submission, submissionSuitsSolution

        this.exception = null;
        this.partialSubmissionAnalysis = new HashMap<>();


    }

    // METHODS
    public void addPartialSubmissionAnalysis(String subID, PmPartialSubmissionAnalysis analysis){
        this.partialSubmissionAnalysis.put(subID, analysis);
    }
    public Iterator<PmPartialSubmissionAnalysis> iterSubmissionAnalysis (){
        return this.partialSubmissionAnalysis.values().iterator();  //returns iterator over the elements in this collection
    }
    // return complete analysis of partial submission
    public void removePartialSubmissionAnalysis(String subID){
        this.partialSubmissionAnalysis.remove(subID);
    }
    public PmPartialSubmissionAnalysis getPartialSubmissionAnalysis(String subID){
        return this.partialSubmissionAnalysis.get(subID);
    }
    public boolean isPartialSubmissionAnalyzed(String subID){
        return this.partialSubmissionAnalysis.containsKey(subID);
    }
    public void setException(AnalysisException exception) {
        this.exception = exception;
    }
    public AnalysisException getPartialSubmissionException(String subID){
        return partialSubmissionAnalysis.get(subID).getException();
    }
}
