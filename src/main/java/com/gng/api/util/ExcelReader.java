package com.gng.api.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelReader {
    private final Workbook workbook;

    // Constructor to load the Excel file
    public ExcelReader(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(filePath));
        workbook = new XSSFWorkbook(fileInputStream);
    }

    public List<Map<String, String>> getSheetData(String sheetName) {
        List<Map<String, String>> sheetData = new ArrayList<>();
        Sheet sheet = workbook.getSheet(sheetName);

        if (sheet == null) {
            throw new RuntimeException("Sheet " + sheetName + " not found");
        }

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row is missing in the sheet " + sheetName);
        }

        int columnCount = headerRow.getLastCellNum();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            // Skip null or empty rows
            if (row == null || row.getCell(0) == null || row.getCell(0).toString().trim().isEmpty()) {
                continue;
            }

            Map<String, String> rowData = new HashMap<>();
            for (int j = 0; j < columnCount; j++) {
                Cell headerCell = headerRow.getCell(j);
                Cell cell = row.getCell(j);
                String header = headerCell.getStringCellValue();
                String value = (cell != null) ? cell.toString().trim() : "";
                rowData.put(header, value);
            }
            sheetData.add(rowData);
        }
        return sheetData;
    }

    public String getCellValue(String sheetName, int row, int col) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) return null;
        Row dataRow = sheet.getRow(row);
        if (dataRow == null) return null;
        Cell cell = dataRow.getCell(col);
        return (cell != null) ? cell.toString() : null;
    }

    public static <E extends Enum<E>, T> T loadRowFromExcelToCustomerData(
            String excelPath,
            String sheetName,
            E testLabel,
            Class<T> clazz) {

        try {
            ExcelReader reader = new ExcelReader(excelPath);
            List<Map<String, String>> sheetData = reader.getSheetData(sheetName);

            // Find the first row where the "testConditions" column matches the enum name
            Map<String, String> rawRow = sheetData.stream()
                    .filter(row -> testLabel.name().equalsIgnoreCase(row.get("testConditions")))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "No matching testConditions found for: " + testLabel.name()));

            // Convert Map<String, String> to Map<String, Object>, handling comma-separated lists
            Map<String, Object> rowData = new HashMap<>(rawRow);
            String testConditions = rawRow.get("testConditions");
            if (testConditions != null) {
                List<String> conditions = Arrays.stream(testConditions.split(",\\s*"))
                        .collect(Collectors.toList());
                rowData.put("testConditions", conditions);
            }

            // Normalise empty strings to null, if desired
            CommonUtil.normalizeBlankStringsToNull(rowData);

            // Map to POJO using Jackson, ignoring unknown properties
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return mapper.convertValue(rowData, clazz);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from Excel", e);
        }
    }

    public void close() throws IOException {
        workbook.close();
    }
}