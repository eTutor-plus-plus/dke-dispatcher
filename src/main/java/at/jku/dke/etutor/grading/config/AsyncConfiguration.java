package at.jku.dke.etutor.grading.config;

import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * The Configuration to enable async handling of requests see {@link at.jku.dke.etutor.grading.service.SubmissionDispatcherService#run(Submission, Locale)}
 */
@Configuration
public class AsyncConfiguration {
    private ApplicationProperties applicationProperties;

    public AsyncConfiguration( ApplicationProperties applicationProperties){
        this.applicationProperties=applicationProperties;
    }

    /**
     * A Bean representing the configuration
     * @return an Executor instance
     */
    @Bean(name="taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(applicationProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(applicationProperties.getAsync().getMaxPoolSize());
        executor.setThreadNamePrefix(applicationProperties.getAsync().getThreadNamePrefix());
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.initialize();
        return executor;
    }
}
