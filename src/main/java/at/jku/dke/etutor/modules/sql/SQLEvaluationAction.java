package at.jku.dke.etutor.modules.sql;

public class SQLEvaluationAction {

	private String action;

	public static final SQLEvaluationAction RUN = new SQLEvaluationAction("RUN");
	public static final SQLEvaluationAction TEST = new SQLEvaluationAction("TEST");
	public static final SQLEvaluationAction CHECK = new SQLEvaluationAction("CHECK");
	public static final SQLEvaluationAction SUBMIT = new SQLEvaluationAction("SUBMIT");
	public static final SQLEvaluationAction DIAGNOSE = new SQLEvaluationAction("DIAGNOSE");

	protected SQLEvaluationAction(String action) {
		super();
		this.action = action;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof SQLEvaluationAction)) {
			return false;
		}

		return this.action.equals(obj.toString());
	}
	
	public int hashCode(){
		return -1;
	}

	public String toString() {
		return this.action;
	}

}
