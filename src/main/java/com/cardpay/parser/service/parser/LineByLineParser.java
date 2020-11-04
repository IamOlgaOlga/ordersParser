package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.cardpay.parser.domain.OutputLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

/**
 * Abstract class which provides implementation of {@link Parser} for line-by-line parsers
 */
@AllArgsConstructor
@Log4j2
public abstract class LineByLineParser implements Parser {
    /**
     * Reference to {@link ExecutorService} to parse lines concurrency.
     */
    protected final ExecutorService threadPoolExecutor;
    /**
     * Reference to {@link ObjectMapper} to deserialize the result line {@link OutputLine}.
     */
    protected final ObjectMapper objectMapper;

    /**
     * Provide an ability to parse file line-by-line concurrency.
     * Each line is submitted to {@link ExecutorService} and call parseLine() method implementation.
     *
     * @param file file to parse.
     * @throws IOException if any IO exception occurs.
     */
    @Override
    public void parseFile(File file) throws IOException {
        try (BufferedReader bufferedReader = Files.newBufferedReader(file.toPath())) {
            String currentLine;
            long lineNumber = 1;
            while ((currentLine = bufferedReader.readLine()) != null) {
                InputLineMetadata inputLineMetadata = new InputLineMetadata(file.getName(), lineNumber, currentLine);
                threadPoolExecutor.submit(() -> System.out.println(parseLine(inputLineMetadata)));
                lineNumber++;
            }
        }
    }

    /**
     * Returns {@link String} value in JSON format of {@link OutputLine} parsed line.
     * "@SneakyThrows" is here because {@link JsonProcessingException} cannot be thrown here.
     *
     * @param outputLine POJO parsed line.
     * @return {@link String} value in JSON format of {@link OutputLine} parsed line.
     */
    @SneakyThrows
    protected String getValueAsString(OutputLine outputLine) {
        return objectMapper.writeValueAsString(outputLine);
    }

    /**
     * Abstract method which provides an ability to parse a content line from {@link InputLineMetadata}.
     *
     * @param inputLineMetadata {@link InputLineMetadata} with metadata about input line
     * @return parsed output(result) line as {@link String}
     */
    protected abstract String parseLine(InputLineMetadata inputLineMetadata);
}
