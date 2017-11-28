package company.workers;

import company.connection.IConnection;
import company.parsers.IParser;
import company.seekers.IHandler;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An implementation for AbstractWorker for a simple single thread.
 */
public class DefaultWorker extends AbstractWorker {
    public DefaultWorker(final Map<String, Object> params,
                         final IConnection connection,
                         final IParser parser,
                         final IHandler handler) {
        super(params, connection, parser, handler);
    }

    @Override
    public Future<?> start() {
        return Executors.newSingleThreadExecutor().submit(() -> {
            try {
                String path = (String) params.get("address");
                connection.connect(path, (receivedData) -> {
                    try {
                        Map<String, Object> parsedInput = parser.parse(receivedData);
                        String profit = (String) handler.handle(parsedInput);
                        StringBuilder result = new StringBuilder((String) parsedInput.get("s"));
                        if (!profit.equals("None")) {
                            logger.info(result.append(" ").append(profit).toString());
                        } else {
                            logger.debug("No profit found");
                        }
                    } catch (Exception ex) {
                        logger.error(ex.toString());
                    }
                });
                logger.info("Connected to " + path);
            } catch (Exception ex) {
                logger.error(ex.toString());
            }
        });
    }
}
