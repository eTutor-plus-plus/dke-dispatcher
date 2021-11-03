package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Intersection extends BinaryOperatorImpl implements BinaryOperator {

	public Intersection() {
		super();
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator iter;
		String attribute;

		this.removeAllSchemaAttributes();

		if (this.getRightExpression() != null) {
			this.getRightExpression().calculateSchema(conn);
		}

		if (this.getLeftExpression() != null) {
			this.getLeftExpression().calculateSchema(conn);
			iter = this.getLeftExpression().iterSchemaAttributes();

			while (iter.hasNext()) {
				attribute = iter.next().toString().toUpperCase().trim();
				this.addSchemaAttribute(attribute);
			}
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "INTERSECTION");
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "INTERSECTION");
		}
	}
}
