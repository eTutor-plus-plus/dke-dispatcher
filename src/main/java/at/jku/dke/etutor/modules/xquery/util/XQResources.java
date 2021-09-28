package at.jku.dke.etutor.modules.xquery.util;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Serves as key holder for message properties in a resource bundle named after this class.
 * The property file is expected to be located in the same package as this class.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class XQResources {
    /*
    public static final String PROPERTIES = "msg/XQResources";
    */

	//------------------- Exception messages -------------------------//
	/*
	public static final String EXCEPTION_INIT_ID = "exception.init.id";
	public static final String EXCEPTION_INIT_FILE = "exception.init.file";
	public static final String EXCEPTION_INIT_MISS = "exception.init.miss";
	
	public static final String EXCEPTION_PROPERTIES_FILE = "exception.properties.file";
	public static final String EXCEPTION_PROPERTIES_LOAD = "exception.properties.load";	
	public static final String EXCEPTION_PROPERTIES_PROPERTY = "exception.properties.property";
	public static final String EXCEPTION_PROPERTIES_VALUE = "exception.properties.value";
	public static final String EXCEPTION_PROPERTIES_VALUE_FILE = "exception.properties.value.file";
	
	public static final String EXCEPTION_RESULT_NOT_FOUND = "exception.result.not_found";
	public static final String EXCEPTION_RESULT_SYNTAX = "exception.result.syntax";
	public static final String EXCEPTION_RESULT_WELLFORMEDNESS = "exception.result.wellformedness";
	
	public static final String EXCEPTION_XPATH_INVALID = "exception.xpath.invalid";
	public static final String EXCEPTION_XPATH_NOT_FOUND = "exception.xpath.not_found";
	
	public static final String EXCEPTION_PARAMETER_VALIDATION = "exception.parameter.validation";
	public static final String EXCEPTION_PARAMETER_SCORE_MISSING = "exception.parameter.score.missing";
	public static final String EXCEPTION_PARAMETER_SCORE_NUMBER = "exception.parameter.score.number";
	public static final String EXCEPTION_PARAMETER_SCORE_CATEGORY = "exception.parameter.score.category";
	public static final String EXCEPTION_PARAMETER_SCORE_LEVEL = "exception.parameter.score.level";
	
	public static final String EXCEPTION_INTERNAL_DEBUG = "exception.internal.debug";
	public static final String EXCEPTION_INTERNAL_CSS = "exception.internal.css";
	public static final String EXCEPTION_INTERNAL_XSL = "exception.internal.xsl";
	public static final String EXCEPTION_INTERNAL_XSL_ADAPT = "exception.internal.xsl.adapt";
	public static final String EXCEPTION_INTERNAL_XSL_ADAPT_UNIQUENESS = "exception.internal.xsl.adapt.uniqueness";
	public static final String EXCEPTION_INTERNAL_XSD = "exception.internal.xsd";
	public static final String EXCEPTION_INTERNAL_DTD = "exception.internal.dtd";
	public static final String EXCEPTION_INTERNAL_XML_STRING = "exception.internal.xml.string";
	public static final String EXCEPTION_INTERNAL_XML_WRITE = "exception.internal.xml.write";
	public static final String EXCEPTION_INTERNAL_XML_IO = "exception.internal.xml.io";
	public static final String EXCEPTION_INTERNAL_SERIAL = "exception.internal.serialization";
	public static final String EXCEPTION_INTERNAL_DESERIAL = "exception.internal.deserialization";
	
	public static final String EXCEPTION_RESOURCE_NOT_FOUND = "exception.resource.not_found";
	
	public static final String EXCEPTION_DB_DRIVER = "exception.db.driver";
	public static final String EXCEPTION_DB_CONNECTION = "exception.db.connection";
	public static final String EXCEPTION_DB_CONNECTION_CLOSE = "exception.db.connection.close";
	public static final String EXCEPTION_DB_EXERCISE = "exception.db.exercise";
	public static final String EXCEPTION_DB_GRADING = "exception.db.grading";
	public static final String EXCEPTION_DB_SORTED_NODES = "exception.db.sorted.nodes";
	public static final String EXCEPTION_DB_URLS = "exception.db.urls";
	
	public static final String EXCEPTION_ATTRIBUTE = "exception.attribute";
	public static final String EXCEPTION_ATTRIBUTE_ACTION = "exception.attribute.action";
	public static final String EXCEPTION_ANALYSIS = "exception.analysis";
	public static final String EXCEPTION_ANALYSIS_COMPARE = "exception.analysis.compare";	
	public static final String EXCEPTION_REPORT = "exception.report";
	public static final String EXCEPTION_DIAGNOSE_LEVEL = "exception.diagnose.level";	
	
	public static final String EXCEPTION_URL_DUPLICATE = "exception.url.duplicate";
	public static final String EXCEPTION_URL_UNDECLARED = "exception.url.undeclared";
	public static final String EXCEPTION_ALIAS_DUPLICATE = "exception.alias.duplicate";
	public static final String EXCEPTION_ALIAS_UNDECLARED = "exception.alias.undeclared";
	*/
	
	// ------------------- UI messages -------------------------//
	
	public static final String ERROR_RESULT_ALIAS = "error.result.alias";
	public static final String ERROR_RESULT_ALIAS_UNDECLARED = "error.result.alias.undeclared";
	public static final String ERROR_RESULT_SYNTAX = "error.result.syntax";
	public static final String ERROR_RESULT_WELLFORMEDNESS = "error.result.wellformedness";
	public static final String ERROR_ANALYZE = "error.analyze";
	
	public static final String SOLUTION_CORRECT = "solution.correct";
	public static final String SOLUTION_INCORRECT = "solution.incorrect";
	
	public static final String GRADING_POINTS = "grading.points";

	public static final String SOLUTION_VALID = "solution.valid";
	public static final String SOLUTION_NOT_VALID = "solution.not_valid";
	
	public static final String NODES_MISSING = "nodes.missing";
	public static final String NODES_MISSING_TITLE = "nodes.missing.title";
	public static final String NODES_MISSING_INSTEAD = "nodes.missing.instead";
	public static final String NODES_MISSING_INSTEAD_SI = "nodes.missing.instead.si";
	public static final String NODES_MISSING_INSTEAD_PL = "nodes.missing.instead.pl";
	public static final String NODES_MISSING_BEFORE_SI = "nodes.missing.before.si";
	public static final String NODES_MISSING_BEFORE_PL = "nodes.missing.before.pl";
	public static final String NODES_MISSING_AFTER_SI = "nodes.missing.after.si";
	public static final String NODES_MISSING_AFTER_PL = "nodes.missing.after.pl";
	public static final String NODES_MISSING_IN_SI = "nodes.missing.in.si";
	public static final String NODES_MISSING_IN_PL = "nodes.missing.in.pl";
	public static final String NODES_MISSING_ROOT_SI = "nodes.missing.root.si";
	public static final String NODES_MISSING_ROOT_PL = "nodes.missing.root.pl";

	public static final String NODES_REDUNDANT = "nodes.redundant";
	public static final String NODES_REDUNDANT_TITLE = "nodes.redundant.title";
	public static final String NODES_REDUNDANT_SI = "nodes.redundant.si";
	public static final String NODES_REDUNDANT_PL = "nodes.redundant.pl";

	public static final String NODES_DISPLACED = "nodes.displaced";
	public static final String NODES_DISPLACED_TITLE = "nodes.displaced.title";
	public static final String NODES_DISPLACED_SI = "nodes.displaced.si";
	public static final String NODES_DISPLACED_PL = "nodes.displaced.pl";

	public static final String ATTRIBUTES_MISSING = "attributes.missing";
	public static final String ATTRIBUTES_MISSING_TITLE = "attributes.missing.title";
	public static final String ATTRIBUTES_MISSING_SI = "attributes.missing.si";
	public static final String ATTRIBUTES_MISSING_PL = "attributes.missing.pl";

	public static final String ATTRIBUTES_REDUNDANT = "attributes.redundant";
	public static final String ATTRIBUTES_REDUNDANT_TITLE = "attributes.redundant.title";
	public static final String ATTRIBUTES_REDUNDANT_SI = "attributes.redundant.si";
	public static final String ATTRIBUTES_REDUNDANT_PL = "attributes.redundant.pl";

	public static final String VALUES_INCORRECT = "values.incorrect";
	public static final String VALUES_INCORRECT_TITLE = "values.incorrect.title";	
	public static final String VALUES_INCORRECT_SI = "values.incorrect.si";
	public static final String VALUES_INCORRECT_PL = "values.incorrect.pl";
	/*
	private static final ResourceBundle bundle = PropertyResourceBundle.getBundle(PROPERTIES);
	*/
	private static final ResourceBundle bundle = PropertyResourceBundle.getBundle(XQResources.class.getName());
	
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