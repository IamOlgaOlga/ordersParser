package com.cardpay.parser.service;

import com.cardpay.parser.service.parser.CsvParser;
import com.cardpay.parser.service.parser.JsonParser;
import com.cardpay.parser.service.parser.Parser;
import com.cardpay.parser.worker.LineWorker;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

/**
 * Worker which handle the file: checks a file extension and call appropriate parser for this file.
 */
@Service
@AllArgsConstructor
public class ParsingService {
    /**
     * Constant value for .csv file extension.
     */
    private static final String CSV_EXTENSION = "CSV";
    /**
     * Constant value for .json file extension.
     */
    private static final String JSON_EXTENSION = "JSON";

    private CsvParser csvParser;

    private JsonParser jsonParser;

    /**
     * Provide an ability to process file.
     */
    public void processFile(File file) {
        String fileExtension = FilenameUtils.getExtension(file.getName());
        switch (fileExtension.toUpperCase()){
            case CSV_EXTENSION:
                csvParser.parseFile(file);
                break;
            case JSON_EXTENSION:
                jsonParser.parseFile(file);
                break;
            default:
                //TODO add log
        }
    }
}
