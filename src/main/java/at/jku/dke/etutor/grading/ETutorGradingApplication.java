package at.jku.dke.etutor.grading;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.config.AsyncConfiguration;
import at.jku.dke.etutor.grading.config.DataSourceConfiguration;
import at.jku.dke.etutor.grading.rest.*;
import at.jku.dke.etutor.grading.service.ModuleEvaluatorFactory;
import at.jku.dke.etutor.grading.service.RepositoryService;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import at.jku.dke.etutor.modules.dlg.DatalogDataSource;
import at.jku.dke.etutor.modules.pm.PmDataSource;
import at.jku.dke.etutor.modules.pm.PmEvaluator;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLDataSource;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.xquery.XQDataSource;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * The SpringApplication
 */
@EnableSwagger2
@ComponentScan(basePackageClasses = {
        ETutorGradingController.class,
        ETutorSubmissionController.class,
        ETutorSQLController.class,
        ModuleEvaluatorFactory.class,
        RepositoryService.class,
        ETutorGradingApplication.class,
        SubmissionDispatcherService.class,
        AsyncConfiguration.class,
        DataSourceConfiguration.class,
        SQLConstants.class,
        SQLEvaluator.class,
        RAEvaluator.class,
        SQLReporter.class,
        XQueryResourceService.class,
        XQExerciseManagerImpl.class,
        DatalogDataSource.class,
        SQLDataSource.class,
        XQDataSource.class,
        ExceptionHandler.class,
        PmDataSource.class,
        ETutorPMController.class,
        PmEvaluator.class

})
@EnableConfigurationProperties({ApplicationProperties.class})
@SpringBootApplication
@EnableAsync
public class ETutorGradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ETutorGradingApplication.class, args);
    }

    public ETutorGradingApplication(){}
    /**
     * The configuration for Swagger
     * @return a Docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis( RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }
}
