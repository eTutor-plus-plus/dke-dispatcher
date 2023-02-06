package at.jku.dke.etutor.grading.rest;

import at.jku.dke.etutor.objects.dispatcher.processmining.PmExerciseConfigDTO;
import at.jku.dke.etutor.objects.dispatcher.processmining.PmExerciseLogDTO;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.PmResourceService;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles requests to manage PM exercises
 */
@RestController
@RequestMapping("/pm")
public class ETutorPMController {
    private final Logger logger;
    private final PmResourceService resourceService;


    /**
     * The constructor
     * @param resourceService the injected SQLResourceManager providing methods for manipulating data related to the SQL module
     */
    public ETutorPMController(PmResourceService resourceService){

        this.logger = (Logger) LoggerFactory.getLogger(ETutorPMController.class);
        this.resourceService = resourceService;

    }

    /**
     * Adds an exercise configuration with the specified parameters
     * @param exerciseConfigDTO the {@link PmExerciseConfigDTO} wrapping the configuration parameters
     */
    @PutMapping("/configuration")
    public ResponseEntity<Integer> createExerciseConfiguration(@RequestBody PmExerciseConfigDTO exerciseConfigDTO) throws ApiException{
        logger.info("Enter: createExerciseConfig() {}", "for config " + exerciseConfigDTO.getConfigNum());
        try{
            int id = resourceService.createExerciseConfiguration(exerciseConfigDTO);

            logger.info("Exit: createExercise() {} with Status Code 200", id);
            return ResponseEntity.ok(id);

        }catch (Exception e) {
            logger.error("Exit: createExercise() with Status Code 500", e);
            logger.info("Deleting exercise");
            throw new ApiException(500, e.toString(), null);
        }
    }

//    /**
//     * VERSION 1
//     * Updates the configuration number of an exercise configuration with the specified name and parameter value
//     * @param id id of exercise config
//     * @param value value to update
//     * @throws ApiException
//     */
//    @PostMapping("/configuration/{id}/value")
//    public ResponseEntity<String> updateExerciseConfiguration(@PathVariable int id, @RequestParam String value) throws ApiException{
//        logger.debug("Enter: updateExerciseConfiguration {}", id);
//        try{
//            resourceService.updateExerciseConfiguration(id, value);
//            logger.info("Exit: updateExerciseConfiguration with status code 200");
//            return ResponseEntity.ok("Configuration updated");
//
//        }catch (DatabaseException e){
//            logger.error("Exit: updateExerciseConfiguration() with status code 500", e);
//            throw new ApiException(500, e.toString(), null);
//        }
//
//    }

    /**
     * VERSION 2 - 01.11.2022
     * Updates the configuration number of an exercise configuration with the specified name and parameter value
     * @param id id of exercise config
     * @param exerciseConfigDTO configuration parameter to be changed
     * @throws ApiException
     */
    @PostMapping("/configuration/{id}/values")
    public ResponseEntity<String> updateExerciseConfiguration(@PathVariable int id, @RequestBody PmExerciseConfigDTO exerciseConfigDTO) throws ApiException{
        logger.debug("Enter: updateExerciseConfiguration {}", id);
        try{
            resourceService.updateExerciseConfiguration(id, exerciseConfigDTO);
            logger.info("Exit: updateExerciseConfiguration with status code 200");
            return ResponseEntity.ok("Configuration updated");

        }catch (DatabaseException e){
            logger.error("Exit: updateExerciseConfiguration() with status code 500", e);
            throw new ApiException(500, e.toString(), null);
        }

    }

