package at.jku.dke.etutor.modules.sql;

//note: start by defining evaluation criterion
// note: question: difference in those eval criteria

public class SQLEvaluationCriterion {

	private String name;

	public static final SQLEvaluationCriterion CORRECT_TUPLES = new SQLEvaluationCriterion("CORRECT TUPLES");
	public static final SQLEvaluationCriterion CORRECT_COLUMNS = new SQLEvaluationCriterion("CORRECT COLUMNS");
	public static final SQLEvaluationCriterion CORRECT_SYNTAX = new SQLEvaluationCriterion("CORRECT SYNTAX");
	public static final SQLEvaluationCriterion CORRECT_ORDER = new SQLEvaluationCriterion("CORRECT ORDER");
	public static final SQLEvaluationCriterion CARTESIAN_PRODUCT = new SQLEvaluationCriterion("CARTESIAN_PRODUCT");

	protected SQLEvaluationCriterion(String name) {
		super();
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof SQLEvaluationCriterion)) {
			return false;
		}

		return this.name.equals(obj.toString());
	}
	
	public int hashCode(){
		return -1;
	}

	public String toString() {
		return this.name;
	}
}
