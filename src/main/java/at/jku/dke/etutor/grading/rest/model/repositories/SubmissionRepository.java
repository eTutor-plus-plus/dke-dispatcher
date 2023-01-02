package at.jku.dke.etutor.grading.rest.model.repositories;

import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for manipulating Submissions in the database
 */
@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
}
