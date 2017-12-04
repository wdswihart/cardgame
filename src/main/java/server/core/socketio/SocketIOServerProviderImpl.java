package server.core.socketio;

import client.core.socketio.SocketIOClientProviderImpl;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import server.configuration.ConfigurationProvider;
import server.handlers.BaseEventHandler;

@Singleton
public class SocketIOServerProviderImpl implements SocketIOServerProvider {
    private ConfigurationProvider mConfigurationProvider;
    private SocketIOServer mServer;

    @Inject
    public SocketIOServerProviderImpl(ConfigurationProvider configurationProvider) {
        mConfigurationProvider = configurationProvider;

        Configuration config = new Configuration();
        config.setHostname(mConfigurationProvider.getHost());
        config.setPort(mConfigurationProvider.getPort());
        config.getSocketConfig().setReuseAddress(true);

        mServer = new SocketIOServer(config);
    }

    @Override
    public void broadcast(String event, Object obj) {
        mServer.getRoomOperations("lobby").sendEvent(event, obj);
    }

    @Override
    public void on(String event, BaseEventHandler<?> handler) {
        mServer.addEventListener(event, String.class, handler);
    }

    @Override
    public void onConnect(ConnectListener listener) {
        mServer.addConnectListener(listener);
    }

    @Override
    public void onDisconnect(DisconnectListener listener) {
        mServer.addDisconnectListener(listener);
    }

    @Override
    public void start() {
        mServer.start();
    }

    @Override
    public void stop() {
        mServer.stop();

        Configuration config = new Configuration();
        config.setHostname(mConfigurationProvider.getHost());
        config.setPort(mConfigurationProvider.getPort());
        config.getSocketConfig().setReuseAddress(true);

        mServer = new SocketIOServer(config);
    }

    @Override
    public void broadcastRoom(String room, String event, String message) {
        mServer.getRoomOperations(room).sendEvent(event, message);
    }
}
