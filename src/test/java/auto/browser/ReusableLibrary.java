package auto.browser;

import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import auto.config.ConfigFileReader;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ReusableLibrary extends Browser {
    public static ThreadLocal<String> marketinvoked = new ThreadLocal<>();
    public static ThreadLocal<Scenario> scenarioThreadLocal = new ThreadLocal<>();
    private static Properties globalConfig = ConfigFileReader.getGlobalConfig();
    private static int explicitWaitTimeout = Integer.parseInt(globalConfig.getProperty("EXPLICIT_WAIT"));
    public static SoftAssert softAssert = new SoftAssert();
    public static CopyOnWriteArrayList<String> warningLogs = new CopyOnWriteArrayList<String>();
    public static ThreadLocal<String> marketCode = new ThreadLocal<>();
    public static ThreadLocal<String> marketID = new ThreadLocal<>();
    public static ThreadLocal<String> dealerThirdParty = new ThreadLocal<>();
    public static ThreadLocal<String> proposalNumber = new ThreadLocal<>();
    public static final Logger loggerShared = LogManager.getLogger(ReusableLibrary.class);
    Scenario scenario = scenarioThreadLocal.get();

    public static void resetProxy() {
        RestAssured.reset();
    }

    public static WebElement findElementByXpathAndReturnNullIfNotFound(String locator) {
        WebElement element = null;
        try {
            sleep(4000);
            element = driver.get().findElement(By.xpath(locator));
            return element;
        } catch (Exception e) {
        }
        return element;
    }


////    /**
////     * lauches browser with url
////     *
////     * @param url
////     */
    public static void launchURL(String url) {
        driver.get().get(url);
    }

////    /**
////     * sends string to input(textbox) element by clearing existing string in textbox
////     * using id
////     *
////     * @param id
////     * @param value
////     */
    public static void sendKeys(WebElement element, String value) {
        element.sendKeys(value);
    }


//    /**
//     * pauses the execution by given time in miliseconds
//     *
//     * @param time
//     */
    public static void sleep(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

//    /**
//     * pauses the execution by given time in miliseconds
//     *
//     * @param time
//     */
    public static void sleep() {

        try {
            long sleepWait = 0;
            if (sleepWait == 0)
                return;
            Thread.sleep(sleepWait);
        } catch (InterruptedException e) {
        }
    }



    public static void takeScreenshot() {
        browserZoomOut("0.75");
        if (captureScreenshot.equalsIgnoreCase("Yes")) {
            Screenshot screenshot = new AShot().takeScreenshot(Browser.getDriver());
            scenarioThreadLocal.get().attach(getScreenShotAsByteArray(screenshot.getImage()), "image/png", "");
        }
        browserZoomOut("1");
    }


    public static void takeFullScreenshot() {
        // Screenshot screenshot = new
        // AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
        // .takeScreenshot(Browser.getDriver());
        browserZoomOut("0.75");
        if (captureScreenshot.equalsIgnoreCase("Yes")) {
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportRetina(1000, 0, 0, devicePixelRatio))
                    .takeScreenshot(Browser.getDriver());
            scenarioThreadLocal.get().attach(getScreenShotAsByteArray(screenshot.getImage()), "image/png", "");
        }
        browserZoomOut("1");
    }


    public static void clickElement(By by) {
        try {
            waitUntilElementIsLocated(by);
            ((JavascriptExecutor) driver.get()).executeScript("arguments[0].scrollIntoView(true);",
                    driver.get().findElement(by));

            // WebElement click
            try {
                driver.get().findElement(by).click();
            } catch (Exception e) {
                // JavascriptExecutor click
                ((JavascriptExecutor) driver.get()).executeScript("arguments[0].click();",
                        driver.get().findElement(by));
            }

        } catch (Exception e) {
            Assert.fail("Failed to Click " + by + "<br><b>Exception :</b>" + e.getStackTrace());
        }
    }


    public static void sendKeys(By by, String value) {

        WebElement element = driver.get().findElement(by);
        element.clear();
        element.sendKeys(value);
    }

////    /**
////     * waits until element is visible
////     *
////     * @param By
////     */
    public static void waitUntilElementIsLocated(By by) {
        getWebDriverWaitInstance(35).until(ExpectedConditions.visibilityOfElementLocated(by));
    }


    public static WebDriverWait getWebDriverWaitInstance(int time) {
        return new WebDriverWait(driver.get(), Duration.ofSeconds(time));
    }

    public static void browserZoomOut(String zoomValue) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) (driver.get());
            executor.executeScript("document.body.style.zoom = '" + zoomValue + "'");
        } catch (Exception e) {
            System.out.println("Exception thrown: " + e.getMessage());
        }
    }

}
