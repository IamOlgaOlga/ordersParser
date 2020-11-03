package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
@Log4j2
public abstract class LineByLineParser implements Parser{

    protected final ExecutorService threadPoolExecutor;

    protected final ObjectMapper objectMapper;

    @Override
    public void parseFile(File file) throws IOException {
        try(BufferedReader bufferedReader = Files.newBufferedReader(file.toPath())){
            String currentLine;
            long lineNumber = 1;
            while ((currentLine = bufferedReader.readLine()) != null) {
                log.debug("Submit line {} from file {} : {}", lineNumber, file.getName(), currentLine);
                InputLineMetadata inputLineMetadata = new InputLineMetadata(file.getName(), lineNumber, currentLine);
                threadPoolExecutor.submit(() -> {
                    String parsedLine = parseLine(inputLineMetadata);
                    System.out.println(parsedLine);
                    log.info("Parsed line result: {}", parsedLine);
                });
                lineNumber++;
            }
        }
    }

    @SneakyThrows
    protected String getValueAsString(OutputLine outputLine) {
        return objectMapper.writeValueAsString(outputLine);
    }

    protected abstract String parseLine(InputLineMetadata inputLineMetadata);
}
