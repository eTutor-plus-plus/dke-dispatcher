package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultReport;
import at.jku.dke.etutor.core.evaluation.Evaluator;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.rest.model.entities.Report;
import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import at.jku.dke.etutor.modules.dlg.analysis.DatalogAnalysis;
import at.jku.dke.etutor.modules.xquery.analysis.XQAnalysis;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

import static at.jku.dke.etutor.grading.rest.ETutorGradingController.waitingThreadMap;
import static at.jku.dke.etutor.grading.rest.ETutorSubmissionController.runningEvaluations;


/**
 * Is used to forward the submission to the corresponding module
 */
@Service
public class SubmissionDispatcherService {
    private final Logger logger;
    private final RepositoryService repositoryService;
    private final ModuleEvaluatorFactory moduleEvaluatorFactory;

    public SubmissionDispatcherService(RepositoryService repositoryService, ModuleEvaluatorFactory moduleEvaluatorFactory) {
        this.logger = (Logger) LoggerFactory.getLogger(SubmissionDispatcherService.class);
        this.repositoryService = repositoryService;
        this.moduleEvaluatorFactory = moduleEvaluatorFactory;
    }

    /**
     * Identifies the module according to submission.taskType
     * and calls the modules' implementations for evaluating the submission.
     * Persists the entities (submission, report, grading)
     */
    @Async("taskExecutor")
    public void run(Submission submission, Locale locale, Boolean persist) {
        try {
            if(Boolean.TRUE.equals(persist)){
                logger.debug("Saving submission to database");
                repositoryService.persistSubmission(submission);
                logger.debug("Finished saving submission to database");
                logger.debug("Evaluating submission");
            }
            Evaluator evaluator = moduleEvaluatorFactory.forTaskType(submission.getTaskType());
            if (evaluator == null) {
                logger.warn("Could not find evaluator for tasktype: {}", submission.getTaskType());
                throw new NoSuchElementException("Could not find evaluator for tasktype:" + submission.getTaskType());
            }
            logger.debug("Analyzing submission");
            Analysis analysis = getAnalysis(evaluator, submission, locale);
            logger.debug("Finished analyzing submission");
            logger.debug("Grading submission");
            at.jku.dke.etutor.core.evaluation.Grading grading = getGrading(evaluator, analysis, submission);
            logger.debug("Finished grading submission");
            logger.debug("Finished evaluating submission");

            Grading gradingEntity = new Grading(submission.getSubmissionId(), grading);
            gradingEntity.setResult(evaluator.generateHTMLResult( analysis, submission.getPassedAttributes(), locale));
            gradingEntity.setSubmissionSuitsSolution(analysis.submissionSuitsSolution());
            if((gradingEntity.getPoints()<gradingEntity.getMaxPoints() || gradingEntity.getPoints() == 0 ) && !(analysis instanceof XQAnalysis) && !(analysis instanceof DatalogAnalysis)) { // For XQ and DLG, the report is included in the result by default
                    logger.info("Requesting report");
                    DefaultReport report = getReport(evaluator, grading, analysis, submission, locale);
                    logger.debug("Received report");
                    gradingEntity.setReport(new Report(submission.getSubmissionId(), report));
            }
            persistGrading(gradingEntity);
        } catch(Exception e){
            logger.warn("Stopped Evaluation due to errors", e);
            logger.info("Persisting default grading for this submission.");
            String errorNotification = "An unhandled exception occurred during evaluation of your submission. ";
            errorNotification += "The exception message is: " + e.getMessage();
            Grading gradingEntity = new Grading();
            gradingEntity.setSubmissionId(submission.getSubmissionId());
            gradingEntity.setResult(errorNotification);
            gradingEntity.setSubmissionSuitsSolution(false);
            gradingEntity.setMaxPoints(0);
            gradingEntity.setPoints(0);
            Report report = new Report();
            report.setHint("Contact an administrator if the error message does not allow you to resolve the issue.");
            gradingEntity.setReport(report);
            persistGrading(gradingEntity);
        }finally {
            // interrupt thread waiting for this evaluation to finish
            runningEvaluations.remove(submission.getSubmissionId());
            Thread waitingThread = waitingThreadMap.get(submission.getSubmissionId());
            if(waitingThread != null)
                waitingThread.interrupt();
        }
    }

    /**
     * Calls the grade() method of the provided evaluator
     * @param evaluator the evaluator
     * @param analysis the analysis
     * @param submission the submission
     * @return the Grading
     * @throws Exception if an error occurs
     */
    public at.jku.dke.etutor.core.evaluation.Grading getGrading(Evaluator evaluator, Analysis analysis, Submission submission) throws Exception {
        return  evaluator
                .grade(analysis, submission.getMaxPoints(),
                        submission.getPassedAttributes(), submission.getPassedParameters());
    }

    /**
     * Calls the analyze() method of the provided Evaluator
     * @param evaluator the evaluator
     * @param submission the submission
     * @return the Analysis
     * @throws Exception if an error occurs
     */
    public Analysis getAnalysis(Evaluator evaluator, Submission submission, Locale locale) throws Exception {
           return evaluator
                    .analyze(submission.getExerciseId(),
                            -1, submission.getPassedAttributes(), submission.getPassedParameters(), locale);
    }

    /**
     * Calls the report() method of the provided evaluator
     * @param evaluator the evaluator
     * @param grading the grading
     * @param analysis the analysis
     * @param submission the submission
     * @param locale the locale
     * @return the report
     * @throws Exception if an error occurs
     */
    public DefaultReport getReport(Evaluator evaluator, at.jku.dke.etutor.core.evaluation.Grading grading, Analysis analysis, Submission submission, Locale locale) throws Exception {
        return (DefaultReport) evaluator.report
                (analysis, grading, submission.getPassedAttributes(),
                        submission.getPassedParameters(), locale);
    }

    /**
     * Persists the grading
     * @param grading the grading
     */
    public void persistGrading(Grading grading){
        try{
            logger.debug("Saving grading to database");
            repositoryService.persistGrading(grading);
            logger.debug("Finished saving grading to database");
        }catch(Exception e){
            logger.error("Could not save grading");
        }
    }
}
