package at.jku.dke.etutor.modules.ra2sql;

import antlr.NoViableAltException;
import antlr.NoViableAltForCharException;

public class UnexpectedAtomException extends RAParserException {

	private String unexpectedAtom;

	public UnexpectedAtomException() {
		super();
		this.unexpectedAtom = new String();
	}
	
	public UnexpectedAtomException(String rule, NoViableAltException nvae) {
		this();
		this.setRule(rule);
		this.setLine(nvae.getLine());
		this.setColumn(nvae.getColumn());
		this.setUnexpectedAtom(nvae.token.getText());
	}

	public UnexpectedAtomException(String rule, NoViableAltForCharException nvae) {
		this();
		this.setRule(rule);
		this.setLine(nvae.getLine());
		this.setColumn(nvae.getColumn());
		this.setUnexpectedAtom(String.valueOf(nvae.foundChar));
	}

	public String getUnexpectedAtom() {
		return this.unexpectedAtom;
	}

	public void setUnexpectedAtom(String unexpectedAtom) {
		if (unexpectedAtom != null){
			this.unexpectedAtom = unexpectedAtom.replaceAll("\"", "").replaceAll("'", "");
		} else {
			this.unexpectedAtom = "EOF";
		}
	}

	public String getMessage(){
		String message = new String();
		message = message.concat("Unexpected atom \"" + this.getUnexpectedAtom() + "\"");
		message = message.concat(" in rule \"" + this.getRule() + "\"");
		message = message.concat(" at line " + this.getLine());
		message = message.concat(" column " + this.getColumn() + ".\n");
		return message;
	}
}
