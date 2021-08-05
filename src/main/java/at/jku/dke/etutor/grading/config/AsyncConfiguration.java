package at.jku.dke.etutor.grading.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * The Configuration to enable async handling of requests
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
    @Bean(name="asyncExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(applicationProperties.getAsync().getCorePoolSize());
        executor.setMaxPoolSize(applicationProperties.getAsync().getMaxPoolSize());
        executor.setQueueCapacity(applicationProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix(applicationProperties.getAsync().getThreadNamePrefix());
        executor.initialize();
        return executor;
    }
}
