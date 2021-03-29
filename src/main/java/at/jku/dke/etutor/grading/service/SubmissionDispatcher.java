package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Analysis;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Grading;

/**
 * Is used to forward the submission to the corresponding module
 *  and persist the resulting Grading which is identified by the submissionId
 */

public class SubmissionDispatcher implements Runnable{
    /**
     * @param submission: the submission which is sent to ETutorSubmissionController
     * @param submissionId: the id which is generated by the rest-service and needed to request the grading
     */
    private Submission submission;
    private int submissionId;

    public SubmissionDispatcher(Submission submission, int submissionId){
        this.submission=submission;
        this.submissionId=submissionId;
    }
    /**
     * Identifies the module according to submission.taskType
     *  and calls the modules' implementations for evaluating the submission.
     *  To be done: saves the Grading-Object
     */
    @Override
    public void run() {
        try {
            Evaluator evaluator = ModuleManager.evaluatorList.get(submission.getTaskType());
            Analysis analysis = evaluator
                    .analyze(submission.getExerciseId(),
                    submission.getUserId(), submission.getPassedAttributes(), submission.getPassedParameters());
            Grading grading = evaluator.grade(analysis, submission.getMaxPoints(),
                    submission.getPassedAttributes(), submission.getPassedParameters());
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

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
}
