package com.example.demo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        File outputFile = new File("D:/Hari/demo_projects/destination/" + fileName);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(decodedBytes);
            System.out.println("File copied to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return MessageBuilder.withPayload(outputFile).build();
    }

    // Method to save as CSV format
    private void saveAsCsv(byte[] contentBytes, String fileName) throws IOException {
        // Write contentBytes to a CSV file
        File csvFile = new File("D:/Hari/demo_projects/destination/" + fileName + ".csv");
        try (FileOutputStream outputStream = new FileOutputStream(csvFile)) {
            outputStream.write(contentBytes);
            System.out.println("CSV file saved: " + csvFile.getAbsolutePath());
        }
    }

    // Method to save as XLSX format
    private void saveAsXlsx(byte[] contentBytes, String fileName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Convert contentBytes to a string (assuming it's text data)
            String content = new String(contentBytes, StandardCharsets.UTF_8);
            String[] rows = content.split("\n");

            int rowNum = 0;
            for (String rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                String[] cells = rowData.split(","); // Assuming data is comma-separated
                int colNum = 0;
                for (String cellData : cells) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }

            // Write workbook to file
            File xlsxFile = new File("D:/Hari/demo_projects/destination/" + fileName + ".xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(xlsxFile)) {
                workbook.write(outputStream);
                System.out.println("XLSX file saved: " + xlsxFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to save as XLS format
    private void saveAsXls(byte[] contentBytes, String fileName) throws IOException {
        // Write contentBytes to an XLS file
        File xlsFile = new File("D:/Hari/demo_projects/destination/" + fileName + ".xls");
        try (FileOutputStream outputStream = new FileOutputStream(xlsFile)) {
            outputStream.write(contentBytes);
            System.out.println("XLS file saved: " + xlsFile.getAbsolutePath());
        }
    }

    // Method to save as text format
    private void saveAsText(byte[] contentBytes, String fileName) throws IOException {
        // Write contentBytes to a text file
        File textFile = new File("D:/Hari/demo_projects/destination/" + fileName + ".txt");
        try (FileOutputStream outputStream = new FileOutputStream(textFile)) {
            outputStream.write(contentBytes);
            System.out.println("Text file saved: " + textFile.getAbsolutePath());
        }
    }
}
