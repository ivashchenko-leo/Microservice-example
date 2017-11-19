package binance.connection;


import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import java.util.function.Consumer;

@ClientEndpoint
public class WsClientEndpoint {
    private final Consumer<byte[]> messageHandler;

    public WsClientEndpoint(Consumer<byte[]> messageHandler) {
        this.messageHandler = messageHandler;
    }

    @OnMessage
    public void onMessage(String message) {
        messageHandler.accept(message.getBytes());
    }
}