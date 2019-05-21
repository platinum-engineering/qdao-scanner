package io.qdao.scanner.configs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qdao.scanner.components.QueueTasksExecutor;
import io.qdao.scanner.utils.JsonDateDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "io.qdao.scanner.repositories" })
@EntityScan(basePackages = { "io.qdao.scanner.models" })
public class CommonConfiguration {

    @Value("${spring.thread.pool-size:25}")
    private Integer pullSize;

    @Bean
    TaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pullSize);
        executor.setMaxPoolSize(pullSize);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        return executor;
    }

    @Bean
    QueueTasksExecutor queueTasksExecutor() {
        return new QueueTasksExecutor();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDateDeserializer())
                .create();
    }
}
