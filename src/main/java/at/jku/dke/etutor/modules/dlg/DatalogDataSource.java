package at.jku.dke.etutor.modules.dlg;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.config.DataSourceConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatalogDataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;


    public static synchronized Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DatalogDataSource(ApplicationProperties properties){
        config.setDriverClassName(properties.getDatasource().getDriverClassName());
        config.setJdbcUrl(properties.getDatasource().getUrl() + properties.getDatalog().getConnUrl());
        config.setUsername(properties.getDatasource().getUsername());
        config.setPassword(properties.getDatasource().getPassword());
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
