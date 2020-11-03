package com.cardpay.parser.service.parser;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for file parsing.
 */
@Service
@FunctionalInterface
public interface Parser {
    /**
     * Parse file
     * @param file file to parse
     * @throws IOException if any IO exception occurs
     */
    void parseFile(File file) throws IOException;
}
