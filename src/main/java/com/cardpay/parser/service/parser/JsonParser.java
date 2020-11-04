package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLine;
import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * Implementation of {@link LineByLineParser} is responsible for JSON files parsing.
 */
@Component
@Log4j2
public class JsonParser extends LineByLineParser {
    /**
     * Constant for open curly brace
     */
    private static final String OPEN_CURLY_BRACE = "{";
    /**
     * Multiple orders in one line error message
     */
    private static final String MULTIPLE_ORDERS_IN_ONE_LINE_ERROR_MSG = "There are more than one order in line.";

    /**
     * Constructor for {@link JsonParser}. Calls a super class constructor.
     */
    public JsonParser(ExecutorService threadPoolExecutor, ObjectMapper objectMapper) {
        super(threadPoolExecutor, objectMapper);
    }

    /**
     * Parse a JSON line using {@link ObjectMapper} and build {@link OutputLine} as a result line.
     *
     * @param inputLineMetadata {@link InputLineMetadata} with metadata about input line
     * @return parsed {@link OutputLine} as {@link String}
     */
    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        OutputLine outputLine;
        String lineContent = inputLineMetadata.getLineContent();
        long lineNumber = inputLineMetadata.getLineNumber();
        if (lineContent.indexOf(OPEN_CURLY_BRACE) != lineContent.lastIndexOf(OPEN_CURLY_BRACE)) {
            log.error("{}: {}", MULTIPLE_ORDERS_IN_ONE_LINE_ERROR_MSG, lineNumber);
            outputLine = OutputLine.fail(MULTIPLE_ORDERS_IN_ONE_LINE_ERROR_MSG)
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(lineNumber)
                    .build();
            return getValueAsString(outputLine);
        }
        try {
            InputLine inputJsonLine = objectMapper.readValue(lineContent, InputLine.class);
            outputLine = OutputLine.success()
                    .setId(inputJsonLine.getOrderId())
                    .setAmount(inputJsonLine.getAmount())
                    .setCurrency(inputJsonLine.getCurrency())
                    .setComment(inputJsonLine.getComment())
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(lineNumber)
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Error while parsing a line {} from file {}: {}",
                    lineNumber,
                    inputLineMetadata.getFileName(),
                    e.getMessage().split("\n")[0]);
            outputLine = OutputLine.fail(e.getMessage().split("\n")[0])
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(lineNumber)
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
