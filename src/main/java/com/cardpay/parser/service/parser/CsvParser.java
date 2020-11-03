package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

/**
 * {@link CsvParser} is responsible for CSV files parsing
 */
@Component
@Log4j2
public class CsvParser extends LineByLineParser{

    @Value("${parser.csv.separator}")
    private String csvSeparator;

    @Value("${parser.csv.csvColumns}")
    private int csvColumns;

    public CsvParser(ExecutorService threadPoolExecutor, ObjectMapper objectMapper) {
        super(threadPoolExecutor, objectMapper);
    }

    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        log.info("Start line parsing {} form file {}: {}",
                inputLineMetadata.getLineNumber(),
                inputLineMetadata.getFileName(),
                inputLineMetadata.getLineContent());
        OutputLine outputLine;
        String[] parsedCsvLine = inputLineMetadata.getLineContent().split(csvSeparator);
        if (parsedCsvLine.length != csvColumns) {
            log.error("Error while parsing a line {} from file {}: {}",
                    inputLineMetadata.getLineNumber(),
                    inputLineMetadata.getFileName(),
                    "Unexpected number of columns.");
            outputLine = OutputLine.fail("Unexpected number of columns.")
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
                    "Incorrect format of row values.");
            outputLine = OutputLine.fail("Incorrect format of row values.")
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
