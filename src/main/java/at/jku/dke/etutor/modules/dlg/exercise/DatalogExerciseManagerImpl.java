package at.jku.dke.etutor.modules.dlg.exercise;

import at.jku.dke.etutor.grading.rest.dto.DatalogTaskGroupDTO;
import at.jku.dke.etutor.modules.dlg.DatalogCoreManager;
import at.jku.dke.etutor.modules.dlg.ExerciseManagementException;
import at.jku.dke.etutor.modules.dlg.GradingException;
import at.jku.dke.etutor.modules.dlg.grading.DatalogScores;
import at.jku.dke.etutor.modules.dlg.util.PropertyFile;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Service
public class DatalogExerciseManagerImpl implements DatalogExerciseManager {
	
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogExerciseManagerImpl.class);
	private static final String LINE_SEP = System.getProperty("line.separator", "\n");

	public DatalogExerciseManagerImpl() {
        DatalogCoreManager.getInstance();
	}
	
	public int createExercise(Serializable exercise, Map attributes, Map parameters) throws Exception {
		String termsTable;
		String predicatesTable;
		String exerciseTable;
		String sql;
		String msg;
		String predicate;
		TermDescription term;
		Connection conn;
		PreparedStatement pStmt;
		DatalogCoreManager coreManager;
		DatalogExerciseBean datalogExercise;
		PropertyFile properties;
		int index;
		
		pStmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try creating datalog exercise with ID ";
		msg += exercise;
        LOGGER.info(msg);
        
		if (exercise == null){
			msg = new String();
			msg += "Passed exercise is null.";
			LOGGER.error(msg);
			return -1;
		}
		
		if (!(exercise instanceof DatalogExerciseBean)){
			msg = new String();
			msg += "Can not cast exercise bean of type ";
			msg += exercise.getClass().getName();
			msg += " to module-specific type ";
			msg += DatalogExerciseBean.class.getName();
			LOGGER.error(msg);
			return -1;
		}
		
        //Fetch properties
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
            termsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_TERMS_UNCHECKED);
            predicatesTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_PREDICATES);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        
		datalogExercise = (DatalogExerciseBean)exercise;

		try {
			conn = coreManager.getConnection();
			conn.setAutoCommit(false);
			
			//GET MAX ID FROM DB
			sql = "SELECT MAX(id) FROM "+exerciseTable;
			msg = new String();
			msg = msg.concat("QUERY for select max id value:\n" + sql);
			LOGGER.info(msg);
			
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int exerciseID = 1;
			if (rs.next()) {
				exerciseID = rs.getInt(1) + 1;
			}
			
			sql = new String();
			sql += "INSERT INTO	" + exerciseTable + " e " + LINE_SEP;
			sql += "(e.id, e.query, e.facts, e.points, e.gradings)" + LINE_SEP;
			sql += "VALUES (?, ?, ?, ?, ?)" + LINE_SEP;
			pStmt = conn.prepareStatement(sql);
			index = 1;
			pStmt.setInt(index++, exerciseID);
			pStmt.setString(index++, datalogExercise.getQuery());
			if (datalogExercise.getFactsId() != null) {
				pStmt.setInt(index++, datalogExercise.getFactsId().intValue());
			} else {
				pStmt.setNull(index++, Types.INTEGER);
			}
			if (datalogExercise.getPoints() != null) {
				pStmt.setDouble(index++, datalogExercise.getPoints().doubleValue());
			} else {
				pStmt.setNull(index++, Types.DOUBLE);
			}
			pStmt.setNull(index++, Types.INTEGER);
			pStmt.executeUpdate();
			
			if (datalogExercise.getTerms() != null && datalogExercise.getTerms().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + termsTable + " t " + LINE_SEP;
				sql += "(t.predicate, t.term, t.position, t.exercise)" + LINE_SEP;
				sql += "VALUES (?, ?, ?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < datalogExercise.getTerms().size(); i++) {
					term = (TermDescription)datalogExercise.getTerms().get(i);
					index = 1;
					pStmt.setString(index++, term.getPredicate());
					pStmt.setString(index++, term.getTerm());
					pStmt.setInt(index++, Integer.parseInt(term.getPosition()));
					pStmt.setInt(index++, exerciseID);
					pStmt.executeUpdate();
				}
			}
			
			if (datalogExercise.getPredicates() != null && datalogExercise.getPredicates().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + predicatesTable + " p " + LINE_SEP;
				sql += "(p.name, p.exercise)" + LINE_SEP;
				sql += "VALUES (?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < datalogExercise.getPredicates().size(); i++) {
					predicate = (String)datalogExercise.getPredicates().get(i);
					index = 1;
					pStmt.setString(index++, predicate);
					pStmt.setInt(index++, exerciseID);
					pStmt.executeUpdate();
				}
			}
			
			conn.commit();

			msg = new String();
			msg += "Created Datalog exercise with id " + exerciseID;
			LOGGER.info(msg);
			return exerciseID;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'createExercise'. ";
			LOGGER.error(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.error(msg, e2);
				}
			}
		} finally {
			if (pStmt != null) {
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.error(msg, e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.error(msg, e);
				}
			}
		}
		return -1;
	}
	
	public boolean deleteExercise(int exerciseId) throws Exception {
		String termsTable;
		String predicatesTable;
		String exerciseTable;
		String sql;
		String msg;
		Connection conn;
		Statement stmt;
		DatalogCoreManager coreManager;
		PropertyFile properties;
		int count;

		stmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try deleting exercise with ID " + exerciseId;
        LOGGER.info(msg);
        
        //Fetch properties
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
            termsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_TERMS_UNCHECKED);
            predicatesTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_PREDICATES);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        
        msg = new String();
		try {
			conn = coreManager.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			
			sql = new String();
			sql += "DELETE FROM " + predicatesTable + " p " + LINE_SEP;
			sql += "WHERE p.exercise = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted predicates " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;

			sql = new String();
			sql += "DELETE FROM " + termsTable + " t " + LINE_SEP;
			sql += "WHERE t.exercise = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted terms " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;
			
			sql = new String();
			sql += "DELETE FROM " + exerciseTable + " e " + LINE_SEP;
			sql += "WHERE e.id = " + exerciseId + LINE_SEP;
			count = stmt.executeUpdate(sql);
			msg += "Deleted datalog exercise with id " + exerciseId;
			msg += " (" + count + " row(s) deleted)" + LINE_SEP;
			
			conn.commit();

			LOGGER.info(msg);
			return true;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'deleteExercise', rolling back. ";
			LOGGER.error(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.error(msg, e2);
				}
			}
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.error(msg, e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.error(msg, e);
				}
			}
		}
		return false;
	}

	public boolean modifyExercise(int exerciseId, Serializable exercise, Map attributes, Map parameters) throws Exception{
		String termsTable;
		String predicatesTable;
		String exerciseTable;
		String sql;
		String msg;
		String predicate;
		Connection conn;
		PreparedStatement pStmt;
		DatalogCoreManager coreManager;
		DatalogExerciseBean datalogExercise;
		PropertyFile properties;
		TermDescription term;
		int index;
		
		pStmt = null;
		conn = null;
		
		msg = new String();
		msg += "Try updating datalog exercise with ID ";
		msg += exerciseId + ":" + LINE_SEP + exercise;
        LOGGER.info(msg);
        
		if (exercise == null){
			msg = new String();
			msg += "Passed exercise is null.";
			LOGGER.error(msg);
			return false;
		}
		
		if (!(exercise instanceof DatalogExerciseBean)){
			msg = new String();
			msg += "Can not cast exercise bean of type ";
			msg += exercise.getClass().getName();
			msg += " to module-specific type ";
			msg += DatalogExerciseBean.class.getName();
			LOGGER.error(msg);
			return false;
		}
		
        //Fetch properties
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
            termsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_TERMS_UNCHECKED);
            predicatesTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_PREDICATES);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }
        
		datalogExercise = (DatalogExerciseBean)exercise;

		try {
			conn = coreManager.getConnection();
			conn.setAutoCommit(false);
			
			sql = new String();
			sql += "UPDATE 	" + exerciseTable + " e " + LINE_SEP;
			sql += "SET 	e.query = ?, e.facts = ?, e.points = ?, " + LINE_SEP;
			sql += "		e.gradings = ? " + LINE_SEP;
			sql += "WHERE 	e.id = ?" + LINE_SEP;
			pStmt = conn.prepareStatement(sql);
			index = 1;
			pStmt.setString(index++, datalogExercise.getQuery());
			if (datalogExercise.getFactsId() != null) {
				pStmt.setInt(index++, datalogExercise.getFactsId().intValue());
			} else {
				pStmt.setNull(index++, Types.INTEGER);
			}
			if (datalogExercise.getPoints() != null) {
				pStmt.setDouble(index++, datalogExercise.getPoints().doubleValue());
			} else {
				pStmt.setNull(index++, Types.DOUBLE);
			}
			pStmt.setNull(index++, Types.INTEGER);
			pStmt.setInt(index++, exerciseId);
			pStmt.executeUpdate();
			
			sql = new String();
			sql += "DELETE FROM " + termsTable + " t " + LINE_SEP;
			sql += "WHERE t.exercise = " + exerciseId + LINE_SEP;
			pStmt.close();
			pStmt = null;
			pStmt = conn.prepareStatement(sql);
			pStmt.executeUpdate();
			
			sql = new String();
			sql += "DELETE FROM " + predicatesTable + " p " + LINE_SEP;
			sql += "WHERE p.exercise = " + exerciseId + LINE_SEP;
			pStmt.close();
			pStmt = null;
			pStmt = conn.prepareStatement(sql);
			pStmt.executeUpdate();
			
			if (datalogExercise.getTerms() != null && datalogExercise.getTerms().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + termsTable + " t " + LINE_SEP;
				sql += "(t.predicate, t.term, t.position, t.exercise)" + LINE_SEP;
				sql += "VALUES (?, ?, ?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < datalogExercise.getTerms().size(); i++) {
					term = (TermDescription)datalogExercise.getTerms().get(i);
					index = 1;
					pStmt.setString(index++, term.getPredicate());
					pStmt.setString(index++, term.getTerm());
					pStmt.setInt(index++, Integer.parseInt(term.getPosition()));
					pStmt.setInt(index++, exerciseId);
					pStmt.executeUpdate();
				}
			}
			
			if (datalogExercise.getPredicates() != null && datalogExercise.getPredicates().size() > 0) {
				sql = new String();
				sql += "INSERT INTO	" + predicatesTable + " p " + LINE_SEP;
				sql += "(p.name, p.exercise)" + LINE_SEP;
				sql += "VALUES (?, ?)" + LINE_SEP;
				pStmt.close();
				pStmt = null;
				pStmt = conn.prepareStatement(sql);
				for (int i = 0; i < datalogExercise.getPredicates().size(); i++) {
					predicate = (String)datalogExercise.getPredicates().get(i);
					index = 1;
					pStmt.setString(index++, predicate);
					pStmt.setInt(index++, exerciseId);
					pStmt.executeUpdate();
				}
			}
			
			conn.commit();

			msg = new String();
			msg += "Updated Datalog exercise with id " + exerciseId;
			LOGGER.info(msg);
			return true;			
		} catch (Exception e) {
			msg = new String();
			msg += "Stopped processing command 'modifyExercise'. ";
			LOGGER.error(msg, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e2){
					msg = new String();
					msg += "Exception at rollback.";
					LOGGER.error(msg, e2);
				}
			}
		} finally {
			if (pStmt != null) {
				try {
					pStmt.close();
				} catch (SQLException e){
					msg = new String();
					LOGGER.error(msg, e);
				}
			}
			try {
				conn.close();
			} catch (SQLException e){
				msg = new String();
				LOGGER.error(msg, e);
			}
		}
		return false;
	}
	
	public String generateHtml(Serializable exercise, Locale locale) throws Exception {
		//TODO: implement interface method
		return null;
	}

	public Serializable fetchExercise(int exerciseId) throws Exception {
		DatalogExerciseBean exercise;
		String factsTable;
		String exerciseTable;
		String predicateTable;
		String termsTable;
		String sql;
		String msg;
		ArrayList predicates;
		ArrayList terms;
		TreeMap facts;
		TermDescription termDef;
		ResultSet rset;
		Statement stmt;
		Connection conn;
		DatalogCoreManager coreManager;
		PropertyFile properties;
		
		rset = null;
		stmt = null;
		conn = null;
		exercise = null;

        //Fetch properties
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            factsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_FACTS);
            exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
            predicateTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_PREDICATES);
            termsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_TERMS_UNCHECKED);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

		sql = new String();
		sql = sql.concat("SELECT 	e.query, e.facts, e.points " + LINE_SEP);
		sql = sql.concat("FROM 		" + exerciseTable + " e " + LINE_SEP);
		sql = sql.concat("WHERE 	e.id = " + exerciseId + LINE_SEP);

        try {
			conn = coreManager.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			if (rset.next()) {
				exercise = new DatalogExerciseBean();

				exercise.setQuery(rset.getString(1));
				exercise.setFactsId(rset.getString(2) != null ? Integer.parseInt(rset.getString(2)) : null);
				exercise.setPoints(rset.getString(3) != null ? Double.parseDouble(rset.getString(3)) : null);
			}

			if (exercise != null) {
				facts = new TreeMap();
				sql = new String();
				sql = sql.concat("SELECT 	f.id, f.name " + LINE_SEP);
				sql = sql.concat("FROM 		" + factsTable + " f " + LINE_SEP);
				
				rset.close();
				rset = null;
				rset = stmt.executeQuery(sql);
				while (rset.next()) {
					facts.put(rset.getString(1), rset.getString(2));
				}
				exercise.setFacts(facts);
				
				predicates = new ArrayList();
				sql = new String();
				sql = sql.concat("SELECT 	p.name " + LINE_SEP);
				sql = sql.concat("FROM 		" + predicateTable + " p " + LINE_SEP);
				sql = sql.concat("WHERE 	p.exercise = " + exerciseId + LINE_SEP);
				
				rset.close();
				rset = null;
				rset = stmt.executeQuery(sql);
				while (rset.next()) {
					predicates.add(rset.getString(1));
				}
				exercise.setPredicates(predicates);
				
				terms = new ArrayList();
				sql = new String();
				sql = sql.concat("SELECT 	t.predicate, t.position, t.term " + LINE_SEP);
				sql = sql.concat("FROM 		" + termsTable + " t " + LINE_SEP);
				sql = sql.concat("WHERE 	t.exercise = " + exerciseId + LINE_SEP);
				
				rset.close();
				rset = null;
				rset = stmt.executeQuery(sql);
				while (rset.next()) {
					termDef = new TermDescription();
					termDef.setPredicate(rset.getString(1));
					termDef.setPosition(rset.getString(2));
					termDef.setTerm(rset.getString(3));
					terms.add(termDef);
				}
				exercise.setTerms(terms);
			}
		} catch (Exception e){
			msg = new String();
			msg += "Stopped processing command 'fetch exercise' ";
			msg += "for id " + exerciseId + ". ";
			LOGGER.error(msg, e);
			throw e;
		} finally {
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
		}

		if (exercise != null) {
			msg = new String();
			msg += "Fetched exercise with id " + exerciseId;
			LOGGER.info(msg);
		} else {
			msg = new String();
			msg += "No exercise found with id " + exerciseId;
			LOGGER.warn(msg);
		}
		
		return exercise;
	}

	public Serializable fetchExerciseInfo() throws Exception {
		DatalogExerciseBean exercise;
		String factsTable;
		String sql;
		String msg;
		TreeMap facts;
		ResultSet rset;
		Statement stmt;
		Connection conn;
		DatalogCoreManager coreManager;
		PropertyFile properties;
		
		rset = null;
		stmt = null;
		conn = null;
		exercise = null;

        //Fetch properties
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();

            factsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_FACTS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

		exercise = new DatalogExerciseBean();

		exercise.setQuery("");
		exercise.setFactsId(null);
		//just internal points, should be > 0, points are set by eTutor core
		exercise.setPoints(1.0);
		exercise.setPredicates(new ArrayList());
		exercise.setTerms(new ArrayList());

		sql = new String();
		sql = sql.concat("SELECT 	f.id, f.name " + LINE_SEP);
		sql = sql.concat("FROM 		" + factsTable + " f " + LINE_SEP);

		try {
			conn = coreManager.getConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			rset = stmt.executeQuery(sql);
			facts = new TreeMap();
			while (rset.next()) {
				facts.put(rset.getString(1), rset.getString(2));
			}
			exercise.setFacts(facts);
		} catch (Exception e){
			msg = new String();
			msg += "Stopped processing command 'fetch exercise info'. ";
			LOGGER.error(msg, e);
			throw e;
		} finally {
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					LOGGER.error(e.getMessage());
				}
			}
		}

		msg = new String();
		msg += "Fetched exercise info.";
		LOGGER.info(msg);
		
		return exercise;
	}
	
    public String fetchFacts(int factsId) throws Exception {
    	String msg;
    	String sql;
    	String facts;
		String factsTable;
        Statement stmt;
        ResultSet rset;
        Connection conn;
        DatalogCoreManager coreManager;
        PropertyFile properties;
        
        facts = null;
        stmt = null;
        rset = null;
        conn = null;

        msg = new String();
    	msg += "Try obtaining datalog facts for facts with ID " + factsId + ".";
        LOGGER.info(msg);
    	
        try {
            coreManager = DatalogCoreManager.getInstance();
            properties = coreManager.getPropertyFile();
            factsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_FACTS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw e;
        }

        sql = new String();
        sql += "SELECT 	fa.facts " + LINE_SEP;
        sql += "FROM 	" + factsTable + " fa " + LINE_SEP;
        sql += "WHERE 	fa.id = " + factsId + LINE_SEP;
        LOGGER.debug(sql);

    	try {
    		conn = coreManager.getConnection();
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            if (rset.next()) {
                facts = rset.getString("facts");
            } else {
                msg = new String();
                msg += "No exercise definition found in exercise table ";
                msg += " based on id " + factsId + ".";
                LOGGER.warn(msg);
            }
            return facts;
    	} catch (Exception e){
			msg = new String();
			msg += "Stopped processing command 'get facts'. ";
			LOGGER.error(msg, e);
			throw e;
		} finally {
			if (rset != null) {
                try {
	        		rset.close();
	            } catch (SQLException e) {
	                msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
	            }
        	}
        	if (stmt != null) {
                try {
	                stmt.close();
	            } catch (SQLException e) {
	                msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
	            }
        	}
        	if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
				}
			}
		}
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
     * @throws GradingException if an SQLException was thrown when fetching the grading
     *             parameters from the database, even after the connection has been 
     *             established successfully.
     * @throws Exception any other exception
     */
    public DatalogScores fetchScores(int exerciseId) throws Exception {
        String msg;
    	DatalogScores scores;
        DatalogCoreManager coreManager;
        PropertyFile properties;
        String exerciseTable;
        String errorCategoryTable;
        String errorGradingTable;
        Connection conn;
        Statement stmt;
        ResultSet rset;
        String sql;
        String category;
        int errorLevel;
        double maxPoints;
        double minusPoints;

        msg = new String();
        msg += "Try obtaining grading information for exercise " + exerciseId;
        LOGGER.info(msg);

        conn = null;
        stmt = null;
        rset = null;
        scores = new DatalogScores();
        
        try {
	        coreManager = DatalogCoreManager.getInstance();
	        properties = coreManager.getPropertyFile();
	        
	        exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
	        errorCategoryTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_ERROR_CATEGORIES);
	        errorGradingTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_ERROR_GRADING);
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
            throw e;	
        }

        msg = new String();
        msg += "Try obtaining grading information from tables " + exerciseTable;
        msg += ", " + errorCategoryTable + " and " + errorGradingTable;
        LOGGER.debug(msg);
        
        sql = new String();
        sql += "SELECT ex.points " + LINE_SEP;
        sql += "FROM " + exerciseTable + " ex " + LINE_SEP;
        sql += "WHERE ex.id = " + exerciseId + LINE_SEP;
        LOGGER.debug(sql);

        try {
        	conn = coreManager.getConnection();
        	stmt = conn.createStatement();
            rset = stmt.executeQuery(sql);
            
            if (rset.next()) {
                maxPoints = rset.getDouble("points");
                scores.setMaxScore(maxPoints);
                msg = new String();
                msg += "Setting max points for exercise " + exerciseId + ": ";
                msg += maxPoints;
                LOGGER.debug(msg);
            } else {
            	msg = "No max points set for exercise " + exerciseId;
                LOGGER.warn(msg);
            }
            
        	sql = new String();
            sql += "SELECT 	cat.name, gr.minus_points, gr.grading_level " + LINE_SEP;
            sql += "FROM 	" + exerciseTable + " ex, " + errorGradingTable + " gr, " + LINE_SEP;
            sql += "		" + errorCategoryTable + " cat " + LINE_SEP;
            sql += "WHERE 	ex.id = " + exerciseId + " AND " + LINE_SEP;
            sql += "		gr.grading_group = ex.gradings AND " + LINE_SEP;
            sql += "		gr.grading_category = cat.id" + LINE_SEP;
            LOGGER.debug(sql);
            
            rset.close();
            rset = null;
            rset = stmt.executeQuery(sql);
            
            msg = new String();
            msg += "Setting grading parameters for exercise " + exerciseId + ":" + LINE_SEP;
            while (rset.next()) {
                category = rset.getString("name");
                errorLevel = rset.getInt("grading_level");
                minusPoints = rset.getDouble("minus_points");
                msg += "\tcategory: " + category + ", error level: " + errorLevel;
                msg += ", minus points: " + minusPoints + LINE_SEP;
                scores.setErrorLevel(category, errorLevel);
                scores.setScore(category, minusPoints);
            }
            LOGGER.debug(msg);
        } catch (Exception e) {
            msg = new String();
            msg += "An exception was thrown when fetching grading parameters ";;
            msg += "from database for exercise with id " + exerciseId + ".";
            LOGGER.error(msg, e);
            throw new GradingException(msg, e);
        } finally {
        	if (rset != null) {
                try {
	        		rset.close();
	            } catch (SQLException e) {
	                msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
	            }
        	}
        	if (stmt != null) {
                try {
	                stmt.close();
	            } catch (SQLException e) {
	                msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
	            }
        	}
        	if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e){
					msg = "Database resource cannot be closed.";
	                LOGGER.error(msg, e);
				}
			}
        }

        return scores;
    }

	public int createTaskGroup(DatalogTaskGroupDTO facts) throws ExerciseManagementException {
		String msg;
		DatalogCoreManager coreManager;
		PropertyFile properties;
		String factsTable;
		int factId;


		msg = "";
		msg += "Try obtaining facts id for new task group:  "+facts;
		LOGGER.info(msg);

		try {
			coreManager = DatalogCoreManager.getInstance();
			properties = coreManager.getPropertyFile();
			factsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_FACTS);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ExerciseManagementException(e);
		}

		String query = "SELECT max(id) AS id FROM "+ factsTable;

		try (Connection conn = coreManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet resultSet = stmt.executeQuery()){
			if(resultSet.next()){
				factId = resultSet.getInt("id");
				factId++;
			}else throw new ExerciseManagementException("Could not fetch available facts id");
		} catch (SQLException throwables) {
			LOGGER.error(throwables.getMessage());
			throw new ExerciseManagementException(throwables);
		}

		msg = "";
		msg += "Found available facts id for new task group: "+factId + "\n";
		msg += "Trying to insert facts.";
		LOGGER.info(msg);

		String update = "INSERT INTO "+factsTable + "(id, facts, name) VALUES (?,?, ?)";
		try(Connection conn = coreManager.getConnection();
		PreparedStatement stmt = conn.prepareStatement(update)){
			stmt.setInt(1, factId);
			stmt.setString(2, facts.getFacts());
			stmt.setString(3, facts.getName());
			stmt.executeUpdate();
			conn.commit();
		}catch (SQLException throwables) {
			LOGGER.error(throwables.getMessage());
			throw new ExerciseManagementException(throwables);
		}
		msg = "";
		msg += "Facts have been inserted";
		LOGGER.info(msg);
		return factId;
	}

	public void deleteTaskGroup(int id) throws ExerciseManagementException {
		String msg;
		DatalogCoreManager coreManager;
		PropertyFile properties;
		String factsTable;
		String exerciseTable;

		msg = "";
		msg += "Try deleting task group and exercises for facts id: "+ id;
		LOGGER.info(msg);

		try {
			coreManager = DatalogCoreManager.getInstance();
			properties = coreManager.getPropertyFile();
			factsTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_FACTS);
			exerciseTable = properties.loadProperty(DatalogCoreManager.KEY_TABLE_EXERCISE);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new ExerciseManagementException(e);
		}
		String deleteGroup = "DELETE FROM "+factsTable + " WHERE id = ?";
		String deleteTasks = "DELETE FROM "+exerciseTable + " WHERE facts = ?";
		try(Connection con = coreManager.getConnection();
		PreparedStatement stmtGroup = con.prepareStatement(deleteGroup);
		PreparedStatement stmtTasks = con.prepareStatement(deleteTasks)){
			stmtGroup.setInt(1, id);
			stmtTasks.setInt(1, id);
			stmtGroup.executeUpdate();
			stmtTasks.executeUpdate();
			con.commit();
		} catch (SQLException throwables) {
			LOGGER.warn(throwables.getMessage());
			throw new ExerciseManagementException(throwables);
		}
		msg= "Deleted facts and exercises for id "+id;
		LOGGER.info(msg);
	}
}
