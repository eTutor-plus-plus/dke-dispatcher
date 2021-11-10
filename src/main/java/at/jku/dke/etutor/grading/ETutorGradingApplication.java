package at.jku.dke.etutor.grading;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.config.AsyncConfiguration;
import at.jku.dke.etutor.grading.config.DataSourceConfiguration;
import at.jku.dke.etutor.grading.rest.ETutorGradingController;
import at.jku.dke.etutor.grading.rest.ETutorSubmissionController;
import at.jku.dke.etutor.grading.rest.ETutorSQLController;
import at.jku.dke.etutor.grading.service.ModuleService;
import at.jku.dke.etutor.grading.service.RepositoryService;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import at.jku.dke.etutor.grading.service.XQueryResourceService;
import at.jku.dke.etutor.modules.ra2sql.RAEvaluator;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import at.jku.dke.etutor.modules.sql.SQLEvaluator;
import at.jku.dke.etutor.modules.sql.report.SQLReporter;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
        ModuleService.class,
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
        XQExerciseManagerImpl.class
})
@EnableConfigurationProperties({ApplicationProperties.class})
@SpringBootApplication
@EnableAsync
public class ETutorGradingApplication {
    private final Environment env;

    public static void main(String[] args) {
        SpringApplication.run(ETutorGradingApplication.class, args);
    }

    public ETutorGradingApplication(Environment env){this.env=env;}
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



    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        //enabling swagger-ui part for visual documentation
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }
}
