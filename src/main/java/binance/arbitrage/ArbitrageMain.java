package binance.arbitrage;

import binance.config.ConfigInstance;
import binance.config.DefaultConfigLoader;
import binance.config.IConfigLoader;
import binance.workers.AbstractWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class ArbitrageMain {
    static {
        System.setProperty("log4j.configurationFile", "log4j2.json");
    }

    private static Logger logger = LoggerFactory.getLogger(ArbitrageMain.class);

    public static void main(String[] args) {
        try {
            IConfigLoader configLoader = new DefaultConfigLoader();
            ConfigInstance configInstance = configLoader.load("config.json");

            //if it's needed we could use these futures
            //Also we could use thread pools to manage workers
            List<Future> startedWorkers = new ArrayList<>();
            for(AbstractWorker worker : configInstance.getWorkers()) {
                startedWorkers.add(worker.start());
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
