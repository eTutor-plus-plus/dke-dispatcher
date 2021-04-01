package at.jku.dke.etutor.modules.sql.analysis;

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
