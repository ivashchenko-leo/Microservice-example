package binance.workers;

import binance.connection.IConnection;
import binance.parsers.IParser;
import binance.seekers.IHandler;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ScheduledWorker extends AbstractWorker {
    public ScheduledWorker(final Map<String, Object> params,
                           final IConnection connection,
                           final IParser parser,
                           final IHandler handler) {
        super(params, connection, parser, handler);
    }

    @Override
    public Future<?> start() {
        Integer period = (Integer) params.get("period");
        TimeUnit timeUnit = TimeUnit.valueOf((String) params.get("timeUnit"));
        String path = (String) params.get("address");
        logger.info("Connected to " + path);
        return Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                StringBuilder result = new StringBuilder((String) params.get("symbol"));
                connection.connect(path, (receivedData) -> {
                    try {
                        Map<String, Object> parsedInput = parser.parse(receivedData);
                        String profit = (String) handler.handle(parsedInput);
                        if (!profit.equals("None")) {
                            logger.info(result.append(" ").append(profit).toString());
                        } else {
                            logger.debug("No profit found");
                        }
                    } catch (Exception ex) {
                        logger.error(ex.toString());
                    }
                });
            } catch (Exception ex) {
                logger.error(ex.toString());
            }
        }, 0, period, timeUnit);
    }
}
