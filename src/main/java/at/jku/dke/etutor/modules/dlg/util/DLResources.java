package at.jku.dke.etutor.modules.dlg.util;

import at.jku.dke.etutor.modules.dlg.InvalidResourceException;

/**
 * Provides access to UI messages
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class DLResources {
	public final static String PROPERTIES_FILE = "/dlg/properties/DLResources.properties";


	public static final String ERROR_RESULT_SYNTAX = "error.result.syntax";
	public static final String ERROR_TIMEOUT = "error.timeout";	

	public static final String SOLUTION_CORRECT = "solution.correct";
	public static final String SOLUTION_INCORRECT = "solution.incorrect";
	
	public static final String GRADING_POINTS = "grading.points";


	public static final String PREDICATES_MISSING = "predicates.missing";
	public static final String PREDICATES_MISSING_TITLE = "predicates.missing.title";
	public static final String PREDICATES_MISSING_SI = "predicates.missing.si";
	public static final String PREDICATES_MISSING_PL = "predicates.missing.pl";


	public static final String FACTS_MISSING = "facts.missing";
	public static final String FACTS_MISSING_TITLE = "facts.missing.title";
	public static final String FACTS_MISSING_SI = "facts.missing.si";
	public static final String FACTS_MISSING_PL = "facts.missing.pl";

	public static final String FACTS_REDUNDANT = "facts.redundant";
	public static final String FACTS_REDUNDANT_TITLE = "facts.redundant.title";
	public static final String FACTS_REDUNDANT_SI = "facts.redundant.si";
	public static final String FACTS_REDUNDANT_PL = "facts.redundant.pl";

	private static PropertyFile propertyFile;

	static {
		try {
			propertyFile = new PropertyFile(DLResources.class, PROPERTIES_FILE);
		} catch (InvalidResourceException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the message defined in a property.
	 * 
	 * @param key One of the constants of this class.
	 * @return The message found.
	 */
	public static String getString(String key)  {
		try {
			return DLResources.propertyFile.loadProperty(key);
		} catch (InvalidResourceException e) {
			e.printStackTrace();
		}
		return "";
	}
}