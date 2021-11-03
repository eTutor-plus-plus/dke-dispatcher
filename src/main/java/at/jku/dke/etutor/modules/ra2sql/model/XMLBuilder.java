package at.jku.dke.etutor.modules.ra2sql.model;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLBuilder {

	public XMLBuilder() {
		super();
	}

	public Document createDocument(Expression exp) {
		URL schema;
		Element root;
		Document raStatement;
		DocumentBuilder builder;

		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

			//CREATING DOCUMENT
			raStatement = builder.newDocument();

			//FINDING SCHEMA PATH
			schema = this.getClass().getResource("/etutor/resources/modules/ra2sql/relational_algebra.xsd");

			//CREATING ROOT
			root = raStatement.createElement("RAStatement");
			root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			root.setAttribute("xsi:noNamespaceSchemaLocation", "file:/" + schema.getPath());

			//APPENDING EXPRESSION
			root.appendChild(exp.createElement(raStatement));

			raStatement.appendChild(root);

			return raStatement;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}
