package at.jku.dke.etutor.modules.sql.ui;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SQLQueryServlet extends HttpServlet implements SingleThreadModel {

	private Logger cmLogger;
	private Logger umLogger;
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url;
		String pwd;
		String user;
		String query;
		String caption;
		ServletOutputStream out;
		Statement stmt;
		ResultSet rset;
		Connection conn;
		ResultSetMetaData rsmd;
		String connData_PWD;
		String connData_URL;
		String connData_USER;
		String connData_QUERY;
		ResultSet connData_RSET;
		Statement connData_STMT;
		Connection connData_CONN;

		pwd = null;
		conn = null;
		stmt = null;
		rset = null;
		connData_CONN = null;
		connData_RSET = null;
		connData_STMT = null;
		url = request.getParameter("url");
		user = request.getParameter("user");
		query = request.getParameter("query");
		caption = request.getParameter("caption");
		out = response.getOutputStream();
		
		if ((user != null) && (user.length() != 0)) {
			if ((url != null) && (url.length() != 0)) {
				if ((query != null) && (query.length() != 0)) {
					try {
						connData_PWD = "anita";
						connData_USER = "hofer";
						connData_URL = "jdbc:oracle:thin:@mond.dke.uni-linz.ac.at:1500:etutor";

						Class.forName("oracle.jdbc.driver.OracleDriver");
						connData_CONN = DriverManager.getConnection(connData_URL, connData_USER, connData_PWD);
						connData_STMT = connData_CONN.createStatement();
						connData_QUERY = new String();
						connData_QUERY = connData_QUERY.concat("SELECT	connpwd AS pwd ");
						connData_QUERY = connData_QUERY.concat("FROM	connectiondb ");
						connData_QUERY = connData_QUERY.concat("WHERE	connuser = '" + user + "' AND");
						connData_QUERY = connData_QUERY.concat("		connstring = '" + url + "'");
						connData_RSET = connData_STMT.executeQuery(connData_QUERY);

						while (connData_RSET.next()) {
							pwd = connData_RSET.getString("pwd");
						}

					} catch (SQLException e) {
						cmLogger.log(Level.SEVERE, "Exception while trying to attempt database for connection information.", e);
						out.println("An internal error occured. Please inform the system administrator.");
						return;
					} catch (ClassNotFoundException e) {
						cmLogger.log(Level.SEVERE, "Could not find class for JDBC driver.", e);
						out.println("An internal error occured. Please inform the system administrator.");
					} finally {
						if (connData_RSET != null) {
							try {
								connData_CONN.close();
							} catch (SQLException e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
						if (connData_STMT != null) {
							try {
								connData_CONN.close();
							} catch (SQLException e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
						if (connData_CONN != null) {
							try {
								connData_CONN.close();
							} catch (SQLException e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
					}

					try {
						if (pwd != null) {
							conn = DriverManager.getConnection("jdbc:oracle:thin:@" + url, user, pwd);

							stmt = conn.createStatement();
							rset = stmt.executeQuery(query);
							rsmd = rset.getMetaData();

							out.println("	<html>");
							out.println("	<head>");
							out.println("		<link rel='stylesheet' href='/etutor/css/etutor.css'></link>");
							out.println("	</head>");
							out.println("	<body>");

							out.println("	<p>");
							if (caption != null){
								out.println(caption);
							} else {
								out.println("Your query \"<i>" + query + "</i>\" returned the following result:");
							}
							out.println("	</p>");

							out.println("<table rules='all' border='1'>");
							out.println("	<thead>");

							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								out.println("	<th>" + rsmd.getColumnName(i) + "</th>");
							}

							out.println("	</thead>");
							out.println("	<tbody>");

							while (rset.next()) {
								out.println("		<tr>");
								for (int i = 1; i <= rsmd.getColumnCount(); i++) {
									out.println("		<td>" + rset.getString(i) + "</td>");
								}
								out.println("		</tr>");
							}

							out.println("	</tbody>");
							out.println("</table>");
							
							out.println("	</body>");
							out.println("	</html>");
							
						} else {
							//invalid pwd
							umLogger.log(Level.FINE, "Could not find password for passed combination of user name and host name.");
							out.println("Could not find password for passed combination of user name and host name.");
						}

					} catch (SQLException e) {
						cmLogger.log(Level.SEVERE, "Exception while trying to attempt database.", e);
						out.println("An internal error occured. Please inform the system administrator.");
						return;
					} finally {
						if (rset != null) {
							try {
								rset.close();
							} catch (Exception e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
						if (stmt != null) {
							try {
								stmt.close();
							} catch (Exception e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
						if (conn != null) {
							try {
								conn.close();
							} catch (Exception e) {
								cmLogger.log(Level.SEVERE, "Exception when closing database resources.", e);
							}
						}
					}
				} else {
					//invalid query
					umLogger.log(Level.INFO, "Can not execute query. Empty query was passed!");
					out.println("Can not execute query. Empty query was passed!");
				}
			} else {
				//invalid url
				umLogger.log(Level.INFO, "Can not establish connection to database. Empty host name was passed!");
				out.println("Can not establish connection to database. Empty host name was passed!");
			}
		} else {
			//invalid user
			umLogger.log(Level.INFO, "Can not establish connection to database. Empty user name was passed!");
			out.println("Can not establish connection to database. Empty user name was passed!");
		}
	}

	public void init(ServletConfig arg0) throws ServletException {
		try {
			this.umLogger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception e){
			e.printStackTrace();
		}
		try {
			this.cmLogger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception e){
			e.printStackTrace();
		}
		super.init(arg0);
	}
}