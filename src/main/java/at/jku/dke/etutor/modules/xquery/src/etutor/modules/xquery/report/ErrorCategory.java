package etutor.modules.xquery.report;

import java.io.Serializable;

/**
 * An instance of this class can be used to represent an error as it was found when comparing one
 * XQuery query result to another one. The categories are predefined, but each instance can be set
 * with a general title and a detailed description.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class ErrorCategory implements Serializable {

    private String description;

    private String title;

    private String cssTitle;

    private int category;

    /**
     * CSS class name of HTML elements which are used to mark missing XML nodes in a query result.
     */
    public final static String NODES_MISSING_CSS = "missing";

    /**
     * CSS class name of HTML elements which are used to mark displaced XML nodes in a query result.
     */
    public final static String NODES_DISPLACED_CSS = "displaced";

    /**
     * CSS class name of HTML elements which are used to mark redundant XML nodes in a query result.
     */
    public final static String NODES_REDUNDANT_CSS = "redundant";

    /**
     * CSS class name of HTML elements which are used to mark missing XML attributes in a query
     * result.
     */
    public final static String ATTRIBUTES_MISSING_CSS = "missing";

    /**
     * CSS class name of HTML elements which are used to mark redundant XML attributes in a query
     * result.
     */
    public final static String ATTRIBUTES_REDUNDANT_CSS = "redundant";

    /**
     * CSS class name of HTML elements which are used to mark XML attributes in a query result which
     * should have a different value.
     */
    public final static String VALUES_INCORRECT_CSS = "incorrect-value";

    /**
     * Denotes an error without a special category.
     */
    public final static int NO_CATEGORY = 0;

    /**
     * Defines an error category of missing nodes.
     */
    public final static int NODES_MISSING = 1;

    /**
     * Defines an error category of displaced nodes.
     */
    public final static int NODES_DISPLACED = 2;

    /**
     * Defines an error category of redundant nodes.
     */
    public final static int NODES_REDUNDANT = 3;

    /**
     * Defines an error category of missing attributes.
     */
    public final static int ATTRIBUTES_MISSING = 4;

    /**
     * Defines an error category of redundant attributes.
     */
    public final static int ATTRIBUTES_REDUNDANT = 5;

    /**
     * Defines an error category of attributes, which should have a different value.
     */
    public final static int VALUES_INCORRECT = 6;

    /**
     * Constructs a new error category object.
     * 
     * @param category Specifies the category, which must be one of the predefined flags.
     * @param title A customized title which describes the category.
     * @throws IllegalArgumentException if the specified category is not one of the predefined
     *             flags.
     */
    public ErrorCategory(
            int category, String title) throws IllegalArgumentException {
        switch (category) {
	        case NO_CATEGORY:
	            this.cssTitle = "";
	            break;
            case NODES_MISSING:
                this.cssTitle = NODES_MISSING_CSS;
                break;
            case NODES_DISPLACED:
                this.cssTitle = NODES_DISPLACED_CSS;
                break;
            case NODES_REDUNDANT:
                this.cssTitle = NODES_REDUNDANT_CSS;
                break;
            case ATTRIBUTES_MISSING:
                this.cssTitle = ATTRIBUTES_MISSING_CSS;
                break;
            case ATTRIBUTES_REDUNDANT:
                this.cssTitle = ATTRIBUTES_REDUNDANT_CSS;
                break;
            case VALUES_INCORRECT:
                this.cssTitle = VALUES_INCORRECT_CSS;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.category = category;
        this.title = title;
        this.description = "";
    }

    /**
     * Constructs a new error category object with the {@link #NO_CATEGORY} category.
     * 
     * @param title A customized title which describes the category.
     */
    public ErrorCategory(String title) {
    	this(NO_CATEGORY, title);
    }

    /**
     * Constructs a new error category object with the {@link #NO_CATEGORY} category
     * and a blank title.
     * 
     */
    public ErrorCategory() {
    	this(NO_CATEGORY, "");
    }
    
    /**
     * @param description The description to append.
     */
    public void appendDescription(String description) {
        this.description += description;
    }

    /**
     * Returns the set description of this error category.
     * 
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this error category.
     * 
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the title for this error category.
     * 
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the error category.
     * 
     * @return The category.
     */
    public int getCategory() {
        return category;
    }

    /**
     * Returns the CSS class name, which is assigned to the category of this
     * <code>ErrorCategory</code> by default.
     * 
     * @return Returns the CSS class name.
     */
    public String getCssTitle() {
        return cssTitle;
    }

    /**
     * Returns the set title of this error category.
     * 
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        ErrorCategory category = new ErrorCategory(this.category, this.title);
        category.setDescription(this.description);
        return category;
    }
}