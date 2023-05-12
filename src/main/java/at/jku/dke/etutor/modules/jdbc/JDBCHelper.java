package at.jku.dke.etutor.modules.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCHelper {

    public static final String EXERCISE_MODE_BEGIN="BEGIN";
    public static final String EXERCISE_MODE_END="END";
    public static final String EXERCISE_MODE_SHADOW_BEGIN="SHADOW_BEGIN";
    public static final String EXERCISE_MODE_SHADOW_END="SHADOW_END";

    private static String PROPS_PATH = "/etutor/resources/modules/jdbc/jdbc.properties";

    private static Logger logger = null;
    private static Properties props = null;
    private static Connection configConnection = null;

    static {
        logger = Logger.getLogger("JDBCModule");
        try {
            logger.addHandler(new FileHandler("./jdbc.log"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            props = new Properties();
            props.load(JDBCHelper.class.getClassLoader().getResourceAsStream(PROPS_PATH));
        } catch (Exception ignore) {
            logger.log(Level.SEVERE, "Exception occured when loading properties " + PROPS_PATH, ignore);
        }
    }


    public static void closeSystemConnection() {
        if (configConnection != null) {
            try {
                configConnection.close();
            } catch (Exception ignore) {
                JDBCHelper.getLogger().log(Level.SEVERE, "Unable to close module connection.", ignore);
            }
        }
    }

    public static Connection getSystemConnection() throws SQLException {
        if ((configConnection != null) && !configConnection.isClosed()) return configConnection;

        String usr = getProperties().getProperty("db_user");
        String pwd = getProperties().getProperty("db_pwd");
        configConnection = getConnection(usr, pwd);

        return configConnection;
    }

    public static Connection getConnection(String usr, String pwd) throws SQLException {
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        String connString = getProperties().getProperty("db_connect_string");
        Connection conn = java.sql.DriverManager.getConnection(connString,usr,pwd);
        return conn;
    }

    public static Connection getConnection(String connString, String usr, String pwd) throws SQLException {
        java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection conn = java.sql.DriverManager.getConnection(connString,usr,pwd);
        return conn;
    }

    public static Connection getConnectionFor(int exerciseID, String mode) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        Connection returnValue = null;

        try {
            String query = "SELECT connect_string, cuser, cpwd FROM connections c, exercisedb e WHERE c.id = " + mode + " AND e.exercise_id = " + exerciseID;

            //System.out.println(query);
            stmt = getSystemConnection().createStatement();
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                returnValue = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@" + rs.getString("connect_string"),rs.getString("cuser"),rs.getString("cpwd"));
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Unable to retrieve connection for exercise " + exerciseID +" in mode " + mode);
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return returnValue;
    }

    public static Properties getProperties() {
        return props;
    }

    public static Logger getLogger() {
        return logger;
    }
}
