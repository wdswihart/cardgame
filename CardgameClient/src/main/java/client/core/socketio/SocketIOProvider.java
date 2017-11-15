package client.core.socketio;

import io.socket.client.Socket;

public interface SocketIOProvider {
    Socket getClient();
    Socket createNewClient(String host) throws Exception;
}
