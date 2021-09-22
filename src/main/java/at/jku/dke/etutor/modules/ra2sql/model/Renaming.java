package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Renaming extends UnaryOperatorImpl implements UnaryOperator {

	private HashMap attributeAliases;

	public Renaming() {
		this.attributeAliases = new HashMap();
	}

	public boolean isAlias(String name) {
		if (name != null) {
			return this.attributeAliases.containsValue(name.toUpperCase());
		} else {
			return false;
		}
	}

	public boolean addAttributeAlias(String attributeName, String alias) {
		if ((attributeName != null) && (alias != null)) {
			alias = alias.toUpperCase().trim();
			attributeName = attributeName.toUpperCase().trim();
			return this.attributeAliases.put(attributeName, alias) == null;
		} else {
			return false;
		}
	}

	public String getAliasForAttribute(String attributeName) {
		if (attributeName != null) {
			if (this.attributeAliases.containsKey(attributeName)) {
				return this.attributeAliases.get(attributeName.toUpperCase().trim()).toString();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public Iterator iterAliasedAttributes() {
		return this.attributeAliases.keySet().iterator();
	}

	public boolean removeAttributeAlias(String attributeName) {
		if (attributeName != null) {
			return this.attributeAliases.remove(attributeName.toUpperCase().trim()) == null;
		} else {
			return false;
		}
	}

	public void removeAllAttributeAliases() {
		this.attributeAliases.clear();
	}

	public boolean definesAliasForAttribute(String attributeName) {
		if (attributeName != null) {
			return this.attributeAliases.containsKey(attributeName.toUpperCase().trim());
		} else {
			return false;
		}
	}

	public String getAttributeForAlias(String aliasName) {
		if ((aliasName != null) && (aliasName.length() != 0)) {
			Iterator i;
			String attributeName;

			i = this.attributeAliases.keySet().iterator();
			while (i.hasNext()) {
				aliasName = aliasName.toUpperCase().trim();
				attributeName = i.next().toString().toUpperCase().trim();

				if (this.attributeAliases.get(attributeName).toString().equals(aliasName)) {
					return attributeName;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	public void calculateSchema(Connection conn) throws SQLException {
		Iterator aliases;
		Iterator attributes;

		super.calculateSchema(conn);

		aliases = this.attributeAliases.values().iterator();
		attributes = this.attributeAliases.keySet().iterator();

		while (attributes.hasNext()) {
			this.removeSchemaAttribute(attributes.next().toString().toUpperCase().trim());
		}

		while (aliases.hasNext()) {
			this.addSchemaAttribute(aliases.next().toString().toUpperCase().trim());
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "RENAMING");
			this.initAliases(element, root);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "RENAMING");
			this.initAliases(element, root);
		}
	}

	private void initAliases(Element element, Document root) {
		String temp;
		Element alias;
		Iterator attributes;

		if ((element != null) && (root != null)) {
			attributes = this.attributeAliases.keySet().iterator();

			while (attributes.hasNext()) {
				temp = attributes.next().toString();

				alias = root.createElement("Alias");
				alias.setAttribute("attribute", temp);
				alias.setAttribute("alias", this.attributeAliases.get(temp).toString());
				element.appendChild(alias);
			}
		}
	}
}
