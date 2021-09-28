package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery;

/**
 * This exception is thrown when certain parameters,   
 * which are needed for setting up a query exercise, are not applicable
 *  
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class ParameterException extends Exception {
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
	public ParameterException() {
		super();
	}
    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
	public ParameterException(String message) {
		super(message);
	}

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause the cause.
     */
	public ParameterException(Throwable cause) {
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
	public ParameterException(String message, Throwable cause) {
		super(message, cause);
	}

}
