package com.example.demo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataProcessorTest {

    private DataProcessor dataProcessor;

    @Mock
    private Message<File> mockMessage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataProcessor = new DataProcessor();
    }

    @Test
    public void testProcessData() throws IOException {
   
        File testFile = new File("testFile.txt");
        MessageHeaders headers = mock(MessageHeaders.class);
        when(mockMessage.getPayload()).thenReturn(testFile);
        when(mockMessage.getHeaders()).thenReturn(headers);
        when(headers.get("filename")).thenReturn("testFileName");
        when(headers.get("fileformat")).thenReturn("txt");

        
        File outputFile = dataProcessor.processData(mockMessage).getPayload();

        verify(mockMessage, times(1)).getPayload();
        verify(mockMessage, times(1)).getHeaders();
        verify(headers, times(1)).get("filename");
        verify(headers, times(1)).get("fileformat");
 
        assert outputFile.exists();
        assert outputFile.getName().startsWith("output_file_");
     
        outputFile.delete();
    }
}
