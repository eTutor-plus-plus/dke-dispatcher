package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Relation extends ExpressionImpl implements Expression {

	private String name;

	public Relation() {
		this.name = new String();
	}

	public boolean setName(String name) {
		if ((name != null) && (name.length() != 0)) {
			this.name = name;
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return this.name;
	}

	public void calculateSchema(Connection conn) throws SQLException, IllegalArgumentException {
		ResultSet rs = null;
		boolean validTable = false;
		DatabaseMetaData dbmd = null;

		this.removeAllSchemaAttributes();

		dbmd = conn.getMetaData();
		
		rs = dbmd.getColumns(null, null, this.getName().toUpperCase(), null);

		while (rs.next()) {
			validTable = true;
			this.addSchemaAttribute(rs.getString(4).trim());
		}
		rs.close();
		
		if (!validTable){
			throw new IllegalArgumentException("Relation '" + this.getName() + "' does not exist!");
		}
	}

	public Element createElement(Document root) {
		Element element;

		if (root != null) {
			element = super.createElement(root);
			element.setAttribute("xsi:type", "RELATION");
			element.setAttribute("name", this.name);
			return element;
		} else {
			return null;
		}
	}

	public void initElement(Element element, Document root) {
		if ((element != null) && (root != null)) {
			super.initElement(element, root);
			element.setAttribute("xsi:type", "RELATION");
			element.setAttribute("name", this.name);
		}
	}
}
