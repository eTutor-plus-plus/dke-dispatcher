package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Analysis;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Evaluator;
import at.jku.dke.etutor.grading.rest.dto.evaluation.Grading;


public class SubmissionDispatcher implements Runnable{
    private Submission submission;
    private int submissionId;

    public SubmissionDispatcher(Submission submission, int submissionId){
        this.submission=submission;
        this.submissionId=submissionId;
    }

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
