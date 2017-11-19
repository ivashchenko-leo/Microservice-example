package binance.config;

public interface IConfigLoader {
    ConfigInstance load(final String path) throws Exception;
}
