package at.jku.dke.etutor.grading.rest.dto;


import org.springframework.stereotype.Component;


import java.io.Serializable;
import java.util.Map;

/**
 * Represents the students submission which hast to be distributed to and evaluated by the corresponding module,
 *  depending on the tasktype.
 *  Parameters according to the modules implementation of the interfaces (see package evaluation).
 *
 */


@Component
public class Submission implements Serializable {
    private String taskType;
    private int exerciseId;
    private int userId;
    private Map passedAttributes;
    private Map passedParameters;
    private int maxPoints;


    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map getPassedAttributes() {
        return passedAttributes;
    }

    public void setPassedAttributes(Map passedAttributes) {
        this.passedAttributes = passedAttributes;
    }

    public Map getPassedParameters() {
        return passedParameters;
    }

    public void setPassedParameters(Map passedParameters) {
        this.passedParameters = passedParameters;
    }
}
