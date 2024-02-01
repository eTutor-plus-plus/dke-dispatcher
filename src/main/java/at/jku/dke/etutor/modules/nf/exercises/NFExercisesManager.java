package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.NFHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Remaining internal exercise manager class for the NF module.
 *  
 * @author Georg Nitsche (12.12.2005)
 * @author Gerald Wimmer (06.01.2024) (conversion to JSON specifications, removal of no longer necessary methods)
 *
 */
public class NFExercisesManager {

	protected Logger logger;

	private NFExercisesManager() {
		// This class is not meant to be instantiated (Gerald Wimmer, 2024-01-11)
	}

	/**
	 * This method directly returns the JSON string representation of an exercise's specification.
	 * @param exerciseID The id of the exercise whose specification is to be returned
	 * @return The specification object of the exercise
	 * @throws SQLException if there is a database error
	 */
	public static String fetchSpecification(int exerciseID) throws SQLException {
		String specification = null;

		try (
				Connection conn = NFHelper.getPooledConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT specification FROM exercises WHERE id = ?")
        ) {
			stmt.setInt(1, exerciseID);

			ResultSet rset = stmt.executeQuery();

			if (rset.next()) {
                specification = rset.getString("specification");
			}
		} catch (SQLException e) {
			NFHelper.getLogger().log(Level.SEVERE, "", e);
			throw e;
		}

		return specification;
	}

	/**
	 * Returns the ordinal of the nf task subtype for the specified exercise
	 * @param exerciseID The id of the exercise whose type is to be queried
	 * @return The ordinal of the nf task subtype for the specified exercise
	 * @throws SQLException if there is a database error
	 */
	public static int fetchInternalType(int exerciseID) throws SQLException {
		int type = -1;
		
		try (
				Connection conn = NFHelper.getPooledConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT rdbd_type FROM exercises WHERE id = ?")
		) {
			stmt.setInt(1, exerciseID);

			ResultSet rset = stmt.executeQuery();

			if (rset.next()) {
				type = rset.getInt("rdbd_type");
			}
		} catch (SQLException e) {
			NFHelper.getLogger().log(Level.SEVERE, "", e);
			throw e;
		}

		return type;
	}
}