package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * {@link JsonParser} is responsible for JSON files parsing
 */
@Component
public class JsonParser extends LineByLineParser {

    public JsonParser(ExecutorService threadPoolExecutor) {
        super(threadPoolExecutor);
    }

    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        return null;
    }
}
