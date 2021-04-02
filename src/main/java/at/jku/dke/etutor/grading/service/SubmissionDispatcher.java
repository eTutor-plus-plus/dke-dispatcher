package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;


import java.util.Locale;


/**
 * Is used to forward the submission to the corresponding module
 *  and persist the resulting at.jku.dke.etutor.evaluation.Grading which is identified by the submissionId
 */

public class SubmissionDispatcher implements Runnable{
    /**
     * @param submission: the submission which is sent to ETutorSubmissionController
     * @param submissionId: the id which is generated by the rest-service and needed to request the grading
     */
    private Submission submission;
    private SubmissionId submissionId;


    public SubmissionDispatcher(Submission submission, SubmissionId submissionId){
        this.submission=submission;
        this.submissionId=submissionId;
    }
    /**
     * Identifies the module according to submission.taskType
     *  and calls the modules' implementations for evaluating the submission.
     *  To be done: saves the at.jku.dke.etutor.evaluation.Grading-Object
     */
    @Override
    public void run() {
        try {
            Evaluator evaluator = ModuleManager.getEvaluator(submission.getTaskType());
            Analysis analysis = evaluator
                    .analyze(submission.getExerciseId(),
                    submission.getUserId(), submission.getPassedAttributes(), submission.getPassedParameters());
            System.out.println(analysis.submissionSuitsSolution());
            Grading grading = evaluator.grade(analysis, submission.getMaxPoints(),
                    submission.getPassedAttributes(), submission.getPassedParameters());
            GradingManager.gradingMap.put(submissionId.getId(), new RestGrading(grading.getPoints(), grading.getMaxPoints()));

            /*
            Report report = evaluator.report(analysis, grading, submission.getPassedAttributes(),
                submission.getPassedParameters(),
                    new Locale.Builder().setLanguage("en").setRegion("US").build());
                     System.out.println(report.toString());
             */


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public SubmissionId getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(SubmissionId submissionId) {
        this.submissionId = submissionId;
    }
}
