package at.jku.dke.etutor.modules.ra2sql;

public class RAConstants {

	private RAConstants(){
		throw new IllegalStateException("Utility class");
	}

	public static final String CONN_PWD = "sql";
	public static final String CONN_USER = "sql";
	public static final String CONN_URL = "jdbc:postgresql://localhost:5433";
	public static final String JDBC_DRIVER = "org.postgresql.Driver";

	public static final String SQL_MSG_MAPPING_PATH = "main/resources/ra/sql_msg_mapping.properties";
	public static final String RULE_ALIASES_PATH =	"/resources/ra/rule_aliases.properties";
	public static final String ATOM_ALIASES_PATH =	"/resources/ra/atom_aliases.properties";
	
	//USER INTERFACE ETUTOR-CORE CONSTANTS
    public static final String ATTR_USER_ID = "userID";			//CoreConstants.ATTR_USER_ID
    public static final String ATTR_TASK_ID = "taskID";			//CoreConstants.ATTR_TASK_ID
    public static final String ATTR_EXERCISE_ID = "exerciseID";	//CoreConstants.ATTR_EXERCISE_ID
    public static final String ATTR_ACTIONS = "actions";		//CoreConstants.ATTR_ACTIONS;
    public static final String ATTR_SUBMISSION_TO_VIEW = "submissionToView";
    public final static String ATTR_EXERCISE_SPEC_MODULE = "moduleExercise";
    public final static String ATTR_EXERCISE_SPEC_CMD = "coreExerciseMgrCmd";
    public final static String ATTR_EXERCISE_SPEC_STATUS = "coreExerciseMgrStatus";
    public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";
	public final static String EXERCISE_SPEC_CMD_BACK = "back";
	public final static String EXERCISE_SPEC_CMD_NEXT = "next";
	public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";
    public static final String ACTION_RUN = "run";
	public static final String ACTION_CHECK = "check";
	public static final String ACTION_SUBMIT = "submit";	
	public static final String ACTION_DIAGNOSE = "diagnose";
    public static final String FILE_UPLOAD_DIR = "/upload";
	public static final String SERVLET_COREMANAGER = "/core/CoreManager";
	public final static String SERVLET_EXERCISE_CORE = "/assistant/ExercisesManager";
	
	public static final String SERVLET_EXERCISE = "/modules/ra2sql/RAExerciseManager";
	public static final String PAGE_EXERCISE = "/modules/ra2sql/exerciseSetting.jsp";
	public static final String PAGE_EDITOR = "/modules/ra2sql/showEditor.jsp";
}    
