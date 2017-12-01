package client.core.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import models.Events;

import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class SocketIOClientProviderImpl implements SocketIOClientProvider {
    // FIELDS:

    private String mServerAddress = "";
    private static SocketIOClientProviderImpl sInstance = new SocketIOClientProviderImpl();
    private static Socket sSocketIOClient = null;
    private boolean mIsConnected;

    // METHODS:

    public static SocketIOClientProviderImpl getInstance() {
        return sInstance;
    }

    public SocketIOClientProviderImpl() {
        try {
            mServerAddress = InetAddress.getLocalHost().getHostAddress() + ":8087";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socket getClient() {
        if (!mIsConnected) {
            mIsConnected = true;

            try {
                sSocketIOClient = IO.socket("http://" + mServerAddress);
                sSocketIOClient.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return sSocketIOClient;
    }

    @Override
    public Socket createNewClient(String host) throws Exception {
        if (host.equals(mServerAddress)) {
            return sSocketIOClient;
        }

        mServerAddress = host;
        sSocketIOClient.disconnect();
        sSocketIOClient.io().socket("http://" + host);

        return sSocketIOClient;
    }

    private void transferListeners(Socket oldVal, Socket newVal) {
        List<String> events = new ArrayList<>();
        events.add(Events.CHAT);
        events.add(Events.PLAYER_JOINED);
        events.add(Events.PLAYER_LIST);
        events.add(Events.LOGIN);
        events.add(Events.CREATE_ACCOUNT);
        events.add(Events.START_GAME);
        events.add(Events.UPDATE_GAME);
        events.add(Events.INVITE_REQUEST);
        events.add(Events.DRAW);
        events.add(Events.PLAY_CARD);
        events.add(Events.PASS_TURN);

        for (String event : events) {
            List<Emitter.Listener> listeners = oldVal.listeners(event);

            for (Emitter.Listener listener : listeners) {
                newVal.on(event, listener);
            }
        }
        oldVal.off();
    }

    @Override
    public void finalize() {
        sSocketIOClient.disconnect();
    }
}
