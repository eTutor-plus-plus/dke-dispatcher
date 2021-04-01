package at.jku.dke.etutor.evaluation;

public class DefaultReport implements Report{

	private String hint;
	private String error;
	private String description;
	
	private boolean showHint;
	private boolean showErrorDescription;

	public DefaultReport() {
		super();
		this.hint = new String();
		this.error = new String();
		this.description = new String();

		this.showHint = false;
		this.showErrorDescription = false;
	}

	public boolean showErrorDescription() {
		return this.showErrorDescription;
	}

	public boolean showHint() {
		return this.showHint;
	}

	public void setShowErrorDescription(boolean showErrorDescription) {
		this.showErrorDescription = showErrorDescription;
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
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
