
package com.example.demo;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class DataProcessor {

    private static final String FILE_NAME = "file_name";
    private static final String FILE_CONTENT = "file_content";

    public Message<File> processData(Message<Map<String, Object>> message) throws IOException {
        Map<String, Object> payload = message.getPayload();
        MessageHeaders headers = message.getHeaders();

        byte[] base64Content = (byte[]) payload.get(FILE_CONTENT);
        String fileName = String.valueOf(payload.get(FILE_NAME));

        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
        String decodedContent = new String(decodedBytes, StandardCharsets.UTF_8);

        File outputFile = new File("D:/Hari/demo_projects/destination/" + fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(decodedContent);
            System.out.println("File copied to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MessageBuilder.withPayload(outputFile).build();
    }
}
