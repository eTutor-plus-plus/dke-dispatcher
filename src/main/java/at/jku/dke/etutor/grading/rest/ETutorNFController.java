package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.ExerciseNotValidException;
import at.jku.dke.etutor.grading.service.NFResourceService;
import at.jku.dke.etutor.objects.dispatcher.nf.NFExerciseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/nf")
public class ETutorNFController {

    private final NFResourceService resourceService;

    public ETutorNFController(NFResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody NFExerciseDTO exerciseDTO) throws ApiException {
        try {
            int newId = resourceService.createExercise(exerciseDTO);

            if(newId < 0) {
                throw new ApiException(500, "Could not create new exercise.", null);
            }

            return ResponseEntity.ok(newId);
        } catch (DatabaseException | ExerciseNotValidException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    @PostMapping("/exercise/{id}")
    public ResponseEntity<String> modifyExercise(@PathVariable int id, @RequestBody NFExerciseDTO exerciseDTO) throws ApiException {
        try {
            if(resourceService.modifyExercise(id, exerciseDTO)) {
                return ResponseEntity.ok("Exercise id " + id + " modified.");
            } else {
                return ResponseEntity.badRequest().body("Exercise id " + id + " could not be modified.");
            }

        } catch (DatabaseException | ExerciseNotValidException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id) throws ApiException {
        try {
            if(resourceService.deleteExercise(id)) {
                return ResponseEntity.ok("Exercise id " + id + " deleted.");
            } else {
                return ResponseEntity.badRequest().body("Exercise id " + id + " could not be deleted.");
            }
        } catch (DatabaseException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    @GetMapping("/exercise/{id}/instruction")
    public ResponseEntity<String> getAssignmentText(@PathVariable int id) throws ApiException {
        try {
            String language = "de";
            String ret = resourceService.generateAssignmentText(id, mapLanguageToLocale(language));

            if(ret == null) {
                throw new ApiException(500, "IO Error in assignment text generator", null);
            }

            return ResponseEntity.ok(ret);
        } catch (DatabaseException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Maps a String to a Locale (shamelessly copied from ETutorSubmissionController) (Gerald Wimmer, 2024-01-06).
     * @param language the language
     * @return the Locale
     */
    private Locale mapLanguageToLocale(String language){
        if(language.equalsIgnoreCase("de")) return Locale.GERMAN;
        else return Locale.ENGLISH;
    }
}
