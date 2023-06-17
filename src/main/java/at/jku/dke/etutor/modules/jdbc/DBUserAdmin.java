package at.jku.dke.etutor.modules.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

/**
 * This class is designed as alternative to a connection pool. The only instance
 * of this class can be used to assure that not more than one student can work on a
 * database scheme at the same time. The reason for not using a connection pool
 * is that ressources like JDBC Statements might be left open and live on if
 * references are kept anywhere in the connection.
 *
 * @author Georg Nitsche (15.11.2005)
 *
 */
public class DBUserAdmin {

    private boolean initialized = false;
    private Vector freeUsers = new Vector();
    private Vector busyUsers = new Vector();

    private static DBUserAdmin admin;

    static {
        try {
            admin = new DBUserAdmin();
        } catch (SQLException e) {
            JDBCHelper.getLogger().log(Level.SEVERE, "Exception when initializing DB user pool.", e);
        }
    }

    private DBUserAdmin() throws SQLException {
        init();
    }

    public static DBUserAdmin getAdmin() {
        return admin;
    }

    private synchronized void init() throws SQLException {
        if (initialized) return;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        String[] info;
        String msg;

        try {
            con = JDBCHelper.getSystemConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT username, password FROM dbusers");
            while (rs.next()) {
                info = new String[]{rs.getString("username"), rs.getString("password")};
                freeUsers.add(info);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    JDBCHelper.getLogger().log(Level.SEVERE, "Exception when closing result set.", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    JDBCHelper.getLogger().log(Level.SEVERE, "Exception when closing statement.", e);
                }
            }
        }
        if (freeUsers.size() == 0) {
            msg = "No user connection info retrieved from dbusers.";
            JDBCHelper.getLogger().log(Level.WARNING, msg);
        } else {
            msg = "Fetched connection info for " + freeUsers.size() + " user(s).\n";
            for (int i = 0; i < freeUsers.size(); i++) {
                info = (String[])freeUsers.get(i);
                msg += (i == 0 ? "" : ", ") + info[0];
            }
            JDBCHelper.getLogger().log(Level.INFO, msg);
        }
        initialized = true;
    }

    public synchronized String getUser() throws IllegalStateException {
        boolean firstTry;
        String msg;

        if (!initialized) throw new IllegalStateException("Connection information not initialized!");
        firstTry = true;
        while (freeUsers.size() == 0) {
            if (firstTry) {
                firstTry = false;
            } else {
                msg = "Already waiting more than 30 seconds for user to become ";
                msg += "available for creating JDBC connection ";
                msg += "(Total amount of busy users: " + busyUsers.size() + ").";
                JDBCHelper.getLogger().log(Level.WARNING, msg);
            }
            try {
                wait(30000);
            } catch (Exception ignore) {}
        }

        String[] info = (String[])freeUsers.remove(0);
        busyUsers.add(info);
        //System.out.println("Connection info retrieved: " + info);
        if (info != null) {
            //return user
            return info[0];
        }
        return null;
    }

    public String getPassword(String user) throws IllegalStateException {
        String[] info;
        if (!initialized) throw new IllegalStateException("Connection information not initialized!");
        for (int i = 0; i < busyUsers.size(); i++) {
            info = (String[])busyUsers.get(i);
            if (user != null && user.equals(info[0])) {
                return info[1];
            }
        }
        return null;
    }

    public synchronized void releaseUser(String user) {
        String[] info;

        if ((info = getBusyUserInfo(user)) != null) {
            busyUsers.remove(info);
            freeUsers.add(info);
            JDBCHelper.getLogger().log(Level.INFO, "User " + user + " released for a new connection.");
            notifyAll();
        }
    }

    private String[] getBusyUserInfo(String user) {
        String[] info;
        for (int i = 0; i < busyUsers.size(); i++) {
            info = (String[])busyUsers.get(i);
            if (user != null && user.equals(info[0])) {
                return info;
            }
        }
        return null;
    }

    public static void main(String[] a) {
        String user;
        String pwd;
        Vector list;
        Connection con;

        con = null;
        list = new Vector();
        for (int i = 0; i < 100; i++) {
            user = DBUserAdmin.getAdmin().getUser();
            pwd = DBUserAdmin.getAdmin().getPassword(user);
            try {
                con = JDBCHelper.getConnection(user, pwd);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(user);
            System.out.println((i + 3) + ": user: " + user + ", pwd: " + pwd);
            if (i % 3 == 0) {
                user = (String)list.remove(0);
                DBUserAdmin.getAdmin().releaseUser(user);
                System.out.println("\tReleased user " + user);
            }
        }
    }
}