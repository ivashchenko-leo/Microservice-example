package binance.connection;

import binance.config.IConfigLoader;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.function.Consumer;

/**
 * Implementation of IConnection for http connection to a data source
 * @see IConnection
 */
public class HttpConnection implements IConnection {
    private final static Logger logger = LoggerFactory.getLogger(IConfigLoader.class);

    @Override
    public void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception {
        logger.debug("Connect to {}", path);
        dataConsumer.accept(IOUtils.toByteArray(new URL(path)));
    }
}
