package at.jku.dke.etutor.modules.jdbc.report;

public class JDBCReporterConfig {

    private String action;
    private int diagnoseLevel;

    public JDBCReporterConfig() {
        super();
        this.action = "DIAGNOSE";
        this.diagnoseLevel = 0;
    }

    public int getDiagnoseLevel() {
        return this.diagnoseLevel;
    }

    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
