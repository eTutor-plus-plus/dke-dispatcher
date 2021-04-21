package at.jku.dke.etutor.grading.rest.repositories;

import at.jku.dke.etutor.grading.rest.dto.ReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportDTORepository extends JpaRepository<ReportDTO, String> {
}
