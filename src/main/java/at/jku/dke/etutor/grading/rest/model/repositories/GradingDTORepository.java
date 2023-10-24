package at.jku.dke.etutor.grading.rest.model.repositories;

import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for manipulating GradingDTO instances in the database
 */
@Repository
public interface GradingDTORepository extends JpaRepository<Grading, String> {
    @Query("select g from Grading g where g.submissionId = ?1")
    Optional<Grading> findBySubmissionId(String submissionId);


}
