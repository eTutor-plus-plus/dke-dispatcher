package at.jku.dke.etutor.grading.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * Primary DataSource bean used by Hibernate-JPA
 */
@Configuration
public class DataSourceConfiguration {
    private final ApplicationProperties properties;

    public DataSourceConfiguration(ApplicationProperties properties){
        this.properties=properties;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(properties.getDatasource().getDriverClassName());
        dataSource.setUsername(properties.getDatasource().getUsername());
        dataSource.setPassword(properties.getDatasource().getPassword());
        dataSource.setUrl(properties.getDatasource().getUrl() + properties.getDatasource().getDatabase());
        dataSource.setMaxConnLifetimeMillis(properties.getDatasource().getMaxLifetime());
        dataSource.setMaxTotal(properties.getDatasource().getMaxPoolSize());

        return dataSource;
    }

}
