package at.jku.dke.etutor.grading.rest.repositories;

import at.jku.dke.etutor.grading.rest.dto.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manipulating ReportDTO instances in the database
 */
public interface ReportDTORepository extends JpaRepository<ReportDTO, String> {
}
