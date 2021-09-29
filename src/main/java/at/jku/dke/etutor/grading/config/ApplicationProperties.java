package at.jku.dke.etutor.grading.config;

import de.fraunhofer.ipsi.xquery.tree.XQuery;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Used to retrieve the application properties
 */
@ConfigurationProperties(value = "application")
public class ApplicationProperties {
    private Async async = new Async();

    private SQL sql = new SQL();

    private XQueryProps xquery = new XQueryProps();

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

    public XQueryProps getXquery() {
        return xquery;
    }

    public void setXquery(XQueryProps xquery) {
        this.xquery = xquery;
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
     * The properties for the XQuery-module
     */
    public static class XQueryProps{
        private XQTable table;
        private boolean modus_debug;
        private String connUser;
        private String connPwd;
        private String connUrl;

        public XQTable getTable() {
            return table;
        }

        public void setTable(XQTable table) {
            this.table = table;
        }

        public boolean isModus_debug() {
            return modus_debug;
        }

        public void setModus_debug(boolean modus_debug) {
            this.modus_debug = modus_debug;
        }

        public String getConnUser() {
            return connUser;
        }

        public void setConnUser(String connUser) {
            this.connUser = connUser;
        }

        public String getConnPwd() {
            return connPwd;
        }

        public void setConnPwd(String connPwd) {
            this.connPwd = connPwd;
        }

        public String getConnUrl() {
            return connUrl;
        }

        public void setConnUrl(String connUrl) {
            this.connUrl = connUrl;
        }

        /**
         * Properties regarding table-names of the xquery-module
         */
        public static class XQTable{
            private String error_categories;
            private String error_grading;
            private String urls;
            private String exercise;
            private String sortings;

            public String getError_categories() {
                return error_categories;
            }

            public void setError_categories(String error_categories) {
                this.error_categories = error_categories;
            }

            public String getError_grading() {
                return error_grading;
            }

            public void setError_grading(String error_grading) {
                this.error_grading = error_grading;
            }

            public String getUrls() {
                return urls;
            }

            public void setUrls(String urls) {
                this.urls = urls;
            }

            public String getExercise() {
                return exercise;
            }

            public void setExercise(String exercise) {
                this.exercise = exercise;
            }

            public String getSortings() {
                return sortings;
            }

            public void setSortings(String sortings) {
                this.sortings = sortings;
            }
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
