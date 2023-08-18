package at.jku.dke.etutor.modules.fd.services;

import at.jku.dke.etutor.modules.fd.entities.Closure;
import at.jku.dke.etutor.modules.fd.entities.Relation;
import at.jku.dke.etutor.modules.fd.entities.Task;
import at.jku.dke.etutor.modules.fd.repositories.ClosureRepository;
import at.jku.dke.etutor.modules.fd.repositories.RelationRepository;
import at.jku.dke.etutor.modules.fd.repositories.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    RelationRepository relationRepository;
    TaskRepository taskRepository;
    ClosureRepository closureRepository;

    public TaskService(RelationRepository relationRepository, TaskRepository taskRepository,
                ClosureRepository closureRepository) {
        this.relationRepository = relationRepository;
        this.taskRepository = taskRepository;
        this.closureRepository = closureRepository;
    }

    public ResponseEntity<Long> createClosureTasks(Long relationId, String[] fDClosureIds) {
        Long groupId = taskRepository.getLatestGroupId();

        if (groupId == null) {
            groupId = 1L;
        } else {
            groupId = groupId +1;
        }
        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        if (optionalRelation.isPresent()) {
            Relation relation = optionalRelation.get();
            Set<Task> tasks = relation.getTasks();
            for (String fDClosureId : fDClosureIds) {
                Long closureId;
                try {
                    closureId = Long.parseLong(fDClosureId);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400).body(null);
                }
                Optional<Closure> optionalClosure = closureRepository.findById(closureId);
                if (optionalClosure.isPresent()) {
                    tasks.add(new Task(relation, "Closure", groupId, optionalClosure.get()));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
            relationRepository.save(relation);
            return ResponseEntity.status(200).body(groupId);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
    public ResponseEntity<Long> updateClosureTask(String inputId, Long relationId, String fDSubtype, String[] fDClosureIds) {
        Long groupId;
        try {
            groupId = Long.parseLong(inputId.replace("Closure-", ""));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }


        if (!fDSubtype.equals("Closure")) {
            taskRepository.deleteByClosureGroupId(groupId);
            return createTask(relationId, fDSubtype);
        }
        /** Das löschen ist notwenig da ein späteres löschen einzelnen nicht möglich ist. Spring Data JPA */
        Collection<Long> closureIds = new ArrayList<>();
        for (String fDClosureId: fDClosureIds) {
            Long closureId;
            try {
                closureId = Long.parseLong(fDClosureId);
                closureIds.add(closureId);
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400).body(null);
            }
        }
        taskRepository.deleteByClosureGroupIdAndClosureNotIn(groupId, closureIds);

        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        Relation relation;
        if (optionalRelation.isPresent()) {
            relation = optionalRelation.get();
        } else {
            return ResponseEntity.status(404).body(null);
        }


        for (Long closureId: closureIds) {
            if (!taskRepository.checkClosureExists(groupId, closureId)) {
                Optional<Closure> optionalClosure = closureRepository.findById(closureId);
                if (optionalClosure.isPresent()) {
                    Closure closure = optionalClosure.get();
                    taskRepository.save(new Task(relation, "Closure", groupId, closure));
                } else {
                    return ResponseEntity.status(404).body(null);
                }
            }
        }
        return ResponseEntity.status(200).body(groupId);
    }

    public ResponseEntity<Long> createTask(Long relationId, String fDSubtype) {
        if (taskRepository.existsTask(relationId, fDSubtype)) {
            return ResponseEntity.status(409).body(null);
        } else {
            Optional<Relation> optionalRelation = relationRepository.findById(relationId);
            if (optionalRelation.isPresent()) {
                Relation relation = optionalRelation.get();
                Task newTask = new Task(relation, fDSubtype);
                taskRepository.save(newTask);
                return ResponseEntity.status(200).body(newTask.getId());
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }
    }

    public ResponseEntity<Long> updateTask(String inputId, Long relationId, String fDSubtype, String[] fDClosureIds) {
        Long id;
        try {
            id = Long.parseLong(inputId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        if (fDSubtype.equals("Closure")) {
            taskRepository.deleteTaskById(id);
            return createClosureTasks(relationId, fDClosureIds);
        }
        Relation relation;
        Optional<Relation> optionalRelation = relationRepository.findById(relationId);
        if (optionalRelation.isPresent()) {
            relation = optionalRelation.get();
        } else {
            return ResponseEntity.status(404).body(null);
        }

        taskRepository.updateRelationAndTypeById(relation, fDSubtype, id);
        return ResponseEntity.status(200).body(id);
    }

    public ResponseEntity<Long> deleteTask(Long id) {
        taskRepository.deleteTaskById(id);
        return ResponseEntity.status(200).body(id);
    }

    public ResponseEntity<Long> deleteClosureTask(Long id) {
        taskRepository.deleteByClosureGroupId(id);
        return ResponseEntity.status(200).body(id);
    }
}
