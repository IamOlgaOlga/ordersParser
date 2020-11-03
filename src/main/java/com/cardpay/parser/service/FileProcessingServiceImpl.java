package com.cardpay.parser.service;

import com.cardpay.parser.service.parser.CsvParser;
import com.cardpay.parser.service.parser.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Worker which handle the file: checks a file extension and call appropriate parser for this file.
 */
@Service
@AllArgsConstructor
@Log4j2
public class FileProcessingServiceImpl implements FileProcessingService{
    /**
     * Constant value for .csv file extension.
     */
    private static final String CSV_EXTENSION = "CSV";
    /**
     * Constant value for .json file extension.
     */
    private static final String JSON_EXTENSION = "JSON";

    private final CsvParser csvParser;

    private final JsonParser jsonParser;

    /**
     * Provide an ability to process file.
     */
    @Override
    public void process(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("Can't find file with path: " + filePath);
        }
        String fileExtension = FilenameUtils.getExtension(file.getName());
        switch (fileExtension.toUpperCase()){
            case CSV_EXTENSION:
                log.info("Start CSV file parsing {}", filePath);
                csvParser.parseFile(file);
                break;
            case JSON_EXTENSION:
                log.info("Start JSON file parsing {}", filePath);
                jsonParser.parseFile(file);
                break;
            default:
                throw new IOException("Not support file extension: " + fileExtension);
        }
    }
}
