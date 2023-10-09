package at.jku.dke.etutor.modules.binarysearch;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class BinarySearchDataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static synchronized Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public BinarySearchDataSource(ApplicationProperties properties) {
        config.setDriverClassName(properties.getDatasource().getDriverClassName());
        config.setJdbcUrl(properties.getDatasource().getUrl() + properties.getBinarySearch().getConnUrl());
        config.setUsername(properties.getBinarySearch().getConnUser());
        config.setPassword(properties.getBinarySearch().getConnPwd());
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
