package at.jku.dke.etutor.modules.sql.report;

import java.io.Serializable;

public class SQLErrorReport implements Serializable{

	private String hint;
	private String error;
	private String description;
	
	public SQLErrorReport() {
		super();

		this.hint = new String();
		this.error = new String();
		this.description = new String();
	}

	public String getDescription() {
		return this.description;
	}

	public String getError() {
		return this.error;
	}

	public String getHint() {
		return this.hint;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
