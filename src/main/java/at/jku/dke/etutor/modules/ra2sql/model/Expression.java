package at.jku.dke.etutor.modules.ra2sql.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface Expression {

	public Iterator<String> iterSchemaAttributes();

	public boolean addSchemaAttribute(String schemaAttributeName);

	public void setSchemaAttributes(Vector<String> v);

	public boolean removeSchemaAttribute(String attributeName);
	
	public void removeAllSchemaAttributes();

	public boolean containsSchemaAttribute(String attributeName);

	public Element createElement(Document root);

	public void initElement(Element element, Document root);

	public abstract void calculateSchema(Connection conn) throws SQLException, IllegalArgumentException;
}