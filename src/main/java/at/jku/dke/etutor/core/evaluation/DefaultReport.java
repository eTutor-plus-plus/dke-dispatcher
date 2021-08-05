package at.jku.dke.etutor.core.evaluation;

/**
 * Class implementing the Report interface that can be extended by the module´s
 */
public class DefaultReport implements Report{
	/**
	 * The hint
	 */
	private String hint;
	/**
	 * The error message
	 */
	private String error;
	/**
	 * The description
	 */
	private String description;
	
	private boolean showHint;
	private boolean showErrorDescription;

	/**
	 * Constructor
	 */
	public DefaultReport() {
		super();
		this.hint = "";
		this.error = "";
		this.description = "";

		this.showHint = false;
		this.showErrorDescription = false;
	}

	/**
	 * Returns showErrorDescription
	 * @return the boolean value
	 */
	public boolean showErrorDescription() {
		return this.showErrorDescription;
	}

	/**
	 * Returns showHint
	 * @return the boolean value
	 */
	public boolean showHint() {
		return this.showHint;
	}

	/**
	 * Sets showErrorDescription
	 * @param showErrorDescription the boolean
	 */
	public void setShowErrorDescription(boolean showErrorDescription) {
		this.showErrorDescription = showErrorDescription;
	}

	/**
	 * Sets showHint
	 * @param showHint the boolean
	 */
	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
	}

	/**
	 * Returns the description
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * Returns the error
	 * @return the error
	 */
	public String getError() {
		return this.error;
	}

	/**
	 * Return the hint
	 * @return the hint
	 */
	public String getHint() {
		return this.hint;
	}

	/**
	 * Sets the description
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the error
	 * @param error the error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Sets the hint
	 * @param hint the hint
	 */
	public void setHint(String hint) {
		this.hint = hint;
	}
}
