package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.specification.DecomposeSpecification;
import at.jku.dke.etutor.modules.nf.specification.NFSpecification;
import at.jku.dke.etutor.modules.nf.specification.NormalizationSpecification;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.ui.SpecificationEditor;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BLOB;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Super class for exercise manager classes for all types of the RDBD module.
 * Since exercise IDs of existing exercises are mapped to the internal type,
 * a single class to process tasks for all RDBD types would be sufficient 
 * (like the RDBDEvaluator implementation). Nevertheless, based on the exercise ID, 
 * passed as argument to method {@link #createExercise(Serializable) createExercise},
 * the retrieval of the appropriate internal type is not possible. This is why
 * each RDBD type has its own implementation which is registered with the eTutor core. 
 *  
 * @author Georg Nitsche (12.12.2005)
 *
 */
public class RDBDExercisesManager {

	protected Logger logger;
	protected int rdbdType;

	public RDBDExercisesManager(int rdbdType) {
		try {
			this.logger = Logger.getLogger("etutor.modules.rdbd");
		} catch (Exception e){
			e.printStackTrace();		
		}
		this.rdbdType = rdbdType;
	}
	
	private RDBDExercisesManager() { 
	}
	
	public int createExercise(Serializable editor) throws Exception {
		BLOB blob = null;
		ObjectOutputStream out = null;
		NFSpecification specification;
		int duplicateId;
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		if (editor == null) {
			String msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is " + editor;
			logger.log(Level.SEVERE, msg);
			throw new Exception(msg);
		}
		
		if (!(editor instanceof SpecificationEditor)) {
			String msg = "Expected object of type " + SpecificationEditor.class.getName();
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
				String msg = "Settings of the specified exercise are equal to those of existing exercise ";
				msg += "with ID " + duplicateId + ". Please change your settings and try again.";
				throw new Exception(msg);
			}
			
			//GET MAX ID FROM DB
			String sql = "SELECT MAX(id) FROM exercises";
			String msg = "QUERY for select max id value:\n" + sql;
			this.logger.log(Level.INFO, msg);
			
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int exerciseId = 1;
			if (rs.next()) {
				exerciseId = rs.getInt(1) + 1;
			}
			
			sql = "INSERT INTO exercises VALUES (";
			sql = sql.concat(exerciseId + ", ");
			sql = sql.concat("empty_blob(), " + this.rdbdType + ")");

			System.out.println("INSERTING EMPTY BLOB QUERY:\n" + sql);
			
			stmt= conn.createStatement();
			stmt.execute(sql);

			sql = "SELECT specification ";
			sql = sql.concat("FROM	 exercises ");
			sql = sql.concat("WHERE	 id=" + exerciseId + " for update");

			System.out.println("RETRIEVING EMPTY BLOB QUERY:\n" + sql);
			
			stmt.close();
			stmt = null;
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);

			if (rset.next()) {
				//TODO: resolve dependency on eTutor core classes
				blob = getOracleBlob(rset, 1);
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

	public boolean modifyExercise(int exerciseId, Serializable editor) throws Exception {
		String msg;
		String sql;
		BLOB blob = null;
		ObjectOutputStream out = null;
		NFSpecification specification;
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
				blob = getOracleBlob(rset, 1);
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
		NFSpecification specification = null;
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
				blob = getOracleBlob(rset, 1);
				//blob = ((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob!=null){
					System.out.println("Found entry ...");
					in = new ObjectInputStream(blob.getBinaryStream());
					specification = (NFSpecification)in.readObject();
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
	
	public int checkForDuplicate(int rdbdType, NFSpecification specification, Connection conn) throws Exception {
		return checkForDuplicate(rdbdType, null, specification, conn);
	}

	public int checkForDuplicate(int rdbdType, Integer exclusiveId, NFSpecification specification, Connection conn) throws Exception {
		Blob blob;
		String sql;
		Statement stmt = null;
		ResultSet rset = null;
		ObjectInputStream in = null;
		NFSpecification refSpecification;
		
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
				blob = getOracleBlob(rset, 2);
				//blob =((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob != null){
					in = new ObjectInputStream(blob.getBinaryStream());
					refSpecification = (NFSpecification)in.readObject();
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
	public static String fetchSpecification(int exerciseID) throws Exception{
		String specification = null;

		try (
			Connection conn = RDBDHelper.getPooledConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("SELECT specification FROM exercises WHERE id = " + exerciseID)
        ) {
			// conn.setAutoCommit(false);

			if (rset.next()) {
				/*//TODO: resolve dependency on eTutor core classes
				Blob blob = getOracleBlob(rset, 1);
				//blob =((oracle.jdbc.OracleResultSet)rset).getBLOB("specification");
				if(blob!=null){
					in = new ObjectInputStream(blob.getBinaryStream()); // Note: "in" was variable of type ObjectInputStream
					specification = (Serializable)in.readObject();
					in.close();
					in = null;
				}*/

				specification = rset.getString("specification");
			}
			
			// conn.commit(); // Note: Why would we commit a SELECT query that changed nothing? (Gerald Wimmer, 2023-12-01)
		} catch (Exception e) {
			/*if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex){
					RDBDHelper.getLogger().log(Level.SEVERE, "", ex);
				}
			}*/ // Note: Again, why would we have to rollback() a SELECT query? (Gerald Wimmer, 2023-12-01)
			RDBDHelper.getLogger().log(Level.SEVERE, "", e);
			throw e;
		}

		return specification;
	}

	public static int fetchInternalType(int exerciseID) throws Exception {
		int type = -1;
		
		try (
			Connection conn = RDBDHelper.getPooledConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("SELECT rdbd_type FROM exercises WHERE id = " + exerciseID)
		) {
			if (rset.next()) {
				type = rset.getInt("rdbd_type");
			}
		} catch (Exception e) {
			RDBDHelper.getLogger().log(Level.SEVERE, "", e);
			throw e;
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

	/*
	 * Note: The following methods were copied from the old eTutor class JDBCAdapter (Gerald Wimmer, 2023-11-21)
	 */
	/**
	 * The following steps are performed: First the passed <code>ResultSet</code> is
	 * unwrapped using the {@link #getNativeResultSet(ResultSet)} method. If the passed
	 * <code>ResultSet</code> is a wrapper, the underlying <code>ResultSet</code> is
	 * retrieved that way (e.g. when the <code>ResultSet</code> comes from a connection
	 * pooling framework). If possible, the <code>ResultSet</code> is then cast to
	 * the Oracle implementation and the proprietary <code>BLOB</code> object at the
	 * specified index returned.
	 *
	 * @param rset A <code>ResultSet</code>, which might be a wrapper from a connection pooling
	 * framework, an Oracle implementation or any other imlementation.
	 * @param index The column index in the <code>ResultSet</code>, which holds the blob result.
	 * @return The Oracle <code>BLOB</code> or <ocde>null</code>, if the passed
	 * <code>ResultSet</code> is no Oracle implementation of the expected JDBC driver.
	 * @throws SQLException if an exception occured when trying to get the native <code>ResultSet</code>
	 * (the passed object may be a wrapper) or when trying to get the BLOB from the Oracle
	 * <code>ResultSet</code>.
	 */
	public static BLOB getOracleBlob(ResultSet rset, int index) throws SQLException {
		BLOB blob = null;

		ResultSet nativeRset = getNativeResultSet(rset);
		if (nativeRset instanceof OracleResultSet) {
			blob = ((OracleResultSet)nativeRset).getBLOB(index);
		}
		return blob;
	}

	/**
	 * Returns a wrapped <code>ResultSet</code> object, if it is wrapped by the passed
	 * <code>ResultSet</code> object.
	 *
	 * @param rset Any <code>ResultSet</code>.
	 * @return The underlying statement if the passed <code>ResultSet</code>
	 * 			object is of the expected type and is a wrapper of it.
	 * 			Otherwise, the passed <code>ResultSet</code> is returned again.
	 * @throws SQLException
	 */
	public static ResultSet getNativeResultSet(ResultSet rset) throws SQLException {
		ResultSet delegate = getNativeSQLObject(rset);
		if (delegate instanceof ResultSet) {
			return delegate;
		} else {
			return rset;
		}
	}

	/**
	 * Assumes that the passed object is a wrapper object from the DBCP project and
	 * tries to return the underlying <code>Connection</code>, <code>Statement</code>,
	 * <code>PreparedStatement</code>, <code>CallableStatement</code> or <code>ResultSet</code>.
	 * (see http://jakarta.apache.org/commons/dbcp/apidocs/index.html)
	 *
	 * @param sqlObject
	 * @return
	 */
	private static ResultSet getNativeSQLObject(ResultSet sqlObject) {
		ResultSet delegate = sqlObject;
		String methodName = "";

		try {
			if (delegate instanceof ResultSet) {
				methodName = "getDelegate";
			} else {
				return null;
			}

			Method method = sqlObject.getClass().getMethod(methodName, null);
			delegate = (ResultSet) method.invoke(sqlObject, null);
		} catch (Exception e) {
			String msg = "Exception was thrown when trying to call method " + methodName;
			msg += " from object " + sqlObject;
			msg += ", which is expected to be a wrapper object. " + e.getMessage();

            RDBDHelper.getLogger().log(Level.WARNING, msg);
		}

		if (delegate != null && !(delegate instanceof ResultSet)) {
			String msg = "Calling method " + methodName + " by reflection on object " + sqlObject;
			msg += " was expected to return a ResultSet object. In fact, a ";
			msg += delegate.getClass().getName() + " object was returned.";

            RDBDHelper.getLogger().log(Level.WARNING, msg);

			delegate = null;
		}

		return delegate;
	}
}