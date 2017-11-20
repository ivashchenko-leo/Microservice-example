package binance.config;

/**
 * An interface for loading a config. Implementation depends on a config type.
 * For example, we can make the config in xml or properties and create a loader for it.
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
