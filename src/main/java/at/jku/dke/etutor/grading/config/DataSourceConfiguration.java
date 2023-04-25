package at.jku.dke.etutor.grading.config;

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
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(defaultDataSource())
                .packages("at.jku.dke.etutor.grading.rest.model.entities")
                .persistenceUnit("Default") //?
                .build();
    }
    @Bean
    public PlatformTransactionManager transactionManager(
            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}
