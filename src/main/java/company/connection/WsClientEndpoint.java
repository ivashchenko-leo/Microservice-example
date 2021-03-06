package company.connection;


import company.config.IConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import java.util.function.Consumer;

/**
 * Javax web socket endpoint implementation. On message calls consumer from IConnection.connect
 * @see WsConnection
 */
@ClientEndpoint
public class WsClientEndpoint {
    private final static Logger logger = LoggerFactory.getLogger(IConfigLoader.class);

    private final Consumer<byte[]> messageHandler;

    public WsClientEndpoint(Consumer<byte[]> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @OnMessage
    public void onMessage(String message) {
        logger.debug("Got web socket's message {}", message);
        messageHandler.accept(message.getBytes());
    }
}