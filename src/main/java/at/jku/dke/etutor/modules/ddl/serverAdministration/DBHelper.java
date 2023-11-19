package at.jku.dke.etutor.modules.ddl.serverAdministration;

import ch.qos.logback.classic.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

// Class to handle the database connections for several users
public class DBHelper {
    //region Constants
    private static final String driverClassName;
    //todo Check if this is necessary
    //private static final String baseUrl;
    private static final String connUrl;
    private static final String connPwd;
    private static final String connUser;
    private static final String maxLifetime;
    private static final String maxPoolSize;
    //region

    //region Fields
    private static Logger logger = null;
    private static Connection conn = null;
    private static Properties properties = null;
    private static HikariConfig config = null;
    private static HikariDataSource dataSource;
    //endregion

    static {
        // Initialize logger
        try {
            logger = (Logger) LoggerFactory.getLogger(DBHelper.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Get application properties
        try {
            properties = new Properties();
            properties.load(new FileInputStream("application.properties"));
        } catch (IOException ex) {
            logger.error("Error while accessing application properties", ex);
        }

        // Initialize connections constants
        driverClassName = properties.getProperty("application.datasource.driverClassName");
        connUrl = properties.getProperty("application.datasource.url");
        connUser = properties.getProperty("application.datasource.username");
        connPwd = properties.getProperty("application.datasource.password");
        maxLifetime = properties.getProperty("application.datasource.maxLifetime");
        maxPoolSize = properties.getProperty("application.datasource.maxPoolSize");

        // Initialize Hikari connection
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(connUrl);
        config.setUsername(connUser);
        config.setPassword(connPwd);
        config.setMaxLifetime(Long.parseLong(maxLifetime));
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("socketTimeout", "30");
        dataSource = new HikariDataSource(config);
    }

    /**
     * Function to get the system connection
     * @return Returns the Connection object for the system connection
     * @throws SQLException if an error occurs while opening the connection
     */
    public static synchronized Connection getSystemConnection() throws SQLException {
        // Check if the connection is already up
        if(conn != null && !conn.isClosed())
            return conn;

        try {
            conn = dataSource.getConnection();
            return conn;
        } catch (SQLException ex) {
            logger.error("Error when opening module connection.", ex);
        }
        return null;
    }

    /**
     * Function to close the system connection
     * @throws SQLException if an error occurs in the closing process
     */
    public static synchronized void closeSystemConnection() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.error("Error when closing module connection.", ex);
            }
        }
    }

    /**
     *  Function to get the conncetion for a specified user
     * @param user Specifies the username
     * @param pwd Specifies the password
     * @return Returns the established connection
     */
    public static Connection getUserConnection(String user, String pwd) {
        if(user == null || pwd == null)
            return null;

        try {
            return dataSource.getConnection(user, pwd);
        } catch (SQLException ex) {
            logger.error("Error when creating user connection.", ex);
        }

        return null;
    }

    public static Logger getLogger() {
        return logger;
    }
}
