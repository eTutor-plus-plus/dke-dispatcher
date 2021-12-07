package at.jku.dke.etutor.modules.dlg;


public class DatalogConstants {
    public final static String ATTR_COMMAND = "command";
    public final static String ATTR_QUERY = "DATALOG_QUERY";
    public final static String ATTR_QUERY_FILE = "DATALOG_QUERY_FILE";
    public final static String ATTR_ACTION = "action";
    public final static String ATTR_SUBMISSION = "submission";
    public final static String ATTR_DIAGNOSE_LEVEL = "diagnoseLevel";
    public final static String ATTR_EXERCISE_SPECIFICATION = "datalogExerciseSpecification";
    public final static String ATTR_FACTS = "facts";
    public final static String ATTR_MESSAGES_ERROR = "errorMsg";
    
    public final static String COMMAND_EVALUATE_SPEC = "evaluateSpec";
    public final static String COMMAND_EVALUATE = "evaluate";
    public final static String COMMAND_LOAD_QUERY = "loadQuery";
    public final static String COMMAND_CANCEL = "cancel";
    public final static String COMMAND_SHOW_FACTS = "showFacts";
    public final static String COMMAND_RUN = "run";
    public final static String COMMAND_NEXT = "goNext";
    public final static String COMMAND_BACK = "goBack";
    public final static String COMMAND_REMOVE_PREDICATE = "removePredicate";
    public final static String COMMAND_ADD_PREDICATE = "addPredicate";
    public final static String COMMAND_REMOVE_TERM = "removeTerm";
    public final static String COMMAND_ADD_TERM = "addTerm";
    
	//user interface constants in correlation with core constants (CoreConstants)
    public static final String ATTR_USER_ID = "userID";
    public static final String ATTR_TASK_ID = "taskID";
    public static final String ATTR_REPORT = "report";
    public static final String ATTR_SUBMISSION_TO_VIEW = "submissionToView";
    public static final String ATTR_ACTIONS = "actions";
    public final static String ATTR_EXERCISE_SPEC_MODULE = "moduleExercise";
    public final static String ATTR_EXERCISE_SPEC_CMD = "coreExerciseMgrCmd";
    public final static String ATTR_EXERCISE_SPEC_STATUS = "coreExerciseMgrStatus";
    public final static String ACTION_SUBMIT = "submit";
    public final static String ACTION_DIAGNOSE = "diagnose";
    public final static String ACTION_RUN = "run";
    public final static String ACTION_CHECK = "check";
	public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";
	public final static String EXERCISE_SPEC_CMD_BACK = "back";
	public final static String EXERCISE_SPEC_CMD_NEXT = "next";
	public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";
	
    public static final String PARAM_COMMAND = "command";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_QUERY_FILE = "queryFile";
    public static final String PARAM_FACTS_ID = "factsId";
    public static final String PARAM_PREDICATE_TO_ADD = "predicateToAdd";
    public static final String PARAM_PREDICATE_INDEX = "predicateIndex";
    public static final String PARAM_TERM_TO_ADD_PREDICATE = "termPredicateToAdd";
    public static final String PARAM_TERM_TO_ADD_POSITION = "termPositionToAdd";
    public static final String PARAM_TERM_TO_ADD_VALUE = "termValueToAdd";
    public static final String PARAM_TERM_INDEX = "termIndex";
    
    //urls
    public static final String FILE_UPLOAD_DIR = "/upload";
    public static final String SERVLET_COREMANAGER = "/core/CoreManager";
    public final static String SERVLET_EXERCISE_CORE = "/assistant/ExercisesManager";
    public final static String SERVLET_FACTS = "/modules/datalog/DatalogFactsServlet";
    public final static String SERVLET_EXERCISE = "/modules/datalog/DatalogExerciseManager";
    public final static String SERVLET_EDITOR = "/modules/datalog/DatalogEditorServlet";
    public final static String PAGE_EXERCISE = "/modules/datalog/exerciseSetting.jsp";
    public final static String PAGE_EDITOR = "/modules/datalog/showEditor.jsp";
    public final static String PAGE_FACTS = "/modules/datalog/showFacts.jsp";
}
