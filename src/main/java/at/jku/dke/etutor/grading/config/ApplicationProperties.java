package at.jku.dke.etutor.grading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Used to retrieve the application properties
 */
@ConfigurationProperties(value = "application")
public class ApplicationProperties {
    private Async async = new Async();

    private SQL sql = new SQL();
    private DDL ddl = new DDL();

    private ProcessMining processMining = new ProcessMining();

    private DataSource datasource = new DataSource();

    private Datalog datalog = new Datalog();

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

    public DDL getDdl() {
        return ddl;
    }

    public void setDdl(DDL ddl) {
        this.ddl = ddl;
    }

    public ProcessMining getProcessMining(){
        return processMining;
    }

    public void setProcessMining(ProcessMining processMining) {
        this.processMining = processMining;
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

    public DataSource getDatasource() {
        return datasource;
    }

    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    public Datalog getDatalog() {
        return datalog;
    }

    public void setDatalog(Datalog datalog) {
        this.datalog = datalog;
    }

    /**
     * Properties for the Datalog module
     */

    public static class Datalog{
        private String connUrl;
        private String DLVPathAsCommand;
        private String cautiousOptionForDlv;
        private String workDirForDlv;
        private String tempFolder;
        private String factEncodingSuffix;
        private String factUrlForLink;

        public String getConnUrl() {
            return connUrl;
        }

        public void setConnUrl(String connUrl) {
            this.connUrl = connUrl;
        }

        public String getDLVPathAsCommand() {
            return DLVPathAsCommand;
        }

        public void setDLVPathAsCommand(String DLVPathAsCommand) {
            this.DLVPathAsCommand = DLVPathAsCommand;
        }

        public String getCautiousOptionForDlv() {
            return cautiousOptionForDlv;
        }

        public void setCautiousOptionForDlv(String cautiousOptionForDlv) {
            this.cautiousOptionForDlv = cautiousOptionForDlv;
        }

        public String getWorkDirForDlv() {
            return workDirForDlv;
        }

        public void setWorkDirForDlv(String workDirForDlv) {
            this.workDirForDlv = workDirForDlv;
        }

        public String getTempFolder() {
            return tempFolder;
        }

        public void setTempFolder(String tempFolder) {
            this.tempFolder = tempFolder;
        }

        public String getFactEncodingSuffix() {
            return factEncodingSuffix;
        }

        public void setFactEncodingSuffix(String factEncodingSuffix) {
            this.factEncodingSuffix = factEncodingSuffix;
        }

        public String getFactUrlForLink() {
            return factUrlForLink;
        }

        public void setFactUrlForLink(String factUrlForLink) {
            this.factUrlForLink = factUrlForLink;
        }
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

    public static class DataSource{
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private long maxLifetime;
        private int maxPoolSize;
        private String database;

        public DataSource(){

        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driver) {
            this.driverClassName = driver;
        }

        public long getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(long maxLifetime) {
            this.maxLifetime = maxLifetime;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }
    }
    /**
     * The properties for the SQL module
     */
    public static class SQL{
        private String connPwd;
        private String connUser;
        private String connUrl;
        private String submissionSuffix;
        private String diagnoseSuffix;
        private String exerciseDatabase;
        private String tableUrlForLink;

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

        public String getTableUrlForLink() {
            return tableUrlForLink;
        }

        public void setTableUrlForLink(String tableUrlForLink) {
            this.tableUrlForLink = tableUrlForLink;
        }
    }

    /**
     * The properties for the DDL module
     */
    public static class DDL {
        //region Fields
        private String systemConnPwd;
        private String systemConnUser;
        private String connUrl;
        private String exerciseDatabase;

        private String connUser1;
        private String connPwdUser1;
        private String connUser2;
        private String connPwdUser2;
        private String connUser3;
        private String connPwdUser3;
        private String connUser4;
        private String connPwdUser4;
        private String connUser5;
        private String connPwdUser5;
        private String connUser6;
        private String connPwdUser6;
        private String connUser7;
        private String connPwdUser7;
        private String connUser8;
        private String connPwdUser8;
        private String connUser9;
        private String connPwdUser9;
        private String connUser10;
        private String connPwdUser10;
        //endregion

        //region Getter/Setter

        public String getSystemConnPwd() {
            return systemConnPwd;
        }

        public void setSystemConnPwd(String systemConnPwd) {
            this.systemConnPwd = systemConnPwd;
        }

        public String getSystemConnUser() {
            return systemConnUser;
        }

        public void setSystemConnUser(String systemConnUser) {
            this.systemConnUser = systemConnUser;
        }

        public String getConnUrl() {
            return connUrl;
        }

        public void setConnUrl(String connUrl) {
            this.connUrl = connUrl;
        }

        public String getExerciseDatabase() {
            return exerciseDatabase;
        }

        public void setExerciseDatabase(String exerciseDatabase) {
            this.exerciseDatabase = exerciseDatabase;
        }

        public String getConnUser1() {
            return connUser1;
        }

        public void setConnUser1(String connUser1) {
            this.connUser1 = connUser1;
        }

        public String getConnPwdUser1() {
            return connPwdUser1;
        }

        public void setConnPwdUser1(String connPwdUser1) {
            this.connPwdUser1 = connPwdUser1;
        }

        public String getConnUser2() {
            return connUser2;
        }

        public void setConnUser2(String connUser2) {
            this.connUser2 = connUser2;
        }

        public String getConnPwdUser2() {
            return connPwdUser2;
        }

        public void setConnPwdUser2(String connPwdUser2) {
            this.connPwdUser2 = connPwdUser2;
        }

        public String getConnUser3() {
            return connUser3;
        }

        public void setConnUser3(String connUser3) {
            this.connUser3 = connUser3;
        }

        public String getConnPwdUser3() {
            return connPwdUser3;
        }

        public void setConnPwdUser3(String connPwdUser3) {
            this.connPwdUser3 = connPwdUser3;
        }

        public String getConnUser4() {
            return connUser4;
        }

        public void setConnUser4(String connUser4) {
            this.connUser4 = connUser4;
        }

        public String getConnPwdUser4() {
            return connPwdUser4;
        }

        public void setConnPwdUser4(String connPwdUser4) {
            this.connPwdUser4 = connPwdUser4;
        }

        public String getConnUser5() {
            return connUser5;
        }

        public void setConnUser5(String connUser5) {
            this.connUser5 = connUser5;
        }

        public String getConnPwdUser5() {
            return connPwdUser5;
        }

        public void setConnPwdUser5(String connPwdUser5) {
            this.connPwdUser5 = connPwdUser5;
        }

        public String getConnUser6() {
            return connUser6;
        }

        public void setConnUser6(String connUser6) {
            this.connUser6 = connUser6;
        }

        public String getConnPwdUser6() {
            return connPwdUser6;
        }

        public void setConnPwdUser6(String connPwdUser6) {
            this.connPwdUser6 = connPwdUser6;
        }

        public String getConnUser7() {
            return connUser7;
        }

        public void setConnUser7(String connUser7) {
            this.connUser7 = connUser7;
        }

        public String getConnPwdUser7() {
            return connPwdUser7;
        }

        public void setConnPwdUser7(String connPwdUser7) {
            this.connPwdUser7 = connPwdUser7;
        }

        public String getConnUser8() {
            return connUser8;
        }

        public void setConnUser8(String connUser8) {
            this.connUser8 = connUser8;
        }

        public String getConnPwdUser8() {
            return connPwdUser8;
        }

        public void setConnPwdUser8(String connPwdUser8) {
            this.connPwdUser8 = connPwdUser8;
        }

        public String getConnUser9() {
            return connUser9;
        }

        public void setConnUser9(String connUser9) {
            this.connUser9 = connUser9;
        }

        public String getConnPwdUser9() {
            return connPwdUser9;
        }

        public void setConnPwdUser9(String connPwdUser9) {
            this.connPwdUser9 = connPwdUser9;
        }

        public String getConnUser10() {
            return connUser10;
        }

        public void setConnUser10(String connUser10) {
            this.connUser10 = connUser10;
        }

        public String getConnPwdUser10() {
            return connPwdUser10;
        }

        public void setConnPwdUser10(String connPwdUser10) {
            this.connPwdUser10 = connPwdUser10;
        }

        //endregion
    }

    /**
     * The properties for the Process Mining module
     */
    public static class ProcessMining{
        private String connPwd;
        private String connUser;
        private String connUrl;
        private int initialExercisesToBuffer=30;
        private int minimumThresholdForExercises=10;
        private int numberOfExercisesToRebuffer=10;

        public String getConnPwd() {
            return connPwd;
        }
        public String getConnUrl() {
            return connUrl;
        }
        public String getConnUser() {
            return connUser;
        }
        public void setConnPwd(String connPwd) {
            this.connPwd = connPwd;
        }
        public void setConnUrl(String connUrl) {
            this.connUrl = connUrl;
        }
        public void setConnUser(String connUser) {
            this.connUser = connUser;
        }

        public int getInitialExercisesToBuffer() {
            return initialExercisesToBuffer;
        }

        public void setInitialExercisesToBuffer(int initialExercisesToBuffer) {
            this.initialExercisesToBuffer = initialExercisesToBuffer;
        }

        public int getMinimumThresholdForExercises() {
            return minimumThresholdForExercises;
        }

        public void setMinimumThresholdForExercises(int minimumThresholdForExercises) {
            this.minimumThresholdForExercises = minimumThresholdForExercises;
        }

        public int getNumberOfExercisesToRebuffer() {
            return numberOfExercisesToRebuffer;
        }

        public void setNumberOfExercisesToRebuffer(int numberOfExercisesToRebuffer) {
            this.numberOfExercisesToRebuffer = numberOfExercisesToRebuffer;
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
        private String questionFolderBaseName;
        private String xmlFileURLPrefix;
        private String tempFolderPath;
        private String log4jPropertiesPath;
        private String propertyFilePath;
        private String xslRenderXqPath;
        private String xslModifyPath;
        private String xercesJarPath;
        private String DDbeJarPath;
        private String xsdFileScoresPath;
        private String xmlUrlForLink;

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

        public String getQuestionFolderBaseName() {
            return questionFolderBaseName;
        }

        public void setQuestionFolderBaseName(String questionFolderBaseName) {
            this.questionFolderBaseName = questionFolderBaseName;
        }

        public String getXmlFileURLPrefix() {
            return xmlFileURLPrefix;
        }

        public void setXmlFileURLPrefix(String xmlFileURLPrefix) {
            this.xmlFileURLPrefix = xmlFileURLPrefix;
        }

        public String getTempFolderPath() {
            return tempFolderPath;
        }

        public void setTempFolderPath(String tempFolderPath) {
            this.tempFolderPath = tempFolderPath;
        }

        public String getLog4jPropertiesPath() {
            return log4jPropertiesPath;
        }

        public void setLog4jPropertiesPath(String log4jPropertiesPath) {
            this.log4jPropertiesPath = log4jPropertiesPath;
        }

        public String getPropertyFilePath() {
            return propertyFilePath;
        }

        public void setPropertyFilePath(String propertyFilePath) {
            this.propertyFilePath = propertyFilePath;
        }

        public String getXslRenderXqPath() {
            return xslRenderXqPath;
        }

        public void setXslRenderXqPath(String xslRenderXqPath) {
            this.xslRenderXqPath = xslRenderXqPath;
        }

        public String getXslModifyPath() {
            return xslModifyPath;
        }

        public void setXslModifyPath(String xslModifyPath) {
            this.xslModifyPath = xslModifyPath;
        }

        public String getXercesJarPath() {
            return xercesJarPath;
        }

        public void setXercesJarPath(String xercesJarPath) {
            this.xercesJarPath = xercesJarPath;
        }

        public String getDDbeJarPath() {
            return DDbeJarPath;
        }

        public void setDDbeJarPath(String DDbeJarPath) {
            this.DDbeJarPath = DDbeJarPath;
        }

        public String getXsdFileScoresPath() {
            return xsdFileScoresPath;
        }

        public void setXsdFileScoresPath(String xsdFileScoresPath) {
            this.xsdFileScoresPath = xsdFileScoresPath;
        }

        public String getXmlUrlForLink() {
            return xmlUrlForLink;
        }

        public void setXmlUrlForLink(String xmlUrlForLink) {
            this.xmlUrlForLink = xmlUrlForLink;
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
            private String taskGroup_fileIds_mapping;

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

            public String getTaskGroup_fileIds_mapping() {
                return taskGroup_fileIds_mapping;
            }

            public void setTaskGroup_fileIds_mapping(String taskGroup_fileIds_mapping) {
                this.taskGroup_fileIds_mapping = taskGroup_fileIds_mapping;
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
        private long sleepDuration;
        private long maxWaitTime;

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

        public long getSleepDuration() {
            return sleepDuration;
        }

        public void setSleepDuration(long sleepDuration) {
            this.sleepDuration = sleepDuration;
        }

        public long getMaxWaitTime() {
            return maxWaitTime;
        }

        public void setMaxWaitTime(long maxWaitTime) {
            this.maxWaitTime = maxWaitTime;
        }
    }


}
