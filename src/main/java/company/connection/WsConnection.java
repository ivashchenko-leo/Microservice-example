package company.connection;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.function.Consumer;

/**
 * Implementation of IConnection for web sockets connection to a data source
 * @see IConnection
 */
public class WsConnection implements IConnection {
    @Override
    public void connect(final String path, final Consumer<byte[]> dataConsumer) throws Exception {
        final WebSocketContainer clientEndPoint =
                ContainerProvider.getWebSocketContainer();
        clientEndPoint.connectToServer(new WsClientEndpoint(dataConsumer), new URI(path));
    }
}