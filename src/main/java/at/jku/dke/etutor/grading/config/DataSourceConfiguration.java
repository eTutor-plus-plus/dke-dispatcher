package at.jku.dke.etutor.grading.config;

import at.jku.dke.etutor.modules.fd.entities.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;


/**
 * Primary DataSource bean used by Hibernate-JPA
 */
@EnableJpaRepositories(basePackages = "at.jku.dke.etutor.grading.rest.model.repositories",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef= "transactionManager")
@Configuration
public class DataSourceConfiguration {
//    private final ApplicationProperties properties;
//    public DataSourceConfiguration(ApplicationProperties properties){
//        this.properties=properties;
//    }
//
//    @Bean
//    @Primary
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//
//        dataSource.setDriverClassName(properties.getDatasource().getDriverClassName());
//        dataSource.setUsername(properties.getDatasource().getUsername());
//        dataSource.setPassword(properties.getDatasource().getPassword());
//        dataSource.setUrl(properties.getDatasource().getUrl() + properties.getDatasource().getDatabase());
//        dataSource.setMaxConnLifetimeMillis(properties.getDatasource().getMaxLifetime());
//        dataSource.setMaxTotal(properties.getDatasource().getMaxPoolSize());
//
//        return dataSource;
//    }

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
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean EntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(defaultDataSource())
                .packages("at.jku.dke.etutor.grading.rest.model.entities")
                .build();
    }

    @Bean
    public PlatformTransactionManager TransactionManager(
            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
