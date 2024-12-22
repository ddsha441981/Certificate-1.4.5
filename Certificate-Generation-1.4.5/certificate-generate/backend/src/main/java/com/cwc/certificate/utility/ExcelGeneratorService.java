package com.cwc.certificate.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cwc.certificate.config.ConstantValue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */
@Service
public class ExcelGeneratorService {
    public byte[] exportDataToExcel(List<Map<String, String>> data, String gateway) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(gateway + ConstantValue.DEFAULT_EXCEL_REPORT_NAME);

            // Create headers
            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            for (String header : data.get(0).keySet()) {
                Cell cell = headerRow.createCell(colNum++);
                cell.setCellValue(header);
                CellStyle style = workbook.createCellStyle();
                style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(style);

                // Calculate the maximum width of data in this column
                int maxLength = header.length();
                for (Map<String, String> rowData : data) {
                    String value = rowData.get(header);
                    if (value != null && value.length() > maxLength) {
                        maxLength = value.length();
                    }
                }
                // Set the column width based on the maximum length of data
                sheet.setColumnWidth(colNum - 1, (maxLength + 1) * 256);
            }

            // Create data rows
            int rowNum = 1;
            for (Map<String, String> rowData : data) {
                Row row = sheet.createRow(rowNum++);
                colNum = 0;
                for (String header : rowData.keySet()) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(rowData.get(header));
                }
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}
