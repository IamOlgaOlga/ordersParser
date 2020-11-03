package com.cardpay.parser.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class ApplicationConfig {

    @Bean
    public ExecutorService threadPoolExecutor(@Value("${parser.threadpool.maxPoolSize}") int maxPoolSize){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                0, maxPoolSize, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
}
