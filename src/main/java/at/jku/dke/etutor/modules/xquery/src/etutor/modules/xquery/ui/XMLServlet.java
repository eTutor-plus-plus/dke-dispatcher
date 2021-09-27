package etutor.modules.xquery.ui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import etutor.modules.xquery.XQCoreManager;

//TODO: This servlet has dependencies on the core, although implementing functionalities which should be
//performed by the xquery module. E.g., xml documents should not be fetched using the core JDBC connection
//but by sending a request to a datalog module stub which returns the datalog facts using the module specific
//connection. On the other hand, if xml documents are managed globally by the core, the xmldocs table
//should not be in charge of the xquery module.
public class XMLServlet extends HttpServlet {

	public static final String DUMMY_ATTRIBUTE = "dummy-attr";
	/**
	 * mandatory parameter for identifying an xml document 
	 */
	public static final String PARAM_DOC_ID = "id";
	/**
	 * optional parameter which implies that the returned xml document has to be distorted;
	 * this parameter is expected to be appended to the value of the 'id' parameter;
	 * reason for not using it as a separate parameter is that the IPSI-XQuery tool (v1.3.2)
	 * fails when resolving an url with parameters concatenated with ampersands. 
	 */
	public static final String PARAM_DOC_DISTORTION = "_enc";
	
	private Logger logger;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String msg;
		
		msg = new String();
		msg = msg.concat("Start processing request.");
		this.logger.log(Level.INFO, msg);
		
