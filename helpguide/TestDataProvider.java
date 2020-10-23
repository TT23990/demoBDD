package com.moodys.pageobjects.sharedservices;

import com.moodys.pageobjects.lightning.BaseObjectsLPO;
import com.moodys.selenium.base.configuration.ConfigUtil;
import com.moodys.selenium.base.lib.Excel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static com.moodys.pageobjects.lightning.BaseObjectsLPO.SANDBOX;
import static com.moodys.pageobjects.lightning.BaseObjectsLPO.outputData;

public class TestDataProvider {
    //TODO: Optimize data providers
    String ActualData = "";
    LinkedHashMap<String, String> previousTestData;

    @DataProvider(name = "profiles")
    public Object[][] getTestProfilesBasedOnTestCycle() {
//        ConfigUtil.config.get("env.sfdc.test.data.location")
        return fetchExcelDataBasedOnTestCycleAndProfiles(ConfigUtil.config.get("env.sfdc.test.data.location"),
                "Profiles", "ALL");
    }

    @DataProvider(name = "test cases")
    public Object[][] getTestCasesBasedOnTestCycle() {
        return fetchExcelDataBasedOnTestCycleAndProfiles(ConfigUtil.config.get("env.sfdc.test.data.location"),
                "Test Cases", BaseObjectsLPO.profiles);
    }

    private Object[][] fetchExcelDataBasedOnTestCycleAndProfiles(String filePath, String sheetName, String profile) {

        List<LinkedHashMap<String, String>> ExcelData_ArrayList = Excel.excelReadHashMap(filePath, sheetName);
        List<LinkedHashMap<String, String>> actualTestData_ArrayList = new ArrayList<>();
        for (LinkedHashMap<String, String> each_HashMap : ExcelData_ArrayList) {
            if (getTestBasedOnTestCycle(BaseObjectsLPO.TEST_CYCLE, each_HashMap) &&
                    filterTestsBasedOnTestProfiles(profile, each_HashMap))
                actualTestData_ArrayList.add(each_HashMap);
        }

        Object[][] actualTestData = new Object[actualTestData_ArrayList.size()][1];
        for (int rows = 0; rows < actualTestData_ArrayList.size(); rows++) {
            actualTestData[rows][0] = actualTestData_ArrayList.get(rows);
        }
        return actualTestData;
    }

    private boolean getTestBasedOnTestCycle(String testCycle, LinkedHashMap<String, String> each_HashMap) {
        boolean testFound = false;
        switch (testCycle.toLowerCase()) {
            case "dryrun":
                if (each_HashMap.get("DryRun").equalsIgnoreCase("YES"))
                    testFound = true;
                break;
            case "regression":
                testFound = true;
                break;
            case "smoke":
                if (each_HashMap.get("Priority").equalsIgnoreCase("High") ||
                        each_HashMap.get("Priority").equalsIgnoreCase("Very High"))
                    testFound = true;
                break;
        }
        return testFound;
    }

    private boolean filterTestsBasedOnTestProfiles(String profile, LinkedHashMap<String, String> each_HashMap) {
        if (profile.equalsIgnoreCase("ALL")) return true;
        boolean testFound = false;
        for (String item : each_HashMap.get("Users").split("\n")) {
            if (item.equalsIgnoreCase(profile)) {
                testFound = true;
                break;
            }
        }
        return testFound;
    }

    public static LinkedHashMap<String, String> getSwitchProfileBasedOnSandbox(String profile) {

        List<LinkedHashMap<String, String>> ExcelData_ArrayList = Excel.excelReadHashMap(
                ConfigUtil.config.get("env.sfdc.test.data.location"), "Profiles");

        LinkedHashMap<String, String> switchUserProfile = new LinkedHashMap<>();

        for (LinkedHashMap<String, String> each_HashMap : ExcelData_ArrayList) {

            if (each_HashMap.get("Sandbox").equalsIgnoreCase(SANDBOX) &&
                    each_HashMap.get("Profiles").equalsIgnoreCase(profile)) {
                switchUserProfile.putAll(each_HashMap);
                break;
            }
        }

        return switchUserProfile;
    }

    public LinkedHashMap<String, String> getSplitDataInHashMap(String testSteps) {
        String[] featureArray = testSteps.split("\n");
        LinkedHashMap<String, String> featureObjects = new LinkedHashMap<>();
        for (String each_Feature : featureArray) {
            String[] data = each_Feature.split("=");
            featureObjects.put(data[0], data[1]);
        }
        return featureObjects;
    }

    public ArrayList<LinkedHashMap<String, String>> getSplitDataInArrayList(String testSteps) {
        String[] featureArray = testSteps.split("\n");
        ArrayList<LinkedHashMap<String, String>> featureObjects = new ArrayList<>();
        LinkedHashMap<String, String> feature;
        for (String each_Feature : featureArray) {
            feature = new LinkedHashMap<>();
            String[] data = each_Feature.split("=");
            feature.put("feature", data[0]);
            feature.put("data", data[1]);
            featureObjects.add(feature);
        }
        return featureObjects;
    }


