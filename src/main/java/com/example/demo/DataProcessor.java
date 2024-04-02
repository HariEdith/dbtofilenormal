package com.example.demo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class DataProcessor {

    private static final String FILE_NAME = "file_name";
    private static final String FILE_CONTENT = "file_content";
    private static final String DESTINATION_DIRECTORY = "D:/Hari/demo_projects/destination/";
    private static Logger logger = LoggerFactory.getLogger(DataProcessor.class);

    public static String getFileName() {
		return FILE_NAME;
	}

	public static String getFileContent() {
		return FILE_CONTENT;
	}

	public static String getDestinationDirectory() {
		return DESTINATION_DIRECTORY;
	}

	public static Logger getLogger() {
		return logger;
	}
	
	public DataProcessor(Logger logger) {
        this.logger = logger;
    }

	

    public Message<File> processData(Message<Map<String, Object>> message)  {
        Map<String, Object> payload = message.getPayload();
        byte[] base64Content = (byte[]) payload.get(FILE_CONTENT);
        String fileName = String.valueOf(payload.get(FILE_NAME));

        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

        File outputFile = new File(DESTINATION_DIRECTORY + fileName);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(decodedBytes);
            logger.info("File copied to: {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Error copying file to: {}", outputFile.getAbsolutePath(), e);
        }

        return MessageBuilder.withPayload(outputFile).build();
    }

  
    public void saveAsCsv(byte[] contentBytes, String fileName) throws IOException {
        File csvFile = new File(DESTINATION_DIRECTORY + fileName + ".csv");
        try (FileOutputStream outputStream = new FileOutputStream(csvFile)) {
            outputStream.write(contentBytes);
            logger.info("CSV file saved: {}", csvFile.getAbsolutePath());
        }
    }

    
    public void saveAsXlsx(byte[] contentBytes, String fileName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

         
            String content = new String(contentBytes, StandardCharsets.UTF_8);
            String[] rows = content.split("\n");

            int rowNum = 0;
            for (String rowData : rows) {
                Row row = sheet.createRow(rowNum++);
                String[] cells = rowData.split(",");
                int colNum = 0;
                for (String cellData : cells) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }

            File xlsxFile = new File(DESTINATION_DIRECTORY + fileName + ".xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(xlsxFile)) {
                workbook.write(outputStream);
                logger.info("XLSX file saved: {}", xlsxFile.getAbsolutePath());
            }
        } catch (Exception e) {
            logger.error("Error saving XLSX file: {}", fileName, e);
        }
    }


    public void saveAsXls(byte[] contentBytes, String fileName) throws IOException {
        File xlsFile = new File(DESTINATION_DIRECTORY + fileName + ".xls");
        try (FileOutputStream outputStream = new FileOutputStream(xlsFile)) {
            outputStream.write(contentBytes);
            logger.info("XLS file saved: {}", xlsFile.getAbsolutePath());
        }
    }

 
    public void saveAsText(byte[] contentBytes, String fileName) throws IOException {
        File textFile = new File(DESTINATION_DIRECTORY + fileName + ".txt");
        try (FileOutputStream outputStream = new FileOutputStream(textFile)) {
            outputStream.write(contentBytes);
            logger.info("Text file saved: {}", textFile.getAbsolutePath());
        }
    }
}
