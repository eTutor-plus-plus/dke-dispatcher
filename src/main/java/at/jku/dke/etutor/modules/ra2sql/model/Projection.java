package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Projection extends UnaryOperatorImpl implements UnaryOperator {

	private final ArrayList<String> projectionAttributes;

	public Projection() {
		super();
		this.projectionAttributes = new ArrayList<>();
	}

	public boolean addProjectionAttribute(String attributeName) {
		if ((attributeName != null) && (attributeName.length() != 0)) {
			return this.projectionAttributes.add(attributeName.toUpperCase().trim());
		} else {
			return false;
		}
	}

	public Iterator<String> iterProjectionAttributes() {
		return this.projectionAttributes.iterator();
	}

	public boolean removeProjectionAttribute(String attributeName) {
		if ((attributeName != null) && (attributeName.length() != 0)) {
			return this.projectionAttributes.remove(attributeName.toUpperCase().trim());
		} else {
			return false;
		}
	}

	@Override
	public void calculateSchema(Connection conn) throws SQLException {
		Iterator<String> iter;

		this.removeAllSchemaAttributes();

		if (this.getExpression() != null) {
			this.getExpression().calculateSchema(conn);
		}
		
		iter = this.iterProjectionAttributes();
		while (iter.hasNext()){
			this.addSchemaAttribute(iter.next());
		}
	}

	@Override
	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "PROJECTION");
			this.initProjectionAttributes(element, root);
			return element;
		} else {
			return null;
		}
	}

	@Override
	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "PROJECTION");
			this.initProjectionAttributes(element, root);
		}
	}

	private void initProjectionAttributes(Element element, Document root) {
		Iterator<String> iter;
		Element attribute;
		Text attributeName;

		if ((element != null) && (root != null)) {
			iter = this.iterProjectionAttributes();
			while (iter.hasNext()) {
				attribute = root.createElement("ProAttribute");
				attributeName = root.createTextNode(iter.next());
				attribute.appendChild(attributeName);
				element.appendChild(attribute);
			}
		}
	}
}
