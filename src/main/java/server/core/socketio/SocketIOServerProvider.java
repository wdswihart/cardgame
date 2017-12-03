package server.core.socketio;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import server.handlers.BaseEventHandler;

public interface SocketIOServerProvider {
    void broadcast(String event, Object obj);
    void on(String event, BaseEventHandler<?> handler);
    void onConnect(ConnectListener listener);
    void onDisconnect(DisconnectListener listener);

    void start();
    void stop();

    void broadcastRoom(String room, String event, String message);
}
