package binance.config;

import binance.connection.IConnection;
import binance.parsers.IParser;
import binance.seekers.IHandler;
import binance.workers.AbstractWorker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class DefaultConfigLoader implements IConfigLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ConfigInstance load(final String path) throws Exception {
        JsonNode rootNode = objectMapper.readTree(
                getClass().getClassLoader().getResourceAsStream("config.json")
        );

        Map<String, IParser> parsersMap = new HashMap<>();
        Iterator<JsonNode> parsersElements = rootNode.get("parsers").elements();
        while (parsersElements.hasNext()) {
            JsonNode next = parsersElements.next();
            IParser type = (IParser) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            parsersMap.put(name, type);
        }

        Map<String, IHandler> handlersMap = new HashMap<>();
        Iterator<JsonNode> handlersElements = rootNode.get("handlers").elements();
        while (handlersElements.hasNext()) {
            JsonNode next = handlersElements.next();
            IHandler type = (IHandler) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            handlersMap.put(name, type);
        }

        Map<String, IConnection> connectionsMap = new HashMap<>();
        Iterator<JsonNode> connectionsElements = rootNode.get("connections").elements();
        while (connectionsElements.hasNext()) {
            JsonNode next = connectionsElements.next();
            IConnection type = (IConnection) Class.forName(next.get("type").textValue())
                    .getConstructor().newInstance();
            String name = next.get("name").textValue();

            connectionsMap.put(name, type);
        }

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
        }

        return new ConfigInstance(
                parsersMap,
                handlersMap,
                connectionsMap,
                workersList);
    }
}