package binance.config;

import binance.connection.IConnection;
import binance.parsers.IParser;
import binance.seekers.IHandler;
import binance.workers.AbstractWorker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Implementation of IConfigLoader for json config
 * @see IConfigLoader
 */
public class DefaultConfigLoader implements IConfigLoader {
    private final static Logger logger = LoggerFactory.getLogger(IConfigLoader.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @see IConfigLoader
     */
    public ConfigInstance load(final String path) throws Exception {
        logger.debug("Build the application from the configuration file {}", path);
        JsonNode rootNode = objectMapper.readTree(
                getClass().getClassLoader().getResourceAsStream("config.json")
        );

        logger.debug("Looking for parsers....");
        Map<String, IParser> parsersMap = new HashMap<>();
        Iterator<JsonNode> parsersElements = rootNode.get("parsers").elements();
        while (parsersElements.hasNext()) {
            JsonNode next = parsersElements.next();
            IParser type = (IParser) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            parsersMap.put(name, type);
            logger.debug("Found parser {} type {}", name, type.getClass().getName());
        }
        logger.debug("Found {} parsers", parsersMap.size());

        logger.debug("Looking for handlers....");
        Map<String, IHandler> handlersMap = new HashMap<>();
        Iterator<JsonNode> handlersElements = rootNode.get("handlers").elements();
        while (handlersElements.hasNext()) {
            JsonNode next = handlersElements.next();
            IHandler type = (IHandler) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            handlersMap.put(name, type);
            logger.debug("Found handler {} type {}", name, type.getClass().getName());
        }
        logger.debug("Found {} handlers", handlersMap.size());

        logger.debug("Looking for connection handlers....");
        Map<String, IConnection> connectionsMap = new HashMap<>();
        Iterator<JsonNode> connectionsElements = rootNode.get("connections").elements();
        while (connectionsElements.hasNext()) {
            JsonNode next = connectionsElements.next();
            IConnection type = (IConnection) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            connectionsMap.put(name, type);
            logger.debug("Found connection handler {} type {}", name, type.getClass().getName());
        }
        logger.debug("Found {} connection handlers....", connectionsMap.size());

        logger.debug("Create workers....");
        List<AbstractWorker> workersList = new ArrayList<>();
        Iterator<JsonNode> workersElements = rootNode.get("workers").elements();
        while (workersElements.hasNext()) {
            JsonNode workerConfig = workersElements.next();

            Map<String, Object> params = objectMapper.readValue(workerConfig.get("params").traverse()
                                                                , HashMap.class);
            AbstractWorker worker = (AbstractWorker) Class.forName(workerConfig.get("type").textValue())
                    .getConstructor(Map.class,
                            IConnection.class,
                            IParser.class,
                            IHandler.class)
                    .newInstance(params,
                            connectionsMap.get(workerConfig.get("connection").textValue()),
                            parsersMap.get(workerConfig.get("parser").textValue()),
                            handlersMap.get(workerConfig.get("handler").textValue()));
            workersList.add(worker);
            logger.debug("Create worker with config {}", workerConfig);
        }
        logger.debug("{} workers are created", workersList.size());

        return new ConfigInstance(
                parsersMap,
                handlersMap,
                connectionsMap,
                workersList);
    }
}
