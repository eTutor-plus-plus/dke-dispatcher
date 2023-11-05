package at.jku.dke.etutor.modules.ddl.serverAdministration;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import ch.qos.logback.classic.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

// Class to handle the database connections for several users
public class DBHelper {
    //region Constants
    private final String driverClassName;
    //todo Check if this is necessary
    //private final String baseUrl;
    private final String connUrl;
    private final String connPwd;
    private final String connUser;
    private final long maxLifetime;
    //region

    //region Fields
    private static Logger logger = null;
    private static Connection conn = null;
    private static HikariDataSource dataSource = null;
    private HikariConfig config = null;
    //endregion

    public DBHelper(ApplicationProperties properties) {
        // Initialize logger
        try {
            logger = (Logger) LoggerFactory.getLogger(RAEvaluator.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize connections constants
        this.driverClassName = properties.getDatasource().getDriverClassName();
        this.connUrl = properties.getDatasource().getUrl();
        this.connUser = properties.getDatasource().getUsername();
        this.connPwd = properties.getDatasource().getPassword();
        this.maxLifetime = properties.getDatasource().getMaxLifetime();

        // Initialize Hikari connection
        config.setDriverClassName(this.driverClassName);
        config.setJdbcUrl(this.connUrl);
        config.setUsername(this.connUser);
        config.setPassword(this.connPwd);
        config.setMaxLifetime(this.maxLifetime);
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("socketTimeout", "30");
        dataSource = new HikariDataSource(config);
    }

    /**
     * Function to close the system connection
     */
    public static synchronized void closeSystemConnection() throws SQLException {
        if(conn != null)
            conn.close();
    }

    /**
     * Function to get the system connection
     * @return Returns the Connection object for the system connection
     * @throws SQLException
     */
    public static synchronized Connection getSystemConnection() throws SQLException {
        if(conn != null && !conn.isClosed())
            return conn;

        conn = dataSource.getConnection();

        return conn;
    }

    /**
     *  Function to get the conncetion for a specified user
     * @param user Specifies the username
     * @param pwd Specifies the password
     * @return Returns the established connection
     */
    public static Connection getUserConnection(String user, String pwd) {
        return null;
    }

    public static Logger getLogger() {
        return logger;
    }
}
