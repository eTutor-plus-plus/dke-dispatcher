package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.RTResourceService;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.modules.rt.RTObject;
import at.jku.dke.etutor.modules.rt.RTSolution;
import at.jku.dke.etutor.modules.rt.analysis.RTAnalysis;
import com.fasterxml.jackson.core.JsonProcessingException;
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


    @PostMapping("/task/addTask")
    public Long addTask(@RequestBody String elem) throws SQLException, ParseException, JsonProcessingException {
        boolean parseError = false;
        RTObject rtObject = null;
        String solution = "";
        long error = 1;
        int checkSolution = 1;
        try {
            rtObject = resourceService.getRTObject(elem);
            if (rtObject.getSolution() != null){
                solution = rtObject.getSolution().toString();
                solution = solution = solution.substring(1, solution.length() - 1);
                List<RTSolution> rtSolutions = resourceService.getRTSolutions(solution);
                checkSolution = resourceService.checkRTSolution(rtSolutions, rtObject.getMaxPoints());
            }
        } catch (RuntimeException e){
            parseError = true;
        }
        if(checkSolution != 1){
            error = checkSolution;
            return error;
        }
        if(!parseError) {
            this.resourceService.insertTask(solution, rtObject.getMaxPoints());
            return error;
        }
        else{
            error = -1;
            return error;
        }
    }

    @PutMapping("/task/editTask")
    public Long editTask(@RequestBody String elem) throws SQLException, ParseException, JsonProcessingException {
        boolean parseError = false;
        RTObject rtObject = null;
        String solution = "";
        long error = 1;
        int checkSolution = 1;
        try {
            rtObject = resourceService.getRTObject(elem);
            if (rtObject.getSolution() != null){
            solution = rtObject.getSolution().toString();
            solution = solution = solution.substring(1, solution.length() - 1);
            List<RTSolution> rtSolutions = resourceService.getRTSolutions(solution);
                checkSolution = resourceService.checkRTSolution(rtSolutions, rtObject.getMaxPoints());
            }
        } catch (RuntimeException e){
            parseError = true;
        }
        if(checkSolution != 1){
            error = checkSolution;
            return error;
        }
        if(!parseError) {
            this.resourceService.editTask(solution, rtObject.getMaxPoints(), rtObject.getId());
            return error;
        }
        else{
            error = -1;
            return error;
        }
    }

    @DeleteMapping("/task/deleteTask/{id}")
    public void deleteTask(@PathVariable Integer id) throws SQLException {
        resourceService.deleteTask(id);
    }

    @GetMapping("/task/getTask/{id}")
    public RTObject getTask(@PathVariable Integer id) throws SQLException {
        return resourceService.getTask(id);
    }


}
