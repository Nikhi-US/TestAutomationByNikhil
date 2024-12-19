package auto.utilities;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExcelWriting {

    public static void excelWriting(String sheetName, String columnName, String columnValue) throws IOException {
        String filePath = System.getProperty("user.dir")
                + "/src/test/resources/auto.testdata/Customer_Details.xlsx";
        XSSFWorkbook workbook;
        XSSFSheet sheet;
        File file = new File(filePath);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    sheet = workbook.createSheet(sheetName);
                }
            } catch (IOException e) {
                throw new IOException("Error reading the Excel file.", e);
            }
        } else {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet(sheetName);
        }
        XSSFRow headerRow = sheet.getRow(0);
        if (headerRow == null) {
            headerRow = sheet.createRow(0);
        }
        int columnIndex = -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            XSSFCell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }
        if (columnIndex == -1) {
            if(columnIndex == headerRow.getLastCellNum()){
                columnIndex = headerRow.getLastCellNum()+1;
            }else {
                columnIndex = headerRow.getLastCellNum();
            }
            XSSFCell headerCell = headerRow.createCell(columnIndex);
            headerCell.setCellValue(columnName);
        }
        XSSFRow dataRow = sheet.getRow(1);
        if (dataRow == null) {
            dataRow = sheet.createRow(1);
        }
        XSSFCell dataCell = dataRow.createCell(columnIndex);
        dataCell.setCellValue(columnValue);
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new IOException("Error writing to the Excel file.", e);
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
