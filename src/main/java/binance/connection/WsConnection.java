package binance.connection;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.function.Consumer;

public class WsConnection implements IConnection {
    @Override
    public void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception {
        final WebSocketContainer clientEndPoint =
                ContainerProvider.getWebSocketContainer();
        clientEndPoint.connectToServer(new WsClientEndpoint(dataConsumer), new URI(path));
    }
}