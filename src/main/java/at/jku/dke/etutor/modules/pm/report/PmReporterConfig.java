package at.jku.dke.etutor.modules.pm.report;

import at.jku.dke.etutor.modules.pm.PmEvaluationAction;

public class PmReporterConfig {

    private int diagnoseLevel;
    private PmEvaluationAction action;

    public PmReporterConfig(){
        super();
        this.diagnoseLevel = 0; // no feedback
        this.action = PmEvaluationAction.DIAGNOSE;
    }

    public int getDiagnoseLevel() {
        return diagnoseLevel;
    }

    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }

    public void setAction(PmEvaluationAction action) {
        this.action = action;
    }

    public PmEvaluationAction getAction() {
        return action;
    }

}