    /**
     * Deletes a exercise configuration
     * @param id id of exercise configuration
     * @throws ApiException
     */
    @DeleteMapping("/configuration/{id}")
    public ResponseEntity<String> deleteExerciseConfiguration(@PathVariable int id) throws ApiException{
        logger.info("Delete: deleteExerciseConfiguration(): {}", id);
        try{
            resourceService.deleteExerciseConfiguration(id);
            logger.info("Exit: deleteExerciseConfiguration() with status code 200");
            return ResponseEntity.ok("Exercise deleted");
        }catch(DatabaseException e){
            logger.error("Exit: deleteExerciseConfiguration() with status code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }

    /**
     * Returns a configuration
     * @param id the id of an existing configuration
     * @return a {@link PmExerciseConfigDTO} representing the configuration
     * @throws ApiException
     */
    @GetMapping("/configurations/{id}")
    public ResponseEntity<PmExerciseConfigDTO> getExerciseConfiguration(@PathVariable int id) throws ApiException{
        logger.info("Enter: getExerciseConfiguration");
        PmExerciseConfigDTO exerciseConfigDTO = null;

        try{
            exerciseConfigDTO = resourceService.fetchExerciseConfiguration(id);
            return ResponseEntity.ok(exerciseConfigDTO);
        } catch (DatabaseException e){
            logger.error("Exit: getExerciseConfiguration() with status code 500", e);
            throw new ApiException(500, e.toString(), null);
        }

    }


//    /**
//     * VERSION 1
//     * Adds a solution of a random generated process (result log) solved with the alpha algorithm {@link at.jku.dke.etutor.modules.pm.AlphaAlgo.AlphaAlgorithm}
//     * @param configId the given configuration parameters stored
//     * @return returns unique exerciseID
//     * @throws ApiException
//     */
//    @PutMapping("/exercise")
//    public ResponseEntity<Integer> createRandomExercise(@RequestParam int configId) throws ApiException{
//        logger.info("Enter: createRandomExercise() {}", "for config: "+configId);
//        try{
//            int id = resourceService.createRandomExercise(configId);
//            logger.info("Exit: createRandomExercise(){} with Status Code 2ßß", id);
//            return ResponseEntity.ok(id);
//        }catch (Exception e){
//            logger.error("Exit: createRandomExercise() with Status Code 500", e);
//            logger.info("Deleting exercise");
//            throw new ApiException(500, e.toString(), null);
//        }
//
//    }

    /**
     * VERSION 2
     * edited 06.11.2022
     * Adds a solution of a random generated process (result log) solved with the alpha algorithm {@link at.jku.dke.etutor.modules.pm.AlphaAlgo.AlphaAlgorithm}
     * @param configId the given configuration parameters stored
     * @return returns unique dispatcher exerciseID
     * @throws ApiException
     */
    @GetMapping("/exercise/{configId}")
    public ResponseEntity<Integer> createRandomExercise(@PathVariable int configId) throws ApiException{
        logger.info("Enter: createRandomExercise() {}", "for config: "+configId);
        try{
            int id = resourceService.getAvailableExerciseForConfiguration(configId);
            logger.info("Exit: createRandomExercise(){} with Status Code 200", id);
            return ResponseEntity.ok(id);
        }catch (Exception e){
            logger.error("Exit: createRandomExercise() with Status Code 500", e);
            logger.info("Deleting exercise");
            throw new ApiException(500, e.toString(), null);
        }

    }

    /**
     * Returns the log corresponding to the required exercise
     * @param exerciseId the given exerciseId
     * @return a {@link PmExerciseLogDTO} wrapping the information
     * @throws ApiException
     * status as of 16.11.2022
     */
    @GetMapping("/log/{exerciseId}")
    public ResponseEntity<PmExerciseLogDTO> getLogToExercise(@PathVariable int exerciseId) throws ApiException{
        logger.info("Enter: getLogToExercise() {}", "for exercise: "+exerciseId);
        PmExerciseLogDTO exerciseLogDTO = null;

        try{
            exerciseLogDTO = resourceService.fetchExerciseLog(exerciseId);
            return ResponseEntity.ok(exerciseLogDTO);
        }catch (DatabaseException e){
            logger.error("Exit: getLogToExercise() with status code 500", e);
            throw new ApiException(500, e.toString(), null);
        }
    }
}
