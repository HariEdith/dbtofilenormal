package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;

import ch.qos.logback.classic.Logger;

import java.io.File;
import java.io.IOException;
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
        
        String fileName = "example.xlsx";
        byte[] fileContent = Base64.getEncoder().encode("Sample file content".getBytes());
        Map<String, Object> payload = new HashMap<>();
        payload.put(DataProcessor.getFileName(), fileName);
        payload.put(DataProcessor.getFileContent(), fileContent);

        Message<Map<String, Object>> message = mock(Message.class);
        when(message.getPayload()).thenReturn(payload);

        File outputFile = dataProcessor.processData(message).getPayload();
        verify(logger).info("File copied to: {}", outputFile.getAbsolutePath());
        verifyNoMoreInteractions(logger);
   
        outputFile.delete();
    }

    @Test
    void testSaveAsCsv() throws IOException {
        byte[] contentBytes = "Test CSV content".getBytes();
        String fileName = "testFile";
      
        dataProcessor.saveAsCsv(contentBytes, fileName);

        String expectedFilePath = "D:\\Hari\\demo_projects\\destination\\testFile.csv";
        verify(logger).info("CSV file saved: {}", expectedFilePath);
        verifyNoMoreInteractions(logger);

       
        new File(DataProcessor.getDestinationDirectory() + fileName + ".csv").delete();
    }


    @Test
    void testSaveAsXlsx() throws IOException {
        byte[] contentBytes = "Test XLSX content".getBytes();
        String fileName = "testFile";
       
        dataProcessor.saveAsXlsx(contentBytes, fileName);
       
        String expectedFilePath = "D:\\Hari\\demo_projects\\destination\\testFile.xlsx"; 
        verify(logger).info("XLSX file saved: {}", expectedFilePath);
        verifyNoMoreInteractions(logger);
       
        new File(DataProcessor.getDestinationDirectory() + fileName + ".xlsx").delete();
    }

    @Test
    void testSaveAsXls() throws IOException {
        byte[] contentBytes = "Test XLS content".getBytes();
        String fileName = "testFile";
    
        dataProcessor.saveAsXls(contentBytes, fileName);
     
        String expectedFilePath = "D:\\Hari\\demo_projects\\destination\\testFile.xls";
        verify(logger).info("XLS file saved: {}", expectedFilePath);
        verifyNoMoreInteractions(logger);

       
        new File(DataProcessor.getDestinationDirectory() + fileName + ".xls").delete();
    }

    @Test
    void testSaveAsText() throws IOException {
        byte[] contentBytes = "Test text content".getBytes();
        String fileName = "testFile";
       
        dataProcessor.saveAsText(contentBytes, fileName);
      
        String expectedFilePath = "D:\\Hari\\demo_projects\\destination\\testFile.txt"; 
        verify(logger).info("Text file saved: {}", expectedFilePath);
        verifyNoMoreInteractions(logger);

       
        new File(DataProcessor.getDestinationDirectory() + fileName + ".txt").delete();
    }
}