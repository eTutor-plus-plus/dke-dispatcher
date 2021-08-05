package at.jku.dke.etutor.grading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Used to retrieve the application properties
 */
@ConfigurationProperties(value = "application")
public class ApplicationProperties {
    private Async async = new Async();

    private SQL sql = new SQL();

    private Grading grading = new Grading();

    public Async getAsync() {
        return async;
    }

    public void setAsync(Async async) {
        this.async = async;
    }

    public SQL getSql() {
        return sql;
    }

    public void setSql(SQL sql) {
        this.sql = sql;
    }

    public Grading getGrading() {
        return grading;
    }

    public void setGrading(Grading grading) {
        this.grading = grading;
    }

    /**
     * The properties needed to configure the async TaskExecutor
     */
    public static class Async{
        private int maxPoolSize;
        private int corePoolSize;
        private int queueCapacity;
        private String threadNamePrefix;

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }
    }

    /**
     * The properties for the SQL module
     */
    public static class SQL{
        private String connPwd;
        private String connUser;
        private String connBaseUrl;
        private String connUrl;
        private String submissionSuffix;
        private String diagnoseSuffix;
        private String exerciseDatabase;

        public String getConnPwd() {
            return connPwd;
        }

        public void setConnPwd(String connPwd) {
            this.connPwd = connPwd;
        }

        public String getConnUser() {
            return connUser;
        }

        public void setConnUser(String connUser) {
            this.connUser = connUser;
        }

        public String getConnBaseUrl() {
            return connBaseUrl;
        }

        public void setConnBaseUrl(String connBaseUrl) {
            this.connBaseUrl = connBaseUrl;
        }

        public String getConnUrl() {
            return connUrl;
        }

        public void setConnUrl(String connUrl) {
            this.connUrl = connUrl;
        }

        public String getSubmissionSuffix() {
            return submissionSuffix;
        }

        public void setSubmissionSuffix(String submissionSuffix) {
            this.submissionSuffix = submissionSuffix;
        }

        public String getDiagnoseSuffix() {
            return diagnoseSuffix;
        }

        public void setDiagnoseSuffix(String diagnoseSuffix) {
            this.diagnoseSuffix = diagnoseSuffix;
        }

        public String getExerciseDatabase() {
            return exerciseDatabase;
        }

        public void setExerciseDatabase(String exerciseDatabase) {
            this.exerciseDatabase = exerciseDatabase;
        }
    }

    /**
     * The properties for the grading package
     */
    public static class Grading{
        private String connSuperUser;
        private String connSuperPwd;
        private String corsPolicy;
        private String JDBCDriver;

        public String getConnSuperUser() {
            return connSuperUser;
        }

        public void setConnSuperUser(String connSuperUser) {
            this.connSuperUser = connSuperUser;
        }

        public String getConnSuperPwd() {
            return connSuperPwd;
        }

        public void setConnSuperPwd(String connSuperPwd) {
            this.connSuperPwd = connSuperPwd;
        }

        public String getCorsPolicy() {
            return corsPolicy;
        }

        public void setCorsPolicy(String corsPolicy) {
            this.corsPolicy = corsPolicy;
        }

        public String getJDBCDriver() {
            return JDBCDriver;
        }

        public void setJDBCDriver(String JDBCDriver) {
            this.JDBCDriver = JDBCDriver; }
    }


}
