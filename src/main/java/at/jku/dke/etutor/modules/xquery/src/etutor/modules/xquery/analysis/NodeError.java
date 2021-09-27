package etutor.modules.xquery.analysis;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Describes an error as it is detected when analyzing an XQuery result and comparing it to another
 * XQuery result.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class NodeError implements Serializable {

    private String xPath;

    private ArrayList attributeList;

    private ArrayList textList;

    private ArrayList elementList;


    /**
     * Creates a new error instance.
     * 
     * @param xPath The XPath expression, which denotes one or more XML nodes in the incorrect XML
     *            document, which are concerned by the error. The incorrect node(s) of this
     *            <code>NodeError</code> are identified by this expression. <br>
     *            For example, an instance <code>NodeError</code> may be initialized with an
     *            expression like <code>/person/*</code>, saying that there is something wrong
     *            about all direct children of the root element called <code>person</code>.
     */
    public NodeError(
            String xPath) {
        this.xPath = xPath;
        this.attributeList = new ArrayList();
        this.textList = new ArrayList();
        this.elementList = new ArrayList();
    }

    /**
     * Adds an XML node to the internal list of nodes, which are considered to be related to the
     * incorrect node(s) of this <code>NodeError</code> somehow. <br>For example, an instance of
     * <code>NodeError</code> might have been initialized with an expression like
     * <code>/person/*</code>, saying that there is something wrong about all direct children of
     * the root element called <code>person</code>. A call to this method adds a node which is
     * considered to be one of these incorrect nodes, for example an <i>incorrectly ordered </i>
     * node.
     * 
     * @param node The node which is used for adding an object, which contains all necessary
     *            information from this node, to the internal list of error descriptions.
     * @return true if the node was successfully transformed into an internal representation and
     *         added to the list; false if the XML node is not of a relevant XML type, which must be
     *         one of these: XML elements, text nodes and attributes.
     */
    public boolean addNode(Node node) {
        boolean relevantType = true;
        if (node instanceof Element && !elementList.contains(node)) {
            elementList.add(new XMLElementDescription((Element)node));
        } else if (node instanceof Text && !textList.contains(node)) {
            textList.add(new XMLTextDescription((Text)node));
        } else if (node instanceof Attr && !attributeList.contains(node)) {
            attributeList.add(new XMLAttributeDescription((Attr)node));
        } else {
            relevantType = false;
        }
        return relevantType;
    }

    /**
     * Indicates if any XML node has been added to this <code>NodeErros</code>.
     * 
     * @return true if there is at least one XML element, text node or attribute in the internal
     *         list of error nodes.
     */
    public boolean containsErrors() {
        return attributeList.size() > 0 || elementList.size() > 0 || textList.size() > 0;
    }

    /**
     * Returns all error node descriptions, that have been added to this <code>NodeError</code> as
     * XML elements.
     * 
     * @return The description of all added XML elements.
     */
    public XMLElementDescription[] getElementNodes() {
        return (XMLElementDescription[])elementList.toArray(new XMLElementDescription[] {});
    }

    /**
     * Returns all error node descriptions, that have been added to this <code>NodeError</code> as
     * XML text nodes.
     * 
     * @return The description of all added XML text nodes.
     */
    public XMLTextDescription[] getTextNodes() {
        return (XMLTextDescription[])textList.toArray(new XMLTextDescription[] {});
    }

    /**
     * Returns all error node descriptions, that have been added to this <code>NodeError</code> as
     * XML attributes.
     * 
     * @return The description of all added XML attributes.
     */
    public XMLAttributeDescription[] getAttributeNodes() {
        return (XMLAttributeDescription[])attributeList.toArray(new XMLAttributeDescription[] {});
    }

    /**
     * Returns all error node descriptions, that have been added to this <code>NodeError</code> as
     * one of the relevant XML nodes: elements, text nodes or attributes.
     * 
     * @return The description of all added XML nodes.
     */
    public XMLNodeDescription[] getAllNodes() {
        ArrayList nodes = new ArrayList();
        nodes.addAll(elementList);
        nodes.addAll(textList);
        nodes.addAll(attributeList);
        return (XMLNodeDescription[])nodes.toArray(new XMLNodeDescription[] {});
    }

    /**
     * The XPath expression of this <code>NodeError</code>, which denotes one or more XML nodes
     * in the incorrect XML document, which are concerned by the error.
     * 
     * @return The XPath expression.
     */
    public String getXPath() {
        return xPath;
    }

    /**
     * Tests the passed object on equality.
     * 
     * @return true, if the passed object is an <code>NodeError</code> and the XPath expression is
     *         equal to the XPath expression of this <code>NodeError</code>.
     */
    public boolean equals(Object obj) {
        return (obj instanceof NodeError && ((NodeError)obj).getXPath().equals(this.xPath));
    }
}