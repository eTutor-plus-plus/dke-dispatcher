package at.jku.dke.etutor.modules.nf;


public class NFConstants {

	private NFConstants() {
		// This class is not meant to be instantiated. (Gerald Wimmer, 2023-12-01)
	}

	public static final String MESSAGE_SOURCE_PATH = "nf/messages";
	
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
	public final static String EXERCISE_SPEC_STATUS_MODULE_SETTINGS = "moduleSettings";
	public final static String EXERCISE_SPEC_CMD_BACK = "back";
	public final static String EXERCISE_SPEC_CMD_NEXT = "next";
	public final static String EXERCISE_SPEC_CMD_CANCEL = "cancel";

	// DIAGNOSE LEVELS
	public static final int LEVEL_NONE = 0;
	public static final int LEVEL_LOW = 1;
	public static final int LEVEL_MEDIUM = 2;
	public static final int LEVEL_HIGH = 3;

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
		SUBMIT, CHECK, DIAGNOSE,
		RUN // unused even in the old module
    }
}
