package at.jku.dke.etutor.modules.fd.repositories;

import at.jku.dke.etutor.modules.fd.entities.MinimalCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinimalCoverRepository extends JpaRepository<MinimalCover, Long> {
}