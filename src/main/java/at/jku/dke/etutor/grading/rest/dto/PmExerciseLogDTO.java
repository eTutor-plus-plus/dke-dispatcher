package at.jku.dke.etutor.grading.rest.dto;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class PmExerciseLogDTO {
    List<String[]> log = new ArrayList<>();
    int exerciseId;

    public PmExerciseLogDTO(){

    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public List<String[]> getLog() {
        return log;
    }

    public void setLog(List<String[]> log) {
        this.log = log;
    }

    public void addTrace(String[] trace){
        this.log.add(trace);
    }
}
