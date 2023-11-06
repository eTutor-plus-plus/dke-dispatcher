package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;

public class DDLAnalyzerConfig {
    //region Fields
    private int diagnoseLevel;
    private String solution;
    private Connection conn;
    private HashSet<DDLEvaluationCriterion> evaluationCriteria;
    //endregion

    public DDLAnalyzerConfig() {
        this.diagnoseLevel = 0;
        this.solution = "";
        this.conn = null;
        this.evaluationCriteria = new HashSet<>();
    }

    public boolean isCriterionToAnalyze(DDLEvaluationCriterion criterion) {
        return this.evaluationCriteria.contains(criterion);
    }

    public Iterator<DDLEvaluationCriterion> iterCriteriaToAnalyze() {
        return this.evaluationCriteria.iterator();
    }

    public void addCriterionToAnalyze(DDLEvaluationCriterion criterion) {
        this.evaluationCriteria.add(criterion);
    }

    //region Getter/Setter
    public int getDiagnoseLevel() {
        return diagnoseLevel;
    }

    public void setDiagnoseLevel(int diagnoseLevel) {
        this.diagnoseLevel = diagnoseLevel;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    //endregion
}
