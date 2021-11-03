package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class UnaryOperatorImpl extends ExpressionImpl implements UnaryOperator {

	private Expression expression;

	public UnaryOperatorImpl() {
		this.expression = null;
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator iter;

		this.removeAllSchemaAttributes();

		this.expression.calculateSchema(conn);
		iter = this.expression.iterSchemaAttributes();

		while (iter.hasNext()) {
			this.addSchemaAttribute(iter.next().toString());
		}
	}

	public Expression getExpression() {
		return this.expression;
	}

	public boolean setExpression(Expression e) {
		if (e != null) {
			this.expression = e;
			return true;
		} else {
			return false;
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			this.initExpression(element, root);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		Element expression;

		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			this.initExpression(element, root);
		}
	}

	private void initExpression(Element element, Document root) {
		Element expression;

		if ((element != null) && (root != null)) {
			if (this.getExpression() != null) {
				expression = root.createElement("Expression");
				this.getExpression().initElement(expression, root);
				element.appendChild(expression);
			}
		}
	}
}
