package at.jku.dke.etutor.modules.ra2sql.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OuterJoin extends BinaryOperatorImpl implements BinaryOperator {

	public OuterJoin() {
		super();
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "OUTER_JOIN");
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "OUTER_JOIN");
		}
	}
}
