package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.analysis;

import java.io.Serializable;

import org.w3c.dom.Element;

/**
 * Internal representation of an XML element, which contains all information necessary for
 * internal use.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class XMLElementDescription implements XMLNodeDescription, Serializable {
	private String name;
	private String prefix;
	private String namespaceUri;
	
	/**
	 * Constructs a new description object by extracting all relevant information from the 
	 * specified XML element.
	 * 
	 * @param element The XML element to build this <code>XMLElementDescription</code> from.
	 */
	public XMLElementDescription(Element element) {
		this.name = element.getNodeName();
		this.prefix = element.getPrefix();
		this.namespaceUri = element.getNamespaceURI();
	}

    /**
     * Returns the name of the XML element, that this <code>XMLElementDescription</code> was built from.
     * 
     * @return The name of the element.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the namespace URI of the XML element, that this <code>XMLElementDescription</code> was built from,
     * if there is any.
     * 
     * @return Returns the namespace URI of the element if existing, <code>null</code> otherwise.
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * Returns the namespace prefix of the XML element, that this <code>XMLElementDescription</code> was built from,
     * if there is any.
     * 
     * @return Returns the namespace prefix of the element if existing, <code>null</code> otherwise.
     */
    public String getPrefix() {
        return prefix;
    }

	/* (non-Javadoc)
	 * @see etutor.modules.xquery.analysis.XMLNodeDescription#getDescription()
	 */
	public String getDescription() {
		return "Element: " + name;
	}

}
