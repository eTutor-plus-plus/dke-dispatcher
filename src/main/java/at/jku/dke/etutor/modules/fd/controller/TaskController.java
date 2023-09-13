package at.jku.dke.etutor.modules.fd.controller;

import at.jku.dke.etutor.modules.fd.services.TaskService;
import at.jku.dke.etutor.modules.fd.utilities.FDTaskSolve;
import at.jku.dke.etutor.modules.fd.utilities.FDTaskSolveResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path="/fd")
public class TaskController {
    TaskService taskService;
    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("/new_task")
    public ResponseEntity<Long> newTask(@RequestBody JsonNode jsonNode) {
        Long relationId;

        String inputTaskGroupId = jsonNode.get("taskGroupId").asText().replace("http://www.dke.uni-linz.ac.at/etutorpp/TaskGroup#FunctionalDependencies-","");
        String inputFDSubtype = jsonNode.get("fDSubtype").asText().replace("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#","");

        String [] fDClosureIds;
        if (!jsonNode.get("fDClosureIds").toString().equals("null")) {
            fDClosureIds = jsonNode.get("fDClosureIds").toString().replaceAll("[\\[\\]\"]","")
                    .split(",");
        } else {
            fDClosureIds = null;
        }

        try {
            relationId = Long.parseLong(inputTaskGroupId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }
        if (inputFDSubtype.equals("Closure")) {
            if (fDClosureIds == null) {
                return ResponseEntity.status(400).body(null);
            } else {
                return taskService.createClosureTasks(relationId, fDClosureIds);
            }
        } else if (inputFDSubtype.equals("MinimalCover") || inputFDSubtype.equals("Key") ||
                inputFDSubtype.equals("Normalform") || inputFDSubtype.equals("Normalization")) {
            return taskService.createTask(relationId, inputFDSubtype);
        } else {
            return ResponseEntity.status(400).body(null);
        }
    }
    @PostMapping("/update_task")
    public ResponseEntity<Long> updateTask(@RequestBody JsonNode jsonNode) {
        String inputId = jsonNode.get("taskId").asText();
        String inputTaskGroupId = jsonNode.get("taskGroupId").asText().replace("http://www.dke.uni-linz.ac.at/etutorpp/TaskGroup#FunctionalDependencies-","");
        String inputFDSubtype = jsonNode.get("fDSubtype").asText().replace("http://www.dke.uni-linz.ac.at/etutorpp/FDSubtype#","");
        String [] fDClosureIds;
        if (!jsonNode.get("fDClosureIds").toString().equals("null")) {
            fDClosureIds = jsonNode.get("fDClosureIds").toString().replaceAll("[\\[\\]\"]","")
                    .split(",");
        } else {
            fDClosureIds = null;
        }
        Long relationId;
        try {
            relationId = Long.parseLong(inputTaskGroupId);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400).body(null);
        }

        if (inputId.contains("Closure-")) {
            return taskService.updateClosureTask(inputId, relationId, inputFDSubtype, fDClosureIds);
        } else {
            return taskService.updateTask(inputId, relationId, inputFDSubtype, fDClosureIds);
        }
    }
    @DeleteMapping("/delete_closure_task")
    public ResponseEntity<Long> deleteClosureTask(@RequestParam Long id) {
        return taskService.deleteClosureTask(id);
    }
    @DeleteMapping("/delete_task")
    public ResponseEntity<Long> deleteTask(@RequestParam Long id) {
        return taskService.deleteTask(id);
    }
    @GetMapping("/assignment/closure")
    public ResponseEntity<Map<Long, String[]>> getLeftSidesClosure(@RequestParam Long id) {
        return taskService.getLeftSidesClosure(id);
    }
    @PostMapping("/assignment/solve")
    public ResponseEntity<FDTaskSolveResponse> fdTaskSolve(@RequestBody FDTaskSolve fdTaskSolve) {
        return taskService.fdTaskSolve(fdTaskSolve);
    }
}
