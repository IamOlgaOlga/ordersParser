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
 * {@link JsonParser} is responsible for JSON files parsing
 */
@Component
@Log4j2
public class JsonParser extends LineByLineParser {

    public JsonParser(ExecutorService threadPoolExecutor, ObjectMapper objectMapper) {
        super(threadPoolExecutor, objectMapper);
    }

    /**
     *
     * TODO write about SneakyThrows
     *
     * @param inputLineMetadata
     * @return
     */
    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        log.info("Start line parsing {} form file {}: {}",
                inputLineMetadata.getLineNumber(),
                inputLineMetadata.getFileName(),
                inputLineMetadata.getLineContent());
        OutputLine outputLine;
        try {
            InputLine inputJsonLine = objectMapper.readValue(inputLineMetadata.getLineContent(), InputLine.class);
            outputLine = OutputLine.success()
                    .setId(inputJsonLine.getOrderId())
                    .setAmount(inputJsonLine.getAmount())
                    .setCurrency(inputJsonLine.getCurrency())
                    .setComment(inputJsonLine.getComment())
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        } catch (JsonProcessingException e) {
            log.error("Error while parsing a line {} from file {}: {}",
                    inputLineMetadata.getLineNumber(),
                    inputLineMetadata.getFileName(),
                    e.getMessage().split("\n")[0]);
            outputLine = OutputLine.fail(e.getMessage().split("\n")[0])
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
