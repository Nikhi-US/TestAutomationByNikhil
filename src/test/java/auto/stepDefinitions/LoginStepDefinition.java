package auto.stepDefinitions;

import auto.browser.ReusableLibrary;
import auto.config.ConfigFileReader;
import auto.managers.ExcelReaderManager;
import auto.managers.FileReaderManager;
import auto.pages.LoginPage;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import auto.utilities.ExcelFileReading;

public class LoginStepDefinition extends ReusableLibrary {
    ExcelReaderManager.ExcelReaderBuilder excelReaderBuilder = new ExcelReaderManager.ExcelReaderBuilder();
    String dataSheet = FileReaderManager.getInstance().getConfigReader().getDataSheet();
    public static Properties configProp = ConfigFileReader.getGlobalConfig();
    static Properties urlProp = ConfigFileReader.getURLConfig();
    public String appName;
    public String appURL = "";
    Properties globalProp = ConfigFileReader.getGlobalConfig();
    LoginPage loginPage = new LoginPage();
    public static String gridFeature = FileReaderManager.getInstance().getConfigReader().getGrid();
    Scenario scenario = scenarioThreadLocal.get();
    public static Map mp = null;
    public static String incognitoModeReader = "OFF";

    private class URLModifier {
        public String generateEnvURL(String url) {
            if (url.split("_").length == 2 && url.split("_")[0].equals("ENV")) {
                String environmentName = configProp.getProperty("Env");
                String sysEnv = System.getProperty("Env");
                if (sysEnv != null && !sysEnv.isEmpty() && sysEnv.length() != 0) {
                    System.out.println("Info: Config Env value is Changed from " + environmentName + " to " + sysEnv);
                    environmentName = sysEnv;
                }
                appName = url.split("_")[1];
                url = urlProp.getProperty(environmentName + "_" + appName);
            } else {
//                throw new FrameworkException("Application Name URL in feature file is not set properly:" + url);
            }
            return url;
        }
    }

    @Then("^User load the data from (.+) in (.+) from (.+)$")
    public void user_loads_the_data_from_testdata(String filename, String sheetname, int rowNumber) {
        row = ExcelFileReading.getRowData(filename, sheetname, rowNumber);
    }

    @When("User initilize the driver and incognitomode with URL {string}")
    public void initilizeDriverWithincognito(String url) {
        incognitoModeReader = "ON";
        if (url.contains("ENV_")) {
            url = new URLModifier().generateEnvURL(url);
            appURL = url;
            createLocalOrRemoteDriver(url);
            launchURL(url);
        } else {
            createLocalOrRemoteDriver(url);
            launchURL(url);
        }
        incognitoModeReader = "OFF";
    }
    @Then("User verify the Logo")
    public void User_verify_the_Logo (){
        loginPage.verifyLogo();
    }
    @Then("User Creates an Account")
    public void User_Creates_an_Account (){
        String Firstname = row.get("Firstname").trim();
        String Lastname = row.get("Lastname").trim();
        String EmailID = row.get("EmailID").trim();
        String Password = row.get("Password").trim();
        loginPage.createAccountLuma(Firstname,Lastname,EmailID,Password);
    }
    @Then("User Logins to Luma Account")
    public void User_Logins_to_Luma_Account (){
        String EmailID = row.get("EmailID").trim();
        String Password = row.get("Password").trim();
        loginPage.signInLuma(EmailID,Password);
    }
}

