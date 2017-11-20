package binance.connection;

import java.util.function.Consumer;

/**
 * An interface that represents connection to a data source.
 * As the data source we can use almost any source, http, web sockets, file etc.
 */
public interface IConnection {
    /**
     * @param path, url, address to data source.
     * @param dataConsumer callback for received data
     * @throws Exception if receiving is failed
     */
    void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception;
}
