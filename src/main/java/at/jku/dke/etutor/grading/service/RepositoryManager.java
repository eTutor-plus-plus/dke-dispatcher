package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.ReportDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.ReportDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *Manages the repositories for the entities
 */
@Service
public class RepositoryManager {
    private  GradingDTORepository gradingRepository;
    private  SubmissionRepository submissionRepository;
    private  ReportDTORepository reportRepository;

    public RepositoryManager(GradingDTORepository gradingRepo,
                             SubmissionRepository submissionRepo, ReportDTORepository reportRepo){
        gradingRepository = gradingRepo;
        submissionRepository = submissionRepo;
        reportRepository = reportRepo;
    }

    public  void persistSubmission(Submission submission){
        submissionRepository.save(submission);
    }

    public  void persistGrading(GradingDTO grading){
        gradingRepository.save(grading);
    }

    public  void persistReport(ReportDTO report){
        reportRepository.save(report);
    }

    public  GradingDTORepository getGradingRepository() {
        return gradingRepository;
    }

    public  void setGradingRepository(GradingDTORepository gradingRepository) {
        this.gradingRepository = gradingRepository;
    }

    public  SubmissionRepository getSubmissionRepository() {
        return submissionRepository;
    }

    public  void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public  ReportDTORepository getReportRepository() {
        return reportRepository;
    }

    public  void setReportRepository(ReportDTORepository reportRepository) {
        this.reportRepository = reportRepository;
    }
}
