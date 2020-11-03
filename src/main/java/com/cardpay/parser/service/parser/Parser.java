package com.cardpay.parser.service.parser;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * {@link Parser} is responsible for file parsing.
 */
@Service
@FunctionalInterface
public interface Parser {

    void parseFile(File file) throws IOException;
}
