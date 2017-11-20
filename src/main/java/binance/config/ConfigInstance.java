package binance.config;

import binance.connection.IConnection;
import binance.parsers.IParser;
import binance.seekers.IHandler;
import binance.workers.AbstractWorker;

import java.util.List;
import java.util.Map;

/**
 * This class represents a loaded config
 */
public class ConfigInstance {
    private final Map<String, IParser> parsers;
    private final Map<String, IHandler> handlers;
    private final Map<String, IConnection> connections;
    private final List<AbstractWorker> workers;

    public ConfigInstance(Map<String, IParser> parsers,
                          Map<String, IHandler> handlers,
                          Map<String, IConnection> connections,
                          List<AbstractWorker> workers) {
        this.parsers = parsers;
        this.handlers = handlers;
        this.connections = connections;
        this.workers = workers;
    }

    public List<AbstractWorker> getWorkers() {
        return workers;
    }

    public Map<String, IParser> getParsers() {
        return parsers;
    }

    public Map<String, IHandler> getHandlers() {
        return handlers;
    }

    public Map<String, IConnection> getConnections() {
        return connections;
    }
}
