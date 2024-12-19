package auto.pages;

import auto.browser.ReusableLibrary;
import auto.config.ConfigFileReader;
import org.openqa.selenium.By;
import java.util.Properties;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class LoginPage extends ReusableLibrary {

    public static Properties configUser = ConfigFileReader.getUserProfileConfig();


    private By lumaLogo = By.xpath("/html/body/div[2]/header/div[2]/a/img");
    //       private By createAccountLink = By.xpath("//span[contains(text(),'Create an Account')]");
    private By createAccountLink = By.xpath("/html/body/div[2]/header/div[1]/div/ul/li[3]/a");
    //    private By createAccountLink = By.xpath("//header//a[contains(@href, 'customer/account/create') and text()='Create an Account']");
    private By signInLink = By.xpath("/html/body/div[2]/header/div[1]/div/ul/li[2]/a");
    private By loginEmail = By.xpath("//input[@name='login[username]']");
    private By loginPassword = By.xpath("//input[@name='login[password]']");
    private By signInButton = By.xpath("//button[@class='action login primary']//span[contains(text(),\"Sign In\")]");
    private By firstName = By.id("firstname");
    private By lastName = By.id("lastname");
    private By emailaddress = By.id("email_address");
    private By confirmPassword = By.id("password-confirmation");
    private By password = By.id("password");
    private By createButton = By.xpath("//button[@type='submit']//span[contains(text(),\"Create an Account\")]");
    Scenario scenario = scenarioThreadLocal.get();


    public void verifyLogo() {
        try {
            WebElement logo = driver.get().findElement(lumaLogo);
            // Verify if the logo is displayed
            if (logo.isDisplayed()) {
                System.out.println("Logo is displayed on the page.");
                Assert.assertTrue(true);
                scenario.log("Logo is displayed on the page.");
            } else {
                System.out.println("Logo is not displayed on the page.");
                Assert.assertTrue(true);
                scenario.log("Logo is not displayed on the page.");
            }
            sleep(3000);
            takeScreenshot();
        } catch (Exception e) {
            System.out.println("Unable to verify the Logo");
        }
    }
    
    public void createAccountLuma(String Firstname, String Lastname, String EmailID, String Password) {
        try {
            clickElement(createAccountLink);
            sendKeys(firstName, Firstname);
            sendKeys(lastName, Lastname);
            sendKeys(emailaddress, EmailID);
            sendKeys(password, Password);
            sendKeys(confirmPassword, Password);
           scenario.log("User First name and Last name");
            clickElement(createButton);
            sleep(3000);
            takeFullScreenshot();
        } catch (Exception e) {
            System.out.println("Unable to Create an Account");
            Assert.assertTrue(false);
        }
    }

    public void signInLuma(String EmailID, String Password) {
        try {
            clickElement(signInLink);
            sendKeys(loginEmail, EmailID);
            sendKeys(loginPassword, Password);
            //           scenario.log("User First name and Last name");
            clickElement(signInButton);
            sleep(3000);
            takeFullScreenshot();
        } catch (Exception e) {
            System.out.println("Unable to Sign In");
            Assert.assertTrue(false);
        }
    }

}
