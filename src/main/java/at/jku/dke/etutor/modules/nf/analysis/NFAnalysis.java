package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.nf.report.NFReport;

import java.util.StringJoiner;

public class NFAnalysis extends DefaultAnalysis {
    private int exerciseId;

    private String syntaxError;

    private Grading grading;

    private NFReport report;

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

    public void appendSyntaxError(String syntaxError) {
        if(this.syntaxError == null) {
            setSyntaxError(syntaxError);
        } else {
            this.syntaxError += ";" + syntaxError;
        }
    }

    public Grading getGrading() {
        return grading;
    }

    public void setGrading(Grading grading) {
        this.grading = grading;
    }

    public NFReport getReport() {
        return report;
    }

    public void setReport(NFReport report) {
        this.report = report;
    }
}
