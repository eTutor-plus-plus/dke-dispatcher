package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.core.evaluation.*;
import at.jku.dke.etutor.grading.rest.dto.*;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        this.logger = Logger.getLogger("at.jku.dke.etutor.grading");
        this.repositoryManager=repositoryManager;
        this.moduleManager=moduleManager;
    }

    /**
     * Identifies the module according to submission.taskType
     * and calls the modules' implementations for evaluating the submission.
     * Persists the entities (submission, report, grading)
     */
    @Async("asyncExecutor")
    public void run(Submission submission) {
        logger.info("Saving submission to database");
        repositoryManager.persistSubmission(submission);
        logger.info("Finished saving submission to database");
        try {
            logger.info("Evaluating submission");
            Evaluator evaluator = moduleManager.getEvaluator(submission.getTaskType());
            if (evaluator != null) {
                logger.info("Analyzing submission");
                Analysis analysis = getAnalysis(evaluator, submission);
                logger.info("Finished analyzing submission");
                logger.info("Grading submission");
                Grading grading = getGrading(evaluator, analysis, submission);
                logger.info("Finished grading submission");
                logger.info("Finished evaluating submission");

                GradingDTO gradingDTO = new GradingDTO(submission.getSubmissionId(), grading);
                gradingDTO.setResult(generateHTMLResult( analysis, submission));

                if(grading.getPoints()<grading.getMaxPoints() || grading.getPoints() == 0) {
                    logger.info("Requesting report");
                    DefaultReport report = getReport(evaluator, grading, analysis, submission, Locale.GERMAN);
                    logger.info("Received report");
                    gradingDTO.setReport(new ReportDTO(submission.getSubmissionId(), report));
                }
                persistGrading(gradingDTO);
            }else{
                logger.log(Level.SEVERE, "Could not find evaluator for tasktype: " + submission.getTaskType());
                return;
            }
        } catch(Exception e){
            logger.log(Level.SEVERE, "Stopped Evaluation due to errors");
            e.printStackTrace();
            return;
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
    public Analysis getAnalysis(Evaluator evaluator, Submission submission) throws Exception {
           return evaluator
                    .analyze(submission.getExerciseId(),
                            -1, submission.getPassedAttributes(), submission.getPassedParameters());
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
            logger.info("Saving grading to database");
            repositoryManager.persistGrading(gradingDTO);
            logger.info("Finished saving grading to database");
            return;
        }catch(Exception e){
            logger.log(Level.SEVERE, "Could not save grading");
            return;
        }
    }
    /**
     * Generates a specific Report according to the tasktype
     * @param analysis
     * @param submission
     * @return
     */
    private String generateHTMLResult(Analysis analysis, Submission submission) {
        if (submission.getPassedAttributes().get("action").equalsIgnoreCase("submit"))return null;

        StringBuilder result = new StringBuilder();

        if (analysis instanceof SQLAnalysis) {
            SQLAnalysis sqlAnalysis = (SQLAnalysis)analysis;
            SQLCriterionAnalysis cartesianProduct = sqlAnalysis.getCriterionAnalysis(SQLEvaluationCriterion.CARTESIAN_PRODUCT);
            if(cartesianProduct != null && !cartesianProduct.isCriterionSatisfied()) return null;

            result.append("<strong>The result of your query: </strong><br>");
            Iterator it= sqlAnalysis.getQueryResultColumnLabels().iterator();


            result.append("<table border=1 frame=void rules=rows>");
            result.append("<tr>");
            while(it.hasNext()){
                result.append("<th>" + it.next().toString() + "</th>");
            }
            result.append("</tr>");

            it = sqlAnalysis.getQueryResultTuples().iterator();
            Collection tuple;
            Iterator tupleAttributesIterator;
            while(it.hasNext()){
                result.append("<tr>");
                tuple = (Collection)it.next();
                tupleAttributesIterator = tuple.iterator();
                while(tupleAttributesIterator.hasNext()){
                    Object next = tupleAttributesIterator.next();
                    if(next != null)    result.append("<td>"+next.toString()+"</td>");
                }
                result.append("</tr>");
            }
            result.append("</table>");

            return result.toString();
        }
        return null;
    }
}
