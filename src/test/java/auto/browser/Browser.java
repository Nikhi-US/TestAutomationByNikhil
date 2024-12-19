package auto.browser;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.safari.SafariOptions;
import auto.managers.ExcelReaderManager;
import auto.managers.FileReaderManager;
import auto.managers.ExcelReaderManager.ExcelReaderBuilder;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Browser {

    public static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
    public String appURL;
    public static String browserName = FileReaderManager.getInstance().getConfigReader().getBrowser();
    public static String gridFeature = FileReaderManager.getInstance().getConfigReader().getGrid();
    public static String gridHost = FileReaderManager.getInstance().getConfigReader().getGridHost();
    public static String executionPlatform = FileReaderManager.getInstance().getConfigReader().getExecutionPlatform();
    public static String captureScreenshot = FileReaderManager.getInstance().getConfigReader().getScreenshotCheck();
    static float devicePixelRatio;
    public static List<HashMap<String, String>> xldata;
    public static ExcelReaderManager xcel;
    public static List<List<String>> list;
    public static HashMap<String, String> row = null;
    public static ExcelReaderBuilder excelReaderBuilder = null;
    public static ChromeOptions options;
    public static boolean incognitoMode = true;

    /**
     * creates driver object
     *
     * @param url
     */
    public void createLocalOrRemoteDriver(String url) {
        this.appURL = url;
        createLocalOrRemoteDriver();

    }
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE
    }



    public static WebDriver createWebDriver(BrowserType browserType) {
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                driver = new ChromeDriver(chromeOptions);
                break;

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;

            // Add more browsers here as needed

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserType);
        }

        return driver;
    }

    /**
     * creates Driver object based on grid options and browsers provided in Config
     * file
     */
    public void createLocalOrRemoteDriver() {

        InternetExplorerOptions ieOptions;
        BrowserFactory factory = new BrowserFactory();
        options = factory.createChromeOptions();
        ieOptions = factory.createIEOptions();

        FirefoxOptions firefoxOptions = factory.createFireFoxOptions();
        ChromeOptions dsChrome = factory.headlessChrome();

        SafariOptions safariOptions = new SafariOptions();

//     setConfigCred();
        String url = "http://" + gridHost + "/wd/hub";
        System.out.println("URL:" + url);
        if (gridFeature.equalsIgnoreCase("Yes")) {
            WebDriverManager.chromedriver().setup();
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920x1080");
            try {
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(url), options);
                remoteWebDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
                remoteWebDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                remoteWebDriver.manage().timeouts().scriptTimeout(Duration.ofSeconds(60));

                driver.set(remoteWebDriver);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (browserName.equalsIgnoreCase("HeadlessChromeCloud")) {
                System.setProperty("webdriver.chrome.driver",
                        "/usr/local/bin/chromedriver-linux64/chromedriver");
                driver.set(new ChromeDriver(BrowserFactory.getHeadlessChromeOptions()));
            }
            else if (browserName.equalsIgnoreCase("HeadlessChrome")) {
                WebDriverManager.chromedriver().setup();
                options.addArguments("--incognito");
                driver.set(new ChromeDriver(BrowserFactory.getHeadlessChromeOptions()));
            }
            else {
                WebDriverManager.chromedriver().setup();
                if(incognitoMode){
                    options.addArguments("--incognito");
                }
                driver.set(new ChromeDriver(
                        ChromeDriverService.createDefaultService(),
                        options,
                        ClientConfig.defaultConfig()));
            }
        }
        factory.setGeneralDriverBehaviors(getDriver());

        /*
         * set device pixel ratio for ashot full page screenshot, to handle different
         * screen resolutions
         */
        setDevicePixelRatio();

        selenideSetupWebdriver();
    }

    /**
     * This method gets a reference for existing webdriver initialized by selenium.
     * Selenide will use this one instead of initializing new instance.
     * Use selenide as usual ($ or $$ to access web elements).
     */
    private void selenideSetupWebdriver() {
        WebDriverRunner.setWebDriver(Browser.getDriver());
    }

    /**
     * get WebDriver instance
     *
     * @return
     */
    public static WebDriver getDriver() {
        // Get driver from ThreadLocalMap
        return driver.get();
    }

    private void setDevicePixelRatio() {
        Object obj = ((JavascriptExecutor) driver.get()).executeScript("return window.devicePixelRatio;");
        if (obj instanceof Double) {
            Double value = (Double) obj;
            devicePixelRatio = value.floatValue();

        }
        if (obj instanceof Float) {
            Float value = (Float) obj;
            devicePixelRatio = value.floatValue();

        }

        if (obj instanceof Long) {
            Long value = (Long) obj;
            devicePixelRatio = value.floatValue();

        }
    }

    public static byte[] getScreenShotAsByteArray(BufferedImage bufferedImage) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] image = byteArrayOutputStream.toByteArray();
            return image;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
