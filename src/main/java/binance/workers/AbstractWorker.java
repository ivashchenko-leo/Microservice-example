package binance.workers;

import binance.connection.IConnection;
import binance.parsers.IParser;
import binance.seekers.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Abstract class that used in a thread. It represents what the thread will do.
 */
public abstract class AbstractWorker {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractWorker.class);

    protected final Map<String, Object> params;
    protected final IConnection connection;
    protected final IParser parser;
    protected final IHandler handler;

    public AbstractWorker(final Map<String, Object> params,
                          final IConnection connection,
                          final IParser parser,
                          final IHandler handler) {
        this.params = params;
        this.connection = connection;
        this.parser = parser;
        this.handler = handler;
    }

    /**
     * Body of the thread
     * @return
     */
    public abstract Future<?> start();
}
