package at.jku.dke.etutor.modules.nf;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Copied from SQLDataSource (Gerald Wimmer, 2023-11-30)
 */
@Configuration
public class NFDataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;


    public static synchronized Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public NFDataSource(ApplicationProperties properties){
        config.setDriverClassName(properties.getDatasource().getDriverClassName());
        config.setJdbcUrl(properties.getDatasource().getUrl() + properties.getNf().getConnUrl());
        config.setUsername(properties.getNf().getConnUser());
        config.setPassword(properties.getNf().getConnPwd());
        config.setMaxLifetime(properties.getDatasource().getMaxLifetime());
        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("socketTimeout", "30");
        ds = new HikariDataSource(config);
    }
}
