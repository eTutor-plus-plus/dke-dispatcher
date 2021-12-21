package at.jku.dke.etutor.modules.dlg;

/**
 * A <code>TimeoutException</code> is thrown when processing of a query is stopped after a defined
 * time limit has been reached.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class TimeoutException extends Exception {

    private long timeout;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public TimeoutException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     */
    public TimeoutException(
            String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     * @param timeout the time limit set for processing of queries.
     */
    public TimeoutException(
            String message, long timeout) {
        super(message);
        setTimeout(timeout);
    }

    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause the cause.
     */
    public TimeoutException(
            Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with <code>cause</code> is <i>not </i>
     * automatically incorporated in this exception's detail message.
     * 
     * @param message the detail message.
     * @param cause the cause.
     */
    public TimeoutException(
            String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Sets the information about the timeout.
     * 
     * @param timeout the timeout in milliseconds.
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets the information about the timeout.
     * 
     * @return timeout the timeout in milliseconds.
     */
    public long getTimeout() {
        return timeout;
    }

}