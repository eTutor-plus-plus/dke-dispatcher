package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.rest.repositories.SubmissionRepository;
import org.springframework.stereotype.Service;

/**
 *Service for handling JPA-repository-methods
 */
@Service
public class RepositoryService {
    private  final GradingDTORepository gradingRepository;
    private  final SubmissionRepository submissionRepository;

    /**
     * The constructor
     * @param gradingRepo the injected JPARepository for GradingDTO´s
     * @param submissionRepo the injected JPARepository for Submissions
     */
    public RepositoryService(GradingDTORepository gradingRepo,
                             SubmissionRepository submissionRepo){
        gradingRepository = gradingRepo;
        submissionRepository = submissionRepo;
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


}
