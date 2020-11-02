package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * {@link CsvParser} is responsible for CSV files parsing
 */
@Component
public class CsvParser extends LineByLineParser{

    public CsvParser(ExecutorService threadPoolExecutor) {
        super(threadPoolExecutor);
    }

    @Override
    protected String parseLine(InputLineMetadata inputLineMetadata) {
        return null;
    }
}
