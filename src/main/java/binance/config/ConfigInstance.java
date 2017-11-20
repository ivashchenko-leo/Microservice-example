package binance.config;

import binance.workers.AbstractWorker;

import java.util.List;

/**
 * This class represents a loaded config
 */
public class ConfigInstance {
    private final List<AbstractWorker> workers;

    public ConfigInstance(List<AbstractWorker> workers) {
        this.workers = workers;
    }

    public List<AbstractWorker> getWorkers() {
        return workers;
    }
}
