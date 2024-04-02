package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import ch.qos.logback.classic.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class DataProcessorTest {

    private DataProcessor dataProcessor;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dataProcessor = new DataProcessor(logger);
    }

    @Test
    void testProcessData() throws Exception {
        // Mock input data
        String fileName = "example.xlsx";
        byte[] fileContent = Base64.getEncoder().encode("Sample file content".getBytes());
        Map<String, Object> payload = new HashMap<>();
        payload.put(DataProcessor.getFileName(), fileName);
        payload.put(DataProcessor.getFileContent(), fileContent);

        Message<Map<String, Object>> message = mock(Message.class);
        when(message.getPayload()).thenReturn(payload);

        // Call the method
        File outputFile = dataProcessor.processData(message).getPayload();

        // Verify file creation and logger
        verify(logger).info("File copied to: {}", outputFile.getAbsolutePath());
        verifyNoMoreInteractions(logger);

        // Clean up (delete the created file)
        outputFile.delete();
    }

}