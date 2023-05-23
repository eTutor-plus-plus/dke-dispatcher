package at.jku.dke.etutor.modules.fd.repositories;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClosureRepository extends JpaRepository<Closure, Long> {
    List<Closure> findByRelation_Id(Long id);

}