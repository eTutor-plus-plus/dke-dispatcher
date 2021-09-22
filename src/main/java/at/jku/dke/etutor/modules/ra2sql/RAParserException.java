package at.jku.dke.etutor.modules.ra2sql;

public class RAParserException extends Exception {

	private int line;
	private int column;
	private String rule;

	public RAParserException() {
		super();
		this.rule = new String();
	}

	public int getColumn() {
		return this.column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLine() {
		return this.line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getRule() {
		return this.rule;
	}

	public void setRule(String rule) {
		if (rule != null){
			this.rule = rule;
		}
	}

}
