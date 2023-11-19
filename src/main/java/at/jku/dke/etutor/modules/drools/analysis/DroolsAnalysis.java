package at.jku.dke.etutor.modules.drools.analysis;

import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.rt.analysis.RTSemanticsAnalysis;

import java.sql.SQLException;

public class DroolsAnalysis extends DefaultAnalysis {
    private int id;
    private String inputRules;

    public DroolsAnalysis(int id, String inputRules) {
        this.id = id;
        this.inputRules = inputRules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInputRules() {
        return inputRules;
    }

    public void setInputRules(String inputRules) {
        this.inputRules = inputRules;
    }

    public boolean hasSyntaxError() {
        return false;
    }

    public boolean hasSemantikError() {
        return false;
    }
}
