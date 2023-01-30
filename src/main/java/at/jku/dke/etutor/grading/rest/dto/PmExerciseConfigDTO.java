package at.jku.dke.etutor.grading.rest.dto;
/**
 * Represents the statements required to create a PM exercise configuration
 */
public class PmExerciseConfigDTO {

    int maxActivity;
    int minActivity;
    int maxLogSize;
    int minLogSize;
    String configNum;

    public PmExerciseConfigDTO(){

    }

    public int getMaxActivity() {
        return maxActivity;
    }
    public int getMinActivity() {
        return minActivity;
    }
    public int getMaxLogSize() {
        return maxLogSize;
    }
    public int getMinLogSize() {
        return minLogSize;
    }
    public String getConfigNum() {
        return configNum;
    }
    public void setConfigNum(String configNum) {
        this.configNum = configNum;
    }
    public void setMaxActivity(int maxActivity) {
        this.maxActivity = maxActivity;
    }
    public void setMinActivity(int minActivity) {
        this.minActivity = minActivity;
    }
    public void setMaxLogSize(int maxLogSize) {
        this.maxLogSize = maxLogSize;
    }
    public void setMinLogSize(int minLogSize) {
        this.minLogSize = minLogSize;
    }
}
