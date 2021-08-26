package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.grading.rest.dto.*;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;


/**
 * Is used to forward the submission to the corresponding module
 * and persist the entities
 */
@Service
public class SubmissionDispatcher  {
    private final Logger logger;
    private final RepositoryManager repositoryManager;
    private final ModuleManager moduleManager;

    public SubmissionDispatcher(RepositoryManager repositoryManager, ModuleManager moduleManager) {
        this.logger = (Logger) LoggerFactory.getLogger(SubmissionDispatcher.class);
        this.repositoryManager=repositoryManager;
        this.moduleManager=moduleManager;
    }

    /**
     * Identifies the module according to submission.taskType
     * and calls the modules' implementations for evaluating the submission.
     * Persists the entities (submission, report, grading)
     */
    @Async("asyncExecutor")
    public void run(Submission submission, Locale locale) {
        logger.debug("Saving submission to database");
        repositoryManager.persistSubmission(submission);
        logger.debug("Finished saving submission to database");
        try {
            logger.debug("Evaluating submission");
            Evaluator evaluator = moduleManager.getEvaluator(submission.getTaskType());
            if (evaluator == null) {
                logger.warn("Could not find evaluator for tasktype: {}", submission.getTaskType());
                return;
            }
            logger.debug("Analyzing submission");
            Analysis analysis = getAnalysis(evaluator, submission, locale);
            logger.debug("Finished analyzing submission");
            logger.debug("Grading submission");
            Grading grading = getGrading(evaluator, analysis, submission);
            logger.debug("Finished grading submission");
            logger.debug("Finished evaluating submission");

            GradingDTO gradingDTO = new GradingDTO(submission.getSubmissionId(), grading);
            gradingDTO.setResult(evaluator.generateHTMLResult( analysis, submission.getPassedAttributes(), locale));

            if(grading.getPoints()<grading.getMaxPoints() || grading.getPoints() == 0) {
                    logger.info("Requesting report");
                    DefaultReport report = getReport(evaluator, grading, analysis, submission, locale);
                    logger.debug("Received report");
                    gradingDTO.setReport(new ReportDTO(submission.getSubmissionId(), report));
            }
            persistGrading(gradingDTO);
        } catch(Exception e){
            logger.warn("Stopped Evaluation due to errors", e);
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
    public Grading getGrading(Evaluator evaluator, Analysis analysis, Submission submission) throws Exception {
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
    public DefaultReport getReport(Evaluator evaluator, Grading grading, Analysis analysis,Submission submission, Locale locale) throws Exception {
        return (DefaultReport) evaluator.report
                (analysis, grading, submission.getPassedAttributes(),
                        submission.getPassedParameters(), locale);
    }

    /**
     * Persists the grading
     * @param gradingDTO the grading
     */
    public void persistGrading(GradingDTO gradingDTO){
        try{
            logger.debug("Saving grading to database");
            repositoryManager.persistGrading(gradingDTO);
            logger.debug("Finished saving grading to database");
        }catch(Exception e){
            logger.error("Could not save grading");
        }
    }
}
