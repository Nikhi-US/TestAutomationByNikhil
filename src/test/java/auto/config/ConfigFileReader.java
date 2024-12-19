package auto.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

import auto.managers.FileReaderManager;
public class ConfigFileReader {

    private static Properties globalProp;
    private static String env;
    private static Properties urlProp;

    private static Properties userProfileConfig;
    public ConfigFileReader() {
        try {
            InputStream inStream = ConfigFileReader.class.getResourceAsStream("/auto.properties/config.properties");
            System.out.println("calling config file =======>" + inStream.toString());
            globalProp = new Properties();
            globalProp.load(new InputStreamReader(inStream, Charset.forName("UTF-8")));
            env = globalProp.getProperty("Env");
            String sysEnv = System.getProperty("Env");
            if (sysEnv != null && !sysEnv.isEmpty() && sysEnv.length() != 0) {
                System.out.println("Info: Config Env value is Changed from " + env + " to " + sysEnv);
                env = sysEnv;
            }
            if (env == null)
                throw new RuntimeException("Missing Env value");
        } catch (Exception e) {
            throw new RuntimeException("Unable to load config.properties file");
        }
// throw new RuntimeException("Configuration properties file not found at: " +
// propertyFilePath );
    }

    public String getApplicationUrl() {
        String url = globalProp.getProperty("url");
        if (url != null)
            return url;
        else
            throw new RuntimeException("Url not specified in the configuration properties file");
    }
    public String getEnv() {
        String env = globalProp.getProperty("Env");
        if (env != null)
            return env;
        else
            throw new RuntimeException("Env not specified in the configuration properties file");
    }
    public String getBrowser() {
        String browser = globalProp.getProperty("browser");

        String browserSys = System.getProperty("browser");
        if (browserSys != null && !browserSys.isEmpty() && browserSys.length() != 0) {
            System.out.println("Info: Config browser value is Changed from " + browser + " to " + browserSys);
            browser = browserSys;
        }
        if (browser == null)
            throw new RuntimeException("browser not specified in the configuration properties file");
        else
            return browser;

    }

    public String getImplicitWait() {
        String implicitWait = globalProp.getProperty("IMPLICIT_WAIT");
        if (implicitWait != null)
            return implicitWait;
        else
            throw new RuntimeException("implicitWait not specified in the configuration properties file");
    }

    public String getPageTimeOut() {
        String pageTimeOut = globalProp.getProperty("PAGE_LOAD_TIMEOUT");
        if (pageTimeOut != null)
            return pageTimeOut;
        else
            throw new RuntimeException("pageTimeOut not specified in the configuration properties file");
    }

    public String getGrid() {
        String grid = globalProp.getProperty("grid");

        String sysGrid = System.getProperty("grid");
        if (sysGrid != null && !sysGrid.isEmpty() && sysGrid.length() != 0) {
            System.out.println("Info: Config Env value is Changed from " + grid + " to " + sysGrid);
            grid = sysGrid;
        }
        if (grid != null)
            return grid;
        else
            throw new RuntimeException("grid not specified in the configuration properties file");
    }

    public String getExecutionPlatform() {
        String grid = globalProp.getProperty("ExecutionPlatform");
        if (grid != null)
            return grid;
        else
            throw new RuntimeException("grid not specified in the configuration properties file");
    }

    public String getScreenshotCheck() {

        String needToCapptureScreenshot = System.getProperty("captureScreenshot");
        if (!(needToCapptureScreenshot != null && !needToCapptureScreenshot.isEmpty())) {
            needToCapptureScreenshot = globalProp.getProperty("captureScreenshot");
        }
        if (needToCapptureScreenshot != null)
            return needToCapptureScreenshot;
        else
            throw new RuntimeException("grid not specified in the configuration properties file");
    }

    public String getSleepWaitTime() {
        String grid = globalProp.getProperty("SLEEP_WAIT");
        if (grid != null)
            return grid;
        else
            throw new RuntimeException("grid not specified in the configuration properties file");
    }

    public String getGridHost() {
        String gridHost = System.getProperty("GridHost");

        if (!(gridHost != null && !gridHost.isEmpty())) {
            gridHost = globalProp.getProperty("GridHost");
        }
        if (gridHost != null)
            return gridHost;
        else
            throw new RuntimeException("gridHost not specified in the configuration properties file");
    }

    public String getDataSheet() {
        String dataSheet = globalProp.getProperty("DataSheet");
        if (dataSheet != null)
            return dataSheet;
        else
            throw new RuntimeException("dataSheet not specified in the configuration properties file");
    }



    /**
     * reads the latest Object repository property file
     *
     * @return
     */


    /**
     * reads global property file
     *
     * @return
     */
    public static Properties getGlobalConfig() {
        if (globalProp != null) {
            return globalProp;
        } else {
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/auto.properties/config.properties");
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("Loading properties file:" + inStream);
            globalProp = new Properties();
            try {
                globalProp.load(inStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return globalProp;
    }

    public static Properties getURLConfig() {
        if (urlProp != null) {
            return urlProp;
        } else {
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/auto.properties/URLs.properties");
                System.out.println("URL loaded ====>" + inStream);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.out.println("Loading URLsproperties file:" + inStream);
            urlProp = new Properties();
            try {
                urlProp.load(inStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return urlProp;
    }

    public static Properties getUserProfileConfig() {
        if (userProfileConfig != null) {
            return userProfileConfig;
        } else {
            InputStream inStream = null;
            try {
                inStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/auto.properties/UserProfiles.properties");
                System.out.println("URL loaded ====>" + inStream);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            System.out.println("Loading URLsproperties file:" + inStream);
            userProfileConfig = new Properties();
            try {
                userProfileConfig.load(inStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return userProfileConfig;
    }

    public String getCucumberReportOpenAfter() {
        String value = globalProp.getProperty("cucumber.report.open.after");
        if (value != null)
            return value;
        else
            throw new RuntimeException("cucumber.report.open.after is not specified in the configuration properties file");
    }


}
