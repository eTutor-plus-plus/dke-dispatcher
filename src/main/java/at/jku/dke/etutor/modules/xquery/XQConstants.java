package at.jku.dke.etutor.modules.xquery;

public class XQConstants {
	public final static String ATTR_COMMAND = "command";
    public final static String ATTR_QUERY = "DATALOG_QUERY";
    public final static String ATTR_QUERY_FILE = "DATALOG_QUERY_FILE";
    public final static String ATTR_EXERCISE_SPECIFICATION = "xqExerciseSpecification";
    public final static String ATTR_DIAGNOSE_LEVEL = "diagnoseLevel";
    public final static String ATTR_MESSAGES_ERROR = "errorMsg";
    public final static String COMMAND_EVALUATE = "evaluate";
    public final static String COMMAND_EVALUATE_SPEC = "evaluateSpec";
    public final static String COMMAND_LOAD_QUERY = "loadQuery";
    public final static String COMMAND_CANCEL = "cancel";
    public final static String COMMAND_RUN = "run";
    public final static String COMMAND_NEXT = "goNext";
    public final static String COMMAND_BACK = "goBack";
    public final static String COMMAND_REMOVE_SORTED_NODES = "removeSortedNodes";
    public final static String COMMAND_ADD_SORTED_NODES = "addSortedNode";
    public final static String COMMAND_REMOVE_URL = "removeUrl";
    public final static String COMMAND_ADD_URL = "addUrl";
    
    //user interface constants in correlation with core constants (CoreConstants)
    public static final String ATTR_USER_ID = "userID";
    public static final String ATTR_TASK_ID = "taskID";
    public static final String ATTR_EXERCISE_ID = "exerciseID";
    public static final String ATTR_REPORT = "report";
    public static final String ATTR_SUBMISSION_TO_VIEW = "submissionToView";
    public final static String ATTR_EXERCISE_SPEC_MODULE = "moduleExercise";
    public final static String ATTR_EXERCISE_SPEC_CMD = "coreExerciseMgrCmd";
    public final static String ATTR_EXERCISE_SPEC_STATUS = "coreExerciseMgrStatus";
    public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";
	public final static String EXERCISE_SPEC_CMD_BACK = "back";
	public final static String EXERCISE_SPEC_CMD_NEXT = "next";
	public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";
    public final static String ATTR_ACTION = "action";
    public static final String ATTR_ACTIONS = "actions";
    public final static String ACTION_SUBMIT = "submit";
    public final static String ACTION_DIAGNOSE = "diagnose";
    public final static String ACTION_RUN = "run";
    public final static String ACTION_CHECK = "check";
    public final static String ATTR_SUBMISSION = "submission";
	
    public static final String PARAM_COMMAND = "command";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_QUERY_FILE = "queryFile";
    public static final String PARAM_SORTED_NODE_TO_ADD = "sortedNodeToAdd";
    public static final String PARAM_SORTED_NODE_INDEX = "sortedNodeIndex";
    public static final String PARAM_HIDDEN_URL_TO_ADD = "hiddenUrlToAdd";
    public static final String PARAM_URL_TO_ADD = "urlToAdd";
    public static final String PARAM_URL_INDEX = "urlIndex";
    
    //urls
    public static final String FILE_UPLOAD_DIR = "/upload";
    public static final String SERVLET_COREMANAGER = "/core/CoreManager";
    public final static String SERVLET_EXERCISE_CORE = "/assistant/ExercisesManager";
    public final static String SERVLET_EXERCISE = "/modules/xquery/XQExerciseManager";
    public final static String SERVLET_EDITOR = "/modules/xquery/XQEditorServlet";
    public final static String PAGE_EXERCISE = "/modules/xquery/exerciseSetting.jsp";
    public final static String PAGE_EDITOR = "/modules/xquery/showEditor.jsp";
}
