package at.jku.dke.etutor.modules.dlg.report;

import java.io.Serializable;

/**
 * An instance of this class can be used to represent an error as it was found 
 * when comparing one Datalog query result to another one. The categories are
 * predefined, but each instance can be set with a general title and a detailed
 * description.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class ErrorCategory implements Serializable {
	private String description;
	private String title;
	private String cssTitle;
	private int category;

	/**
	 * CSS class name of HTML elements which are used to mark predicates, with too many terms.
	 */
	public final static String P_ARITY_HIGH_CSS = "high-term-predicate";
	/**
	 * CSS class name of HTML elements which are used to mark predicates, with too few terms.
	 */
	public final static String P_ARITY_LOW_CSS = "low-term-predicate";
	/**
	 * CSS class name of HTML elements which are used to mark predicates, which are missing in a query result.
	 */
	public final static String P_MISSING_CSS = "missing-predicate";
	/**
	 * CSS class name of HTML elements which are used to mark predicates, which are missing in a query result.
	 */
	public final static String P_REDUNDANT_CSS = "redundant-predicate";
	/**
	 * CSS class name of HTML elements which are used to mark facts, which are missing in a query result.
	 */
	public final static String F_MISSING_CSS = "missing-fact";
	/**
	 * CSS class name of HTML elements which are used to mark facts, which are redundant in a query result.
	 */
	public final static String F_REDUNDANT_CSS = "redundant-fact";
	/**
	 * CSS class name of HTML elements which are used to mark facts, which are falsely <i>not</i> negated in a query result.
	 */
	public final static String F_POSITIVE_CSS = "positive-fact";
	/**
	 * CSS class name of HTML elements which are used to mark facts, which are falsely negated in a query result.
	 */
	public final static String F_NEGATIVE_CSS = "negative-fact";

	/**
	 * Defines an error category of predicates, having too many terms.
	 */
	public final static int P_ARITY_HIGH = 0;
	/**
	 * Defines an error category of predicates, having too few terms.
	 */
	public final static int P_ARITY_LOW = 1;
	/**
	 * Defines an error category of predicates, which are missing in a result.
	 */
	public final static int P_MISSING = 2;
	/**
	 * Defines an error category of predicates, which are redundant in a result.
	 */
	public final static int P_REDUNDANT = 3;
	/**
	 * Defines an error category of facts, which are missing in a result.
	 */
	public final static int F_MISSING = 4;
	/**
	 * Defines an error category of facts, which are redundant in a result.
	 */
	public final static int F_REDUNDANT = 5;
	/**
	 * Defines an error category of facts, which are falsely <i>not</i> negated in a result.
	 */
	public final static int F_POSITIVE = 6;
	/**
	 * Defines an error category of facts, which are falsely negated in a result.
	 */
	public final static int F_NEGATIVE = 7;
	
	/**
	 * Constructs a new error category object.
	 * 
	 * @param category Specifies the category, which must be one of the predefined flags.
	 * @param title A customized title which describes the category. 
	 * @throws IllegalArgumentException if the specified category is not one of the predefined flags.
	 */
	public ErrorCategory(int category, String title) throws IllegalArgumentException {
		switch(category) {
			case P_ARITY_HIGH: this.cssTitle = P_ARITY_HIGH_CSS; break;
			case P_ARITY_LOW: this.cssTitle = P_ARITY_LOW_CSS; break;
			case P_MISSING: this.cssTitle = P_MISSING_CSS; break;
			case P_REDUNDANT: this.cssTitle = P_REDUNDANT_CSS; break;
			case F_MISSING: this.cssTitle = F_MISSING_CSS; break;
			case F_REDUNDANT: this.cssTitle = F_REDUNDANT_CSS; break;
			case F_POSITIVE: this.cssTitle = F_POSITIVE_CSS; break;
			case F_NEGATIVE: this.cssTitle = F_NEGATIVE_CSS; break;
			default: throw new IllegalArgumentException();
 		}
		this.category = category;
		this.title = title;
		this.description = "";
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
     * Returns the CSS class name, which is assigned to the category 
     * of this <code>ErrorCategory</code> by default. 
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

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		ErrorCategory category = new ErrorCategory(this.category, this.title);
		category.setDescription(this.description);
		return category;
	}
}
