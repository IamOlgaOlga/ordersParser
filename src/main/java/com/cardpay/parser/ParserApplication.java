package com.cardpay.parser;

import com.cardpay.parser.service.FileProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 *
 */
@SpringBootApplication
@AllArgsConstructor
public class ParserApplication implements CommandLineRunner {

    private final ExecutorService threadPoolExecutor;

    private final FileProcessingService fileProcessingService;

    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2) {
            System.out.println("Please provide 2 arguments.");
            System.exit(1);
        }
        String filePath1 = args[0];
        String filePath2 = args[1];
        CountDownLatch finish = new CountDownLatch(1);
        new Thread(() -> {
            try {
                fileProcessingService.process(filePath1);
            } catch (IOException e) {
                System.out.println("Cannot parse file: " + filePath1);
            } finally {
                finish.countDown();
            }
        }).start();
        new Thread(() -> {
            try {
                fileProcessingService.process(filePath2);
            } catch (IOException e) {
                System.out.println("Cannot parse file: " + filePath2);
            } finally {
                finish.countDown();
            }
        }).start();
    }
}
