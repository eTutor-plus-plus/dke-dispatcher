package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

public class NFAnalysis extends DefaultAnalysis {
    private int exerciseId;

    public NFAnalysis() {
        super();
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
