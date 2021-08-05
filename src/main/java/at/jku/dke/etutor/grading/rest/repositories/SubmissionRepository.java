package at.jku.dke.etutor.grading.rest.repositories;

import at.jku.dke.etutor.grading.rest.dto.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manipulating Submissions in the database
 */
public interface SubmissionRepository extends JpaRepository<Submission, String> {
}
