package at.jku.dke.etutor.modules.rt;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class RTDataSource {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public static synchronized Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public RTDataSource(ApplicationProperties properties){
        config.setDriverClassName(properties.getDatasource().getDriverClassName());
        config.setJdbcUrl(properties.getDatasource().getUrl() + properties.getRt().getConnUrl());
        config.setUsername(properties.getRt().getConnUser());
        config.setPassword(properties.getRt().getConnPwd());
        config.setMaxLifetime(properties.getDatasource().getMaxLifetime());
        config.setMaximumPoolSize(30);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("socketTimeout", "30");
        ds = new HikariDataSource(config);
    }
}
