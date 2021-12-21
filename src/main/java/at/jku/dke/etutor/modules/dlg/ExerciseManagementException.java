package at.jku.dke.etutor.modules.dlg;

/**
 * Exception that indicates errors thrown while creating, updating or deleting datalog exercises and task groups.
 */
public class ExerciseManagementException extends Exception{
    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public ExerciseManagementException() {
        super();
    }
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param   message   the detail message.
     */
    public ExerciseManagementException(String message) {
        super(message);
    }
    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause the cause.
     */
    public ExerciseManagementException(Throwable cause) {
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
    public ExerciseManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
