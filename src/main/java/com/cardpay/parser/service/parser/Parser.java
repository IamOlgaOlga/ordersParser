package com.cardpay.parser.service.parser;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * {@link Parser} is responsible for file parsing.
 */
@Service
public interface Parser {

    void parseFile(File file);
}
