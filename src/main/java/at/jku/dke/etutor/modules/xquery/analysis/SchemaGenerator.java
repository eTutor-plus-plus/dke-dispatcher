package at.jku.dke.etutor.modules.xquery.analysis;

import at.jku.dke.etutor.modules.xquery.InternalException;
import at.jku.dke.etutor.modules.xquery.XQCoreManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * A class used for generating a String which represents the DTD or the XML
 * Schema of an XML document. This is done by utilizing <i>Data Descriptors by
 * Example (Version 1.2) </i>, copyrighted by <i>IBM Corporation </i>. Required
 * libraries are loaded using reflection.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class SchemaGenerator {

	/**
	 * Flag for generating a DTD.
	 */
	private final static int DTD = 0;

	/**
	 * Flag for generating an XML Schema.
	 */
	private final static int XML_SCHEMA = 1;

	/**
	 * Generates a String representing the DTD of an XML file.
	 * 
	 * @param xmlInputFile
	 *            The file path of the XML document.
	 * @return A String representing the DTD of the XML file.
	 * @throws InternalException
	 *             if any exception occured when using the generating library by
	 *             reflection or if the libraries denoted by the predefined
	 *             paths are not found.
	 */
	public String generateDTD(String xmlInputFile) throws InternalException {
		return generateDocumentDefinition(DTD, xmlInputFile);
	}

	/**
	 * Generates a String representing the XML Schema of an XML file. It must be
	 * noted, that the elements of the produced schema are not in scope of any
	 * namespace.
	 * 
	 * @param xmlInputFile
	 *            The file path of the XML document.
	 * @return A String representing the XML Schema of the XML file.
	 * @throws InternalException
	 *             if any exception occured when using the generating library by
	 *             reflection or if the libraries denoted by the predefined
	 *             paths are not found.
	 */
	public String generateSchema(String xmlInputFile) throws InternalException {
		return generateDocumentDefinition(XML_SCHEMA, xmlInputFile);
	}

	/**
	 * Generates a String representing either the DTD or the XML Schema of an
	 * XML file, depending on the flag.
	 * 
	 * @param xmlInputFile
	 *            The file path of the XML document.
	 * @param flag
	 *            A flag denoting whether a DTD or a Schema shall be generated.
	 * @return A String representing either the DTD or the XML Schema of the XML
	 *         file.
	 * @throws InternalException
	 *             if any exception occured when using the generating library by
	 *             reflection or if the libraries denoted by the predefined
	 *             paths are not found.
	 */
	private String generateDocumentDefinition(int flag, String xmlInputFile)
			throws InternalException {

		// Set parameters which make the only differences between generating DTD
		// and XML Schema
		String methodName = null;
		String fieldName = null;
		if (flag == DTD) {
			methodName = "setDtdRequested";
			fieldName = "DTD";
		} else {
			methodName = "setSchemaRequested";
			fieldName = "XML_SCHEMA";
		}

		try {

			URL url1 = XQCoreManager.getInstance().getResource(
					XQCoreManager.JAR_XERCES).toURI().toURL();
			URL url2 = XQCoreManager.getInstance().getResource(
					XQCoreManager.JAR_DDBE).toURI().toURL();

			URLClassLoader classLoader = new URLClassLoader(new URL[] { url1,
					url2 }, null);

			// Instantiate a DatalogParameter object using the class loader
			Class class_Parameter = Class.forName(
					"com.ibm.DDbEv2.Utilities.Parameter", false, classLoader);
			Constructor constr_Parameter = class_Parameter.getConstructor(null);
			Object obj_Parameter = constr_Parameter.newInstance(null);

			Method method_setContinueOnError = class_Parameter.getMethod(
					"setContinueOnError", new Class[] { boolean.class });
			Method method_addSource = class_Parameter.getMethod("addSource",
					new Class[] { String.class });
			Method method_setDtdRequested = class_Parameter.getMethod(
					methodName, new Class[] { boolean.class });

			method_setContinueOnError.invoke(obj_Parameter,
					new Object[] { Boolean.valueOf(false) });
			method_addSource.invoke(obj_Parameter,
					new Object[] { xmlInputFile });
			method_setDtdRequested.invoke(obj_Parameter,
					new Object[] { Boolean.valueOf(true) });

			// Instantiate a DDModel object using the class loader
			Class class_DDModel = Class.forName(
					"com.ibm.DDbEv2.Models.DDModel", false, classLoader);

			Constructor constr_DDModel = class_DDModel
					.getConstructor(new Class[] { class_Parameter });
			Object obj_DDModel = constr_DDModel
					.newInstance(new Object[] { obj_Parameter });

			Method method_process = class_DDModel.getMethod("process", null);
			Method method_result = class_DDModel.getMethod("printAs",
					new Class[] { int.class });

			method_process.invoke(obj_DDModel, null);

			// To make it more complicated we need another class just for
			// obtaining a simple constant field
			Class class_Constants = Class.forName(
					"com.ibm.DDbEv2.Interfaces.Constants", false, classLoader);
			Field field_DTD = class_Constants.getDeclaredField(fieldName);
			Object obj_value = field_DTD.get(null);

			return (String) method_result.invoke(obj_DDModel,
					new Object[] { obj_value });

		} catch (Exception e) {
			String msg = "An internal exception was thrown when trying to generate a DTD file from an XML document.\n";
			msg += e.getClass().getName() + ": " + e.getMessage();
			throw new InternalException(msg, e);
		}
		/*
		 * URL url = XQProcessor.class.getResource("/lib/ddbe/xerces.jar");
		 * System.out.println("Library: " + (url != null ? url.getPath():null));
		 * URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
		 * classLoader.setDefaultAssertionStatus(true);
		 * 
		 * Class entityResolver =
		 * classLoader.loadClass("org.apache.xerces.framework.XMLParser");
		 * System.out.println("Loaded Class: " + entityResolver);
		 * System.out.println("Extract DTD from file " + xmlInputFile);
		 * 
		 * DatalogParameter parameter = new DatalogParameter();
		 * parameter.setContinueOnError(false);
		 * parameter.addSource(xmlInputFile); parameter.setDtdRequested(true);
		 * 
		 * DDModel dtdModel = new DDModel(parameter); dtdModel.process(); return
		 * dtdModel.printAs(Constants.DTD);
		 */
	}

}