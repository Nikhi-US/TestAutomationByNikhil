package auto.testRunners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import auto.config.ConfigFileReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.runner.JUnitCore;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import auto.browser.Browser;
import auto.managers.FileReaderManager;
import auto.utilities.ExcelWriting;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.json.support.Status;
import net.masterthought.cucumber.presentation.PresentationMode;

@CucumberOptions(features = { "src/test/resources/features/" }, dryRun = false, glue = {
        "auto.stepDefinitions" }, plugin = {
        "json:target/cucumber-reports/CucumberTestReport.json","rerun:target/failed-scenarios-details/failed.txt" }, tags = "@Test2")

public class TestNGRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    public static File file;


    @AfterSuite
    public static void cucumberSandwichReportGeneration() throws InvalidFormatException, IOException {
        String executionPlatform = System.getProperty("Env");
        if (!(executionPlatform != null && !executionPlatform.isEmpty())) {
            executionPlatform = FileReaderManager.getInstance().getConfigReader().getEnv();
        }


        String timeStamp = getSimpleTimeStamp();
        String sandwichReportPath = String
                .format(System.getProperty("user.dir") + "/report/SandwichReports" + "_" + "%s", timeStamp);
        ExcelWriting.excelWriting("ReportPaths", "SandwichReportPath", sandwichReportPath);
        File reportOutputDirectory = new File(sandwichReportPath);
        String firstRunReportPath = reportOutputDirectory.getAbsolutePath() + "/FirstRunReport";
        File firstRunReportDirectory = new File(firstRunReportPath);

// File reportOutputDirectory = null;

        String buildNumber = System.getProperty("buildNumber");
// reportOutputDirectory = new File(
// System.getProperty("user.dir") + "/report/SandwichReports" + System.currentTimeMillis());
        if (buildNumber == null)
            buildNumber = "";

        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/cucumber-reports/CucumberTestReport.json");

        String projectName = "Test Automation By Nikhil U S";
// Configuration configuration = new Configuration(firstRunReportDirectory,
// projectName);
        Configuration configuration = new Configuration(firstRunReportDirectory, projectName);
        configuration.setNotFailingStatuses(Collections.singleton(Status.SKIPPED));
        configuration.setBuildNumber(buildNumber);
        configuration.addClassifications("Platform", System.getProperty("os.name"));
        configuration.addClassifications("Browser", FileReaderManager.getInstance().getConfigReader().getBrowser());
        configuration.addClassifications("Environment", executionPlatform);

// optionally add metadata presented on main page via properties file

        List<String> classificationFiles = new ArrayList<>();

        configuration.addClassificationFiles(classificationFiles);

// optionally specify qualifiers for each of the report json files
        configuration.addPresentationModes(PresentationMode.PARALLEL_TESTING);

        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
// on windows open browser window with report
        if (System.getProperty("os.name").contains("Windows")
                && new ConfigFileReader().getCucumberReportOpenAfter().equals("true")) {
            file = configuration.getReportDirectory();
            Browser.createWebDriver(Browser.BrowserType.CHROME)
                    .get(file.toString() + "/cucumber-html-reports/overview-features.html");
        }
    }

    /**
     * generates timestamp for files or logs
     * @return string in a foramt "dd_MMM_yyyy_HH_mm_ss"
     */
    private static String getSimpleTimeStamp() {
        return new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss").format(new Date());
    }
}
