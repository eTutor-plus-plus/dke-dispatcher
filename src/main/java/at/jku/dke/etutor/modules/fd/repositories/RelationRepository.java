package at.jku.dke.etutor.modules.fd.repositories;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    @Query(value = "SELECT nextval('fd.exercise_id_seq');", nativeQuery = true)
    Long getNextId();
}