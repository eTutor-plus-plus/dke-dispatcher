package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.ReportDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import org.springframework.stereotype.Component;

/**
 *Manages the repositories for the entities
 */
@Component
public class RepositoryManager {
    private static GradingDTORepository gradingRepository;
    private static SubmissionRepository submissionRepository;
    private static ReportDTORepository reportRepository;

    public RepositoryManager(GradingDTORepository gradingRepo,
                             SubmissionRepository submissionRepo, ReportDTORepository reportRepo){
        gradingRepository = gradingRepo;
        submissionRepository = submissionRepo;
        reportRepository = reportRepo;
    }

    public static GradingDTORepository getGradingRepository() {
        return gradingRepository;
    }

    public static void setGradingRepository(GradingDTORepository gradingRepository) {
        RepositoryManager.gradingRepository = gradingRepository;
    }

    public static SubmissionRepository getSubmissionRepository() {
        return submissionRepository;
    }

    public static void setSubmissionRepository(SubmissionRepository submissionRepository) {
        RepositoryManager.submissionRepository = submissionRepository;
    }

    public static ReportDTORepository getReportRepository() {
        return reportRepository;
    }

    public static void setReportRepository(ReportDTORepository reportRepository) {
        RepositoryManager.reportRepository = reportRepository;
    }
}
