package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Minus extends BinaryOperatorImpl implements BinaryOperator {

	public Minus() {
		super();
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator iter;

		this.removeAllSchemaAttributes();

		if (this.getRightExpression() != null) {
			this.getRightExpression().calculateSchema(conn);
		}

		if (this.getLeftExpression() != null) {
			this.getLeftExpression().calculateSchema(conn);

			iter = this.getLeftExpression().iterSchemaAttributes();
			while (iter.hasNext()) {
				this.addSchemaAttribute(iter.next().toString().toUpperCase().trim());
			}
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "MINUS");
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "MINUS");
		}
	}
}
