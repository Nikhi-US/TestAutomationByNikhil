package auto.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import auto.config.ConfigFileReader;


import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.http.Method;

public class ExcelReaderManager {

    public static enum ExcelDataType {
        API, GUI;
    }

    /**
     * enum for excel row number
     *
     * @author GTIDKE
     *
     */
    public static enum ExcelRowInstance {
        FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5), SIXTH(6), SEVENTH(7), EIGHT(8), NINE(9), TEN(10), ELEVENTH(
                11), TWELTH(12), THIRTEENTH(13), FOURTEENTH(14);

        private int instance;

        private ExcelRowInstance(int instance) {
            this.instance = instance;
        }
    }

    private String fileName;
    private String sheetName;
    private int sheetIndex;
    private XSSFWorkbook book;
    private static List<HashMap<String, String>> apiTestDataMapsAsList;
    private static List<HashMap<String, String>> guiTestDataMapsAsList;
    private static Properties globalConfProp = ConfigFileReader.getGlobalConfig();

    public ExcelReaderManager(ExcelReaderBuilder excelReaderBuilder) {
        this.fileName = excelReaderBuilder.fileName;
        this.sheetIndex = excelReaderBuilder.sheetIndex;
        this.sheetName = excelReaderBuilder.sheetName;
    }

    public static class ExcelReaderBuilder{

        private String fileName;
        private String sheetName;
        private int sheetIndex;

        public ExcelReaderBuilder setFileLocation(String location) {
            this.fileName = location;
            return this;
        }

        public ExcelReaderBuilder setSheet(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public ExcelReaderBuilder setSheet(int index) {
            this.sheetIndex = index;
            return this;
        }

        public ExcelReaderManager build() {
            return new ExcelReaderManager(this);
        }

    }

    private XSSFWorkbook getWorkBook(String filePath) throws InvalidFormatException, IOException {
        return new XSSFWorkbook(new File(filePath));
    }

    private XSSFSheet getWorkBookSheet(String fileName, String sheetName) throws InvalidFormatException, IOException {
        this.book = getWorkBook(fileName);
        return this.book.getSheet(sheetName);
    }

    private XSSFSheet getWorkBookSheet(String fileName, int sheetIndex) throws InvalidFormatException, IOException {
        this.book = getWorkBook(fileName);
        return this.book.getSheetAt(sheetIndex);
    }

    public List<List<String>> getSheetData() throws IOException {
        XSSFSheet sheet;
        List<List<String>> outerList = new LinkedList<>();

        try {
            sheet = getWorkBookSheet(fileName, sheetName);
            outerList = getSheetData(sheet);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            this.book.close();
        }
        return outerList;
    }

    public List<List<String>> getSheetDataAt() throws InvalidFormatException, IOException {

        XSSFSheet sheet;
        List<List<String>> outerList = new LinkedList<>();

        try {
            sheet = getWorkBookSheet(fileName, sheetIndex);
            outerList = getSheetData(sheet);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            this.book.close();
        }
        return outerList;
    }

    private List<List<String>> getSheetData(XSSFSheet sheet) {
        List<List<String>> outerList = new LinkedList<>();
        prepareOutterList(sheet, outerList);
        return Collections.unmodifiableList(outerList);
    }

    private void prepareOutterList(XSSFSheet sheet, List<List<String>> outerList) {
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            List<String> innerList = new LinkedList<>();
            XSSFRow xssfRow = sheet.getRow(i);

            for (int j = xssfRow.getFirstCellNum(); j < xssfRow.getLastCellNum(); j++) {
                prepareInnerList(innerList, xssfRow, j);
            }
            outerList.add(Collections.unmodifiableList(innerList));
        }
    }

    public List<HashMap<String, String>> getSheetDataAsMap() {
        List<HashMap<String, String>> excelData = new ArrayList<>();
        XSSFSheet sheet;
        try {

            sheet = getWorkBookSheet(fileName, sheetName);
            Row HeaderRow = sheet.getRow(0);
            for (int r = 1; r < sheet.getPhysicalNumberOfRows(); r++) {
                Row CurrentRow = sheet.getRow(r);
                // each row of data is stored in new hashmap
                HashMap<String, String> currentRowMap = new HashMap<String, String>();

                for (int c = 0; c < HeaderRow.getLastCellNum(); c++) {
                    Cell currentCell = CurrentRow.getCell(c);
                    if (currentCell != null)
                        currentCell.setCellType(CellType.STRING);
                    if (currentCell != null && currentCell.getCellType() == CellType.NUMERIC) {
                        currentRowMap.put(HeaderRow.getCell(c).getStringCellValue(),
                                currentCell != null ? currentCell.getStringCellValue() : "");
                    } else {
                        currentRowMap.put(HeaderRow.getCell(c).getStringCellValue(),
                                currentCell != null ? currentCell.getStringCellValue() : "");
                    }

                }
                excelData.add(currentRowMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return excelData;
    }

    private void prepareInnerList(List<String> innerList, XSSFRow xssfRow, int j) {
        switch (xssfRow.getCell(j).getCellType()) {

            case BLANK:
                innerList.add("");
                break;

            case STRING:
                innerList.add(xssfRow.getCell(j).getStringCellValue());
                break;

            case NUMERIC:
                innerList.add(xssfRow.getCell(j).getNumericCellValue() + "");
                break;

            case BOOLEAN:
                innerList.add(xssfRow.getCell(j).getBooleanCellValue() + "");
                break;

            default:
                throw new IllegalArgumentException("Cannot read the column : " + j);
        }
    }

    /**
     * based on config file values ,returns the excel row data as list
     *
     * @param dataType
     * @return
     */
    private static List<HashMap<String, String>> getExcelAPIReaderManager(ExcelDataType dataType) {

        if (dataType == ExcelDataType.API) {

            if (apiTestDataMapsAsList == null) {
                String dataSheet = FileReaderManager.getInstance().getConfigReader().getDataSheet();
                ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
                excelReaderBuilder.setFileLocation(System.getProperty("user.dir") + "/src/test/resources/" + dataSheet);
                Properties globalProp = ConfigFileReader.getGlobalConfig();
                String env=System.getProperty("Env");
                if(!(env!=null && !env.isEmpty()))
                {
                    env=globalProp.getProperty("Env");
                }

                String sapEnv=System.getProperty("SAP_ENV");
                if(!(sapEnv!=null && !sapEnv.isEmpty()))
                {
                    sapEnv=globalConfProp.getProperty("SAP_ENV").trim();
                }


                if (env.contains("PERF")) {
                    excelReaderBuilder.setSheet("APITestData-PERF-" + sapEnv);
                }
                else if (env.contains("PROD"))  {
                    excelReaderBuilder.setSheet("APITestData-PROD-" +sapEnv);
                }else {
                    excelReaderBuilder.setSheet("APITestData-QA2-" + sapEnv);
                }
                ExcelReaderManager excelAPIReaderManager = new ExcelReaderManager(excelReaderBuilder);
                apiTestDataMapsAsList = excelAPIReaderManager.getSheetDataAsMap();
            }
            return apiTestDataMapsAsList;

        } else {
            if (guiTestDataMapsAsList == null) {
                String dataSheet = FileReaderManager.getInstance().getConfigReader().getDataSheet();
                ExcelReaderBuilder excelReaderBuilder = new ExcelReaderBuilder();
                excelReaderBuilder.setFileLocation(System.getProperty("user.dir") + "/src/test/resources/" + dataSheet);
                excelReaderBuilder.setSheet("GUITestData".trim());
                ExcelReaderManager excelAPIReaderManager = new ExcelReaderManager(excelReaderBuilder);
                guiTestDataMapsAsList = excelAPIReaderManager.getSheetDataAsMap();
            }
            return guiTestDataMapsAsList;
        }

    }

    /**
     * returns first excel data row for API based on matching test case id
     *
     * @param testcaseId
     * @param dataType
     * @return
     */
    public static HashMap<String, String> readExcelApiField(String testcaseId, ExcelDataType dataType) {
        HashMap<String, String> row = null;
        List<HashMap<String, String>> testDataMapsAsList = getExcelAPIReaderManager(dataType);
        for (int i = 0; i < testDataMapsAsList.size(); i++) {
            if (testcaseId != null && testcaseId.equalsIgnoreCase(testDataMapsAsList.get(i).get("TC_ID"))) {
                System.out.println(testDataMapsAsList.get(i).get("TargetURL"));
                System.out.println(testDataMapsAsList.get(i).get("HTTPMethod"));
                System.out.println(testDataMapsAsList.get(i).get("RequestHeadersJson"));
                System.out.println(testDataMapsAsList.get(i).get("QueryParamsJson"));
                System.out.println(testDataMapsAsList.get(i).get("ExpectedParamNameValue"));
                row = testDataMapsAsList.get(i);
                break;
            }

        }
        return row;
    }

//    public static HashMap<String, String> readExcelApiRow(String testcaseId, ExcelDataType dataType,
//                                                          ExcelRowInstance instanceNumber) {
//        HashMap<String, String> row = null;
//        System.out.println();
//        List<HashMap<String, String>> testDataMapsAsList = getExcelAPIReaderManager(dataType);
//        int rowCounter = 0;
//        for (int i = 0; i < testDataMapsAsList.size(); i++) {
//            int excelInstanceNumber = Integer.parseInt(testDataMapsAsList.get(i).get("Instance"));
//            if (testcaseId != null && testcaseId.trim().equalsIgnoreCase(testDataMapsAsList.get(i).get("TC_ID").trim())
//                    && excelInstanceNumber == instanceNumber.instance) {
//                // System.out.println(testDataMapsAsList.get(i).get("TargetURL"));
//                // System.out.println(testDataMapsAsList.get(i).get("HTTPMethod"));
//                // System.out.println(testDataMapsAsList.get(i).get("RequestHeadersJson"));
//                // System.out.println(testDataMapsAsList.get(i).get("QueryParamsJson"));
//                // System.out.println(testDataMapsAsList.get(i).get("ExpectedParamNameValue"));
//                row = testDataMapsAsList.get(i);
//                break;
//
//            }
//
//        }
//        if (row == null) {
//            throw new FrameworkException(
//                    "Framework Exception- Unable to find scenario \"" + testcaseId + "\" in Excel");
//        }
//
//        return row;
//    }

    /**
     * returns query parameters map of given API data row
     *
     * @param row
     * @return
     */
    public static Map<String, String> getQueryParamsJson(Map<String, String> row) {

        if (row.get("QueryParamsJson") != null && row.get("QueryParamsJson").equalsIgnoreCase("NA")) {
            return null;
        }
        return JsonManager.convertJsonStrToMap(row.get("QueryParamsJson"));
    }

    /**
     * returns request body map of given API data row
     *
     * @param row
     * @return
     */
    public static Map<String, String> getRequestBodyJson(Map<String, String> row) {

        if (row.get("RequestBodyJson") != null && row.get("RequestBodyJson").equalsIgnoreCase("NA")) {
            return null;
        }
        return JsonManager.convertJsonStrToMap(row.get("RequestBodyJson"));
    }

    /**
     * returns request base URI of given API data row
     *
     * @param row
     * @return
     */
    public static String getEndPoint(Map<String, String> row) {
        return row.get("BaseURI") + row.get("EndPoint");
    }

    /**
     * returns request HTTP method name of given API data row
     *
     * @param row
     * @return
     */
//    public static Method getHttpMethod(Map<String, String> row) {
//
//        return RestAssuredComponents.getHttpMethod(row.get("HTTPMethod"));
//    }

    /**
     * returns response expected status of given API data row
     *
     * @param row
     * @return
     */
    public static int getStatusCode(Map<String, String> row) {
        return Integer.parseInt(row.get("StatusCode"));
    }

    /**
     * returns request body string of given API data row
     *
     * @param row
     * @return
     */
    public static String getRequestBody(Map<String, String> row) {

        if (row.get("RequestBodyJson") != null && row.get("RequestBodyJson").equalsIgnoreCase("NA"))
            return null;
        return row.get("RequestBodyJson");
    }

    /**
     * returns request headers string of given API data row
     *
     * @param row
     * @return
     */

    public static Headers getHeaders(Map<String, String> row) {

        List<Header> headersList = new ArrayList<>();

        if (row.get("RequestHeadersJson") == null
                || (row.get("RequestHeadersJson") != null && row.get("RequestHeadersJson").equalsIgnoreCase("NA"))) {
            Headers headers = new Headers(headersList);
            return headers;
        }
        Map<String, String> headersMap = JsonManager.convertJsonStrToMap(row.get("RequestHeadersJson"));

        for (Entry<String, String> entry : headersMap.entrySet()) {
            Header header = new Header(entry.getKey(), entry.getValue());
            headersList.add(header);
        }
        Headers headers = new Headers(headersList);
        return headers;
    }

    /**
     * replace given row's common String with provided value
     *
     * @param row
     * @return
     */

    public static Map<String, String> replaceExcelFieldValue(Map<String, String> row, String coloumnName,
                                                             String valueTobeReplaced, String valueTobePlaced) {
        String headersStr = row.get(coloumnName);
        headersStr.replace(valueTobeReplaced, valueTobePlaced);
        row.put(coloumnName, headersStr);
        return row;
    }

    public static String replaceExcelQueryParamsStr(Map<String, String> row, String valueTobeReplaced,
                                                    String valueTobePlaced) {
        String queryParamsStr = row.get("QueryParamsJson");
        queryParamsStr = queryParamsStr.replace(valueTobeReplaced, valueTobePlaced);
        return queryParamsStr;
    }

    /**
     * replace given row's header generic string with provided value as excel row's
     * column values are made generic to be used accross the test cases
     *
     * @param row
     * @return
     */
    public static Headers replaceExcelHeadersWithValue(Map<String, String> row, String valueTobeReplaced,
                                                       String valueTobePlaced) {
        String headersStr = row.get("RequestHeadersJson");
        headersStr = headersStr.replace(valueTobeReplaced, valueTobePlaced);
        row.put("RequestHeadersJson", headersStr);
        return getHeaders(row);
    }

    /**
     * replace given row's body generic string with provided value as excel row's
     * column values are made generic to be used accross the test cases
     *
     * @param row
     * @return
     */
    public static String replaceExcelBodyWithValue(Map<String, String> row, String valueTobeReplaced,
                                                   String valueTobePlaced) {

        String bodyStr = row.get("RequestBodyJson");
        bodyStr = bodyStr.replace(valueTobeReplaced, valueTobePlaced);
        row.put("RequestBodyJson", bodyStr);
        return bodyStr;
    }

    public static String getBodyContenType(Map<String, String> row) {

        return row.get("RequestBodyType");
    }

//    public static void main(String[] args) {
//        SharePointUtil.getMasterDataFileContent("masterexcel.xlsx");
//        ExcelReaderBuilder builder = new ExcelReaderBuilder().
//                setFileLocation("masterexcel.xlsx").setSheet("E2E_TestCaseAssignment_Data");
//        ExcelReaderManager erm = builder.build();
//    }
}
