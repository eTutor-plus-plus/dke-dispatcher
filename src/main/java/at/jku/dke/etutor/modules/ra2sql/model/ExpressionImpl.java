package at.jku.dke.etutor.modules.ra2sql.model;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public abstract class ExpressionImpl implements Expression {

	private Vector schemaAttributes;

	public ExpressionImpl() {
		super();
		this.schemaAttributes = new Vector();
	}

	public Iterator iterSchemaAttributes() {
		return this.schemaAttributes.iterator();
	}

	public boolean addSchemaAttribute(String attribute) {
		if (attribute != null) {
			attribute = attribute.toUpperCase().trim();
			if (!this.schemaAttributes.contains(attribute)) {
				return this.schemaAttributes.add(attribute);
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public void setSchemaAttributes(Vector v) {
		if (v != null) {
			for (int i = 0; i < v.size(); i++) {
				this.addSchemaAttribute(v.get(i).toString().toUpperCase().trim());
			}
		}
	}

	public boolean removeSchemaAttribute(String attributeName) {
		if (attributeName != null) {
			return this.schemaAttributes.remove(attributeName.toUpperCase().trim());
		} else {
			return false;
		}
	}
	
	public void removeAllSchemaAttributes(){
		this.schemaAttributes.clear();
	}

	public boolean containsSchemaAttribute(String attributeName) {
		return this.schemaAttributes.contains(attributeName.toUpperCase().trim());
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = root.createElement("Expression");
			this.initSchemaAttributes(element, root);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			this.initSchemaAttributes(element, root);
		}
	}

	private void initSchemaAttributes(Element element, Document root) {
		Text attributeName;
		Element schemaAttribute;
		Iterator iter;

		if ((element != null) && (root != null)) {
			iter = this.iterSchemaAttributes();
			while (iter.hasNext()) {
				schemaAttribute = root.createElement("SchemaAttribute");
				attributeName = root.createTextNode(iter.next().toString());
				schemaAttribute.appendChild(attributeName);
				element.appendChild(schemaAttribute);
			}
		}
	}
}
