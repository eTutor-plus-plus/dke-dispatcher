package at.jku.dke.etutor.modules.ra2sql.model;

import java.util.Objects;

public class ComparisonOperator {

	public static final ComparisonOperator EQUAL = new ComparisonOperator("EQUAL");
	public static final ComparisonOperator NOT_EQUAL = new ComparisonOperator("NOT_EQUAL");
	public static final ComparisonOperator LESS_THAN = new ComparisonOperator("LESS_THAN");
	public static final ComparisonOperator GREATER_THAN = new ComparisonOperator("GREATER_THAN");
	public static final ComparisonOperator LESS_OR_EQUAL = new ComparisonOperator("LT_OR_EQUAL");
	public static final ComparisonOperator GREATER_OR_EQUAL = new ComparisonOperator("GT_OR_EQUAL");

	private String name;

	protected ComparisonOperator(String name) {
		super();
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof ComparisonOperator compOp) {
				return this.name.equals((compOp).getName());
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		String toString = "";

		if (this.name.equals("EQUAL")) {
			toString = "=";
		}
		if (this.name.equals("NOT_EQUAL")) {
			toString = "<>";
		}
		if (this.name.equals("LESS_THAN")) {
			toString = "<";
		}
		if (this.name.equals("GREATER_THAN")) {
			toString = ">";
		}
		if (this.name.equals("GT_OR_EQUAL")) {
			toString = ">=";
		}
		if (this.name.equals("LT_OR_EQUAL")) {
			toString = "<=";
		}

		return toString;
	}
}
