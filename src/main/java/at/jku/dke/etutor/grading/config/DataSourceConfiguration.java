package at.jku.dke.etutor.grading.config;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {
    private final ApplicationProperties properties;

    public DataSourceConfiguration(ApplicationProperties properties){
        this.properties=properties;
    }


    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(properties.getDatasource().getDriver());
        dataSource.setUsername(properties.getDatasource().getUsername());
        dataSource.setPassword(properties.getDatasource().getPassword());
        dataSource.setUrl(properties.getDatasource().getUrl());

        return dataSource;
    }

}