		this.doFetch(request, response);
	}
	
	private void doFetch(HttpServletRequest request, HttpServletResponse response) {
		String xml = null;
		String filename = null;
		String msg = null;
		Connection conn = null;

		int id;
		String id_value = null;
		boolean distort = false;
		
		id_value = request.getParameter(PARAM_DOC_ID);
		
		if (id_value != null && id_value.endsWith(PARAM_DOC_DISTORTION)) {
			distort = true;
			int index = id_value.lastIndexOf(PARAM_DOC_DISTORTION);
			id_value = id_value.substring(0, index);
		}
		
		msg = new String();
		msg += "Try fetching xml string (" + PARAM_DOC_ID + " = ";
		msg += id_value + ", distorting = " + distort + ").";
		this.logger.log(Level.INFO, msg);

		try {
			id = Integer.parseInt(id_value);
		} catch (NumberFormatException e1) {
			msg = msg.concat("Stopped fetching xml string. ");
			if (id_value == null) {
				msg = msg.concat("Parameter '" + PARAM_DOC_ID + "' was not passed.");	
			} else {
				msg = msg.concat("Parameter '" + PARAM_DOC_ID + "': ");
				msg = msg.concat(id_value + " is not a valid integer.");
			}
			this.logger.log(Level.INFO, msg);
			this.showError(response);
			return;
		}
		
		try {
            conn = XQCoreManager.getInstance().getConnection();
			//CLOB treatment within transaction boundaries!
			conn.setAutoCommit(false);
            xml = getXMLContent(id, conn);
            if (xml == null) {
    			msg = "No xml content specified for id " + id + ". ";
    			msg += "Returning 404 status.";
    			this.logger.log(Level.INFO, msg);
    			this.showError(response);
    			return;
    		}

    		try {
    			filename = getXMLFilename(id, conn);
    		} catch (Exception e){
    			msg = new String();
            	msg += "An exception was thrown when trying to fetch xml ";
            	msg += "filename for id " + id + ".";
                this.logger.log(Level.SEVERE, msg, e);
    			//go on
    		}

    		conn.commit();
		} catch (Exception e){
			msg = new String();
            msg += "An exception was thrown when trying to fetch xml ";
            msg += "content for id " + id + ".";
			this.logger.log(Level.SEVERE, msg, e);
			this.showInternalError(response);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					logger.log(Level.SEVERE, "", ex);
				}
			}
			return;
		} finally {
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					this.logger.log(Level.SEVERE, "", e);
				}
			}
		}

		if (filename == null) {
			filename = "etutor.xml";
			msg = new String();
			msg += "Filename is not specified, setting filename to '";
			msg += filename + "'.";
			this.logger.log(Level.WARNING, msg);
		}

		try {
			response.setHeader("Content-disposition" , "attachment; filename=" + filename);
			response.setContentType("text/xml");
			Writer writer = response.getWriter();
			if (distort) {
				try {
					//writer is used immediately, so if an exception occurs
					//parts of the result might have already been flushed 
					distortSAX(xml, writer);
				} catch (Exception e) {
					msg = new String();
					msg += "An exception was thrown when distorting the xml content ";
					msg += "by adding dummy attributes.";
					this.logger.log(Level.SEVERE, msg);
					this.showInternalError(response);
					return;
				}
			} else {
				writer.write(xml);
			}
			writer.flush();
		} catch (Exception e) {
			msg = new String();
			msg = msg.concat("Stopped writing fetched xml string to response.");
			this.logger.log(Level.SEVERE, msg, e);
			this.showInternalError(response);
			return;
		}
	}

	/**
	 * Returns the xml content according to a unique id.
	 * 
	 * @param exerciseID
	 * @return the content. If no xml content was found according to the specified id, 
	 * <code>null</code> is returned.
	 * @throws Exception If any exception occured when trying to fetch the xml content from
	 * the database.
	 */
	private String getXMLContent(int id, Connection conn) throws Exception {
		String msg;
		String query;
		Statement stmt = null;
		ResultSet rset = null;
		String content = null;
		
		//TODO: which connection, table 'xmldocs' in core db?
		
		query = "SELECT x.doc.getClobVal() xmlcontent ";
		query += "FROM etutor_xquery.xmldocs x ";
		query += "WHERE x.id = " + id + " ";
		
		try {
            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            
            while (rset.next() && content == null) {
                Clob clobSolution = rset.getClob(1);
                if (clobSolution != null) {
                	long clobLength = clobSolution.length();
                	content = clobSolution.getSubString(1, (int)clobLength);
                	/*
                	//algorithm if clobLength is greater than integer maximum value and casting to int is erroneous
                	content = "";
        			int maxInt = Integer.MAX_VALUE;
        			long clobLength = clobSolution.length();
        			long pos = 1;
        			while (pos <= clobLength) {
        				long length = Math.min(maxInt, clobLength - pos + 1);
        				//length is an int value
        				content += clobSolution.getSubString(pos, (int)length);
        				pos += length;
        			}
        			*/
                }

            }
            return content;
        } finally {
            if (rset != null){
			    try {
			        rset.close();
			    } catch (SQLException e){
	            	msg = new String();
	            	this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (stmt != null){
			    try {
			        stmt.close();
			    } catch (SQLException e){
	            	msg = new String();
	            	this.logger.log(Level.SEVERE, msg, e);
				}
			}
        }
	}
	
	/**
	 * Returns the filename of an xml document, which can be downloaded from the web application.
	 * 
	 * @param exerciseID
	 * @return
	 * @throws Exception If any exception occured when trying to fetch the filename for the 
	 * xml content from the database
	 */
	private String getXMLFilename(int id, Connection conn) throws Exception {
		String msg;
		String query;
		Statement stmt = null;
		ResultSet rset = null;
		String filename = null;
		
		//TODO: which connection, table 'xmldocs' in core db?
		query = "SELECT x.filename ";
		query += "FROM etutor_xquery.xmldocs x ";
		query += "WHERE x.id = " + id;
		
		try {

            stmt = conn.createStatement();
            rset = stmt.executeQuery(query);
            while (rset.next() && filename == null) {
                filename = rset.getString("filename");
            }
            return filename;
        } finally {
            if (rset != null){
			    try {
			        rset.close();
			    } catch (SQLException e){
			    	msg = new String();
	            	this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (stmt != null){
			    try {
			        stmt.close();
			    } catch (SQLException e){
			    	msg = new String();
	            	this.logger.log(Level.SEVERE, msg, e);
				}
			}
        }
	}

	/**
	 * @param xml
	 * @return
	 * @throws Exception
	 * @deprecated more effective way to manipulate the XML document is by 
	 * using a SAX parser rather than building a DOM tree
	 */
	private String distortDOM(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		addDummyAttributes(doc.getDocumentElement());
		
		StringWriter writer = new StringWriter();
		Source source = new DOMSource(doc);
		Result result = new StreamResult(writer);
		TransformerFactory xformFactory = TransformerFactory.newInstance();
		Transformer xformer = xformFactory.newTransformer();
		xformer.transform(source, result);
		return writer.toString();
	}
	
	private void addDummyAttributes(Element element) {
		try {
			if (element == null) {
				return;
			}
			element.setAttribute(DUMMY_ATTRIBUTE, "");
			NodeList list = element.getChildNodes();
			if (list != null) {
				for (int i = 0; i < list.getLength(); i++) {
					Node node = null;
					if ((node = list.item(i)) instanceof Element) {
						addDummyAttributes((Element)node);
					}
				}
			}
		} catch (DOMException e) {
			throw e;
		}
	}
	
	private void distortSAX(String xml, Writer out) throws Exception {
		SAXParserFactory saxFactory;
		SAXParser parser;
		DefaultHandler handler;
		ByteArrayInputStream in = null;
		try {
			in = new ByteArrayInputStream(xml.getBytes());
			handler = new XMLDistortionHandler(out, DUMMY_ATTRIBUTE);
	        // Use the default (non-validating) parser
			saxFactory = SAXParserFactory.newInstance();
			parser = saxFactory.newSAXParser();
			//SAXSource saxSource = new SAXSource(new InputSource(in));
			parser.parse(in, handler);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	private void showError(HttpServletResponse response) {
		String msg;
		try{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (Exception ex){
			msg = new String();
			this.logger.log(Level.SEVERE, msg, ex);
		}
	}
	
	private void showInternalError(HttpServletResponse response) {
		String msg;
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} catch (Exception ex) {
			msg = new String();
			this.logger.log(Level.SEVERE, msg, ex);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#destroy()
	 */
	public void destroy() {
		super.destroy();
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig arg0) throws ServletException {
		try{
			this.logger = Logger.getLogger("etutor.modules.xquery");
		} catch (Exception e){
			e.printStackTrace();
		}
		super.init(arg0);
	}
}