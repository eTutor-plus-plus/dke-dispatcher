package at.jku.dke.etutor.modules.fd;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.config.DataSourceConfiguration; //?
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//@Configuration
public class FdDataSource {
//    private static HikariConfig config = new HikariConfig();
//    private static HikariDataSource ds;
//
//
//    public static synchronized Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }
//
//    public FdDataSource(ApplicationProperties properties) {
//        config.setDriverClassName(properties.getDatasource().getDriverClassName());
//        config.setJdbcUrl(properties.getDatasource().getUrl() + properties.getFd().getConnUrl());
//        config.setUsername(properties.getFd().getConnUser());
//        config.setPassword(properties.getFd().getConnPwd());
//        config.setMaxLifetime(properties.getDatasource().getMaxLifetime());
//        config.setMaximumPoolSize(10);
//        config.setAutoCommit(false);
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        config.addDataSourceProperty("socketTimeout", "30");
//        ds = new HikariDataSource(config);
//    }


//    private final ApplicationProperties properties;
//
//    public FdDataSource(ApplicationProperties properties){
//        this.properties=properties;
//    }
//
//    @Bean
//    public HikariDataSource dataSourceFd() {
//
//        HikariDataSource hikariDataSource = new HikariDataSource();
//        hikariDataSource.setDriverClassName(properties.getDatasource().getDriverClassName());
//        hikariDataSource.setJdbcUrl(properties.getDatasource().getUrl() + properties.getFd().getConnUrl());
//        hikariDataSource.setSchema("fd");
//        hikariDataSource.setUsername(properties.getFd().getConnUser());
//        hikariDataSource.setPassword(properties.getFd().getConnPwd());
//        hikariDataSource.setMaxLifetime(properties.getDatasource().getMaxLifetime());
//        hikariDataSource.setMaximumPoolSize(10);
//        hikariDataSource.setAutoCommit(false);
//        hikariDataSource.addDataSourceProperty("cachePrepStmts", "true");
//        hikariDataSource.addDataSourceProperty("prepStmtCacheSize", "250");
//        hikariDataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        hikariDataSource.addDataSourceProperty("socketTimeout", "30");
//
//
//        return hikariDataSource;
//    }


//    @Bean
//    @ConfigurationProperties("application.datasource.fd")
//    public DataSourceProperties fdDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//    @Bean
//    @ConfigurationProperties("application.datasource.fd.configuration")
//    public DataSource fdDataSource() {
//        return fdDataSourceProperties().initializeDataSourceBuilder()
//                .type(HikariDataSource.class).build();
//    }


}
