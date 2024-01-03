package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

import java.util.StringJoiner;

public class NFAnalysis extends DefaultAnalysis {
    private int exerciseId;

    private String syntaxError;

    public NFAnalysis() {
        super();
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getSyntaxError() {
        return syntaxError;
    }

    public void setSyntaxError(String syntaxError) {
        this.syntaxError = syntaxError;
    }

    public void setSyntaxError(String ... syntaxErrors) {
        StringJoiner sj = new StringJoiner(";");

        for(String s : syntaxErrors) {
            sj.add(s);
        }

        this.syntaxError = sj.toString();
    }
}
