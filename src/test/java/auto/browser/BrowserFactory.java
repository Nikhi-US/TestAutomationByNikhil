package auto.browser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import auto.managers.FileReaderManager;
import auto.stepDefinitions.LoginStepDefinition;

public class BrowserFactory {
    String executionPlatform = FileReaderManager.getInstance().getConfigReader().getExecutionPlatform();
    public static String gridFeature = FileReaderManager.getInstance().getConfigReader().getGrid();

    public void setGeneralDriverBehaviors(WebDriver webDriver) {
        if (executionPlatform.equalsIgnoreCase("Android-web")) {
            Long pageTimeout = Long.parseLong(FileReaderManager.getInstance().getConfigReader().getPageTimeOut());
            Long implicitWait = Long.parseLong(FileReaderManager.getInstance().getConfigReader().getImplicitWait());
            webDriver.manage().timeouts().pageLoadTimeout(pageTimeout, TimeUnit.SECONDS);
            webDriver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            webDriver.manage().deleteAllCookies();
        } else {
            webDriver.manage().window().maximize();
            Long pageTimeout = Long.parseLong(FileReaderManager.getInstance().getConfigReader().getPageTimeOut());
            Long implicitWait = Long.parseLong(FileReaderManager.getInstance().getConfigReader().getImplicitWait());
            webDriver.manage().timeouts().pageLoadTimeout(pageTimeout, TimeUnit.SECONDS);
            webDriver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            webDriver.manage().deleteAllCookies();

        }
    }

    public ChromeOptions createChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);

        prefs.put("download.prompt_for_download", false);
        prefs.put("browserVersion", "latest");
        prefs.put("safeBrowsing.enabled", false);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
        prefs.put("browser.set_download_behavior", "allow");
        prefs.put("plugins.always_open_pdf_externally", true);
        prefs.put("download.default_directory",System.getProperty("user.dir")+"\\DownloadedPDFS\\");
//      prefs.put("plugins.always_open_pdf_externally", true);
        chromeOptions.setExperimentalOption("prefs", prefs);

        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-extensions");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--ignore-certificate-errors");
        chromeOptions.addArguments("--disable-logging");
        chromeOptions.addArguments("--remote-allow-origins=*");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--incognito");

        if(LoginStepDefinition.incognitoModeReader.equalsIgnoreCase("ON") && gridFeature.equalsIgnoreCase("No"))
        {
            chromeOptions.addArguments("--incognito");
        }
        //just to get network log
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        chromeOptions.setCapability("goog:loggingPrefs", logPrefs);

        return chromeOptions;
    }

    public InternetExplorerOptions createIEOptions() {
        InternetExplorerOptions options = new InternetExplorerOptions();

        options.setCapability("ignoreZoomSetting", true);

        return options;
    }

    public FirefoxOptions createFireFoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        options.setCapability("ignoreZoomSetting", true);

        return options;
    }

    public ChromeOptions headlessChrome() {

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/Utilities/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1400,800");
        options.addArguments("headless");

        return options;
    }

    public static ChromeOptions getHeadlessChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.prompt_for_download", false);

        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1280,800");

        return options;
    }
}
