package etutor.modules.xquery;

/**
 * This exception denotes that an XML document is not valid with regard to a certain document
 * definition, given by a DTD or XML Schema.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class ValidationException extends Exception {

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     */
    public ValidationException(
            String message) {
        super(message);

    }

    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause the cause.
     */
    public ValidationException(Throwable cause) {
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
    public ValidationException(
            String message, Throwable cause) {
        super(message, cause);

    }
}