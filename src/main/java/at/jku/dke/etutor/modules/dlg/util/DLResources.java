package at.jku.dke.etutor.modules.dlg.util;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Provides keys for message properties in a resource bundle named after this class.
 * The property file is expected to be located in the same package as this class.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class DLResources {
    
    /*
    public static final String PROPERTIES = "msg/XQResources";
    */

	//------------------- Exception messages -------------------------//
	/*
	public static final String EXCEPTION_INIT_ID = "exception.init.id";
	public static final String EXCEPTION_INIT_FILE = "exception.init.file";
	public static final String EXCEPTION_INIT_PROCESSOR = "exception.init.processor";
	public static final String EXCEPTION_INIT_MISS = "exception.init.miss";
	
	public static final String EXCEPTION_EXECUTION_TIMEOUT = "exception.execution.timeout";
	
	public static final String EXCEPTION_PROPERTIES_FILE = "exception.properties.file";
	public static final String EXCEPTION_PROPERTIES_LOAD = "exception.properties.load";	
	public static final String EXCEPTION_PROPERTIES_PROPERTY = "exception.properties.property";
	public static final String EXCEPTION_PROPERTIES_VALUE = "exception.properties.value";
	public static final String EXCEPTION_PROPERTIES_VALUE_FILE = "exception.properties.value.file";
	
	public static final String EXCEPTION_RESULT_SYNTAX = "exception.result.syntax";
	
	public static final String EXCEPTION_PARAMETER_VALIDATION = "exception.parameter.validation";
	public static final String EXCEPTION_PARAMETER_SCORE_MISSING = "exception.parameter.score.missing";
	public static final String EXCEPTION_PARAMETER_SCORE_NUMBER = "exception.parameter.score.number";
	public static final String EXCEPTION_PARAMETER_SCORE_CATEGORY = "exception.parameter.score.category";
	public static final String EXCEPTION_PARAMETER_SCORE_LEVEL = "exception.parameter.score.level";
	
	public static final String EXCEPTION_INTERNAL_DEBUG = "exception.internal.debug";
	public static final String EXCEPTION_INTERNAL_CSS = "exception.internal.css";
	public static final String EXCEPTION_INTERNAL_XSL = "exception.internal.xsl";
	public static final String EXCEPTION_INTERNAL_XSD = "exception.internal.xsd";
	public static final String EXCEPTION_INTERNAL_XML_STRING = "exception.internal.xml.string";
	
	public static final String EXCEPTION_RESOURCE_NOT_FOUND = "exception.resource.not_found";
	
	public static final String EXCEPTION_DB_DRIVER = "exception.db.driver";
	public static final String EXCEPTION_DB_CONNECTION = "exception.db.connection";
	public static final String EXCEPTION_DB_CONNECTION_CLOSE = "exception.db.connection.close";
	public static final String EXCEPTION_DB_EXERCISE = "exception.db.exercise";
	public static final String EXCEPTION_DB_GRADING = "exception.db.grading";
	public static final String EXCEPTION_DB_PREDICATES = "exception.db.predicates";
	
	public static final String EXCEPTION_ATTRIBUTE = "exception.attribute";
	public static final String EXCEPTION_ATTRIBUTE_ACTION = "exception.attribute.action";
	public static final String EXCEPTION_ANALYSIS = "exception.analysis";
	public static final String EXCEPTION_REPORT = "exception.report";
	public static final String EXCEPTION_DIAGNOSE_LEVEL = "exception.diagnose.level";	
	
	public static final String EXCEPTION_PREDICATE = "exception.predicate";
	public static final String EXCEPTION_PREDICATE_NOT_FOUND = "exception.predicate.not_found";
	
	public static final String EXCEPTION_MODEL = "exception.model";
	public static final String EXCEPTION_MODEL_ANALYSIS = "exception.model.analysis";
	*/
	
	// ------------------- UI messages -------------------------//
	
	public static final String ERROR_RESULT_SYNTAX = "error.result.syntax";
	public static final String ERROR_TIMEOUT = "error.timeout";	
	public static final String ERROR_ANALYZE = "error.analyze";
	
	public static final String SOLUTION_CORRECT = "solution.correct";
	public static final String SOLUTION_INCORRECT = "solution.incorrect";
	
	public static final String GRADING_POINTS = "grading.points";

	public static final String MODEL_CONSISTENT = "model.consistent";
	public static final String MODEL_CONSISTENT_EMPTY = "model.consistent.empty";
	public static final String MODEL_MULTIPLE = "model.multiple";
	public static final String MODEL_MULTIPLE_CORRECT = "model.multiple.correct";
	public static final String MODEL_MULTIPLE_INCORRECT = "model.multiple.incorrect";

	public static final String PREDICATES_MISSING = "predicates.missing";
	public static final String PREDICATES_MISSING_TITLE = "predicates.missing.title";
	public static final String PREDICATES_MISSING_SI = "predicates.missing.si";
	public static final String PREDICATES_MISSING_PL = "predicates.missing.pl";

	public static final String PREDICATES_REDUNDANT = "predicates.redundant";
	public static final String PREDICATES_REDUNDANT_TITLE = "predicates.redundant.title";
	public static final String PREDICATES_REDUNDANT_SI = "predicates.redundant.si";
	public static final String PREDICATES_REDUNDANT_PL = "predicates.redundant.pl";

	public static final String PREDICATES_ARITY_LOW = "predicates.arity.low";
	public static final String PREDICATES_ARITY_LOW_TITLE = "predicates.arity.low.title";
	public static final String PREDICATES_ARITY_LOW_SI = "predicates.arity.low.si";
	public static final String PREDICATES_ARITY_LOW_PL = "predicates.arity.low.pl"; 

	public static final String PREDICATES_ARITY_HIGH = "predicates.arity.high";
	public static final String PREDICATES_ARITY_HIGH_TITLE = "predicates.arity.high.title";
	public static final String PREDICATES_ARITY_HIGH_SI = "predicates.arity.high.si";
	public static final String PREDICATES_ARITY_HIGH_PL = "predicates.arity.high.pl";	

	public static final String FACTS_MISSING = "facts.missing";
	public static final String FACTS_MISSING_TITLE = "facts.missing.title";
	public static final String FACTS_MISSING_SI = "facts.missing.si";
	public static final String FACTS_MISSING_PL = "facts.missing.pl";

	public static final String FACTS_REDUNDANT = "facts.redundant";
	public static final String FACTS_REDUNDANT_TITLE = "facts.redundant.title";
	public static final String FACTS_REDUNDANT_SI = "facts.redundant.si";
	public static final String FACTS_REDUNDANT_PL = "facts.redundant.pl";

	public static final String FACTS_NEGATIVE = "facts.negative";
	public static final String FACTS_NEGATIVE_TITLE = "facts.negative.title";
	public static final String FACTS_NEGATIVE_SI = "facts.negative.si";
	public static final String FACTS_NEGATIVE_PL = "facts.negative.pl";

	public static final String FACTS_POSITIVE = "facts.positive";
	public static final String FACTS_POSITIVE_TITLE = "facts.positive.title";
	public static final String FACTS_POSITIVE_SI = "facts.positive.si";
	public static final String FACTS_POSITIVE_PL = "facts.positive.pl";
	
	/*
	private static final ResourceBundle bundle = PropertyResourceBundle.getBundle(PROPERTIES);
	*/
	private static final ResourceBundle bundle = PropertyResourceBundle.getBundle(DLResources.class.getName());
	
	/**
	 * Gets the message defined in a property.
	 * 
	 * @param key One of the constants of this class.
	 * @return The message found.
	 */
	public static String getString(String key) {
		return bundle.getString(key);
	}
	
	/**
	 * Gets an enumeration of all keys found in the properties file.
	 * 
	 * @return Enumeration of all keys.
	 */
	public static Enumeration getKeys() {
		return bundle.getKeys();
	}
}