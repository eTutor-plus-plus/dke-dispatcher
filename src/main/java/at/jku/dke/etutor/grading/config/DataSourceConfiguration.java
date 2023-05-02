package at.jku.dke.etutor.grading.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


/**
 * Primary DataSource bean used by Hibernate-JPA
 */
@Configuration
public class DataSourceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("application.datasource")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }
    @Bean
    @Primary
    @ConfigurationProperties("application.datasource.configuration")
    public DataSource defaultDataSource() {
        return defaultDataSourceProperties().initializeDataSourceBuilder()
                .type(BasicDataSource.class).build();
    }
}
