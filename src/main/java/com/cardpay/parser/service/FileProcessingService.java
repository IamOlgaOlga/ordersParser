package com.cardpay.parser.service;

import java.io.IOException;

/**
 * Service which process input file.
 */
@FunctionalInterface
public interface FileProcessingService {
    /**
     * Provide an ability to process file
     * @param filePath path to file
     * @throws IOException if any IO exception occurs
     */
    void process(String filePath) throws IOException;
}
