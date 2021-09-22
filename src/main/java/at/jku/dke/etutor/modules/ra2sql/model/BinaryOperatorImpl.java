package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class BinaryOperatorImpl extends ExpressionImpl implements BinaryOperator {

	private Expression leftExpression;
	private Expression rightExpression;

	public Expression getLeftExpression() {
		return this.leftExpression;
	}

	public Expression getRightExpression() {
		return this.rightExpression;
	}

	public boolean setLeftExpression(Expression e) {
		if (e != null) {
			this.leftExpression = e;
			return true;
		} else {
			return false;
		}
	}

	public boolean setRightExpression(Expression e) {
		if (e != null) {
			this.rightExpression = e;
			return true;
		} else {
			return false;
		}
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator iter;
		
		this.removeAllSchemaAttributes();

		if (this.leftExpression != null) {
			this.leftExpression.calculateSchema(conn);
			iter = this.leftExpression.iterSchemaAttributes();
			while (iter.hasNext()) {
				this.addSchemaAttribute(iter.next().toString().toUpperCase().trim());
			}
		}

		if (this.rightExpression != null) {
			this.rightExpression.calculateSchema(conn);
			iter = this.rightExpression.iterSchemaAttributes();
			while (iter.hasNext()) {
				this.addSchemaAttribute(iter.next().toString().toUpperCase().trim());
			}
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			this.initExpressions(element, root);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			this.initExpressions(element, root);
		}
	}

	private void initExpressions(Element element, Document root) {
		Element leftExpression;
		Element rightExpression;

		if ((element != null) && (root != null)) {
			if (this.getLeftExpression() != null) {
				leftExpression = root.createElement("LeftExpression");
				this.getLeftExpression().initElement(leftExpression, root);
				element.appendChild(leftExpression);
			}

			if (this.getRightExpression() != null) {
				rightExpression = root.createElement("RightExpression");
				this.getRightExpression().initElement(rightExpression, root);
				element.appendChild(rightExpression);
			}
		}
	}
}
