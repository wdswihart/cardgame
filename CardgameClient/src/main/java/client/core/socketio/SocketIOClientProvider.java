package client.core.socketio;

import io.socket.client.Socket;

public interface SocketIOClientProvider {
    Socket getClient();
    Socket createNewClient(String host) throws Exception;
}
