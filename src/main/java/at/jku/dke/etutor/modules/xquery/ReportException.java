package at.jku.dke.etutor.modules.xquery;

/**
 * Exception used for indicating some serious problem, which occurs
 * when generating the report for some submitted query.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class ReportException extends Exception {

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public ReportException() {
		super();
	}

    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
    public ReportException(String message) {
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
    public ReportException(String message, Throwable cause) {
		super(message, cause);
	}
}
