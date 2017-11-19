package binance.connection;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.util.function.Consumer;

public class HttpConnection implements IConnection {
    @Override
    public void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception {
        dataConsumer.accept(IOUtils.toByteArray(new URL(path)));
    }
}
