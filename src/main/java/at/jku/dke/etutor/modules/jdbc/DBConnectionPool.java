package at.jku.dke.etutor.modules.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

import oracle.jdbc.OracleDriver;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;


/*
 * THIS CLASS MAY NOT BE INSTANTIATED IN MULTIPLE VIRTUAL MACHINES WHILE
 * REFERENCING THE SAME DATABASE! THIS WILL RESULT IN A DATABASE
 * ERROR!
 *
 * To enable access from multiple virtual machines configure different
 * DB-Access parameters!
 *
 * @author eiching
 */
public class DBConnectionPool implements ConnectionEventListener {

    private boolean initialized = false;
    private SQLException globalError = null;
    private Vector freeConnections = new Vector();
    private Vector openedConnections = new Vector();
    private String connString;
    private String usr;
    private String pwd;

    private static DBConnectionPool pool;

    static {
        try {
            pool = new DBConnectionPool();
        } catch (SQLException e) {
            JDBCHelper.getLogger().log(Level.SEVERE, "Exception when initializing DB connection pool.", e);
        }
    }

    private DBConnectionPool() throws SQLException {
        connString = JDBCHelper.getProperties().getProperty("db_connect_string");
        usr = JDBCHelper.getProperties().getProperty("db_user");
        pwd = JDBCHelper.getProperties().getProperty("db_pwd");
        init();
    }

    public static DBConnectionPool getPool() {
        return pool;
    }

    public synchronized void reInit() throws SQLException {
        close();
        init();
        globalError = null;
    }

    private synchronized void init() throws SQLException {
        if (initialized) return;
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            DriverManager.registerDriver(new OracleDriver());
            con = DriverManager.getConnection(connString,usr,pwd);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT username, password FROM dbusers");
            OracleConnectionPoolDataSource pds = new OracleConnectionPoolDataSource();
            pds.setURL(connString);
            String user = null;
            String password = null;
            PooledConnection pc = null;
            while (rs.next()) {
                user = rs.getString("username");
                password = rs.getString("password");
                pc = pds.getPooledConnection(user,password);
                pc.addConnectionEventListener(this);
                freeConnections.add(pc);
                //System.out.println("Connection for user " + user + " with password " + password + " pooled.");
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
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    JDBCHelper.getLogger().log(Level.SEVERE, "Exception when closing connection.", e);
                }
            }

        }
        initialized = true;
        globalError = null;
    }

    public synchronized Connection getConnection() throws SQLException, IllegalStateException {
        if (!initialized) throw new IllegalStateException("Connection pool not initialized!");

		/*
		while (freeConnections.size() == 0) {
			try {
			    this.wait();
			} catch (Exception ignore) {}
		}
		*/

        while (freeConnections.size() == 0) {
            if (globalError != null) {
                throw globalError;
            }
            try {
                wait();
            } catch (Exception ignore) {}
        }

        //JDBCHelper.getLogger().log(Level.INFO, "Number of free Connections: " + freeConnections.size());
		/*
		if (freeConnections.size() == 0) {
			throw new IllegalStateException("Unable to aquire DB connection. Try again later.");
		}
		*/
        PooledConnection pc = (PooledConnection) freeConnections.remove(0);
		/*
		while (pc.getConnection().isClosed()) {
			reInit();
			pc = (PooledConnection) freeConnections.remove(0);
		}
		*/
        openedConnections.add(pc);
        //System.out.println("Connection retrieved: " + pc);
        if (pc != null) {
            return pc.getConnection();
        }
        return null;
    }

    public synchronized void close() {
        if (!initialized) return;

        initialized = false;

        PooledConnection pc = null;
        int size = freeConnections.size();
        for (int i=0;i<size;i++) {
            pc = (PooledConnection) freeConnections.remove(0);
            try {
                pc.close();
            } catch (SQLException ignore) {
                JDBCHelper.getLogger().log(Level.SEVERE, "Exception when closing pooled connection.", ignore);
            }
        }
        size = openedConnections.size();
        for (int i=0;i<size;i++) {
            pc = (PooledConnection) openedConnections.remove(0);
            try {
                pc.close();
            } catch (SQLException ignore) {
                JDBCHelper.getLogger().log(Level.SEVERE, "Exception when closing pooled connection.", ignore);
            }
        }
    }

    public synchronized void connectionClosed(ConnectionEvent event) {
        if (openedConnections.contains(event.getSource())) {
            openedConnections.remove(event.getSource());
            freeConnections.add(event.getSource());
            JDBCHelper.getLogger().log(Level.INFO, "Connection returned: " + event.getSource());
            notifyAll();
        }
    }

    public synchronized void connectionErrorOccurred(ConnectionEvent event) {
        JDBCHelper.getLogger().log(Level.SEVERE, "Connection " + event.getSource() + " with error " + event.getSQLException());

        if (openedConnections.contains(event.getSource())) {
            openedConnections.remove(event.getSource());
        }
        if (freeConnections.contains(event.getSource())) {
            freeConnections.remove(event.getSource());
        }
        if ((freeConnections.size() == 0) && (openedConnections.size() == 0)) {
            try {
                reInit();
            } catch (SQLException e) {
                globalError = e;
            }
            notifyAll();
        }

    }
}