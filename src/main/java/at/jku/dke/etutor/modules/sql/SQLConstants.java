package at.jku.dke.etutor.modules.sql;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.springframework.stereotype.Service;

@Service
public class SQLConstants {

    // USER INTERFACE ETUTOR-CORE CONSTANTS
    public static final String ATTR_USER_ID = "userID"; // CoreConstants.ATTR_USER_ID

    public static final String ATTR_TASK_ID = "taskID"; // CoreConstants.ATTR_TASK_ID

    public static final String ATTR_EXERCISE_ID = "exerciseID"; // CoreConstants.ATTR_EXERCISE_ID

    public static final String ATTR_REPORT = "report"; // CoreConstants.ATTR_EVALUATION_REPORT;

    public static final String ATTR_ACTIONS = "actions"; // CoreConstants.ATTR_ACTIONS;

    public static final String ATTR_SUBMISSION_TO_VIEW = "submissionToView";

    public final static String ATTR_EXERCISE_SPEC_MODULE = "moduleExercise";

    public final static String ATTR_EXERCISE_SPEC_CMD = "coreExerciseMgrCmd";

    public final static String ATTR_EXERCISE_SPEC_STATUS = "coreExerciseMgrStatus";

    public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";

    public final static String EXERCISE_SPEC_CMD_BACK = "back";

    public final static String EXERCISE_SPEC_CMD_NEXT = "next";

    public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";

    public final static String ACTION_SUBMIT = "submit"; // CoreConstants.ACTION_SUBMIT

    public final static String ACTION_DIAGNOSE = "diagnose"; // CoreConstants.ACTION_DIAGNOSE

    public final static String ACTION_CHECK = "check"; // CoreConstants.ACTION_CHECK

    public final static String ACTION_RUN = "run"; // CoreConstants.ACTION_RUN

    public static final String UPLOAD_DIR = "/upload";

    public static final String SERVLET_COREMANAGER = "/core/CoreManager";

    public final static String SERVLET_EXERCISE_CORE = "/assistant/ExercisesManager";

    public final static String SERVLET_EXERCISE = "/modules/sql/SQLExercisesManager";

    public static final String PAGE_EXERCISE = "/modules/sql/exerciseSetting.jsp";

    public final static String PAGE_EDITOR = "/modules/sql/showEditor.jsp";

    public static final String PROPERTIES_PATH = "/etutor/resources/modules/sql/sql.properties";

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
