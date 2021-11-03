package at.jku.dke.etutor.modules.ra2sql;

import antlr.MismatchedCharException;
import antlr.MismatchedTokenException;

public class MismatchedAtomException extends RAParserException {

	private  String foundAtom;
	private  String expectedAtom;

	public MismatchedAtomException() {
		super();
		this.foundAtom = "";
		this.expectedAtom = "";
	}
	
	public MismatchedAtomException(String rule, MismatchedCharException mce) {
		this();
		this.setRule(rule);
		this.setLine(mce.line);
		this.setColumn(mce.column);
		this.setFoundAtom(Integer.toString(mce.foundChar));
		this.setExpectedAtom(Integer.toString(mce.expecting));
	}

	public MismatchedAtomException(String rule, MismatchedTokenException mte, String[] tokenNames) {
		this();
		this.setRule(rule);
		this.setLine(mte.line);
		this.setColumn(mte.column);
		this.setFoundAtom(mte.token.getText());
		this.setExpectedAtom(tokenNames[mte.expecting]);
	}

	public String getExpectedAtom() {
		return this.expectedAtom;
	}

	public void setExpectedAtom(String expectedAtom) {
		if (expectedAtom != null){
			this.expectedAtom = expectedAtom.replace("\"", "").replace("'", "");
		} else {
			this.expectedAtom = "EOF";
		}
	}

	public String getFoundAtom() {
		return this.foundAtom;
	}

	public void setFoundAtom(String foundAtom) {
		if (foundAtom != null){
			this.foundAtom = foundAtom.replace("\"", "").replace("'", "");
		} else {
			this.foundAtom = "EOF";
		}
	}

	@Override
	public String getMessage(){
		String message = "";
		message = message.concat("Atom mismatch in rule \"" + this.getRule() + "\"");
		message = message.concat(" at line " + this.getLine());
		message = message.concat(" column " + this.getColumn() + ".\n");
		message = message.concat("Expected atom: " + "\"" + this.getExpectedAtom() + "\"\n");
		message = message.concat("Found atom   : " + "\"" + this.getFoundAtom() + "\"");
		return message;
	}
}
