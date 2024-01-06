package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.RDBDHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Remaining internal exercise manager class for the NF module.
 *  
 * @author Georg Nitsche (12.12.2005)
 * @author Gerald Wimmer (06.01.2024) (conversion to JSON specifications, removal of no longer necessary methods)
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

	/**
	 * This method directly returns the JSON string representation of an exercise's specification.
	 *  
	 * @param exerciseID
	 * @return The specification object 
	 * @throws Exception
	 */
	public static String fetchSpecification(int exerciseID) throws SQLException {
		String specification = null;

		try (
			Connection conn = RDBDHelper.getPooledConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("SELECT specification FROM exercises WHERE id = " + exerciseID)
        ) {
			// conn.setAutoCommit(false);

			if (rset.next()) {
                specification = rset.getString("specification");
			}

			// conn.commit(); // Note: Why would we commit a SELECT query that changed nothing? (Gerald Wimmer, 2023-12-01)
		} catch (SQLException e) {
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

	/*public String generateHtml(Serializable s, Locale locale) {
		if (s == null) {
			String msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is null";
			logger.log(Level.SEVERE, msg);
		} else if (!(s instanceof SpecificationEditor)) {
			String msg = "Expected object of type " + SpecificationEditor.class.getName();
			msg += ", but passed object is of type " + s.getClass().getName();
			logger.log(Level.SEVERE, msg);
		} else {
			try {
				return RDBDHelper.getAssignmentText(((SpecificationEditor)s).getSpecTmp(), 0, locale, this.rdbdType);
			} catch (IOException e) {
				String msg = "Unexpected exception occurred when generating assignment text ";
				msg += "for RDBD specification of type " + this.rdbdType;
				logger.log(Level.SEVERE, msg);
			}			
		}
		
		return null;
	}*/
}