package at.jku.dke.etutor.modules.ra2sql;

public class InvalidAtomException extends RAParserException {

	private String invalidAtom;

	public InvalidAtomException() {
		super();
		this.invalidAtom = new String();
	}
	
	public InvalidAtomException(String rule, int line, int column, String invalidAtom) {
		this();
		this.setRule(rule);
		this.setLine(line);
		this.setColumn(column);
		this.setInvalidAtom(invalidAtom);
	}

	public String getInvalidAtom() {
		return this.invalidAtom;
	}

	public void setInvalidAtom(String invalidAtom) {
		if (invalidAtom != null){
			this.invalidAtom = invalidAtom.replaceAll("\"", "").replaceAll("'", "");
		} else {
			this.invalidAtom = "EOF";
		}
	}

	public String getMessage(){
		String message = new String();
		message = message.concat("Invalid atom \"" + this.getInvalidAtom() + "\"");
		message = message.concat(" in rule \"" + this.getRule() + "\"");
		message = message.concat(" at line " + this.getLine());
		message = message.concat(" column " + this.getColumn() + ".\n");
		return message;
	}

}
