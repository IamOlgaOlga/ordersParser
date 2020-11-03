package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;

/**
 * {@link CsvParser} is responsible for CSV files parsing
 */
@Component
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
        OutputLine outputLine;
        String[] parsedCsvLine = inputLineMetadata.getLineContent().split(csvSeparator);
        if (parsedCsvLine.length != csvColumns) {
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
            outputLine = OutputLine.fail("Incorrect format of row values.")
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
