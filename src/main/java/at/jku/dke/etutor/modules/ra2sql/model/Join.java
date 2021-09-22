package at.jku.dke.etutor.modules.ra2sql.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Join extends BinaryOperatorImpl implements BinaryOperator {

	public Join() {
		super();
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "JOIN");
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "JOIN");
		}
	}
}
