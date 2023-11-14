package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.core.manager.ModuleExerciseManager;
import at.jku.dke.etutor.core.utils.JDBCAdapter;
import at.jku.dke.etutor.modules.nf.DecomposeSpecification;
import at.jku.dke.etutor.modules.nf.NormalizationSpecification;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.RDBDSpecification;
import at.jku.dke.etutor.modules.nf.ui.SpecificationEditor;
import oracle.sql.BLOB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.*;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Super class for exercise manager classes for all types of the RDBD module.
 * Since exercise IDs of existing exercises are mapped to the internal type,
 * a single class to process tasks for all RDBD types would be sufficient 
 * (like the RDBDEvaluator implementation). Nevertheless, based on the exercise ID, 
 * passed as argument to method {@link #createExercise(int, Serializable, Map, Map)}, 
 * the retrieval of the appropriate internal type is not possible. This is why
 * each RDBD type has its own implementation which is registered with the eTutor core. 
 *  
 * @author Georg Nitsche (12.12.2005)
 *
 */
public class RDBDExercisesManager implements ModuleExerciseManager {

	protected Logger logger;
	protected int rdbdType;

	public RDBDExercisesManager(int rdbdType) { 
		super();
		try {
			this.logger = Logger.getLogger("etutor.modules.rdbd");
		} catch (Exception e){
			e.printStackTrace();		
		}
		this.rdbdType = rdbdType;
	}
	
	private RDBDExercisesManager() { 
	}
	
	public int createExercise(Serializable editor, Map attributes, Map parameters) throws Exception {
		String msg;
		String sql;
		BLOB blob = null;
		ObjectOutputStream out = null;
		RDBDSpecification specification;
		int duplicateId;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		if (editor == null) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is " + editor;
			logger.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		
		if (!(editor instanceof SpecificationEditor)) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is of type " + editor.getClass().getName();
			logger.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		
		specification = ((SpecificationEditor)editor).getSpecTmp();
		
		try {
			conn = RDBDHelper.getPooledConnection();
			conn.setAutoCommit(false);

			duplicateId = checkForDuplicate(this.rdbdType, specification, conn);
			if (duplicateId > -1) {
				msg = "Settings of the specified exercise are equal to those of existing exercise ";
				msg += "with ID " + duplicateId + ". Please change your settings and try again.";
				throw new Exception(msg);
			}
			
			//GET MAX ID FROM DB
			sql = "SELECT MAX(id) FROM exercises";
			msg = "";
			msg = msg.concat("QUERY for select max id value:\n" + sql);
			this.logger.log(Level.INFO, msg);
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int exerciseId = 1;
			if (rs.next()) {
				exerciseId = rs.getInt(1) + 1;
			}
			
			sql = "";
			sql = sql.concat("INSERT INTO exercises VALUES (");
			sql = sql.concat(exerciseId + ", ");
			sql = sql.concat("empty_blob(), " + this.rdbdType + ")");

			System.out.println("INSERTING EMPTY BLOB QUERY:\n" + sql);
			
			stmt= conn.createStatement();
			stmt.execute(sql);

			sql = "";
			sql = sql.concat("SELECT specification ");
			sql = sql.concat("FROM	 exercises ");
			sql = sql.concat("WHERE	 id=" + exerciseId + " for update");

			System.out.println("RETRIEVING EMPTY BLOB QUERY:\n" + sql);
			
			stmt.close();
			stmt = null;
			stmt= conn.createStatement();
			rset = stmt.executeQuery(sql);

			if (rset.next()) {
				//TODO: resolve dependency on eTutor core classes
				blob = JDBCAdapter.getOracleBlob(rset, 1);
				//blob = ((oracle.jdbc.OracleResultSet)rset).getBLOB(1);
			}

			if (blob != null) {
				out = new ObjectOutputStream(blob.getBinaryOutputStream());
				out.writeObject(specification);
				//close before commit!
				out.flush();
				out.close();
				out = null;
			}

			conn.commit();
			return exerciseId;
		} catch (SQLException | IOException e){
			logger.log(Level.SEVERE, "", e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					logger.log(Level.SEVERE, "", ex);
				}
			}
		} finally {
			if (out != null) {
				try {
				    out.flush();
					out.close();
				} catch (IOException e){
					this.logger.log(Level.SEVERE, "", e);
				}
			}
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}

			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
		}
		
		return -1;
	}

	public boolean modifyExercise(int exerciseId, Serializable editor, Map attributes, Map parameters) throws Exception {
		String msg;
		String sql;
		BLOB blob = null;
		ObjectOutputStream out = null;
		RDBDSpecification specification;
		int duplicateId;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		if (editor == null) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is " + editor;
			logger.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		
		if (!(editor instanceof SpecificationEditor)) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is of type " + editor.getClass().getName();
			logger.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		
		specification = ((SpecificationEditor)editor).getSpecTmp();
		
		try {
			conn = RDBDHelper.getPooledConnection();
			conn.setAutoCommit(false);

			duplicateId = checkForDuplicate(this.rdbdType, exerciseId, specification, conn);
			if (duplicateId > -1) {
				msg = "Settings of the specified exercise are equal to those of existing exercise ";
				msg += "with ID " + duplicateId + ". Please change your settings and try again.";
				throw new Exception(msg);
			}
			
			sql = "";
			sql = sql.concat("UPDATE exercises SET specification = empty_blob() ");
			sql = sql.concat("WHERE id = " + exerciseId);

			System.out.println("UPDATING WITH EMPTY BLOB QUERY:\n" + sql);
			
			stmt= conn.createStatement();
			stmt.execute(sql);

			sql = "";
			sql = sql.concat("SELECT specification ");
			sql = sql.concat("FROM	 exercises ");
			sql = sql.concat("WHERE	 id=" + exerciseId + " for update");

			System.out.println("RETRIEVING EMPTY BLOB QUERY:\n" + sql);
			
			stmt.close();
			stmt = null;
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);

			if (rset.next()) {
				//TODO: resolve dependency on eTutor core classes
				blob = JDBCAdapter.getOracleBlob(rset, 1);
				//blob =((oracle.jdbc.OracleResultSet)rset).getBLOB(1);
			}

			if(blob!=null){
				out = new ObjectOutputStream(blob.getBinaryOutputStream());
				out.writeObject(specification);
				//close before commit!
				out.flush();
				out.close();
				out = null;
			}

			conn.commit();
			return true;
		} catch (SQLException | IOException e){
			logger.log(Level.SEVERE, "", e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					logger.log(Level.SEVERE, "", ex);
				}
			}
		} finally {
			if (out != null) {
				try {
				    out.flush();
					out.close();
				} catch (IOException e){
					this.logger.log(Level.SEVERE, "", e);
				}
			}
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}

			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
		}
		
		return false;
	}
	
	public boolean deleteExercise(int exerciseID) throws Exception {
		String sql;
		Statement stmt = null;
		Connection conn = null;
		
		try {
			conn = RDBDHelper.getPooledConnection();
			conn.setAutoCommit(false);
			
			sql = "";
			sql = sql.concat("DELETE FROM exercises WHERE id = " + exerciseID);
			
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			conn.commit();
			return true;
		} catch (SQLException e){
			logger.log(Level.SEVERE, "An exception was thrown when deleting RDBD exercise " + exerciseID, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					logger.log(Level.SEVERE, "", ex);
				}
			}
			throw e;
		} finally{
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
		}

	}
	
	public Serializable fetchExercise(int exerciseID) throws Exception {
		Blob blob;
		String sql;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		RDBDSpecification specification = null;
		ObjectInputStream in = null;
		SpecificationEditor editor = null;

		sql = "";
		sql = sql.concat("SELECT 	specification ");
		sql = sql.concat("FROM 		exercises ");
		sql = sql.concat("WHERE		id = " + exerciseID);

		this.logger.log(Level.INFO, "Fetching RDBD exercise " + exerciseID);

		try {
			conn = RDBDHelper.getPooledConnection();
			conn.setAutoCommit(false);
			stmt= conn.createStatement();
			rset = stmt.executeQuery(sql);
	
			if (rset.next()) {
				//TODO: resolve dependency on eTutor core classes
				blob = JDBCAdapter.getOracleBlob(rset, 1);
				//blob = ((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob!=null){
					System.out.println("Found entry ...");
					in = new ObjectInputStream(blob.getBinaryStream());
					specification = (RDBDSpecification)in.readObject();
					in.close();
					in = null;
					editor = new SpecificationEditor(this.rdbdType);
					editor.setSpec(specification);
					editor.setSpecTmp(RDBDHelper.clone(specification, this.rdbdType));
					editor.setParser(RDBDHelper.initParser(this.rdbdType));
					editor.setSpecText(editor.getParser().getText(editor.getSpec()));
					if (specification instanceof DecomposeSpecification) {
						editor.setTargetLevel(((DecomposeSpecification)specification).getTargetLevel());
						editor.setMaxLostDependencies(Integer.toString(((DecomposeSpecification)specification).getMaxLostDependencies()));
					} else if (specification instanceof NormalizationSpecification) {
						editor.setTargetLevel(((NormalizationSpecification)specification).getTargetLevel());
						editor.setMaxLostDependencies(Integer.toString(((NormalizationSpecification)specification).getMaxLostDependencies()));
					}

				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "An exception was thrown when fetching RDBD exercise " + exerciseID, e);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					logger.log(Level.SEVERE, "", ex);
				}
			}
			throw e;
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}

			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					logger.log(Level.SEVERE, "", e);
				}
			}
		}
		
		this.logger.log(Level.INFO, "Specification: " + specification + ", Editor: " + editor);
		return editor;
	}

	public Serializable fetchExerciseInfo() throws Exception {
		SpecificationEditor editor;
		
		editor = new SpecificationEditor(this.rdbdType);
		editor.setParser(RDBDHelper.initParser(this.rdbdType));
		editor.setSpec(RDBDHelper.initSpecification(this.rdbdType));
		editor.setSpecTmp(RDBDHelper.clone(editor.getSpec(), this.rdbdType));
		editor.setSpecText(editor.getParser().getText(editor.getSpec()));
		if (editor.getSpec() instanceof DecomposeSpecification) {
			editor.setTargetLevel(((DecomposeSpecification)editor.getSpec()).getTargetLevel());
			editor.setMaxLostDependencies(Integer.toString(((DecomposeSpecification)editor.getSpec()).getMaxLostDependencies()));
		} else if (editor.getSpec() instanceof NormalizationSpecification) {
			editor.setTargetLevel(((NormalizationSpecification)editor.getSpec()).getTargetLevel());
			editor.setMaxLostDependencies(Integer.toString(((NormalizationSpecification)editor.getSpec()).getMaxLostDependencies()));
		}
		return editor;
	}
	
	public int checkForDuplicate(int rdbdType, RDBDSpecification specification, Connection conn) throws Exception {
		return checkForDuplicate(rdbdType, null, specification, conn);
	}

	public int checkForDuplicate(int rdbdType, Integer exclusiveId, RDBDSpecification specification, Connection conn) throws Exception {
		Blob blob;
		String sql;
		Statement stmt = null;
		ResultSet rset = null;
		ObjectInputStream in = null;
		RDBDSpecification refSpecification = null;
		
		int duplicateExerciseID = -1;
		
		try {
			sql = "";
			sql = sql.concat("SELECT	id, specification ");
			sql = sql.concat("FROM		exercises ");
			sql = sql.concat("WHERE		rdbd_type = " + rdbdType + " ");
			if (exclusiveId != null) {
				sql = sql.concat("AND		id != " + exclusiveId);
			}
			
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
	
			while ((rset.next()) && (duplicateExerciseID == -1)) {
				//TODO: resolve dependency on eTutor core classes
				blob = JDBCAdapter.getOracleBlob(rset, 2);
				//blob =((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob != null){
					in = new ObjectInputStream(blob.getBinaryStream());
					refSpecification = (RDBDSpecification)in.readObject();
					in.close();
					in = null;
					if (refSpecification.semanticallyEquals(specification)) {
						duplicateExerciseID = rset.getInt("id");
					}
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (rset != null) {
				try {
					rset.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "", e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "", e);
				}
			}
		}
		
		return duplicateExerciseID;
	}

	/**
	 * While {@link #fetchExercise(int)} delivers an object embedding the specification,
	 * which is designed to be used for view components (exercise manager servlets),
	 * this method directly returns the specification object.
	 *  
	 * @param exerciseID
	 * @return The specification object 
	 * @throws Exception
	 */
	public static Serializable fetchSpecification(int exerciseID) throws Exception{
		Blob blob;
		String query;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		Serializable specification = null;
		ObjectInputStream in = null;
		
		query = "";
		query = query.concat("SELECT 	specification ");
		query = query.concat("FROM 		exercises ");
		query = query.concat("WHERE		id = " + exerciseID);

		try {
			conn = RDBDHelper.getPooledConnection();
			conn.setAutoCommit(false);
			stmt= conn.createStatement();
			rset = stmt.executeQuery(query);
	
			if (rset.next()) {
				//TODO: resolve dependency on eTutor core classes
				blob = JDBCAdapter.getOracleBlob(rset, 1);
				//blob =((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob!=null){
					in = new ObjectInputStream(blob.getBinaryStream());
					specification = (Serializable)in.readObject();
					in.close();
					in = null;
				}
			}
			
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					RDBDHelper.getLogger().log(Level.SEVERE, "", ex);
				}
			}
			throw e;
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
			if (rset != null){
				try {
					rset.close();
				} catch (SQLException e){
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}

			}
			if (stmt != null){
				try {
					stmt.close();
				} catch (SQLException e){
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e){
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
		}

		return specification;
	}

	public static int fetchInternalType(int exerciseID) throws Exception {
		int type = -1;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		try{
			conn = RDBDHelper.getPooledConnection();
			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT rdbd_type FROM exercises WHERE id = " + exerciseID);
			if (rset.next()) {
				type = rset.getInt("rdbd_type");
			}
		} catch (Exception e){
			throw e;
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch (Exception e) {
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					RDBDHelper.getLogger().log(Level.SEVERE, "", e);
				}
			}
		}

		return type;
	}

	public int getRdbdType() {
		return rdbdType;
	}

	public void setRdbdType(int rdbdType) {
		this.rdbdType = rdbdType;
	}

	public String generateHtml(Serializable s, Locale locale) {
		String msg;
		
		if (s == null) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is null";
			logger.log(Level.SEVERE, msg);
		} else if (!(s instanceof SpecificationEditor)) {
			msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is of type " + s.getClass().getName();
			logger.log(Level.SEVERE, msg);
		} else {
			try {
				return RDBDHelper.getAssignmentText(((SpecificationEditor)s).getSpecTmp(), 0, locale, this.rdbdType);
			} catch (IOException e) {
				msg = "Unexpected exception occured when generating assignment text ";
				msg += "for RDBD specification of type " + this.rdbdType;
				logger.log(Level.SEVERE, msg);
			}			
		}
		
		return null;
	}
}