package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.grading.rest.dto.*;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;
import at.jku.dke.etutor.modules.sql.feedback.SQLFeedback;
import at.jku.dke.etutor.modules.sql.report.SQLErrorReport;


import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Is used to forward the submission to the corresponding module
 * and persist the resulting at.jku.dke.etutor.evaluation.Grading, identified by the submissionId
 */

public class SubmissionDispatcher implements Runnable {
    /**
     * @param submission: the submission which is sent to ETutorSubmissionController
     * @param submissionRepository: repository for manipulating table "submission" (jpa)
     * @param gradingDTORepository: repository for manipulating table "grading" (jpa)
     */
    private Submission submission;
    private Logger logger;
    private SubmissionRepository submissionRepository;
    private GradingDTORepository gradingDTORepository;


    public SubmissionDispatcher(Submission submission, SubmissionRepository submissionRepository, GradingDTORepository gradingDTORepository) {
        this.submission = submission;
        this.submissionRepository = submissionRepository;
        this.gradingDTORepository = gradingDTORepository;
        this.logger = Logger.getLogger("at.jku.dke.etutor.grading");

        logger.info("Saving submission to database");
        submissionRepository.save(submission);
        logger.info("Finished saving submission to database");
    }

    /**
     * Identifies the module according to submission.taskType
     * and calls the modules' implementations for evaluating the submission.
     * Persists the resulting grading
     */
    @Override
    public void run() {
        try {
            logger.info("Evaluating submission");
            Evaluator evaluator = ModuleManager.getEvaluator(submission.getTaskType());
            if (evaluator != null) {
                Analysis analysis = evaluator
                        .analyze(submission.getExerciseId(),
                                submission.getUserId(), submission.getPassedAttributes(), submission.getPassedParameters());

                Grading grading = evaluator.grade(analysis, submission.getMaxPoints(),
                        submission.getPassedAttributes(), submission.getPassedParameters());
                logger.info("Finished evaluating submission");

                GradingDTO gradingDTO = new GradingDTO(submission.getSubmissionId(), grading.getPoints(), grading.getMaxPoints());
                logger.info("Saving grading to database");
                gradingDTORepository.save(gradingDTO);
                logger.info("Finished saving grading to database");

                Feedback feedback = evaluator.giveFeedback(analysis, grading,
                        submission.getPassedAttributes(), submission.getPassedParameters(), Locale.GERMAN);
                System.out.println(feedback.getDescription());
                System.out.println(feedback.getError());
                System.out.println(feedback.getHint());

            }else{
                logger.log(Level.SEVERE, "Could not find evaluator for tasktype: " + submission.getTaskType());
            }
        } catch(Exception e){
            logger.log(Level.SEVERE, "Stopped Evaluation due to errors");
            e.printStackTrace();
        }
    }



    private void processAnalysis(Evaluator evaluator, Grading grading, Analysis analysis, Submission submission) {
        if (analysis instanceof SQLAnalysis) {
            Feedback feedback = evaluator.giveFeedback(analysis, grading,
                    submission.getPassedAttributes(), submission.getPassedParameters(), Locale.GERMAN);
            System.out.println(feedback.getDescription());
            System.out.println(feedback.getError());
            System.out.println(feedback.getHint());

            SQLFeedback sqlFeedback = (SQLFeedback) feedback;
        }
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }


}
