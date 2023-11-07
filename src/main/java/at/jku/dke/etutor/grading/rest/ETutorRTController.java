package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.RTResourceService;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.modules.rt.RTObject;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/rt")
public class ETutorRTController {

    private final RTResourceService resourceService;

    public ETutorRTController(RTResourceService resourceService){
        this.resourceService = resourceService;
    }

    @PostMapping("/task/getEntry")
    public boolean getEntry(@RequestBody String name, int id) throws SQLException {
        RTAnalysis elem = new RTAnalysis(name);
        RTObject rtObject = resourceService.getTask(id);
        System.out.println(rtObject.getSolutionRows().toString());
        return true;
    }

    @PostMapping("/task/addTask")
    public Long getTask(@RequestBody String elem) throws SQLException, ParseException {
        RTObject rtObject = resourceService.getRTObject(elem);
        String solution = rtObject.getSolution().toString();
        solution = solution = solution.substring(1, solution.length() - 1);
        return this.resourceService.insertTask(solution, rtObject.getMaxPoints());
    }

    @PutMapping("/task/editTask")
    public void editTask(@RequestBody String elem) throws SQLException, ParseException {
        RTObject rtObject = resourceService.getRTObject(elem);
        String solution = rtObject.getSolution().toString();
        solution = solution = solution.substring(1, solution.length() - 1);
        this.resourceService.editTask(solution, rtObject.getMaxPoints(), rtObject.getId());
    }

    @DeleteMapping("/task/deleteTask/{id}")
    public void deleteTask(@PathVariable Integer id) throws SQLException {
        resourceService.deleteTask(id);
    }
}
