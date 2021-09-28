package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.exercise;

import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;

import etutor.modules.xquery.XQCoreManager;

/*
 * Created on 17.05.2005
 *
 */

/**
 * @author nitsche
 *  
 */
public class XQExerciseManagerHelper {

	private static final Logger LOGGER = Logger.getLogger(XQExerciseManagerHelper.class);
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");
	
	private static String readFile(String path) throws Exception {
		FileReader f = null;
		int c;
		StringBuffer temp = new StringBuffer();

		LOGGER.info("Reading file: " + path);

		try {
			f = new FileReader(path);
			while ((c = f.read()) != -1) {
				temp.append((char) c);
			}
			f.close();

			return temp.toString();
		} finally {
            if (f != null) {
                try {
                    f.close();
                } catch (IOException e) {
                	String msg = "Exception when closing file input stream.";
                    LOGGER.fatal(msg, e);
                }
            }
        }
	}

	private static Connection getConnection() throws Exception {
		return XQCoreManager.getInstance().getConnection();
	}

	private static void updateExerciseQuery(String path, int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String text = null;

		try {
			text = readFile(path);
		} catch (Exception e) {
			LOGGER.fatal("Fehler beim Lesen der Datei");
			e.printStackTrace();
			return;
		}

		try {
			conn = getConnection();
	
			String msg = new String();
			msg += "QUERY: " + "\n";
			msg += text;
			LOGGER.debug(msg);

			String sql = "UPDATE ETUTOR_XQUERY.exercise SET query = ? where id = " + id;
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, text);
			stmt.executeUpdate();
			
			LOGGER.info("UPDATE SUCCESSFUL.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void insertExercise(String path, int id, int urls, int gradings, int points) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String text = null;

		try {
			text = readFile(path);
		} catch (Exception e) {
			LOGGER.fatal("Fehler beim Lesen der Datei");
			e.printStackTrace();
			return;
		}

		try {
			conn = getConnection();

			String msg = new String();
			msg += "QUERY: " + "\n";
			msg += text;
			LOGGER.debug(msg);

			String sql = "";
			sql += "INSERT INTO ETUTOR_XQUERY.exercise VALUES ";
			sql += "(" + id + ",? " + ", " + gradings + ", " + points + ")";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, text);
			stmt.executeUpdate();
			
			LOGGER.info("UPDATE SUCCESSFUL.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Reads an XML document from a file and inserts it into the database as XMLType.
	 * Limitation w.r.t size of XML document!
	 * 
	 * @param path
	 * @param filename
	 * @param id
	 */
	public static void insertXML(String path, String filename, int id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		String doc;

		try {
			doc = readFile(path);
		} catch (Exception e) {
			LOGGER.fatal("Fehler beim Lesen der Datei");
			e.printStackTrace();
			return;
		}

		try {
			conn = getConnection();
			conn.setAutoCommit(false);
	
			String msg = new String();
			msg += "XML DOC: " + "\n";
			msg += doc;
			LOGGER.debug(msg);

			String sql = "";
			sql += "INSERT INTO ETUTOR_XQUERY.xmldocs (id, doc, filename) ";
			sql += "VALUES (" + id + ", XMLType(?), '" + filename + "')";

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, doc);
			stmt.executeUpdate();
			
			LOGGER.info("UPDATE SUCCESSFUL.");

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Reads an XML document from a file and inserts it into the database as XMLType by
	 * treating it as CLOB.
	 * No limitation w.r.t size of XML document!
	 * 
	 * @param path
	 * @param filename
	 * @param id
	 */
	public static void insertXMLAsClob(String path, String filename, int id) {
		Connection conn = null;
		CLOB myClob1 = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		PreparedStatement stmt3 = null;

		String doc;

		try {
			doc = readFile(path);
		} catch (Exception e) {
			LOGGER.fatal("Fehler beim Lesen der Datei");
			e.printStackTrace();
			return;
		}

		try {
			conn = getConnection();
			conn.setAutoCommit(false);
	
			String msg = new String();
			msg += "XML DOC: " + "\n";
			msg += doc;
			LOGGER.debug(msg);

			//inserts entry with dummy XMLType
			String sql = "";
			sql += "INSERT INTO ETUTOR_XQUERY.xmldocs (id, doc, filename) ";
			sql += "VALUES (" + id + ", XMLType('<a/>'), '" + filename + "')";

			stmt1 = conn.createStatement();
			stmt1.executeUpdate(sql);
			
			LOGGER.info("INSERT SUCCESSFUL.");

			//selects dummy XMLType
			sql = "";
			sql += "SELECT x.doc.getStringVal(), nvl(x.doc.getClobVal(), empty_clob()) ";
			sql += "FROM ETUTOR_XQUERY.xmldocs x ";
			sql += "WHERE x.id = " + id;

			stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery(sql);
			if (rs.next()) {
				Clob myClob = rs.getClob(2);
				myClob1 = getCLOB(doc, ((oracle.sql.CLOB) myClob)
						.getConnection());
			}
			
			LOGGER.info("UPDATE SUCCESSFUL.");

			//updates the XMLType
			if (myClob1 != null) {
				sql = "";
				sql += "UPDATE ETUTOR_XQUERY.xmldocs x SET x.doc = XMLType(?) ";
				sql += "WHERE x.id = " + id;

				stmt3 = conn.prepareStatement(sql);
				stmt3.setObject(1, myClob1);
				stmt3.executeUpdate();
			}

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (stmt3 != null) {
				try {
					stmt3.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	private static CLOB getCLOB(String xmlData, Connection conn)
			throws SQLException {
		CLOB tempClob = null;
		try {
			//If the temporary CLOB has not yet been created, create new
			tempClob = CLOB.createTemporary(conn, true, CLOB.DURATION_SESSION);

			//Open the temporary CLOB in readwrite mode to enable writing
			tempClob.open(CLOB.MODE_READWRITE);
			//Get the output stream to write
			Writer tempClobWriter = tempClob.getCharacterOutputStream();
			//Write the data into the temporary CLOB
			tempClobWriter.write(xmlData);

			//Flush and close the stream
			tempClobWriter.flush();
			tempClobWriter.close();

			//Close the temporary CLOB
			tempClob.close();
		} catch (SQLException sqlexp) {
			tempClob.freeTemporary();
			sqlexp.printStackTrace();
		} catch (Exception exp) {
			tempClob.freeTemporary();
			exp.printStackTrace();
		}
		return tempClob;
	}

	public static void main(String[] args) {
		try {

		    insertXMLAsClob("D:/temp2.xml", "handelskette3.xml", 1010);

		    /*
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq1.query", 13053, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq2.query", 13054, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq3.query", 13055, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq4.query", 13056, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq5.query", 13057, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq6.query", 13058, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq7.query", 13059, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq8.query", 13060, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq9.query", 13061, 1, 2, 1, 20);
			insertExercise("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq10.query", 13062, 1, 2, 1, 20);
			*/
		    /*
		    updateExerciseContent("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/task.htm", 13060);
		    */
		    /*
		    updateExerciseGroupContent("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq1.htm", 1207);
		    */
		    /*
		    updateExerciseQuery("I:/eTutor/ETutor_XQ_Datalog/exercises/xquery/SS05/xq2.query", 13054);
		    */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}