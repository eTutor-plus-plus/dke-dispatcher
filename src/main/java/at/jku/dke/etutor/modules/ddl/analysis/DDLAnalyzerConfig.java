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
    private List<String> dmlStatements;
    private Connection exerciseConn;
    private Connection userConn;
    private HashSet<DDLEvaluationCriterion> evaluationCriteria;
    //endregion

    public DDLAnalyzerConfig() {
        this.diagnoseLevel = 0;
        this.dmlStatements = new ArrayList<>();
        this.exerciseConn = null;
        this.userConn = null;
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

    public List<String> getDmlStatements() {
        return dmlStatements;
    }

    public void setDmlStatements(List<String> dmlStatements) {
        this.dmlStatements = dmlStatements;
    }

    public Connection getExerciseConn() {
        return exerciseConn;
    }

    public void setExerciseConn(Connection exerciseConn) {
        this.exerciseConn = exerciseConn;
    }

    public Connection getUserConn() {
        return userConn;
    }

    public void setUserConn(Connection userConn) {
        this.userConn = userConn;
    }
    //endregion
}
