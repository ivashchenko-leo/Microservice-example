package binance.connection;

import java.util.function.Consumer;

public interface IConnection {
    void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception;
}
