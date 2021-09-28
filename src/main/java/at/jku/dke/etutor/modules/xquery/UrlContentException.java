package at.jku.dke.etutor.modules.xquery;

import java.util.ArrayList;



/**
 * This exception indicates an error, that was detected when mapping from XQuery documents, given as
 * URLs, to alias names for these URLs and vice versa. <br>
 * Usually, an URL can be accessed in an XQuery query using the <code>doc</code> function. If
 * mapping is activated, the content of a <code>doc</code> function will either be interpreted as
 * URL and replaced by an alias name for this URL, or the other way around, interpreted as alias for
 * an URL and replaced by it. <br>
 * An error might occur if there is no alias name specified for an URL, or there is no URL specified
 * for an alias name.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class UrlContentException extends Exception {

    private UrlContent[] aliasList;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public UrlContentException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message.
     */
    public UrlContentException(
            String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause the cause.
     */
    public UrlContentException(
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
    public UrlContentException(
            String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified detail message, additionally adding
     * information for this exception. The <code>ArrayList</code> will be iterated, searching
     * for <code>UrlContent</code> objects, which are added to this <code>UrlContentException</code>
	 * for later retrieval.
     * 
     * @param message the detail message.
     * @param urlContents List of <code>UrlContent</code> objects.
     */
    public UrlContentException(
            String message, ArrayList urlContents) {
        super(message);
        addUrlContents(urlContents);
        
    }

    /**
	 * Adds information about the content of a <code>doc</code> function to this <code>UrlContentException</code>.
	 *
     * @param urlContent The object holding information about the content of a <code>doc</code> function which
	 * lead to this Exception.
     */
    public void addUrlContent(UrlContent urlContent) {
        ArrayList list = new ArrayList();
        list.add(urlContent);
        addUrlContents(list);
    }
    /**
	 * Adds information about the contents of some <code>doc</code> functions to this <code>UrlContentException</code>.
     * @param urlContents The objects holding information about the contents of <code>doc</code> functions which
	 * lead to this Exception.
     */
    public void addUrlContents(ArrayList urlContents) {
        ArrayList list = new ArrayList();
        if (this.aliasList != null) {
            for (int i = 0; i < this.aliasList.length; i++) {
                list.add(aliasList[i]);
            }
        }
        for (int i = 0; i < urlContents.size(); i++) {
            if (urlContents.get(i) instanceof UrlContent) {
                list.add(urlContents.get(i));
            }
        }
        this.aliasList = (UrlContent[])list.toArray(new UrlContent[]{});
    }
    /**
	 * Gets information about contents of <code>doc</code> functions within an XQuery query, which lead to
	 * this Exception.
	 * 
     * @return The url content objects.
     */
    public UrlContent[] getUrlContents() {
        if (this.aliasList == null) {
            this.aliasList = new UrlContent[]{};
        }
        return this.aliasList;
    }
}
