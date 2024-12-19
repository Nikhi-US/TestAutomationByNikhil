package auto.utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import auto.browser.Browser;
import auto.config.ConfigFileReader;
import auto.managers.ExcelReaderManager;
import auto.managers.FileReaderManager;
import auto.managers.ExcelReaderManager.ExcelReaderBuilder;

public class ExcelFileReading extends Browser
{
    synchronized public static HashMap<String, String> getRowData(String filename,String sheetName,int rowNumber)
    {
        excelReaderBuilder = new ExcelReaderBuilder();
        excelReaderBuilder.setFileLocation(System.getProperty("user.dir") + "/src/test/resources/auto.testData/" + filename);
        excelReaderBuilder.setSheet(sheetName);

        xcel = new ExcelReaderManager(excelReaderBuilder);
        try {
            list = xcel.getSheetData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        xldata = xcel.getSheetDataAsMap();
        row= xldata.get(rowNumber);
        return row;
    }

}
