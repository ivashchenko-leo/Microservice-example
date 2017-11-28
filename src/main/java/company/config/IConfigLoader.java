package company.config;

/**
 * An interface for loading a config. Implementation depends on a config type.
 * For example, we can make the config in xml or properties and create a loader for it.
 *
 * I believe it would be better to put the config somewhere else, not in resources, so
 * we could change some parameters and just restart the application. But it will be easier
 * for you to execute this app for the test purpose with configs in resources.
 */
public interface IConfigLoader {
    /**
     *
     * @param path to config file
     * @return ConfigInstance object contains loaded data from config
     * @throws Exception if loading is failed
     */
    ConfigInstance load(final String path) throws Exception;
}
