package etutor.modules.xquery.ui;

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class XMLDistortionHandler extends DefaultHandler {
    private Writer out;
    private String dummyAttribute;
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");

    //===========================================================
    // SAX DocumentHandler methods
    //===========================================================

    /**
     * Creates a handler for parsing and simultaneously transforming an XML tree
     * by adding dummy attributes to each element.
     * 
     * @param out a writer to write results of the transformed XML tree to; The writer
     * is not flushed within this class.
     * 
     * @param dummyAttribute name of an attribute which is added to each XML element
     * of the parsed XML tree with an empty String value; If an attribute with such
     * a name already exists, the list of attributes of the corresponding element 
     * remains unchanged.
     */
    public XMLDistortionHandler(Writer out, String dummyAttribute) {
    	super();
    	this.out = out;
    	this.dummyAttribute = dummyAttribute;
    }
    
    public void startDocument() throws SAXException {
        emit("<?xml version='1.0' encoding='UTF-8'?>");
        nl();
    }

    public void endDocument() throws SAXException {
    	nl();
    	/*
    	try {
            nl();
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
        */
    }

    public void startElement(String namespaceURI, 
    		String lName, 
    		String qName, 
    		Attributes attrs) throws SAXException {
    	boolean dummyExists = false;
    	String eName = lName; // element name
        if ("".equals(eName)) {
        	eName = qName; // namespaceAware = false
        }
        emit("<"+eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
            	String aName = attrs.getLocalName(i); // attribute name 
                if ("".equals(aName)) {
                	aName = attrs.getQName(i);
                }
                if (aName.equalsIgnoreCase(dummyAttribute)) {
                	dummyExists = true;
                }
                emit(" ");
                emit(aName+"=\""+attrs.getValue(i)+"\"");
            }
        }
        if (!dummyExists) {
	        emit(" ");
	        emit(dummyAttribute+"=\"\"");
        }
        emit(">");
    }

    public void endElement(String namespaceURI, String lName, String qName) throws SAXException {
    	String eName = lName; // element name
        if ("".equals(eName)) {
        	eName = qName; // namespaceAware = false
        }
        emit("</"+eName+">");
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        emit(s);
    }

    //===========================================================
    // Utility Methods ...
    //===========================================================

    // Wrap I/O exceptions in SAX exceptions, to
    // suit handler signature requirements
    private void emit(String s) throws SAXException {
    	try {
            out.write(s);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    // Start a new line
    private void nl() throws SAXException {
        try {
            out.write(LINE_SEP);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }
}