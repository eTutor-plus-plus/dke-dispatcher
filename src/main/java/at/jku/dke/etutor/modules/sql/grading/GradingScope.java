package at.jku.dke.etutor.modules.sql.grading;

public class GradingScope {

	private String scope;

	public static final GradingScope EXAMPLE = new GradingScope("EXAMPLE");
	public static final GradingScope CRITERION = new GradingScope("CRITERIOIN");

	protected GradingScope(String scope) {
		super();
		this.scope = scope;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
		if (!(obj instanceof GradingScope)) {
			return false;
		}

		return this.scope.equals(obj.toString());
	}
	
	public int hashCode(){
		return -1;
	}

	public String toString() {
		return this.scope;
	}
}
