package com.example.demo;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class DataProcessor {

    private static final String FILE_NAME = "file_name";
    private static final String FILE_CONTENT = "file_content";

    public Message<File> processData(Message<Map<String, Object>> message) throws IOException {
      
        Map<String, Object> payload = message.getPayload();
        MessageHeaders headers = message.getHeaders();

        
        byte[] fileContent = (byte[]) payload.get(FILE_CONTENT);
        String fileName = String.valueOf(payload.get(FILE_NAME));

       
        File outputFile = new File("D:/Hari/demo_projects/destination/" + fileName);

        try (OutputStream os = new FileOutputStream(outputFile)) {
            
            os.write(fileContent);
            System.out.println("File copied to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
           
            e.printStackTrace();
           
        }

       
        return MessageBuilder.withPayload(outputFile).build();
    }
}
