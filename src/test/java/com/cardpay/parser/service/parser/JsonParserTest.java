package com.cardpay.parser.service.parser;

import com.cardpay.parser.domain.InputLineMetadata;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Executors;

/**
 * Unit tests for {@link JsonParser}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class JsonParserTest {
    /**
     * Constant for input file name
     */
    private static final String TEST_FILE_NAME = "testFile";
    /**
     * Constant for line number
     */
    private static final int TEST_LINE_NUMBER = 1;
    /**
     * Constant for valid line
     */
    private static final String TEST_VALID_LINE_CONTENT = "{\"orderId\": 1, \"amount\": 100.5, " +
            "\"currency\": \"RUB\", \"comment\": \"some comments\"}";
    /**
     * Constant for valid line with string numbers
     */
    private static final String TEST_VALID_STRING_NUMBER_LINE_CONTENT = "{\"orderId\": \"1\", \"amount\": \"100.5\", " +
            "\"currency\": \"RUB\", \"comment\": \"some comments\"}";
    /**
     * Constant for line with invalid order ID value
     */
    private static final String TEST_INVALID_ORDER_ID_LINE_CONTENT = "{\"orderId\": \"test\", \"amount\": 100.5, " +
            "\"currency\": \"RUB\", \"comment\": \"some comments\"}";
    /**
     * Constant for line with invalid amount value
     */
    private static final String TEST_INVALID_AMOUNT_LINE_CONTENT = "{\"orderId\": 1, \"amount\": \"test\", " +
            "\"currency\": \"RUB\", \"comment\": \"some comments\"}";
    /**
     * Constant for line with invalid columns number
     */
    private static final String TEST_INVALID_COLUMNS_LINE_CONTENT = "{\"orderId\": 1, \"amount\": 100.5, " +
            "\"currency\": \"RUB\", \"comment\": \"some comments\", \"extra property\": \"extra value\"}";
    /**
     * Constant for empty line
     */
    private static final String TEST_DOUBLED_LINE =
            "{\"orderId\": 1, \"amount\": 100.5, \"currency\": \"RUB\", \"comment\": \"some comments\"}" +
                    "{\"orderId\": 2, \"amount\": 100.5, \"currency\": \"RUB\", \"comment\": \"some comments\"}";
    /**
     * Constant for empty line
     */
    private static final String TEST_EMPTY_COLUMN_LINE = "{:,:,:,:}";
    /**
     * Constant for empty line
     */
    private static final String TEST_EMPTY_VALUES_LINE = "{\"\":\"\",\"\":\"\",\"\":\"\",\":\"}";
    /**
     * Constant for empty line
     */
    private static final String TEST_EMPTY_LINE = "";
    /**
     * Reference to tested {@link JsonParser}
     */
    private JsonParser jsonParser;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonParser = new JsonParser(Executors.newFixedThreadPool(1), mapper);
    }
    /**
     * Testing that valid JSON line is parsed successfully.
     */
    @Test
    public void testParseLine_validInput_expectedSuccessResult() {
        String expected = "{\"id\":1,\"amount\":100.5,\"currency\":\"RUB\",\"comment\":\"some comments\"," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"OK\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_VALID_LINE_CONTENT);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that valid JSON line with numbers in String format is parsed successfully.
     */
    @Test
    public void testParseLine_validInputWithStringNumbers_expectedSuccessResult() {
        String expected = "{\"id\":1,\"amount\":100.5,\"currency\":\"RUB\",\"comment\":\"some comments\"," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"OK\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_VALID_STRING_NUMBER_LINE_CONTENT);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that JSON line with invalid amount value is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidAmountValue_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"Cannot deserialize value of type `java.math.BigDecimal` from String \\\"test\\\": " +
                "not a valid representation\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_AMOUNT_LINE_CONTENT);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that JSON line with invalid order ID value is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidOrderIdValue_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"Cannot deserialize value of type `java.lang.Long` from String \\\"test\\\": " +
                "not a valid Long value\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_ORDER_ID_LINE_CONTENT);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that JSON line with extra properties is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidInputColumns_expectedSuccessResult() {
        String expected = "{\"id\":1,\"amount\":100.5,\"currency\":\"RUB\",\"comment\":\"some comments\"," +
                "\"filename\":\"testFile\",\"line\":1,\"result\":\"OK\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_INVALID_COLUMNS_LINE_CONTENT);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that JSON line with a few orders is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidInputDoubledLine_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"There are more than one order in line.\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_DOUBLED_LINE);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that corrupted JSON line is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidInputEmptyCorruptedLine_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"Unexpected character (':' (code 58)): was expecting double-quote to start field name\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_EMPTY_COLUMN_LINE);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that JSON line with empty values is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidInputEmptyValuesLine_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"Unexpected character ('}' (code 125)): was expecting a colon to separate field name and value\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_EMPTY_VALUES_LINE);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Testing that empty line is parsed with error result
     * and null values for below properties:
     *  - id
     *  - amount
     *  - currency
     *  - comment
     */
    @Test
    public void testParseLine_invalidInputEmptyLine_expectedFailResult() {
        String expected = "{\"id\":null,\"amount\":null,\"currency\":null,\"comment\":null," +
                "\"filename\":\"testFile\",\"line\":1," +
                "\"result\":\"No content to map due to end-of-input\"}";
        InputLineMetadata inputLineMetadata = new InputLineMetadata(TEST_FILE_NAME,
                TEST_LINE_NUMBER, TEST_EMPTY_LINE);
        String actual = jsonParser.parseLine(inputLineMetadata);
        Assert.assertEquals(expected, actual);
    }
}
