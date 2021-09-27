package etutor.modules.xquery;


/**
 * Serves as exception which denotes problems with resources which can not be
 * found or are not applicable in the context they are required.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class InvalidResourceException extends /*Runtime*/Exception {
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
	public InvalidResourceException() {
		super();
	}
    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
	public InvalidResourceException(String message) {
		super(message);
	}
    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause the cause.
     */
	public InvalidResourceException(Throwable cause) {
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
	public InvalidResourceException(String message, Throwable cause) {
		super(message, cause);
	}
}
