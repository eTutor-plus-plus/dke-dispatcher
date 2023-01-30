package at.jku.dke.etutor.modules.pm.grading;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.pm.analysis.PmPartialSubmissionAnalysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PmGrader extends DefaultGrading implements Grading {

    // OBJECT FIELD
    // points (SumTotalAchieved)
    // maxPoints (reachable)
    private Map<String, PmPartialSubmissionGrading> partialSubmissionGrading;


    // CONSTRUCTOR
    public PmGrader(){
        super();

        this.partialSubmissionGrading = new HashMap<>();
    }

    // METHODS
    public void sumUpTotalPoints(){
        int overallPoints = 0;
        PmPartialSubmissionGrading submissionGrading;

        Iterator<PmPartialSubmissionGrading> submissionGradingIterator = gradingIterator();
        while(submissionGradingIterator.hasNext()){
            submissionGrading = submissionGradingIterator.next();

            overallPoints += submissionGrading.getTotalAchievedPoints();
        }

        // set overall achieved points in DefaultGrading:
        setPoints(overallPoints);
    }

    public void addPmPartialSubmissionGrading(String subID, PmPartialSubmissionGrading pmPartialSubmissionGrading){
        this.partialSubmissionGrading.put(subID, pmPartialSubmissionGrading);
    }
    public Iterator<PmPartialSubmissionGrading> gradingIterator(){
        return this.partialSubmissionGrading.values().iterator();
    }
    public Map<String, PmPartialSubmissionGrading> getPartialSubmissionGrading() {
        return partialSubmissionGrading;
    }
}
