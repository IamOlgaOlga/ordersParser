package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.Executors;

/**
 * Unit tests for {@link CsvParser}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CsvParserTest {
    /**
     * Constant for input file name
     */
    private static final String TEST_FILE_NAME = "testFile";
    /**
     * Constant for line number
     */
    private static final Long TEST_LINE_NUMBER = 1L;
    /**
     * Constant for valid line
     */
    private static final String TEST_VALID_LINE_CONTENT = "1,100.5,RUB,some comments";
    /**
     * Constant for line with invalid amount value
     */
    private static final String TEST_INVALID_AMOUNT_LINE_CONTENT = "1,test,RUB,some comments";
    /**
     * Constant for line with invalid order ID value
     */
    private static final String TEST_INVALID_ORDER_ID_LINE_CONTENT = "test,100.5,RUB,some comments";
    /**
     * Constant for line with invalid columns number
     */
    private static final String TEST_INVALID_COLUMNS_LINE_CONTENT = "1,test,RUB,some comments, some extra column";
    /**
     * Constant for line with empty columns
     */
    private static final String TEST_EMPTY_COLUMN_LINE = ",,,";
    /**
     * Constant for empty line
     */
    private static final String TEST_EMPTY_LINE = "";
    /**
     * Reference to tested {@link CsvParser}
     */
    private CsvParser csvParser;

    /**
     * Set test @Value properties to {@link CsvParser} object before test
     */
    @Before
    public void before() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        csvParser = new CsvParser(Executors.newFixedThreadPool(1), mapper);
        ReflectionTestUtils.setField(csvParser, "csvSeparator", ",");
        ReflectionTestUtils.setField(csvParser, "csvColumns", 4);
    }

    /**
     * Testing that valid CSV line is parsed successfully.
     */
    @Test
    public void testParseLine_validInput_expectedSuccessResult() {
        String expected = "{\"id\":1,\"amount\":100.5,\"currency\":\"RUB\",\"comment\":\"some comments\"," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"OK\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_VALID_LINE_CONTENT);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that CSV line with invalid amount value is parsed with error result and null values fo below properties:
     * - id
     * - amount
     * - currency
     * - comment
     */
    @Test
    public void testParseLine_invalidAmountValue_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"Incorrect format of row values.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_AMOUNT_LINE_CONTENT);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that CSV line with invalid order ID value is parsed with error result and null values fo below properties:
     * - id
     * - amount
     * - currency
     * - comment
     */
    @Test
    public void testParseLine_invalidOrderIdValue_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"Incorrect format of row values.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_ORDER_ID_LINE_CONTENT);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that CSV line with extra columns is parsed with error result and null values fo below properties:
     * - id
     * - amount
     * - currency
     * - comment
     */
    @Test
    public void testParseLine_invalidInputColumns_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"Unexpected number of columns.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_COLUMNS_LINE_CONTENT);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that CSV line with empty columns is parsed with error result and null values fo below properties:
     * - id
     * - amount
     * - currency
     * - comment
     */
    @Test
    public void testParseLine_invalidInputEmptyColumns_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"Unexpected number of columns.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_EMPTY_COLUMN_LINE);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that empty line is parsed with error result and null values fo below properties:
     * - id
     * - amount
     * - currency
     * - comment
     */
    @Test
    public void testParseLine_invalidInputEmptyLine_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"Unexpected number of columns.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_EMPTY_LINE);
        String actual = csvParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }
}