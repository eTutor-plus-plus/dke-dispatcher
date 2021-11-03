package at.jku.dke.etutor.modules.xquery;

/**
 * This exception is thrown whenever the result of an XQuery query is required to be processed as an
 * XML document but can not be interpreted as wellformed XML. First of all, a result must be
 * supplemented with a root element anyway, in order to make it comparable with another result.
 * Nevertheless, problems might occur if for example there are two equally named attributes in an
 * element, like the following example shows:
 * 
 * <pre>
 *  &lt;element name='one attribute' name='the other attribute'/&gt;
 * </pre>
 * 
 * Although this might be the valid result of a query, it is not applicable for comparing it with
 * another result and analyzing the differences.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class WellformednessException extends Exception {

    private String description;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public WellformednessException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     */
    public WellformednessException(
            String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause the cause.
     */
    public WellformednessException(
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
    public WellformednessException(
            String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Specifies a message which can be treated separately from the basic message methods of
     * <code>Exception</code>. This is intended to be used for mere messages concerning the
     * syntax errors.
     * 
     * @param description The description of syntax errors.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a message which can be treated separately from the basic message methods of
     * <code>Exception</code>. This is intended to be used for mere messages concerning the
     * syntax errors.
     * 
     * @return The description of syntax errors.
     */
    public String getDescription() {
        return description;
    }

}