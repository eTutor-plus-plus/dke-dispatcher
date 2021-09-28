package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import oracle.xml.parser.schema.XMLSchema;
import oracle.xml.parser.schema.XSDBuilder;
import oracle.xml.parser.schema.XSDException;
import oracle.xml.parser.v2.DocumentBuilder;
import oracle.xml.parser.v2.SAXParser;
import oracle.xml.parser.v2.XMLDocument;
import oracle.xml.parser.v2.XMLError;
import oracle.xml.parser.v2.XMLNode;
import oracle.xml.schemavalidator.XSDValidator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A utility class for dealing with XML documents, like parsing, reparsing or saving them to a file.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XMLUtil {

	private final static String LINE_SEP = System.getProperty("line.separator", "\n");
	private final static Logger LOGGER = Logger.getLogger(XMLUtil.class);
	
    /**
     * Parses an XML document from a String.
     * 
     * @param xml The string which represents the XML document.
     * @param whitespace Denotes if whitespaces are preserved so that a number of empty spaces are
     *            treated as belonging to a text node.
     * @return The parsed XML document.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws IOException if an IOException occured when parsing.
     */
    public static XMLDocument parse(String xml, boolean whitespace) throws SAXException,
            IOException {
        //TODO: comments are not parsed?
        StringReader reader = new StringReader(xml);
        SAXParser parser = new SAXParser();
        parser.setPreserveWhitespace(whitespace);
        DocumentBuilder docBuilder = new DocumentBuilder();
        parser.setContentHandler(docBuilder);
        parser.parse(reader);
        return docBuilder.getDocument();
    }

    /**
     * Parses an XML document from an URL.
     * 
     * @param url The URL from which the document has to be parsed.
     * @param whitespace Denotes if whitespaces are preserved so that a number of empty spaces are
     *            treated as belonging to a text node.
     * @return The parsed XML document.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws IOException if an IOException occured when accessing the URL or when parsing the
     *             content.
     */
    public static XMLDocument parse(URL url, boolean whitespace) throws SAXException, IOException {
        SAXParser parser = new SAXParser();
        parser.setPreserveWhitespace(whitespace);
        DocumentBuilder docBuilder = new DocumentBuilder();
        parser.setContentHandler(docBuilder);
        parser.parse(url);
        return docBuilder.getDocument();
    }

    /**
     * Parses an XML Schema from an URL.
     * 
     * @param schemaURL The URL from which the schema has to be parsed.
     * @return The parsed XML Schema or null if the URL is null.
     * @throws XSDException if the URL is invalid or the content can not be parsed as XML Schema.
     */
    public static XMLSchema parseSchema(URL schemaURL) throws XSDException {
        /*
         * URL schemaURL = c.getResource(resource);
         */
        if (schemaURL != null) {
            XSDBuilder schemaBuilder = new XSDBuilder();
            return schemaBuilder.build(schemaURL);
        }
        return null;
    }

    /**
     * Checks if an XML document is valid with regard to a schema, given by a String.
     * 
     * @param doc The XML document to check.
     * @param schemaString The XML Schema to check the document with.
     * @return true if there is no validation error, false if there is at least one validation
     *         error.
     * @throws IOException if an IOException occurs when parsing the schema.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static boolean isValid(XMLDocument doc, String schemaString) throws IOException,
            SAXException, XSDException {
        return validate(doc, schemaString).getListTrees().size() == 0;

    }

    /**
     * Checks if an XML document is valid with regard to a schema, given by an URL.
     * 
     * @param doc The XML document to check.
     * @param schemaURL The URL of the XML Schema to check the document with.
     * @return true if there is no validation error, false if there is at least one validation
     *         error.
     * @throws IOException if an IOException occurs when trying to find the schema or when parsing
     *             the content.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static boolean isValid(XMLDocument doc, URL schemaURL) throws IOException, SAXException,
            XSDException {
        return validate(doc, schemaURL).getListTrees().size() == 0;
    }

    /**
     * Checks if an XML document is valid with regard to a schema, given by an
     * <code>XMLSchema</code> object.
     * 
     * @param doc The XML document to check.
     * @param schema The XML Schema to check the document with.
     * @return true if there is no validation error, false if there is at least one validation
     *         error.
     * @throws IOException if an IOException occurs when parsing the schema.
     * @throws SAXException if a parsing exception occured.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static boolean isValid(XMLDocument doc, XMLSchema schema) throws IOException,
            SAXException, XSDException {
        return validate(doc, schema).getListTrees().size() == 0;

    }

    /**
     * Validates a given XML document with an XML Schema, given as String.
     * 
     * @param doc The XML document to validate.
     * @param schemaString The XML Schema to check the document with.
     * @return An object which holds the found validation errors, if any.
     * @throws IOException if an IOException occurs when parsing the schema.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static XMLError validate(XMLDocument doc, String schemaString) throws IOException,
            SAXException, XSDException {

        InputSource source = new InputSource(new StringReader(schemaString));
        XSDBuilder schemaBuilder = new XSDBuilder();
        XMLSchema schemaDoc = schemaBuilder.build(source);
        return validate(doc, schemaDoc);
    }

    /**
     * Validates a given XML document with an XML Schema, given as URL.
     * 
     * @param doc The XML document to validate.
     * @param schemaURL The XML Schema to check the document with.
     * @return An object which holds the found validation errors, if any.
     * @throws IOException if an IOException occurs when parsing the schema.
     * @throws SAXException if a parsing exception occured, basically due to a String which is not
     *             wellformed with regard to XML.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static XMLError validate(XMLDocument doc, URL schemaURL) throws IOException,
            SAXException, XSDException {
        XMLSchema schema = parseSchema(schemaURL);
        return validate(doc, schema);
    }

    /**
     * Validates a given XML document with an XML Schema, given as <code>XMLSchema</code> object.
     * 
     * @param doc The XML document to check.
     * @param schema The XML Schema to check the document with.
     * @return An object which holds the found validation errors, if any.
     * @throws IOException if an IOException occurs when parsing the schema.
     * @throws SAXException if a parsing exception occured.
     * @throws XSDException if the content can not be parsed as XML Schema.
     */
    public static XMLError validate(XMLDocument doc, XMLSchema schema) throws XSDException,
            SAXException, IOException {
        XSDValidator validator = new XSDValidator();
        validator.setSchema(schema);
        validator.validate(doc);

        return validator.getError();
    }

    /**
     * Checks if an XML document is valid with regard to a DTD.
     * 
     * @param doc The XML document to check.
     * @param dtdElementName The name of the root element of the DTD.
     * @param dtdFile The path of the DTD file.
     * @return true if no validation error could be detected, false if at least one validation error
     *         could be detected.
     * @throws IOException if an internal IO exception occured when processing the XML document so
     *             that it can be validated.
     * @throws DocumentException if an internal parsing exception occured when processing the XML
     *             document so that it can be validated.
     * @throws SAXException if the DTD is not applicable itself.
     */
    public static boolean isValid(XMLDocument doc, String dtdElementName, String dtdFile)
            throws IOException, DocumentException, SAXException {

        //TODO: doc.cloneNode() ?
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        doc.print(writer);
        String xml = stringWriter.toString();

        SAXReader saxReader = new SAXReader();
        saxReader.setStripWhitespaceText(true);
        Document docCopy = saxReader.read(new java.io.StringReader(xml));
        docCopy.addDocType(dtdElementName, null, dtdFile);

        XMLErrorHandler errorHandler = new XMLErrorHandler();
        SAXValidator validator = new SAXValidator();
        validator.setErrorHandler(errorHandler);
        validator.validate(docCopy);
        
        String msg = new String();
        msg += "XML validation results w.r.t. " + dtdFile + ": " + LINE_SEP;
        msg += errorHandler.getErrors().asXML();
        LOGGER.debug(msg);

        return errorHandler.getErrors().elements().size() == 0;
    }

    /**
     * Writes the contents of an XML node to a file.
     * 
     * @param node The node to be written to the file.
     * @param file The destination file.
     * @throws IOException if an IO exception occured when writing the document to the file.
     */
    public static void printXMLFile(XMLNode node, File file) throws IOException {
    	BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			node.print(out);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    /**
     * Writes the contents of an XML document to a file.
     * 
     * @param doc The document to be written to the file.
     * @param directory The directory in which the file should be placed.
     * @param filename The destination filename.
     * @throws IOException if an IO exception occured when writing the document to the file.
     */
    public static void printXMLFile(XMLNode doc, File directory, String filename) throws IOException {
        printFile(getXMLString(doc), new File(directory, filename));
    }

    /**
     * Writes some text to a file.
     * 
     * @param s The String to be written to the file.
     * @param directory The directory in which the file should be placed.
     * @param filename The destination filename.
     * @throws IOException if an IO exception occured when writing the document to the file.
     */
    public static void printFile(String s, File directory, String filename) throws IOException {
        printFile(s, new File(directory, filename));
    }

    /**
     * Writes a String to a file.
     * 
     * @param s The content to be written to the file.
     * @param file The destination file.
     * @throws IOException if an IO exception occured when writing the document to the file.
     */
    public static void printFile(String s, File file) throws IOException {
		BufferedOutputStream out = null;
		PrintWriter writer = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			writer = new PrintWriter(out);
			writer.write(s);
		} finally {
        	//PrintWriter must be closed first!
            if (writer != null) {
                writer.close();
            }
        	if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    /**
     * Transforms the contents of an XML node to a String.
     * 
     * @param node The XML node to transform.
     * @return The String holding the information of the contents of the XML node.
     * @throws IOException if an exception occured when printing the contents of the XML node to the
     *             String.
     */
    public static String getXMLString(XMLNode node) throws IOException {
        return getXMLWriter(node).toString();
    }

    /**
     * Gets the <code>Writer</code> object holding the information of an XML node.
     * 
     * @param node The XML node, from which contents are printed to the <code>Writer</code>
     * @return The writer holding the information of the XML node.
     * @throws IOException if an exception occured when printing the contents of the XML node to the
     *             writer.
     */
    public static Writer getXMLWriter(XMLNode node) throws IOException {
        StringWriter stringWriter = new StringWriter();
        node.print(new PrintWriter(stringWriter));
        return stringWriter;
    }

}