package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.analysis;

import java.io.Serializable;

import org.w3c.dom.Text;

/**
 * Internal representation of an XML text node, which contains all information necessary for
 * internal use.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class XMLTextDescription implements XMLNodeDescription, Serializable {
	private String value;
	
	/**
	 * Constructs a new description object by extracting all relevant information from the 
	 * specified XML text node.
	 * 
	 * @param text The XML text node to build this <code>XMLTextDescription</code> from.
	 */
	public XMLTextDescription(Text text) {
		this.value = text.getNodeValue();
	}

	/**
	 * Returns the value of the XML text node, that this <code>XMLTextDescription</code> was built from.
	 * 
	 * @return Returns the value of the text.
	 */
	public String getNodeValue() {
		return value;
	}
	
	/* (non-Javadoc)
	 * @see etutor.modules.xquery.analysis.XMLNodeDescription#getDescription()
	 */
	public String getDescription() {
		String description = value.trim();
		int carriage = description.indexOf("\n");
		if (carriage > -1) {
			description = description.substring(0, carriage) + " ..."; 
		}
		return "Text: " + value;
	}
}