    private LinkedHashMap<String, String> testDataFromDependentTest(LinkedHashMap<String, String> testCaseData) {

        List<LinkedHashMap<String, String>> ExcelData_ArrayList = Excel.excelReadHashMap(
                ConfigUtil.config.get("env.sfdc.test.data.location"), "Test Cases");

        LinkedHashMap<String, String> previousTestDataSet = new LinkedHashMap<>();

        for (String dependentTest : testCaseData.get("dependsOnTest").split("\n")) {
            for (LinkedHashMap<String, String> each_HashMap : ExcelData_ArrayList) {

                if (each_HashMap.get("Test Cases").equalsIgnoreCase(dependentTest)) {
                    LinkedHashMap<String, String> splitData = getSplitDataInHashMap(each_HashMap.get("Actual Result"));
                    splitData.values().removeIf(Objects::isNull);
                    previousTestDataSet.putAll(splitData);
                    break;
                }
            }
        }
        return previousTestDataSet;
    }

    public ArrayList<LinkedHashMap<String, String>> getActionDataInArrayList(String featureSheetName, String dataID, LinkedHashMap<String, String> testCaseData) {
        List<LinkedHashMap<String, String>> ExcelData_ArrayList = Excel.excelReadHashMap(
                ConfigUtil.config.get("env.sfdc.test.data.location"), featureSheetName);
        ArrayList<LinkedHashMap<String, String>> actualFeatureData_ArrayList = new ArrayList<>();

        boolean dependsOnTest = testCaseData.get("dependsOnTest") != null;
        if (dependsOnTest)
            previousTestData = testDataFromDependentTest(testCaseData);

        for (LinkedHashMap<String, String> each_FeatureData : ExcelData_ArrayList) {

            for (String dataCombination : each_FeatureData.get(featureSheetName).split("\n")) {
                if (dataCombination.equalsIgnoreCase(dataID)) {

                    if (dependsOnTest)
                        each_FeatureData.entrySet().forEach(entry -> {
                            if (previousTestData.containsKey(entry.getKey()))
                                entry.setValue(previousTestData.get(entry.getKey()));
                        });

                    each_FeatureData.values().removeIf(Objects::isNull);
                    actualFeatureData_ArrayList.add(each_FeatureData);
                    break;
                }
            }
        }
        return actualFeatureData_ArrayList;
    }

    public boolean setTestResultDataInExcel(LinkedHashMap<String, String> testCaseData) {
        try {
            setDataInExcel("Test Cases", testCaseData.get("Test Cases"),
                    "Actual Result", setSplitDataInString(testCaseData.get("Test Steps")));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String setSplitDataInString(String TestSteps) {
        try {
            getSplitDataInHashMap(TestSteps).entrySet().forEach(entry -> {
                String data = entry.getKey().equals("Home") ? "Application" :
                        entry.getKey().equals("Cases") ? "Case Number" :
                                entry.getKey().equals("Proposals") ? "Proposal ID" :
                                        entry.getKey().equals("Products") ? "Products" :
                                                entry.getKey().equals("Agreements") ? "Agreement Name" :
                                                        entry.getKey().equals("Accounts") ? "Account Name" :
                                                                entry.getKey().equals("Opportunities") ? "Opportunity Name" : entry.getKey();
                if (!ActualData.isEmpty()) ActualData = ActualData.concat("\n");
                ActualData = ActualData.concat(data + "=" + outputData.get(entry.getKey()).get(data));
            });
            return ActualData;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    private boolean setDataInExcel(String colSearch, String rowData, String colData, String writeData) {
        try {
            File file = new File(ConfigUtil.config.get("env.sfdc.test.data.location"));
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheet("Test Cases");

            int rownr = findRow(sheet, rowData, findColumn(sheet.getRow(0), colSearch));
            int colnr = findColumn(sheet.getRow(0), colData);
            sheet.getRow(rownr).getCell(colnr).setCellValue(writeData);
//            formatCellColor(workbook,sheet.getRow(rownr).getCell(colnr));
            workbook.write(new FileOutputStream(file));
            workbook.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int findRow(XSSFSheet sheet, String cellContent, int columnIndex) {
        for (Row row : sheet) {
            if (row.getCell(columnIndex).getCellType() == Cell.CELL_TYPE_STRING) {
                if (row.getCell(columnIndex).getRichStringCellValue().getString().trim().equals(cellContent)) {
                    return row.getRowNum();
                }
            }
        }
        return 0;
    }

    private static int findColumn(XSSFRow headerRow, String cellContent) {
        for (Cell cell : headerRow) {
            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                if (cell.getRichStringCellValue().getString().trim().equals(cellContent)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return 0;
    }

    private boolean formatCellColor(XSSFWorkbook wb, Cell cell) {
        CellStyle style = wb.createCellStyle();
        // Setting Background color
        style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
//        style.setFillPattern(FillPatternType.BIG_SPOTS);
        cell.setCellStyle(style);
        return true;
    }
}
 