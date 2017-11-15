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
                sSocketIOClient = IO.socket("http://127.0.0.1:8087");
                sSocketIOClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return sSocketIOClient;
    }

    @Override
    public Socket createNewClient(String host) throws Exception {
        if (mIsConnected) {
            sSocketIOClient.disconnect();
        }

        sSocketIOClient = IO.socket(host);
        sSocketIOClient.connect();

        return sSocketIOClient;
    }

    @Override
    public void finalize() {
        sSocketIOClient.disconnect();
    }
}
