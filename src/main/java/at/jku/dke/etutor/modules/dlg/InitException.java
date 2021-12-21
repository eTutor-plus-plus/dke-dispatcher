package at.jku.dke.etutor.modules.dlg;

/**
 * This exception is thrown, when a query exercise can not be set up successfully.
 *
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class InitException extends Exception {
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public InitException() {
		super();
	}
    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
	public InitException(String message) {
		super(message);
	}
    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause the cause.
     */
	public InitException(Throwable cause) {
		super(cause);
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
	public InitException(String message, Throwable cause) {
		super(message, cause);
	}
}
