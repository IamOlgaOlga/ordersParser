package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

/**
 * Implementation of {@link LineByLineParser} is responsible for CSV files parsing
 */
@Component
@Log4j2
public class CsvParser extends LineByLineParser {
    /**
     * Unexpected number of columns error message.
     */
    private static final String UNEXPECTED_NUMBER_OF_COLUMNS_ERROR_MSG = "Unexpected number of columns.";
    /**
     * Incorrect format of row values error message.
     */
    private static final String INCORRECT_ROW_FORMAT_ERROR_MSG = "Incorrect format of row values.";
    /**
     * Reference to property about CSV file separator.
     */
    @Value("${parser.csv.separator}")
    private String csvSeparator;
    /**
     * Reference to property about colunm number in CSV files
     */
    @Value("${parser.csv.csvColumns}")
    private int csvColumns;

    /**
     * Constructor for {@link JsonParser}. Calls a super class constructor.
     */
    @Autowired
    public CsvParser(ExecutorService threadPoolExecutor, ObjectMapper objectMapper) {
        super(threadPoolExecutor, objectMapper);
    }

    /**
     * Parse a line from CSV file and build {@link OutputLine} as a result line.
     *
     * @param inputLineMetadata {@link InputLineMetadata} with metadata about input line
     * @return parsed {@link OutputLine} as {@link String}
     */
    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        OutputLine outputLine;
        String[] parsedCsvLine = inputLineMetadata.getLineContent().split(csvSeparator);
        if (parsedCsvLine.length != csvColumns) {
            log.error("Error while parsing a line {} from file {}: {}",
                    inputLineMetadata.getLineNumber(),
                    inputLineMetadata.getFileName(),
                    UNEXPECTED_NUMBER_OF_COLUMNS_ERROR_MSG);
            outputLine = OutputLine.fail(UNEXPECTED_NUMBER_OF_COLUMNS_ERROR_MSG)
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
            return getValueAsString(outputLine);
        }
        try {
            outputLine = OutputLine.success()
                    .setId(Long.parseLong(parsedCsvLine[0]))
                    .setAmount(new BigDecimal(parsedCsvLine[1]))
                    .setCurrency(parsedCsvLine[2])
                    .setComment(parsedCsvLine[3])
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        } catch (NumberFormatException e) {
            log.error("Error while parsing a line {} from file {}: {}",
                    inputLineMetadata.getLineNumber(),
                    inputLineMetadata.getFileName(),
                    INCORRECT_ROW_FORMAT_ERROR_MSG);
            outputLine = OutputLine.fail(INCORRECT_ROW_FORMAT_ERROR_MSG)
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
