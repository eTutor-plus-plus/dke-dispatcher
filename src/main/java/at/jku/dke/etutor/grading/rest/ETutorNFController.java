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

/**
 * REST-controller that manages requests for handling NF exercises
 */
@RestController
@RequestMapping("/nf")
public class ETutorNFController {

    private final NFResourceService resourceService;

    public ETutorNFController(NFResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Creates a new exercise in the database from the supplied <code>NFExerciseDTO</code>.
     * @param exerciseDTO The <code>NFExerciseDTO</code> with the content of the new exercise
     * @return The id of the newly created exercise
     * @throws ApiException If an error occurs during the exercise's creation
     */
    @PutMapping("/exercise")
    public ResponseEntity<Integer> createExercise(@RequestBody NFExerciseDTO exerciseDTO) throws ApiException {
        try {
            int newId = resourceService.createExercise(exerciseDTO);

            if(newId < 0) {
                throw new ApiException(500, "Could not create new exercise.", null);
            }

            return ResponseEntity.ok(newId);
        } catch (ExerciseNotValidException e) {
            throw new ApiException(400, e.toString(), null);
        } catch (DatabaseException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Replaces the specified exercise in the database with one specified in the supplied <code>NFExerciseDTO</code>.
     * @param id The id of the exercise to be replaced
     * @param exerciseDTO The <code>NFExerciseDTO</code> whose content is to replace the existing exercise
     * @return A boolean string indicating whether the operation succeeded
     * @throws ApiException If an error occurs during the replacement of the exercise
     */
    @PostMapping("/exercise/{id}")
    public ResponseEntity<String> modifyExercise(@PathVariable int id, @RequestBody NFExerciseDTO exerciseDTO) throws ApiException {
        try {
            if(resourceService.modifyExercise(id, exerciseDTO)) {
                return ResponseEntity.ok("Exercise id " + id + " modified.");
            } else {
                throw new ApiException(400, "Exercise id " + id + " could not be modified.", null);
            }

        } catch (ExerciseNotValidException e) {
            throw new ApiException(400, e.toString(), null);
        } catch (DatabaseException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Deletes the exercise with the specified id from the database.
     * @param id The id of the exercise to be deleted
     * @return Whether the deletion succeeded
     * @throws ApiException If an error occurs during the deletion of the exercise
     */
    @DeleteMapping("/exercise/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable int id) throws ApiException {
        try {
            if(resourceService.deleteExercise(id)) {
                return ResponseEntity.ok("Exercise id " + id + " deleted.");
            } else {
                throw new ApiException(400, "Exercise id " + id + " could not be deleted.", null);
            }
        } catch (DatabaseException e) {
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Returns the auto-generated assignment text for the exercise with the specified id.
     * @param id The id of the exercise whose assignment text is to be generated.
     * @return The auto-generated assignment text for the exercise with the specified id.
     * @throws ApiException If an error occurs during the assignment text generation.
     */
    @GetMapping("/exercise/{id}/instruction")
    public ResponseEntity<String> getAssignmentText(@PathVariable int id) throws ApiException {
        try {
            String language = "en";
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
