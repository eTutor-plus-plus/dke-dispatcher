package at.jku.dke.etutor.modules.ddl.analysis;

import at.jku.dke.etutor.modules.ddl.DDLEvaluationCriterion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class DDLAnalyzerConfig {
    //region Fields
    private int diagnoseLevel;
    private String solution;
    private List<String> dmlStatements;
    private Connection conn;
    private HashSet<DDLEvaluationCriterion> evaluationCriteria;
    //endregion

    public DDLAnalyzerConfig() {
        this.diagnoseLevel = 0;
        this.solution = "";
        this.dmlStatements = new ArrayList<>();
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

    public List<String> getDmlStatements() {
        return dmlStatements;
    }

    //todo Get DMLStatements from plattform
    public void setDmlStatements(List<String> dmlStatements) {
        this.dmlStatements = dmlStatements;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
    //endregion
}
