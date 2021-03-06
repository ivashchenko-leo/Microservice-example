package company.arbitrage;

import company.config.ConfigInstance;
import company.config.DefaultConfigLoader;
import company.config.IConfigLoader;
import company.workers.AbstractWorker;
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

    /**
     Entry point, it loads config and starts workers.
     Stores Futures from workers in list, we could use them in the future.
     */
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
