package com.cardpay.parser.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Application config
 */
@Configuration
public class ApplicationConfig {
    /**
     * Number of threads for thread pool start
     */
    private static final int CORE_POOL_SIZE = 0;
    /**
     * Time that idle threads will wait for new tasks before terminating
     */
    private static final int KEEP_ALIVE_TIME = 100;

    /**
     * Bean for thread pool executor.
     *
     * @param maxPoolSize value for max pool size.
     * @return {@link ExecutorService}
     */
    @Bean
    public ExecutorService threadPoolExecutor(@Value("${parser.threadpool.maxPoolSize}") int maxPoolSize,
                                              @Value("${parser.threadpool.queue.capacity}") int capacity) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, maxPoolSize, KEEP_ALIVE_TIME, MILLISECONDS, new ArrayBlockingQueue<>(capacity));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * Bean for {@link ObjectMapper} to serialize/deserialize JSON data
     *
     * @return {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
