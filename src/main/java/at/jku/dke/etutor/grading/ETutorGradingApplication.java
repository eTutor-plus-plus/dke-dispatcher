package at.jku.dke.etutor.grading;


import at.jku.dke.etutor.grading.rest.ETutorGradingController;
import at.jku.dke.etutor.grading.rest.ETutorSubmissionController;
import at.jku.dke.etutor.grading.rest.ETutorSQLController;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.service.ModuleManager;
import at.jku.dke.etutor.grading.service.RepositoryManager;
import at.jku.dke.etutor.grading.service.SubmissionDispatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


@EnableSwagger2
@ComponentScan(basePackageClasses = {
        ETutorGradingController.class,
        ETutorSubmissionController.class,
        ETutorSQLController.class,
        ModuleManager.class,
        RepositoryManager.class,
        ETutorGradingApplication.class,
        SubmissionDispatcher.class,
})
@SpringBootApplication
@EnableAsync
public class ETutorGradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ETutorGradingApplication.class, args);
    }

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

    @Bean(name="asyncExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("SubmissionDispatcher-");
        executor.initialize();
        return executor;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        //enabling swagger-ui part for visual documentation
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
    }
}
