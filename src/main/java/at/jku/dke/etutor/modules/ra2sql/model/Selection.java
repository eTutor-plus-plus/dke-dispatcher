package at.jku.dke.etutor.modules.ra2sql.model;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Selection extends UnaryOperatorImpl implements UnaryOperator {

	private Vector comparisons;

	public Selection() {
		super();
		this.comparisons = new Vector();
	}

	public boolean addComparison(Comparison ac) {
		if (ac != null) {
			this.comparisons.add(ac);
			return true;
		} else {
			return false;
		}
	}

	public Iterator iterComparisons() {
		return this.comparisons.iterator();
	}

	public boolean removeComparison(Comparison c) {
		return this.comparisons.remove(c);
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "SELECTION");
			this.initComparisons(element, root);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "SELECTION");
			this.initComparisons(element, root);
		}
	}

	private void initComparisons(Element element, Document root) {
		Iterator iter;
		Comparison comp;
		Element comparison;

		if ((element != null) && (root != null)) {
			iter = this.iterComparisons();

			while (iter.hasNext()) {
				comp = (Comparison)iter.next();
				comparison = root.createElement("Comparison");

				comparison.setAttribute("leftValue", comp.getLeftValue());
				comparison.setAttribute("leftValueType", comp.getLeftValueType().toString());
				comparison.setAttribute("rightValue", comp.getRightValue());
				comparison.setAttribute("rightValueType", comp.getRightValueType().toString());
				comparison.setAttribute("operator", comp.getOperator().toString());
				element.appendChild(comparison);
			}
		}
	}
}
