package at.jku.dke.etutor.modules.sql;

import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;



public class SQLExerciseManager /* implements ModuleExerciseManager */ {
	
	private Logger logger;

	public SQLExerciseManager() { 
		super();

		try {
			//java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			//this.conn = DriverManager.getConnection(CONN_URL, CONN_USER, CONN_PWD);
			//this.conn.setAutoCommit(true);

			this.logger = Logger.getLogger("at.jku.dke.etutor.modules.sql");

		} catch (Exception e){
			e.printStackTrace();		
		}
	}

	/**
	 * @return exerciseId of created exercise, -1 if an error occured
	 */
	public int createExercise(Serializable exercise, Map attributes, Map parameters) throws Exception {
		SQLExerciseBean sqlExercise;
		
		String sql;
		String msg;

		PreparedStatement pStmt;
		Statement stmt;
		Connection conn;
		
		Object trialDB;
		Object solution;
		Object submissionDB;

		pStmt = null;
		stmt = null;
		conn = null;
		
		if (exercise == null){
			msg = new String();
			msg = msg.concat("Passed exercise is null.");
			this.logger.log(Level.SEVERE, msg);
			return -1;
		}
		
		if (!(exercise instanceof SQLExerciseBean)){
			msg = new String();
			msg = msg.concat("Can not cast exercise bean of type ");
			msg = msg.concat(exercise.getClass().getName());
			msg = msg.concat(" to module-specific type ");
			msg = msg.concat(SQLExerciseBean.class.getName());
			this.logger.log(Level.SEVERE, msg);
			return -1;
		}
		
		sqlExercise = (SQLExerciseBean)exercise;
		
		try {
			//READING ATTRIBUTES
			solution = sqlExercise.getQuery();
			trialDB = sqlExercise.getSelected_trial_db();
			submissionDB = sqlExercise.getSelected_submission_db();

			if (solution == null){
				msg = new String();
				msg = msg.concat("Solution is null.");
				this.logger.log(Level.SEVERE, msg);
				return -1;
			}

			if (trialDB == null){
				msg = new String();
				msg = msg.concat("Trial DB is null.");
				this.logger.log(Level.SEVERE, msg);
				return -1;
			}

			if (submissionDB == null){
				msg = new String();
				msg = msg.concat("Submission DB is null.");
				this.logger.log(Level.SEVERE, msg);
				return -1;
			}
									
			//INSERTING SQL EXERCISE INTO DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
			//GET MAX ID FROM DB
			sql = "SELECT MAX(id) FROM exercises";
			msg = new String();
			msg = msg.concat("QUERY for select max id value:\n" + sql);
			this.logger.log(Level.INFO, msg);
			
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int exerciseID = 1;
			if (rs.next()) {
				exerciseID = rs.getInt(1) + 1;
			}
			
			sql = new String();
			sql = sql.concat("INSERT INTO exercises (id, submission_db, practise_db, solution) VALUES( ");
			sql = sql.concat(exerciseID + ", " + submissionDB + ", " + trialDB + ", ?)");
			
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, solution.toString());
			pStmt.executeUpdate();
			conn.commit();

			msg = new String();
			msg = msg.concat("QUERY for creating SQL Exercise:\n" + sql);
			this.logger.log(Level.INFO, msg);
			
			msg = new String();
			msg = msg.concat("Created SQL Exercise with id " + exerciseID);
			this.logger.log(Level.INFO, msg);
			
			return exerciseID;			

		} catch (SQLException e){
			msg = new String();
			msg = msg.concat("Stopped processing command 'createExercise'. ");
			msg = msg.concat("Exception during database access.");
			this.logger.log(Level.SEVERE, msg, e);
		} finally {
			if (pStmt != null){
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'createExercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (Exception e) {
					msg = new String();
					msg = msg.concat("Stopped processing command 'createExercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'createExercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
		}
		
		return -1;
	}
	
	public boolean deleteExercise(int exerciseID) throws Exception{
		int delExercisesCount = 0;
		
		String sql;
		String msg;

		Statement stmt;
		Connection conn;
		
		stmt = null;
		conn = null;
		
		try{
			sql = new String();
			sql = sql.concat("DELETE FROM exercises WHERE id = " + exerciseID);
									
			//DELETING SQL EXERCISE FROM DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			stmt = conn.createStatement();
			delExercisesCount = stmt.executeUpdate(sql);
			conn.commit();
			
			msg = new String();
			msg = msg.concat("Deleted SQL Exercise with id " + exerciseID);
			this.logger.log(Level.INFO, msg);
			
		} catch (SQLException e){
			msg = new String();
			msg = msg.concat("Stopped processing command 'delete exercise'. ");
			msg = msg.concat("Exception during database access.");
			this.logger.log(Level.SEVERE, msg, e);
		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'delete exercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (conn != null){
				try {
					conn.close(); 
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'delete exercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
		}
		
		return delExercisesCount > 0;
	}

	public Serializable fetchExercise(int exerciseID) throws Exception {
		SQLExerciseBean sqlExercise;
		
		String sql;
		String msg;
		TreeMap connections;
		
		ResultSet rset;
		Statement stmt;
		Connection conn;
		
		rset = null;
		stmt = null;
		conn = null;
		
		sqlExercise = null;
		
		try{
			sql = new String();
			sql = sql.concat("SELECT submission_db, practise_db, solution ");
			sql = sql.concat("FROM exercises ");
			sql = sql.concat("WHERE id = " + exerciseID);
									
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			if (rset.next()) {
				sqlExercise = new SQLExerciseBean();
				
				sqlExercise.setSelected_submission_db(rset.getString(1));
				sqlExercise.setSelected_trial_db(rset.getString(2));
				sqlExercise.setQuery(rset.getString(3));
			}

			if (sqlExercise != null) {
				connections = new TreeMap();
				rset.close();
				rset = null;
				rset = stmt.executeQuery("SELECT id, conn_user FROM connections ORDER BY id");
				while (rset.next()){
					connections.put(rset.getString("id"), rset.getString("conn_user"));
				}
				sqlExercise.setConnections(connections);
			}
			
		} catch (SQLException e){
			msg = new String();
			msg = msg.concat("Stopped processing command 'fetch exercise' ");
			msg = msg.concat("for id " + exerciseID + ". ");
			msg = msg.concat("Exception during database access.");
			this.logger.log(Level.SEVERE, msg, e);
		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'fetch exercise' ");
					msg = msg.concat("for id " + exerciseID + ". ");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (conn != null){
				try {
					conn.close(); 
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'fetch exercise' ");
					msg = msg.concat("for id " + exerciseID + ". ");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
		}

		if (sqlExercise != null) {
			msg = new String();
			msg = msg.concat("Fetched exercise with id " + exerciseID);
			this.logger.log(Level.INFO, msg);
		} else {
			msg = new String();
			msg = msg.concat("No exercise found with id " + exerciseID);
			this.logger.log(Level.WARNING, msg);
		}
		
		return sqlExercise;
	}

	public Serializable fetchExerciseInfo() throws Exception {
		SQLExerciseBean sqlExercise;
		
		String msg;
		TreeMap connections;
		
		ResultSet rset;
		Statement stmt;
		Connection conn;
		
		rset = null;
		stmt = null;
		conn = null;
		
		sqlExercise = null;
		sqlExercise = new SQLExerciseBean();
		connections = new TreeMap();
		
		try {
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT id, conn_user FROM connections ORDER BY id");
			while (rset.next()){
				connections.put(rset.getString("id"), rset.getString("conn_user"));
			}
			sqlExercise.setConnections(connections);
			
		} catch (SQLException e){
			msg = new String();
			msg = msg.concat("Stopped processing command 'fetch exercise info'. ");
			msg = msg.concat("Exception during database access.");
			this.logger.log(Level.SEVERE, msg, e);
		} finally {
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'fetch exercise info'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (conn != null){
				try {
					conn.close(); 
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'fetch exercise info'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
		}

		return sqlExercise;
	}

	public boolean modifyExercise(int exerciseID, Serializable exercise, Map attributes, Map parameters) throws Exception{
		SQLExerciseBean sqlExercise;
		
		String sql;
		String msg;

		PreparedStatement pStmt;
		Connection conn;
		
		Object trialDB;
		Object solution;
		Object submissionDB;

		pStmt = null;
		conn = null;
		
		if (exercise == null){
			msg = new String();
			msg = msg.concat("Passed exercise is null.");
			this.logger.log(Level.SEVERE, msg);
			return false;
		}
		
		if (!(exercise instanceof SQLExerciseBean)){
			msg = new String();
			msg = msg.concat("Can not cast exercise bean of type ");
			msg = msg.concat(exercise.getClass().getName());
			msg = msg.concat(" to module-specific type ");
			msg = msg.concat(SQLExerciseBean.class.getName());
			this.logger.log(Level.SEVERE, msg);
			return false;
		}
		
		sqlExercise = (SQLExerciseBean)exercise;
		
		try {
			//READING ATTRIBUTES
			solution = sqlExercise.getQuery();
			trialDB = sqlExercise.getSelected_trial_db();
			submissionDB = sqlExercise.getSelected_submission_db();

			if (solution == null){
				msg = new String();
				msg = msg.concat("Solution is null.");
				this.logger.log(Level.SEVERE, msg);
				return false;
			}

			if (trialDB == null){
				msg = new String();
				msg = msg.concat("Trial DB is null.");
				this.logger.log(Level.SEVERE, msg);
				return false;
			}

			if (submissionDB == null){
				msg = new String();
				msg = msg.concat("Submission DB is null.");
				this.logger.log(Level.SEVERE, msg);
				return false;
			}
			
			sql = new String();
			sql = sql.concat("UPDATE exercises ");
			sql = sql.concat("SET submission_db = " + submissionDB + ", practise_db = " + trialDB + ", solution = ? ");
			sql = sql.concat("WHERE id = " + exerciseID);

			msg = new String();
			msg = msg.concat("QUERY for creating SQL Exercise:\n" + sql);
			this.logger.log(Level.INFO, msg);
									
			//INSERTING SQL EXERCISE INTO DATABASE
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		
			conn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, solution.toString());
			pStmt.executeUpdate();
			conn.commit();
			
			msg = new String();
			msg = msg.concat("Updated SQL exercise with id " + exerciseID);
			this.logger.log(Level.INFO, msg);
			
			return true;			

		} catch (SQLException e){
			msg = new String();
			msg = msg.concat("Stopped processing command 'updateExercise'. ");
			msg = msg.concat("Exception during database access.");
			this.logger.log(Level.SEVERE, msg, e);
		} finally {
			if (pStmt != null){
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'updateExercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
			if (conn != null){
				try {
					conn.close(); 
				} catch (SQLException e){
					msg = new String();
					msg = msg.concat("Stopped processing command 'updateExercise'. ");
					msg = msg.concat("Exception during database access.");
					this.logger.log(Level.SEVERE, msg, e);
				}
			}
		}
		
		return false;
	}
	
	private static SQLExerciseComparison compareExercises(String[] exerciseIDs, Connection conn1, Connection conn2){
		String row1;
		String row2;
		
		String currSolution;
		String currExerciseID;
		SQLExerciseComparison comparison;

		int columnCount;
		int rset1RowsCount;
		int rset2RowsCount;
		
		boolean rset1HasNoRows;
		boolean rset2HasNoRows;
		boolean rowsAreIdentical;
		
		Statement stmt1 = null;
		ResultSet rset1 = null;
		Statement stmt2 = null;
		ResultSet rset2 = null;
		
		Connection exercisesConn = null;
		ResultSet exercisesRset = null;
		Statement exercisesStmt = null;

		comparison = new SQLExerciseComparison();
		
		try {
			comparison.setConn1Name(conn1.getCatalog());
			comparison.setConn2Name(conn2.getCatalog());

			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			exercisesConn = DriverManager.getConnection(SQLConstants.CONN_URL, SQLConstants.CONN_USER, SQLConstants.CONN_PWD);
			exercisesStmt = exercisesConn.createStatement();
			exercisesRset = exercisesStmt.executeQuery("SELECT id, solution FROM exercises WHERE id IN " + Arrays.asList(exerciseIDs).toString().replaceFirst("\\[", "(").replaceFirst("\\]", ")"));
			
			while (exercisesRset.next()){
				currExerciseID = exercisesRset.getString("ID");
				currSolution = exercisesRset.getString("SOLUTION");
				if (currSolution.endsWith(";")){
					currSolution = currSolution.substring(0, currSolution.length()-1);
				}

				try {
					stmt1 = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rset1 = stmt1.executeQuery(currSolution);
					
					stmt2 = conn2.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
					rset2 = stmt2.executeQuery(currSolution);

					//CHECK FOR ZERO ROWS RETURNED
					rset1.last();
					rset2.last();
					
					rset1RowsCount = rset1.getRow();
					rset2RowsCount = rset2.getRow();
					
					rset1HasNoRows = (rset1RowsCount == 0);
					rset2HasNoRows = (rset2RowsCount == 0);
					
					if (rset1HasNoRows || rset2HasNoRows){
						comparison.addZeroRowsReturningExercise(currExerciseID, rset1HasNoRows, rset2HasNoRows);
					} else {
						//CHECK FOR IDENTICAL ROWS
						if (rset1RowsCount == rset2RowsCount){
							rset1.beforeFirst();
							rset2.beforeFirst();
							rowsAreIdentical = true;
							columnCount = rset1.getMetaData().getColumnCount();
							
							while (rset1.next() && rowsAreIdentical){
								rset2.next();
								
								row1 = new String();
								row2 = new String();

								for (int i=1; i<=columnCount; i++){
									row1 = row1.concat(" " + rset1.getString(i).trim().toUpperCase());
									row2 = row2.concat(" " + rset2.getString(i).trim().toUpperCase());
								}
								rowsAreIdentical = (row1.equals(row2));
							}
						
							if (rowsAreIdentical) {
								comparison.addExercisesReturningIdenticalRows(currExerciseID);
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (rset1 != null) rset1.close();
						if (stmt1 != null) stmt1.close();
						if (rset2 != null) rset2.close();
						if (stmt2 != null) stmt2.close();
					} catch (SQLException e){
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (exercisesRset != null) exercisesRset.close();
				if (exercisesStmt != null) exercisesStmt.close();
				if (exercisesConn != null) exercisesConn.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return comparison;
	}
	
	public String generateHtml(Serializable exercise, Locale locale) {
		//TODO: implement interface method
		return null;
	}
	
	public static void main(String[] args){
		String path;
		String[] exerciseIDs;
		Connection conn1 = null;
		Connection conn2 = null;
		FileWriter writer = null;
		SQLExerciseComparison comp;		
		
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			
			path = "D:/comparison.html";
			exerciseIDs = new String[]{"69", "70", "71", "72"};
			conn1 = DriverManager.getConnection(SQLConstants.CONN_URL, "SQL_TRIAL_BEGIN", "TRIAL");
			conn2 = DriverManager.getConnection(SQLConstants.CONN_URL, "SQL_SUBMISSION_BEGIN", "SUBMISSION");

			comp = compareExercises(exerciseIDs, conn1, conn2);
			writer = new FileWriter(path);
			writer.write(comp.printHTML());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) writer.close();
				if (conn1 != null) conn1.close();
				if (conn2 != null) conn2.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
}
