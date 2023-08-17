package at.jku.dke.etutor.modules.fd.repositories;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClosureRepository extends JpaRepository<Closure, Long> {
    List<Closure> findByRelation_Id(Long id);

}