package at.jku.dke.etutor.modules.ddl.serverAdministration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

// Class to ensure that every user has their own database scheme
public class DBUserAdmin {
    //region Fields
    private Vector freeUsers;
    private Vector busyUsers;
    private boolean initialized = false;
    private static DBUserAdmin admin;
    //endregion

    static {
        try {
            admin = new DBUserAdmin();
        } catch (SQLException ex) {
            DBHelper.getLogger().error("Exception when initializing DB user pool.", ex);
        }
    }

    private DBUserAdmin() throws SQLException {
        freeUsers = new Vector();
        busyUsers = new Vector();
        init();
    }

    public static DBUserAdmin getAdmin() {
        return admin;
    }

    /**
     * Function to initialize the database connection class
     * @throws SQLException if the connection failed
     */
    private synchronized void init() throws SQLException {
        if(initialized)
            return;

        // Create auxiliary variables
        Connection conn;
        Statement stmt = null;
        ResultSet rs = null;
        String[] info;

        // Get all available users and add them as free users
        try {
            conn = DBHelper.getSystemConnection();
            if(conn == null) {
                DBHelper.getLogger().error("Connection is null.");
                return;
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT conn_user, conn_pwd, schema_name FROM connections");
            while (rs.next()) {
                info = new String[] { rs.getString("conn_user"), rs.getString("conn_pwd"), rs.getString("schema_name") };
                freeUsers.add(info);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    DBHelper.getLogger().error("Exception when closing result set.", ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    DBHelper.getLogger().error("Exception when closing statement.", ex);
                }
            }
        }

        initialized = true;
    }

    /**
     * Function to get a free user
     * @return Returns the user or null if something went wrong
     * @throws IllegalStateException if not initalized
     */
    public synchronized String getUser() throws IllegalStateException {
        if(!initialized)
            throw new IllegalStateException("Connection information not initialized!");

        boolean firstTry = true;
        String msg;

        while (freeUsers.size() == 0) {
            if (firstTry) {
                firstTry = false;
            } else {
                msg = "Already waiting more than 30 seconds for user to become ";
                msg += "available for creating JDBC connection ";
                msg += "(Total amount of busy users: " + busyUsers.size() + ").";
                DBHelper.getLogger().warn(msg);
            }
            try {
                wait(30000);
            } catch (Exception ignore) {
            }
        }

        String[] info = (String[]) freeUsers.remove(0);

        if(info != null) {
            busyUsers.add(info);
            return info[0];
        }

        return null;
    }

    /**
     * Function to get the password for a specified user
     * @param user Specifies the user
     * @return Returns the password for the user or null if the user is not in the busyUsers vector
     * @throws IllegalStateException if not initialized
     */
    public String getPwd(String user) throws IllegalStateException {
        if (!initialized)
            throw new IllegalStateException("Connection information not initialized!");

        String[] info;

        for (Object busyUser : busyUsers) {
            info = (String[]) busyUser;
            if (user != null && user.equals(info[0])) {
                return info[1];
            }
        }
        return null;
    }

    /**
     * Function to get the schema for a specified user
     * @param user Specifies the user
     * @return Returns the schema for the user or null if the user is not in the busyUsers vector
     * @throws IllegalStateException if not initialized
     */
    public String getSchema(String user) throws IllegalStateException {
        if (!initialized)
            throw new IllegalStateException("Connection information not initialized!");

        String[] info;

        for (Object busyUser : busyUsers) {
            info = (String[]) busyUser;
            if (user != null && user.equals(info[0])) {
                return info[2];
            }
        }
        return null;
    }

    /**
     * Function to release a busy user
     * @param user Specifies the user
     */
    public synchronized void releaseUser(String user) {
        if (!initialized)
            throw new IllegalStateException("Connection information not initialized!");

        String[] info;

        if ((info = getBusyUserInfo(user)) != null) {
            busyUsers.remove(info);
            freeUsers.add(info);
            DBHelper.getLogger().info("User " + user + " released for a new connection.");
            notifyAll();
        }
    }

    /**
     * Function to get the vector for a specified user
     * @param user Specifies the user
     * @return Returns the vector for the user or null if the user is not busy
     */
    private String[] getBusyUserInfo(String user) {
        String[] info;
        for (Object busyUser : busyUsers) {
            info = (String[]) busyUser;
            if (user != null && user.equals(info[0])) {
                return info;
            }
        }
        return null;
    }
}
