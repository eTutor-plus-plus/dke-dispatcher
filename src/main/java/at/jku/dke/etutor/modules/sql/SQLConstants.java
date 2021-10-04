package at.jku.dke.etutor.modules.sql;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.springframework.stereotype.Service;

@Service
public class SQLConstants {
    private final String connPwd;
    private final String connUser;
    private final String connURLBase;
    private final String connURL;
    private final String submissionSuffix;
    private final String diagnoseSuffix;
    private final String exerciseDB;
    private final String jdbcDriver;

    public SQLConstants(ApplicationProperties properties) {
        connPwd = properties.getSql().getConnPwd();
        connUser = properties.getSql().getConnUser();
        connURLBase = properties.getSql().getConnBaseUrl();
        connURL = properties.getSql().getConnUrl();
        submissionSuffix = properties.getSql().getSubmissionSuffix();
        diagnoseSuffix = properties.getSql().getDiagnoseSuffix();
        exerciseDB = properties.getSql().getExerciseDatabase();
        jdbcDriver = properties.getGrading().getJDBCDriver();
    }
    
    public String getConnPwd() {
        return connPwd;
    }

    public String getConnUser() {
        return connUser;
    }

    public String getConnURLBase() {
        return connURLBase;
    }

    public String getConnURL() {
        return connURL;
    }

    public String getSubmissionSuffix() {
        return submissionSuffix;
    }

    public String getDiagnoseSuffix() {
        return diagnoseSuffix;
    }

    public String getExerciseDB() {
        return exerciseDB;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }
}
