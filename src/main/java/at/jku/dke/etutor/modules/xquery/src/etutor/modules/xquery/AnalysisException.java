package etutor.modules.xquery;

/**
 * Exception used for indicating serious problem, which occurs during 
 * the analysis of some query.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class AnalysisException extends Exception {

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public AnalysisException() {
		super();
	}

    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
    public AnalysisException(String message) {
		super(message);
	}

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message.
     * @param  cause the cause.
     */
    public AnalysisException(String message, Throwable cause) {
		super(message, cause);
	}
}
