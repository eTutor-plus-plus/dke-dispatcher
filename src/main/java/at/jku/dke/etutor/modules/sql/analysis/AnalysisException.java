package at.jku.dke.etutor.modules.sql.analysis;

/**
 * Represents an exception that gets thrown during the analysis
 */
public class AnalysisException extends Exception {

	public AnalysisException() {
		super();
	}

	public AnalysisException(String message) {
		super(message);
	}

	public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}
}
