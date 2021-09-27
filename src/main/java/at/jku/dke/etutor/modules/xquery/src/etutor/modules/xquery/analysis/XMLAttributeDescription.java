package etutor.modules.xquery.analysis;

import java.io.Serializable;

import org.w3c.dom.Attr;

/**
 * Internal representation of an XML attribute, which contains all information necessary for
 * internal use.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class XMLAttributeDescription implements XMLNodeDescription, Serializable {
	private String value;
	private String name;
	private String prefix;
	private String namespaceUri;

	
	/**
	 * Constructs a new description object by extracting all relevant information from the 
	 * specified XML attribute.
	 * 
	 * @param attribute The XML attribute to build this <code>XMLAttributeDescription</code> from.
	 */
	public XMLAttributeDescription(Attr attribute) {
		this.name = attribute.getNodeName();
		this.value = attribute.getNodeValue();
		this.prefix = attribute.getPrefix();
		this.namespaceUri = attribute.getNamespaceURI();
	}

    /**
     * Returns the name of the XML attribute, that this <code>XMLAttributeDescription</code> was built from.
     * 
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the namespace URI of the XML attribute, that this <code>XMLAttributeDescription</code> was built from,
     * if there is any.
     * 
     * @return Returns the namespace URI of the attribute if existing, <code>null</code> otherwise.
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * Returns the namespace prefix of the XML attribute, that this <code>XMLAttributeDescription</code> was built from,
     * if there is any.
     * 
     * @return Returns the namespace prefix of the attribute if existing, <code>null</code> otherwise.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns the value of the XML attribute, that this <code>XMLAttributeDescription</code> was built from.
     * 
     * @return Returns the value of the attribute.
     */
    public String getValue() {
        return value;
    }

	/* (non-Javadoc)
	 * @see etutor.modules.xquery.analysis.XMLNodeDescription#getDescription()
	 */
	public String getDescription() {
		return "Attribute: " + name + " = '" + value + "'";
	}
}
