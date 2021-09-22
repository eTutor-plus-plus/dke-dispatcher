package at.jku.dke.etutor.modules.ra2sql.model;

public class Comparison {

	private String leftValue;
	private String rightValue;
	private ComparisonOperator operator;
	private ComparisonValueType leftValueType;
	private ComparisonValueType rightValueType;

	public Comparison() {
		super();
		this.leftValue = new String();
		this.rightValue = new String();
		this.operator = ComparisonOperator.EQUAL;
		this.rightValueType = ComparisonValueType.LITERAL;
		this.leftValueType = ComparisonValueType.LITERAL;
	}

	public String getLeftValue() {
		return this.leftValue;
	}

	public boolean setLeftValue(String leftValue) {
		if ((leftValue != null) && (leftValue.length() != 0)) {
			this.leftValue = leftValue;
			return true;
		} else {
			return false;
		}
	}

	public String getRightValue() {
		return this.rightValue;
	}

	public boolean setRightValue(String rightValue) {
		if ((rightValue != null) && (rightValue.length() != 0)) {
			this.rightValue = rightValue;
			return true;
		} else {
			return false;
		}
	}

	public ComparisonOperator getOperator() {
		return this.operator;
	}

	public boolean setOperator(ComparisonOperator operator) {
		if (operator != null) {
			this.operator = operator;
			return true;
		} else {
			return false;
		}
	}

	public ComparisonValueType getRightValueType() {
		return this.rightValueType;
	}

	public boolean setRightValueType(ComparisonValueType rightValueType) {
		if (rightValueType != null) {
			this.rightValueType = rightValueType;
			return true;
		} else {
			return false;
		}
	}

	public ComparisonValueType getLeftValueType() {
		return this.leftValueType;
	}

	public boolean setLeftValueType(ComparisonValueType leftValueType) {
		if (leftValueType != null) {
			this.leftValueType = leftValueType;
			return true;
		} else {
			return false;
		}
	}

}
