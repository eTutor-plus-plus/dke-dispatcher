package at.jku.dke.etutor.grading.rest.repositories;

import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manipulating GradingDTO instances in the database
 */
public interface GradingDTORepository extends JpaRepository<GradingDTO, String> {
}
