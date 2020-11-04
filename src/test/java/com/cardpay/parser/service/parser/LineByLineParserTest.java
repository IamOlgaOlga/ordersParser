package com.cardpay.parser.service.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Unit tests for {@link LineByLineParser}. It's abstract class with a few common methods for line-by-line parsers.
 * This way, all tests are written using {@link JsonParser} implementation. In case {@link JsonParser} is removed,
 * please use another implementation.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LineByLineParserTest {
    /**
     * File with 4 lines.
     */
    private static final String TEST_FILE_4_LINES_PATH = "src/test/resources/testFile_4Lines.json";
    /**
     * Empty file.
     */
    private static final String TEST_EMPTY_FILE_PATH = "src/test/resources/testFile_empty.json";
    /**
     * Any (not CSV or JSON) file with 1 line.
     */
    private static final String TEST_ANY_FILE_1_LINE = "src/test/resources/testFile_any_1_line.txt";
    /**
     * Incorrect file path
     */
    private static final String TEST_INCORRECT_FILE_PATH = "src/test/resources/incorrectFilePath.txt";
    /**
     * Mock for {@link ExecutorService}
     */
    @Mock
    private ExecutorService executorService;
    /**
     * Abstract class to test
     */
    private LineByLineParser lineByLineParser;

    /**
     * Initialization of tested abstract class.
     */
    @Before
    public void setup() {
        lineByLineParser = new JsonParser(executorService, new ObjectMapper());
    }

    /**
     * Testing that for file with 4 lines inside {@link ExecutorService} is called 4 times
     * to send each line to next processing.
     *
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testParseFile_valid4LineFile_expectedSubmitTask4Times() throws IOException {
        File testFile = new File(TEST_FILE_4_LINES_PATH);
        lineByLineParser.parseFile(testFile);
        Mockito.verify(executorService, Mockito.times(4)).submit(Mockito.any(Runnable.class));
    }

    /**
     * Testing that for empty file {@link ExecutorService} is called 0 times.
     *
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testParseFile_emptyFile_expectedSubmitTask0Times() throws IOException {
        File testFile = new File(TEST_EMPTY_FILE_PATH);
        lineByLineParser.parseFile(testFile);
        Mockito.verify(executorService, Mockito.times(0)).submit(Mockito.any(Runnable.class));
    }

    /**
     * Testing that for any file with 1 line {@link ExecutorService} is called 1 time.
     *
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testParseFile_AnyFile1Line_expectedSubmitTask1Time() throws IOException {
        File testFile = new File(TEST_ANY_FILE_1_LINE);
        lineByLineParser.parseFile(testFile);
        Mockito.verify(executorService, Mockito.times(1)).submit(Mockito.any(Runnable.class));
    }

    /**
     * Testing that for incorrect file path {@link ExecutorService} is called 0 times
     * and {@link IOException} is thrown.
     */
    @Test
    public void testParseFile_incorrectFilePath_expectedIOException() {
        File testFile = new File(TEST_INCORRECT_FILE_PATH);
        try {
            lineByLineParser.parseFile(testFile);
        } catch (IOException e) {
            Mockito.verify(executorService, Mockito.times(0)).submit(Mockito.any(Runnable.class));
            return;
        }
        Assert.fail();
    }
}
