package at.jku.dke.etutor.modules.nf;


public class RDBDConstants {

	private RDBDConstants() {
		// This class is not meant to be instantiated. (Gerald Wimmer, 2023-12-01)
	}
	
    //USER INTERFACE ETUTOR-CORE CONSTANTS
    public static final String ATT_USER_ID = "userID";			//CoreConstants.ATTR_USER_ID
    public static final String ATT_TASK_ID = "taskID";			//CoreConstants.ATTR_TASK_ID
    public static final String ATT_EXERCISE_ID = "exerciseID";	//CoreConstants.ATTR_EXERCISE_ID
    public static final String ATT_ACTIONS = "actions";			//CoreConstants.ATTR_ACTIONS;
    public static final String ATT_SUBMISSION_TO_VIEW = "submissionToView";
	public static final String ATT_EXERCISE_FOR_SUBMISSION = "EXERCISE_FOR_SUBMISSION";
    public final static String ATTR_EXERCISE_SPEC_MODULE = "moduleExercise";
    public final static String ATTR_EXERCISE_SPEC_CMD = "coreExerciseMgrCmd";
    public final static String ATTR_EXERCISE_SPEC_STATUS = "coreExerciseMgrStatus";
	public static final String EVAL_ACTION_RUN = "run";
	public static final String EVAL_ACTION_CHECK = "check";
	public static final String EVAL_ACTION_SUBMIT = "submit";	
	public static final String EVAL_ACTION_DIAGNOSE = "diagnose";
	public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";
	public final static String EXERCISE_SPEC_CMD_BACK = "back";
	public final static String EXERCISE_SPEC_CMD_NEXT = "next";
	public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";

	// DIAGNOSE LEVELS
	public static final int LEVEL_NONE = 0;
	public static final int LEVEL_LOW = 1;
	public static final int LEVEL_MEDIUM = 2;
	public static final int LEVEL_HIGH = 3;
	
	//USER INTERFACE RDBD-CONSTANTS
	public static final String PREFIX_SUBMISSION_ATT = "RDBD_SUBMISSION_";
	public static final String PREFIX_SPECIFICATION_ATT = "RDBD_SPECIFICATION_";

	//EDITOR URLs
	public static final String PAGE_EDITOR_RBR = "/modules/rdbd/reductionByResolution/showEditor.jsp";
	public static final String PAGE_EDITOR_DECOMPOSE = "/modules/rdbd/decompose/showEditor.jsp";
	public static final String PAGE_EDITOR_MINIMAL_COVER = "/modules/rdbd/minimalCover/showEditor.jsp";
	public static final String PAGE_EDITOR_NORMALIZATION = "/modules/rdbd/normalization/showEditor.jsp";
	public static final String PAGE_EDITOR_ATTRIBUTE_CLOSURE = "/modules/rdbd/attributeClosure/showEditor.jsp";
	public static final String PAGE_EDITOR_KEYS_DETERMINATION = "/modules/rdbd/keysDetermination/showEditor.jsp";
	public static final String PAGE_EDITOR_NORMALFORM_DETERMINATION = "/modules/rdbd/normalform/showEditor.jsp";
		
	//EXERCISE MANAGER URLs
	public static final String PAGE_EXERCISE_RBR = "/modules/rdbd/reductionByResolution/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_DECOMPOSE = "/modules/rdbd/decompose/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_MINIMAL_COVER = "/modules/rdbd/minimalCover/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_NORMALIZATION = "/modules/rdbd/normalization/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_ATTRIBUTE_CLOSURE = "/modules/rdbd/attributeClosure/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_KEYS_DETERMINATION = "/modules/rdbd/keysDetermination/specifyExercise.jsp";
	public static final String PAGE_EXERCISE_NORMALFORM_DETERMINATION = "/modules/rdbd/normalform/specifyExercise.jsp";

	//SERVLET URLs
	public static final String SERVLET_COREMANAGER = "/core/CoreManager";
	public final static String SERVLET_EXERCISE_CORE = "/assistant/ExercisesManager";
	public static final String SERVLET_EXERCISE = "/modules/rdbd/RDBDExerciseManager";
	
	//COMMANDS
	public static final String CMD_SHOW_EDITOR = "SHOW_EDITOR";
	public static final String CMD_EVALUATE = "evaluate";
	public static final String CMD_BACK = "back";
	public static final String CMD_NEXT = "next";
	public static final String CMD_CANCEL = "cancel";
	public static final String CMD_PARSE_SPEC = "parseSpec";
	public static final String CMD_RESET_SPEC = "resetSpec";
	public static final String CMD_APPLY_NORMALFORM = "applyNormalform";

	public static final String CMD_NEW_REL = "NEW_RELATION";
	public static final String CMD_SPLIT_REL = "SPLIT_RELATION";
	public static final String CMD_DEL_SUB_RELATIONS = "DELETE_SUB_RELATIONS";

	public static final String CMD_ADD_KEY = "ADD_KEY";
	public static final String CMD_ADD_ATT = "ADD_ATTRIBUTES";
	public static final String CMD_ADD_DEP = "ADD_DEPENDENCY";
	public static final String CMD_ADD_BAS = "ADD_BASE_ATTRIBUTES";

	public static final String CMD_DEL_KEY = "DELETE_KEY";
	public static final String CMD_DEL_REL = "DELETE_RELATION";
	public static final String CMD_DEL_ATT = "DELETE_ATTRIBUTES";
	public static final String CMD_DEL_DEP = "DELETE_DEPENDENCIES";
	public static final String CMD_DEL_BAS = "DELETE_BASE_ATTRIBUTES";

	//PARAMETERS
	public static final String PARAM_ACTION = "action";
	public static final String PARAM_REPORT = "report";
	public static final String PARAM_COMMAND = "command";
	public static final String PARAM_LEVEL = "diagnoseLevel";
	public static final String PARAM_RDBD_TYPE = "rdbdType";
	public static final String PARAM_REL_ID = "relationID";
	public static final String PARAM_SUB_RELATION_ATTRIBUTE = "subRelationAttribute";
	public static final String PARAM_SUB_RELATION_1_ATTRIBUTE = "subRelation1Attribute";
	public static final String PARAM_SUB_RELATION_2_ATTRIBUTE = "subRelation2Attribute";
	public static final String PARAM_NORMALFORM_LEVEL = "normalform_level";
	public static final String PARAM_MAX_LOST_DEPENDENCIES = "maxLostDependencies";
	public static final String PARAM_DIAGNOSE_RELATION = "diagnoseRelation";
	public static final String PARAM_SPEC_TXT = "specTxt";
	
	//TASK SPECIFIC USER INTERFACE RDBD-CONSTANTS
	public static String calcSubmissionIDFor(int exerciseId){
		return PREFIX_SUBMISSION_ATT + exerciseId;
	}
	public static String calcSpecificationIDFor(int exerciseId){
		return PREFIX_SPECIFICATION_ATT + exerciseId;
	}

	public enum Type {
		KEYS_DETERMINATION,			// 0
		NORMALIZATION,				// 1
		MINIMAL_COVER,				// 2
		ATTRIBUTE_CLOSURE,			// 3
		NORMALFORM_DETERMINATION,	// 4
		RBR,						// 5
		DECOMPOSE					// 6
	}

	public enum EvalAction {
		SUBMIT, CHECK, DIAGNOSE
    }
}
