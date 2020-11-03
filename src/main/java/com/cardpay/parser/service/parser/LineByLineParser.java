package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
public abstract class LineByLineParser implements Parser{

    protected final ExecutorService threadPoolExecutor;

    protected final ObjectMapper objectMapper;

    @Override
    public void parseFile(File file) throws IOException {
        try(BufferedReader bufferedReader = Files.newBufferedReader(file.toPath())){
            String currentLine;
            long lineNumber = 1;
            while ((currentLine = bufferedReader.readLine()) != null) {
                InputLineMetadata inputLineMetadata = new InputLineMetadata(file.getName(), lineNumber, currentLine);
                threadPoolExecutor.submit(() -> System.out.println(parseLine(inputLineMetadata)));
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
