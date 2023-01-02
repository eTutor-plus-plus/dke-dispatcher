package at.jku.dke.etutor.grading.rest.model.repositories;

import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manipulating GradingDTO instances in the database
 */
public interface GradingDTORepository extends JpaRepository<Grading, String> {
}
