package at.jku.dke.etutor.modules.ddl.report;


import at.jku.dke.etutor.modules.ddl.DDLEvaluationAction;

public class DDLReporterConfig {
    //region Fields
    private int diagnoseLevel;
    private DDLEvaluationAction action;
    //endregion

    public DDLReporterConfig() {
        this.diagnoseLevel = 0;
        this.action = DDLEvaluationAction.RUN;
    }

    //region Getter/Setter
    public int getDiagnoseLevel() {
        return diagnoseLevel;
    }

    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }

    public DDLEvaluationAction getAction() {
        return action;
    }

    public void setAction(DDLEvaluationAction action) {
        this.action = action;
    }
    //endregion
}
