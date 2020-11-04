package com.cardpay.parser.service;

import com.cardpay.parser.service.parser.JsonParser;
import com.cardpay.parser.service.parser.CsvParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * Unit test for {@link FileProcessingServiceImpl} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FileProcessingServiceImplTest {
    /**
     * Csv valid file path.
     */
    private static final String TEST_CSV_FILE_PATH = "src/test/resources/testFile.csv";
    /**
     * Json valid file path.
     */
    private static final String TEST_JSON_FILE_PATH = "src/test/resources/testFile_4Lines.json";
    /**
     * Incorrect file path
     */
    private static final String TEST_INCORRECT_FILE_PATH = "src/test/resources/incorrectFilePath.txt";
    /**
     * Correct file path, but not supported extension.
     */
    private static final String TEST_UNSUPPORTED_EXTENSION = "src/test/resources/testFile_any_1_line.txt";
    /**
     * Mock for {@link JsonParser}
     */
    @Mock
    private JsonParser jsonParser;
    /**
     * Mock for {@link CsvParser}
     */
    @Mock
    private CsvParser csvParser;
    /**
     * Reference to {@link FileProcessingServiceImpl}
     */
    @InjectMocks
    private FileProcessingServiceImpl fileProcessingService;

    /**
     * Testing that for valid CSV file {@link CsvParser} only is called 1 time.
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testProcess_csvFile_expectedCsvParser() throws IOException {
        fileProcessingService.process(TEST_CSV_FILE_PATH);
        Mockito.verify(csvParser, Mockito.times(1)).parseFile(Mockito.any(File.class));
        Mockito.verify(jsonParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
    }

    /**
     * Testing that for valid JSON file {@link JsonParser} only is called 1 time.
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testProcess_jsonFile_expectedJsonParser() throws IOException {
        fileProcessingService.process(TEST_JSON_FILE_PATH);
        Mockito.verify(csvParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
        Mockito.verify(jsonParser, Mockito.times(1)).parseFile(Mockito.any(File.class));
    }

    /**
     * Testing that for invalid file path {@link IOException} is thrown and no one parser is called.
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testProcess_incorrectFilePath_expectedIOException() throws IOException {
        try {
            fileProcessingService.process(TEST_INCORRECT_FILE_PATH);
        } catch (IOException e) {
            String expectedErrorMessage = "Can't find file with path: " + TEST_INCORRECT_FILE_PATH;
            Assert.assertEquals(expectedErrorMessage, e.getMessage());
            Mockito.verify(csvParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
            Mockito.verify(jsonParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
            return;
        }
        Assert.fail();
    }

    /**
     * Testing that for valid file with unsupported extension {@link IOException} is thrown and no one parser is called.
     * @throws IOException if any IO exception occurs.
     */
    @Test
    public void testProcess_notSupportedExtension_expectedIOException() throws IOException {
        try {
            fileProcessingService.process(TEST_UNSUPPORTED_EXTENSION);
        } catch (IOException e) {
            String expectedErrorMessage = "Not support file extension: TXT";
            Assert.assertEquals(expectedErrorMessage, e.getMessage());
            Mockito.verify(csvParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
            Mockito.verify(jsonParser, Mockito.times(0)).parseFile(Mockito.any(File.class));
            return;
        }
        Assert.fail();
    }
}
