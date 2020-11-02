package com.cardpay.parser.config;


import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationConfig {

    @Value("${parser.threadpool.nThreads}")
    private int nThreads;

    @Bean
    public ExecutorService threadPoolExecutor(){
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        return executor;
    }
}
