package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.analysis;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Node;

/**
 * Serves as container class for {@link NodeError} objects.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class NodeErrorList implements Serializable {

    private ArrayList errorList;

	/**
	 * Constructs a new <code>NodeErrorList</code>. 
	 */
	public NodeErrorList() {
		errorList = new ArrayList();
	}
	/**
	 * Adds a new <code>NodeError</code> to the internal list of <code>NodeError</code> objects,
	 * using the specified XPath expression, which is the identifier for a <code>NodeError</code>.
	 * If a <code>NodeError</code> with the specified XPath expression already exist, the given
	 * node is added to this object, otherwise a new <code>NodeError</code> is created and added
	 * to the internal list.
	 * 
	 * @param xPath The XPath expression to identify an already existing <code>NodeError</code> or to create a new one, if not already existing.
	 * @param node The node to add to the found or created <code>NodeError</code>.
	 * @return true if the node was successfully added to the found or created <code>NodeError</code>, else false.
	 * @see NodeError#addNode(Node)
	 */
	public boolean addNode(String xPath, Node node) {
		NodeError nodeError = new NodeError(xPath);
		int index = errorList.indexOf(nodeError);
		if (index > -1) {
			nodeError = (NodeError)errorList.get(index);
			return nodeError.addNode(node);
		} else if (nodeError.addNode(node)){
			errorList.add(nodeError);
			return true;
		}
		return false;
		
	}

	/**
	 * Adds a new <code>NodeError</code> to the internal list of <code>NodeError</code> objects,
	 * using the specified XPath expression, which is the identifier for a <code>NodeError</code>.
	 * If a <code>NodeError</code> with the specified XPath expression already exist, the given
	 * nodes are added to this object, otherwise a new <code>NodeError</code> is created and added
	 * to the internal list.
	 * 
	 * @param xPath The XPath expression to identify an already existing <code>NodeError</code> or to create a new one, if not already existing.
	 * @param nodes The nodes to add to the found or created <code>NodeError</code>.
	 */
	public void addNodes(String xPath, Node[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			this.addNode(xPath, nodes[i]);
		}
	}

	/**
	 * Returns all <code>NodeError</code> objects that have been added to this container.
	 *
	 * @return An array of error descriptions.
	 */
	public NodeError[] getErrorList() {
		return (NodeError[])errorList.toArray(new NodeError[]{});
	}
	
	/**
	 * Tests if there is at least one error, that has been added to this container.
	 * 
	 * @return true if there is at least one error added to this container, else false.
	 */
	public boolean containsErrors() {
		return errorList.size() > 0;
	}
	
	/**
	 * Returns the number of added errors.
	 * 
	 * @return The size of the internal list of added errors.
	 */
	public int size() {
		return errorList.size();
	}
}
