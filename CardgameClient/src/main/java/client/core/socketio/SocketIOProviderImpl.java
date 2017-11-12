package client.core.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;

public class SocketIOProviderImpl implements SocketIOProvider {
    // FIELDS:

    private static SocketIOProviderImpl sInstance = new SocketIOProviderImpl();
    private static Socket sSocketIOClient = null;
    private boolean mIsConnected;

    // METHODS:

    public static SocketIOProviderImpl getInstance() {
        return sInstance;
    }

    @Override
    public Socket getClient() {
        if (!mIsConnected) {
            mIsConnected = true;

            try {
                sSocketIOClient = IO.socket("http://192.168.2.8");
                sSocketIOClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return sSocketIOClient;
    }
}
