package auto.stepDefinitions;

import auto.browser.Browser;
import auto.browser.ReusableLibrary;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.Map;
import static auto.browser.ReusableLibrary.sleep;

public class Hooks {
    private static String randomSTBusinessName;
    private static String randomPTRBusinessName;
    private static String randomLTDBusinessName;
    private static String randomBusinessSurname;
    private static boolean stFlag = false;
    private static boolean ptrFlag = false;
    private static boolean ltdFlag = false;
    private static final Map<Scenario, Map<String, String>> scenarioContextMap = new HashMap<>();

    public static Map<String, String> hm = new HashMap<String, String>();

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        ReusableLibrary.scenarioThreadLocal.set(scenario);
        scenarioContextMap.put(scenario, new HashMap<>());
        if (scenario.getName().contains("API")) {
//            ExtentTestManager.startTest(scenario.getName());
        }
        stFlag = false;
        ptrFlag = false;
        ltdFlag = false;
    }

    @Before
    public void generateRandomNames() {
        if (randomSTBusinessName == null) {
            randomSTBusinessName = "Business name ST " + RandomStringUtils.randomAlphabetic(20);
        }
        if (randomPTRBusinessName == null) {
            randomPTRBusinessName = "Business name PTR " + RandomStringUtils.randomAlphabetic(20);
        }
        if (randomLTDBusinessName == null) {
            randomLTDBusinessName = "Business name LTD " + RandomStringUtils.randomAlphabetic(20);
        }
    }

    @Before("@SecondScenario")
    public void beforeSecondScenario(Scenario scenario) {
        while (!stFlag) {
            sleep(5000);
        }
        while (!ptrFlag) {
            sleep(5000);
        }
        while (!ltdFlag) {
            sleep(5000);
        }
    }

    public static String getRandomBusinessName() {
        return randomSTBusinessName;
    }

    public static String getRandomPTRBusinessName() {
        return randomPTRBusinessName;
    }

    public static String getRandomLTDBusinessName() {
        return randomLTDBusinessName;
    }

    public static String getRandomBusinessSurname() {
        return randomBusinessSurname;
    }

    @After
    public void afterScenario(Scenario scenario) {
        getSCFourScenarioStatus(scenario);

    }

    @After("@FirstScenario")
    public void afterSecondScenario(Scenario scenario) {
        stFlag = true;
        ptrFlag = true;
        ltdFlag = true;
    }

    public Map<String, String> getSCFourScenarioStatus(Scenario scenario) {
        String featureFilePath = scenario.getUri().getPath();
        if (featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC16_ST.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC16_LTD.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC16_PTR.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC20_ST.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC20_LTD.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC20_PTR.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC18.feature")||featureFilePath.endsWith("E2ETesting_PurchaseProposal_SC4_TC19.feature")) {
            String scenarioName = scenario.getName();
            if (scenario.isFailed()) {
                hm.clear();
                hm.put(scenarioName, "Failed");
                return hm;
            }
        }
        return hm;
    }

}
