package at.jku.dke.etutor.modules.ra2sql;

import antlr.TokenStreamIOException;

public class AtomStreamIOException extends RAParserException {

	public AtomStreamIOException() {
		super();
	}
	
	public AtomStreamIOException(String rule, TokenStreamIOException tsioe){
		this();
	}
	
	public String getMessage(){
		return "An IO Exception at the atom stream occured!";
	}
}
