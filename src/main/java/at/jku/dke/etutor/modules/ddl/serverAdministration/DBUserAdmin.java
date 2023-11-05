package at.jku.dke.etutor.modules.ddl.serverAdministration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

// Class to ensure that every user has their own database scheme
public class DBUserAdmin {
    //region Fields
    private Vector freeUsers = null;
    private Vector buysUsers = null;
    //endregion

    public DBUserAdmin() throws SQLException {
        freeUsers = new Vector();
        buysUsers = new Vector();
        init();
    }

    /**
     * Function to initialize the database connection class
     */
    private synchronized void init() throws SQLException {
        // Create auxiliary variables
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String[] info;

        // Get all available users and add them as free users
        try {
            conn = DBHelper.getSystemConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT username, password FROM dbusers");
            while (rs.next()) {
                info = new String[] { rs.getString("username"), rs.getString("password") };
                freeUsers.add(info);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    DBHelper.getLogger().error("Exception when closing result set.", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    DBHelper.getLogger().error("Exception when closing statement.", e);
                }
            }
        }

    }

    public String getUser() {
        return "";
    }

    public String getPwd() {
        return "";
    }

    public void releaseUser(String user) {

    }

    //todo Implement Singelton??
    public DBUserAdmin getAdmin() {
        return this;
    }

    private String[] getBusyUserInfo(String user) {
        return null;
    }
}
