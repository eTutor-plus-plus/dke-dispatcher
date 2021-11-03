package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Division extends BinaryOperatorImpl implements BinaryOperator {

	public Division() {
		super();
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator iter;
		String attribute;
		Expression leftExp = this.getLeftExpression();
		Expression rightExp = this.getRightExpression();

		this.removeAllSchemaAttributes();

		if ((leftExp != null) && (rightExp != null)) {
			leftExp.calculateSchema(conn);
			rightExp.calculateSchema(conn);
			iter = leftExp.iterSchemaAttributes();

			while (iter.hasNext()) {
				attribute = iter.next().toString().toUpperCase().trim();
				if (!rightExp.containsSchemaAttribute(attribute)) {
					this.addSchemaAttribute(attribute);
				}
			}
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "DIVISION");
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "DIVISION");
		}
	}
}
