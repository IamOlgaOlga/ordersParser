package com.cardpay.parser.service;

import java.io.IOException;

@FunctionalInterface
public interface FileProcessingService {
    void process(String filePath) throws IOException;
}
