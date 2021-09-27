package etutor.modules.xquery;

/**
 * An exception which is used to describe the syntax errors of a query. This class provides additional
 * methods for setting and getting the concrete syntax error message as it might be returned by the
 * query processor.
 *  
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class QuerySyntaxException extends Exception {
	private String description;
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
	public QuerySyntaxException() {
		super();
	}
    /**
     * Constructs a new exception with the specified detail message. 
     *
     * @param   message   the detail message. 
     */
	public QuerySyntaxException(String message) {
		super(message);
	}
    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause the cause.
     */
	public QuerySyntaxException(Throwable cause) {
		super(cause);
	}
    /**
     * Constructs a new exception with the specified detail message and
     * cause. <p>Note that the detail message associated with
     * <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message.
     * @param  cause the cause.
     */
	public QuerySyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

    /**
     * Specifies a message which can be treated separately from the basic message methods
     * of <code>Exception</code>. This is intended to be used for messages which are exclusively focussed on  
     * the syntax errors.
     * 
     * @param description The description of syntax errors.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a message which can be treated separately from the basic message methods
     * of <code>Exception</code>. This is intended to be used for messages which are exclusively focussed on 
     * the syntax errors.
     * 
     * @return The description of syntax errors.
     */
    public String getDescription() {
        return description;
    }

}