package com.cardpay.parser;

import com.cardpay.parser.service.FileProcessingService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Spring boot application for orders parsing.
 */
@SpringBootApplication
@AllArgsConstructor
@Log4j2
public class ParserApplication implements CommandLineRunner {

    /**
     * Reference
     */
    private final FileProcessingService fileProcessingService;

    /**
     * Parser application entry point.
     * @param args two orders file paths for processing.
     */
    public static void main(String[] args) {
        SpringApplication.run(ParserApplication.class, args);
    }

    /**
     * Run method of application. There are 2 files as args.
     * If there are more than 2 files application stops its work with message.
     * If some of file can't be processed the error message will be in log file.
     * In this case another file will be processed, 2 files are processed in different threads.
     * @param args two orders file paths for processing.
     * @throws Exception if any error occurs
     */
    @Override
    public void run(String... args) throws Exception {
        if (args.length != 2) {
            log.error("Please provide 2 arguments.");
            System.exit(1);
        }
        String filePath1 = args[0];
        String filePath2 = args[1];
        CountDownLatch finish = new CountDownLatch(2);
        log.info("Call the first thread to parse file {}", filePath1);
        new Thread(() -> {
            try {
                fileProcessingService.process(filePath1);
            } catch (IOException e) {
                log.error("Cannot parse file: {}", filePath1, e);
            } finally {
                finish.countDown();
            }
        }).start();
        log.info("Call the second thread to parse file {}", filePath1);
        new Thread(() -> {
            try {
                fileProcessingService.process(filePath2);
            } catch (IOException e) {
                log.error("Cannot parse file: {}", filePath2, e);
            } finally {
                finish.countDown();
            }
        }).start();
        log.info("Waiting threads to finish work");
        finish.await();
    }
}
