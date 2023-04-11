package at.jku.dke.etutor.modules.fd;

import at.jku.dke.etutor.modules.fd.entities.*;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "at.jku.dke.etutor.modules.fd.repositories",
        entityManagerFactoryRef = "fdEntityManagerFactory",
        transactionManagerRef= "fdTransactionManager")
public class FdDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("application.datasource.fd")
    public DataSourceProperties fdDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("application.datasource.fd.configuration")
    public DataSource fdDataSource() {
        return fdDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "fdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean fdEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(fdDataSource())
                .packages("at.jku.dke.etutor.modules.fd.entities")
                .build();
    }

    @Bean
    public PlatformTransactionManager fdTransactionManager(
            final @Qualifier("fdEntityManagerFactory") LocalContainerEntityManagerFactoryBean fdEntityManagerFactory) {
        return new JpaTransactionManager(fdEntityManagerFactory.getObject());
    }
}