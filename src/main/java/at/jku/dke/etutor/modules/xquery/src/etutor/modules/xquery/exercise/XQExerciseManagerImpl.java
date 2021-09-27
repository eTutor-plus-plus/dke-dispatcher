package etutor.modules.xquery.exercise;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import etutor.modules.xquery.GradingException;
import etutor.modules.xquery.InvalidResourceException;
import etutor.modules.xquery.ParameterException;
import etutor.modules.xquery.XQCoreManager;
import etutor.modules.xquery.analysis.UrlContentMap;
import etutor.modules.xquery.grading.XQGradingConfig;
import etutor.modules.xquery.util.PropertyFile;

public class XQExerciseManagerImpl extends UnicastRemoteObject implements XQExerciseManager {
	
	private static final Logger LOGGER = Logger.getLogger(XQExerciseManagerImpl.class);
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");

	public XQExerciseManagerImpl() throws RemoteException { 
		super();
        //evaluator implementation is bound to RMI registry at startup process; 
        //requesting core manager instance causes creation of singleton
        //and initialization of basic resources with configuration errors
        //being logged
        XQCoreManager.getInstance();
	}
	
	public int createExercise(Serializable exercise, Map attributes, Map parameters) throws Exception {
		List sortedNodes;
		UrlContentMap urlMap;
		Iterator it;
		String url;
		String hiddenUrl;
		String sortedNodesTable;
		String urlsTable;
		String exerciseTable;
		String sql;
		String msg;
		Connection conn;
		PreparedStatement pStmt;
		XQCoreManager coreManager;
		XQExerciseBean xqExercise;
		PropertyFile properties;
		int index;
		
		pStmt = null;
		Statement stmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try creating XQuery exercise with ID ";
		msg += LINE_SEP + exercise;
        LOGGER.info(msg);
        
		if (exercise == null){
			msg = new String();
			msg += "Passed exercise is null.";
			LOGGER.fatal(msg);
			return -1;
		}
		
		if (!(exercise instanceof XQExerciseBean)){
			msg = new String();
			msg += "Can not cast exercise bean of type ";
			msg += exercise.getClass().getName();
			msg += " to module-specific type ";
			msg += XQExerciseBean.class.getName();
			LOGGER.fatal(msg);
			return -1;
		}
		
        //Fetch properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(XQCoreManager.KEY_TABLE_EXERCISE);
            sortedNodesTable = properties.loadProperty(XQCoreManager.KEY_TABLE_SORTINGS);
            urlsTable = properties.loadProperty(XQCoreManager.KEY_TABLE_URLS);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
        
		xqExercise = (XQExerciseBean)exercise;
		
		try {
			conn = coreManager.getConnection();
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.setAutoCommit(false);
			
			//GET MAX ID FROM DB
			sql = "SELECT MAX(id) FROM "+ exerciseTable;
			msg = new String();
			msg = msg.concat("QUERY for select max id value:\n" + sql);
			LOGGER.info(msg);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int exerciseID = 1;
			if (rs.next()) {
				exerciseID = rs.getInt(1) + 1;
			}
			
			sql = new String();
			sql += "INSERT INTO	" + exerciseTable + " e " + LINE_SEP;
			sql += "(e.id, e.query, e.gradings, e.points)" + LINE_SEP;
			sql += "VALUES (?, ?, ?, ?)" + LINE_SEP;
			pStmt = conn.prepareStatement(sql);
			index = 1;
			pStmt.setInt(index++, exerciseID);
			pStmt.setString(index++, xqExercise.getQuery());
			pStmt.setNull(index++, Types.INTEGER);
			if (xqExercise.getPoints() != null) {
				pStmt.setDouble(index++, xqExercise.getPoints().doubleValue());
			} else {
				pStmt.setNull(index++, Types.DOUBLE);
			}
			pStmt.executeUpdate();
			
			sortedNodes = xqExercise.getSortedNodes();
			if (sortedNodes != null && sortedNodes.size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + sortedNodesTable + " s " + LINE_SEP;
				sql += "(s.exercise, s.xpath)" + LINE_SEP;
				sql += "VALUES (?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < sortedNodes.size(); i++) {
					index = 1;
					pStmt.setInt(index++, exerciseID);
					pStmt.setString(index++, (String)sortedNodes.get(i));
					pStmt.executeUpdate();
				}
			}
			
			urlMap = xqExercise.getUrls();
			if (urlMap != null && urlMap.aliasSet().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + urlsTable + " u " + LINE_SEP;
				sql += "(u.exercise, u.url, u.hidden_url)" + LINE_SEP;
				sql += "VALUES (?, ?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				
				it = urlMap.aliasSet().iterator();
				while (it.hasNext()) {
					hiddenUrl = (String)it.next();
		        	url = urlMap.getUrl(hiddenUrl);
					index = 1;
					pStmt.setInt(index++, exerciseID);
					pStmt.setString(index++, url);
					pStmt.setString(index++, hiddenUrl);
					pStmt.executeUpdate();
				}
			}
			
			conn.commit();

			msg = new String();
			msg += "Created XQuery exercise with id " + exerciseID;
			LOGGER.info(msg);
			return exerciseID;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'createExercise'. ";
			LOGGER.fatal(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.fatal(msg, e2);
				}
			}
		} finally {
			if (pStmt != null) {
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
		}
		return -1;
	}
	
	public boolean modifyExercise(int exerciseId, Serializable exercise, Map attributes, Map parameters) throws Exception{
		List sortedNodes;
		UrlContentMap urlMap;
		Iterator it;
		String url;
		String hiddenUrl;
		String sortedNodesTable;
		String urlsTable;
		String exerciseTable;
		String sql;
		String msg;
		Connection conn;
		PreparedStatement pStmt;
		XQCoreManager coreManager;
		XQExerciseBean xqExercise;
		PropertyFile properties;
		int index;
		
		pStmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try updating XQuery exercise with ID ";
		msg += exerciseId + ":" + LINE_SEP + exercise;
        LOGGER.info(msg);
        
		if (exercise == null){
			msg = new String();
			msg += "Passed exercise is null.";
			LOGGER.fatal(msg);
			return false;
		}
		
		if (!(exercise instanceof XQExerciseBean)){
			msg = new String();
			msg += "Can not cast exercise bean of type ";
			msg += exercise.getClass().getName();
			msg += " to module-specific type ";
			msg += XQExerciseBean.class.getName();
			LOGGER.fatal(msg);
			return false;
		}
		
        //Fetch properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(XQCoreManager.KEY_TABLE_EXERCISE);
            sortedNodesTable = properties.loadProperty(XQCoreManager.KEY_TABLE_SORTINGS);
            urlsTable = properties.loadProperty(XQCoreManager.KEY_TABLE_URLS);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
        
		xqExercise = (XQExerciseBean)exercise;

		try {
			conn = coreManager.getConnection();
			conn.setAutoCommit(false);
			
			sql = new String();
			sql += "UPDATE 	" + exerciseTable + " e " + LINE_SEP;
			sql += "SET 	e.query = ?, e.gradings = ?, e.points = ? " + LINE_SEP;
			sql += "WHERE 	e.id = ?" + LINE_SEP;
			pStmt = conn.prepareStatement(sql);
			index = 1;
			pStmt.setString(index++, xqExercise.getQuery());
			pStmt.setNull(index++, Types.INTEGER);
			if (xqExercise.getPoints() != null) {
				pStmt.setDouble(index++, xqExercise.getPoints().doubleValue());
			} else {
				pStmt.setNull(index++, Types.DOUBLE);
			}
			pStmt.setInt(index++, exerciseId);
			pStmt.executeUpdate();
			
			sql = new String();
			sql += "DELETE FROM " + sortedNodesTable + " s " + LINE_SEP;
			sql += "WHERE s.exercise = " + exerciseId + LINE_SEP;
			pStmt.close();
			pStmt = null;
			pStmt = conn.prepareStatement(sql);
			pStmt.executeUpdate();
			
			sql = new String();
			sql += "DELETE FROM " + urlsTable + " u " + LINE_SEP;
			sql += "WHERE u.exercise = " + exerciseId + LINE_SEP;
			pStmt.close();
			pStmt = null;
			pStmt = conn.prepareStatement(sql);
			pStmt.executeUpdate();
			
			sortedNodes = xqExercise.getSortedNodes();
			if (sortedNodes != null && sortedNodes.size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + sortedNodesTable + " s " + LINE_SEP;
				sql += "(s.exercise, s.xpath)" + LINE_SEP;
				sql += "VALUES (?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < sortedNodes.size(); i++) {
					index = 1;
					pStmt.setInt(index++, exerciseId);
					pStmt.setString(index++, (String)sortedNodes.get(i));
					pStmt.executeUpdate();
				}
			}
			
			urlMap = xqExercise.getUrls();
			if (urlMap != null && urlMap.aliasSet().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + urlsTable + " u " + LINE_SEP;
				sql += "(u.exercise, u.url, u.hidden_url)" + LINE_SEP;
				sql += "VALUES (?, ?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				
				it = urlMap.aliasSet().iterator();
				while (it.hasNext()) {
					hiddenUrl = (String)it.next();
		        	url = urlMap.getUrl(hiddenUrl);
					index = 1;
					pStmt.setInt(index++, exerciseId);
					pStmt.setString(index++, url);
					pStmt.setString(index++, hiddenUrl);
					pStmt.executeUpdate();
				}
			}
			
			conn.commit();

			msg = new String();
			msg += "Updated XQuery exercise with id " + exerciseId;
			LOGGER.info(msg);
			return true;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'modifyExercise'. ";
			LOGGER.fatal(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.fatal(msg, e2);
				}
			}
		} finally {
			if (pStmt != null) {
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
			try {
				conn.close();
			} catch (SQLException e){
				msg = new String();
				LOGGER.fatal(msg, e);
			}
		}
		return false;
	}
	
	public boolean deleteExercise(int exerciseId) throws Exception {
		String sortedNodesTable;
		String urlsTable;
		String exerciseTable;
		String sql;
		String msg;
		Connection conn;
		Statement stmt;
		XQCoreManager coreManager;
		PropertyFile properties;
		int count;

		stmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try deleting exercise with ID " + exerciseId;
        LOGGER.info(msg);
        
        //Fetch properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(XQCoreManager.KEY_TABLE_EXERCISE);
            sortedNodesTable = properties.loadProperty(XQCoreManager.KEY_TABLE_SORTINGS);
            urlsTable = properties.loadProperty(XQCoreManager.KEY_TABLE_URLS);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
        
        msg = new String();
		try {
			conn = coreManager.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			sql = new String();
			sql += "DELETE FROM " + sortedNodesTable + " s " + LINE_SEP;
			sql += "WHERE s.exercise = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted sorted nodes " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;

			sql = new String();
			sql += "DELETE FROM " + urlsTable + " u " + LINE_SEP;
			sql += "WHERE u.exercise = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted urls " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;
			
			sql = new String();
			sql += "DELETE FROM " + exerciseTable + " e " + LINE_SEP;
			sql += "WHERE e.id = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted XQuery exercise with id " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;
			
			conn.commit();

			LOGGER.info(msg);
			return true;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'deleteExercise', rolling back. ";
			LOGGER.fatal(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.fatal(msg, e2);
				}
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.fatal(msg, e);
				}
			}
		}
		return false;
	}

	public String generateHtml(Serializable exercise, Locale locale) throws Exception {
		//TODO: implement interface method
		return null;
	}

	public Serializable fetchExercise(int exerciseId) throws Exception {
		String msg;
		String sql;
		XQCoreManager coreManager;
		PropertyFile properties;
		String exerciseTable;
        String urlsTable;
        String sortedNodesTable;
        XQExerciseBean exercise;
        ArrayList nodeList;
        UrlContentMap urlMap;
        String url;
        String hiddenUrl;
        Connection conn;
        Statement stmt;
        ResultSet rset;
        
        conn = null;
        stmt = null;
        rset = null;
        exercise = null;
        
        //fetch properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.getProperty(XQCoreManager.KEY_TABLE_EXERCISE);
            urlsTable = properties.getProperty(XQCoreManager.KEY_TABLE_URLS);
            sortedNodesTable = properties.getProperty(XQCoreManager.KEY_TABLE_SORTINGS);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }
        
		sql = new String();
        sql += "SELECT 	query, points " + LINE_SEP;
        sql += "FROM 	" + exerciseTable + " " + LINE_SEP;
        sql += "WHERE 	id = " + exerciseId + " " + LINE_SEP;

        try {
        	conn = coreManager.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            if (rset.next()) {
            	exercise = new XQExerciseBean();
                exercise.setQuery(rset.getString("query"));
                exercise.setPoints(rset.getString("points") != null ? new Double(rset.getString("points")) : null);
            }
            if (exercise != null) {
                // Fetch nodes required to be in certain order in the result of the query
            	nodeList = new ArrayList();
            	sql = new String();
                sql += "SELECT 	xpath " + LINE_SEP;
                sql += "FROM 	" + sortedNodesTable + " " + LINE_SEP;
                sql += "WHERE 	exercise = " + exerciseId + " " + LINE_SEP;

                rset.close();
				rset = null;
				rset = stmt.executeQuery(sql);
	            while (rset.next()) {
	                nodeList.add(rset.getString("xpath"));
	            }
	            exercise.setSortedNodes(nodeList);

	            // Fetch urls which are allowed to be accessed by xquery statements of this exercise
	            urlMap = new UrlContentMap();
	            sql = new String();
	            sql += "SELECT 	url, hidden_url " + LINE_SEP;
	            sql += "FROM 	" + urlsTable + " " + LINE_SEP;
	            sql += "WHERE 	exercise = " + exerciseId + " " + LINE_SEP;

	            rset.close();
				rset = null;
				rset = stmt.executeQuery(sql);
	            while (rset.next()) {
	                url = rset.getString("url");
	                hiddenUrl = rset.getString("hidden_url");
	                urlMap.addUrlAlias(hiddenUrl, url);
	            }
	            exercise.setUrls(urlMap);
            }
        } catch (Exception e) {
        	msg = new String();
        	msg += "An exception was thrown when fetching exercise parameters ";
        	msg += "from database for exercise with id " + exerciseId + ".";
            LOGGER.error(msg, e);
            throw e;
        } finally {
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					LOGGER.fatal(e);
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					LOGGER.fatal(e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					LOGGER.fatal(e);
				}
			}
        }
		return exercise;
	}

	public Serializable fetchExerciseInfo() throws Exception {
        XQExerciseBean exercise;
        
    	exercise = new XQExerciseBean();
        exercise.setQuery("");
		//just internal points, should be > 0, points are set by eTutor core
        exercise.setPoints(new Double(1));
        exercise.setSortedNodes(new ArrayList());
        exercise.setUrls(new UrlContentMap());

        return exercise;
	}
	
    /**
     * Returns an object which contains grading information according to the specified exercise ID.
     * The information is retrieved from the exercise database identified by the exercise ID. 
     * 
     * @param exerciseId Identifier for the exercise.
     * @return The object which contains the grading information.
     * @throws ClassNotFoundException if the connection can not be established due to problems with
     *             the database driver.
     * @throws SQLException if the connection can not be established due to an SQLException.
     * @throws InvalidResourceException if some important information for the database connection is
     *             not defined in the properties file.
     * @throws ParameterException if some grading parameter fetched from the database is not
     *             applicable.
     * @throws GradingException if an SQLException was thrown when fetching the grading parameters
     *             from the database, even after the connection has been established successfully.
     */
    public XQGradingConfig fetchGradingConfig(int exerciseId) throws Exception {
    	String msg;
        XQGradingConfig config;
        XQCoreManager coreManager;
        PropertyFile properties;
        Connection conn;
        Statement stmt;
        ResultSet rset;
        String sql;
        String exerciseTable;
        String errorCategoryTable;
        String errorGradingTable;
        String category;
        int errorLevel;
        double minusPoints;
        double maxPoints;

        msg = new String();
        msg += "Try obtaining grading information for exercise " + exerciseId;
        LOGGER.info(msg);

        conn = null;
        stmt = null;
        rset = null;
        config = new XQGradingConfig();

        // Fetch properties
        try {
            coreManager = XQCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.getProperty(XQCoreManager.KEY_TABLE_EXERCISE);
            errorCategoryTable = properties.getProperty(XQCoreManager.KEY_TABLE_ERROR_CATEGORIES);
            errorGradingTable = properties.getProperty(XQCoreManager.KEY_TABLE_ERROR_GRADING);
        } catch (Exception e) {
            LOGGER.error(e);
            throw e;
        }

        msg = new String();
        msg += "Try obtaining grading information from tables ";
        msg += exerciseTable + ", " + errorCategoryTable;
        msg += " and " + errorGradingTable;
        LOGGER.debug(msg);
        
        sql = new String();
        sql += "SELECT 	ex.points " + LINE_SEP;
        sql += "FROM 	" + exerciseTable + " ex " + LINE_SEP;
        sql += "WHERE 	ex.id = " + exerciseId + " " + LINE_SEP;
        LOGGER.debug(sql);

        try {
        	conn = coreManager.getConnection();
        	stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            
            if (rset.next()) {
            	do {
	                maxPoints = rset.getDouble("points");
	                msg = new String();
	                msg += "Setting max points for exercise " + exerciseId;
	                msg += ": " + maxPoints;
	                LOGGER.debug(msg);
	                config.setMaxScore(maxPoints);
            	} while (rset.next());
            } else {
            	msg = "No max points set for exercise " + exerciseId;
                LOGGER.warn(msg);
            }
            
            sql = new String();
            sql += "SELECT 	cat.name, gr.grading_level, gr.minus_points " + LINE_SEP;
            sql += "FROM 	" + exerciseTable + " ex, " + errorGradingTable + " gr, " + LINE_SEP;
            sql += "		" + errorCategoryTable + " cat " + LINE_SEP;
            sql += "WHERE 	ex.id = " + exerciseId + " " + LINE_SEP;
            sql += "AND 	gr.grading_group = ex.gradings " + LINE_SEP;
            sql += "AND 	gr.grading_category = cat.id " + LINE_SEP;
            LOGGER.debug(sql);

            rset.close();
            rset = null;
            rset = stmt.executeQuery(sql);
            
            while (rset.next()) {
                category = rset.getString("name");
                errorLevel = rset.getInt("grading_level");
                minusPoints = rset.getDouble("minus_points");
                msg = new String();
                msg += "Setting grading parameter for exercise " + exerciseId + ":";
                msg += LINE_SEP + "\t Error level for category " + category + ": " + errorLevel;
                msg += LINE_SEP + "\t Minus points for category " + category + ": " + minusPoints;
                LOGGER.debug(msg);
                config.setErrorLevel(category, errorLevel);
                config.setScore(category, minusPoints);
            }
            
        } catch (Exception e) {
        	msg = new String();
        	msg += "An exception was thrown when fetching grading parameters ";
        	msg += "from database for exercise with id " + exerciseId + ".";
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
        } finally {
        	if (rset != null) {
                try {
	        		rset.close();
	            } catch (SQLException e) {
	            	LOGGER.fatal(e);
	            }
        	}
        	if (stmt != null) {
                try {
	                stmt.close();
	            } catch (SQLException e) {
	            	LOGGER.fatal(e);
	            }
        	}
        	if (conn != null) {
                try {
	                conn.close();
	            } catch (SQLException e) {
	            	LOGGER.fatal(e);
	            }
        	}
        }

        return config;
    }
}
