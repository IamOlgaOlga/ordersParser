package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
public abstract class LineByLineParser implements Parser{

    protected ExecutorService threadPoolExecutor;

    @Override
    public void parseFile(File file) {
        try(BufferedReader bufferedReader = Files.newBufferedReader(file.toPath())){
            String currentLine;
            int lineNumber = 1;
            while ((currentLine = bufferedReader.readLine()) != null) {
                InputLineMetadata inputLineMetadata = new InputLineMetadata(file.getName(), lineNumber, currentLine);
                threadPoolExecutor.submit(() -> System.out.println(parseLine(inputLineMetadata)));
                lineNumber++;
            }
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        } finally {
            threadPoolExecutor.shutdown();
        }
    }

    protected abstract String parseLine(InputLineMetadata inputLineMetadata);
}
