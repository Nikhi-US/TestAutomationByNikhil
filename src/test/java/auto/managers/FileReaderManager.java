package auto.managers;

import auto.config.ConfigFileReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;

    private FileReaderManager() {

    }

    public static FileReaderManager getInstance() {
        return fileReaderManager;
    }

    public ConfigFileReader getConfigReader() {
        configFileReader = (configFileReader == null) ? new ConfigFileReader() : configFileReader;
        return configFileReader;
    }

}


