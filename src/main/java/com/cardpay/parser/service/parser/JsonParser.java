package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLine;
import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * {@link JsonParser} is responsible for JSON files parsing
 */
@Component
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
            outputLine = OutputLine.fail(e.getMessage().split("\n")[0])
                    .setFilename(inputLineMetadata.getFileName())
                    .setLine(inputLineMetadata.getLineNumber())
                    .build();
        }
        return getValueAsString(outputLine);
    }
}
