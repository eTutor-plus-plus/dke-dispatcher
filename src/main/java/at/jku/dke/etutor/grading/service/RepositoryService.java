package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.ReportDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.ReportDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import org.springframework.stereotype.Service;

/**
 *Service for handling entities
 */
@Service
public class RepositoryService {
    private  final GradingDTORepository gradingRepository;
    private  final SubmissionRepository submissionRepository;
    private  final ReportDTORepository reportRepository;

    /**
     * The constructor
     * @param gradingRepo the injected JPARepository for GradingDTO´s
     * @param submissionRepo the injected JPARepository for Submissions
     * @param reportRepo the injected JPARepository for ReportDTO´s
     */
    public RepositoryService(GradingDTORepository gradingRepo,
                             SubmissionRepository submissionRepo, ReportDTORepository reportRepo){
        gradingRepository = gradingRepo;
        submissionRepository = submissionRepo;
        reportRepository = reportRepo;
    }

    /**
     * Persists a submission
     * @param submission the submission
     */
    public  void persistSubmission(Submission submission){
        submissionRepository.save(submission);
    }

    /**
     * Persists a GradingDTO
     * @param grading the grading
     */
    public  void persistGrading(GradingDTO grading){
        gradingRepository.save(grading);
    }

    /**
     * Persists a ReportDTO
     * @param report the report
     */
    public  void persistReport(ReportDTO report){
        reportRepository.save(report);
    }


}
