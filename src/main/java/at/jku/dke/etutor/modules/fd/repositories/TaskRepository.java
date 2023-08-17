package at.jku.dke.etutor.modules.fd.repositories;

import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Transactional
    @Modifying
    @Query("update Task t set t.relation = ?1, t.type = ?2 where t.id = ?3")
    void updateRelationAndTypeById(Relation relation, String type, Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from fd.task t where t.closure_group_id = ?1 and t.closure_id not in ?2", nativeQuery = true)
    void deleteByClosureGroupIdAndClosureNotIn(Long closureGroupId, Collection<Long> closures);

    @Transactional
    @Modifying
    @Query("delete from Task t where t.closureGroupId = ?1")
    void deleteByClosureGroupId(Long closureGroupId);

    @Transactional
    @Modifying
    @Query("delete from Task t where t.id = ?1")
    void deleteTaskById(Long closureGroupId);

    @Query(value = "SELECT MAX(closure_group_id) from fd.task", nativeQuery = true)
    Long getLatestGroupId();
    @Query(value ="select (count(t) > 0) from fd.task as t where t.relation_id = ?1 and t.type = ?2",
            nativeQuery = true)
    boolean existsTask(Long relation, String type);
    @Query(value ="select (count(t) > 0) from fd.task as t where t.closure_id = ?1 and t.type = 'Closure' ",
            nativeQuery = true)
    boolean existsClosureTask(Long closureId);

    @Query(value = "select (count(t) > 0) from fd.task t where t.closure_group_id = ?1 and t.closure_id = ?2",
            nativeQuery = true)
    boolean checkClosureExists(Long closureGroupId, Long closureId);


}