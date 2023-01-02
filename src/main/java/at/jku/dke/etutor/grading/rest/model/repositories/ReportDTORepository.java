package at.jku.dke.etutor.grading.rest.model.repositories;

import at.jku.dke.etutor.grading.rest.model.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manipulating ReportDTO instances in the database
 */
public interface ReportDTORepository extends JpaRepository<Report, String> {
}
