package at.jku.dke.etutor.modules.ra2sql.model;

public class ComparisonValueType {

	private String type;

	public static final ComparisonValueType DATE = new ComparisonValueType("DATE");
	public static final ComparisonValueType NUMBER = new ComparisonValueType("NUMBER");
	public static final ComparisonValueType LITERAL = new ComparisonValueType("LITERAL");
	public static final ComparisonValueType ATTRIBUTE = new ComparisonValueType("ATTRIBUTE");

	protected ComparisonValueType(String type) {
		super();
		this.type = type;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof ComparisonValueType) {
				return this.type.equals(obj.toString());
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String toString() {
		return this.type;
	}
}
