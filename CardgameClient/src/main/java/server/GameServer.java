package server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Inject;
import models.Events;
import models.Player;
import server.configuration.ConfigurationProvider;
import storage.StorageProvider;
import models.responses.PlayerList;
import util.JSONUtils;

import java.util.*;

public class GameServer {
    private StorageProvider mStorageProvider;
    private ConfigurationProvider mConfigurationProvider;

    private SocketIOServer mServer;

    private Map<String, User> mActiveUsers = new HashMap<>();

    public class User {
        // FIELDS:
        public SocketIOClient client;

        public Player player;

        // CONSTRUCTORS:
        public User(SocketIOClient client, Player player) {
            this.client = client;
            this.player = player;
        }

    }

    @Inject
    public GameServer(StorageProvider storageProvider, ConfigurationProvider configurationProvider) {
        mStorageProvider = storageProvider;
        mConfigurationProvider = configurationProvider;

        Configuration config = new Configuration();
        config.setHostname(mConfigurationProvider.getHost());
        config.setPort(mConfigurationProvider.getPort());
        config.getSocketConfig().setReuseAddress(true);

        mServer = new SocketIOServer(config);
        setEventListeners();
    }

    @Override
    public void finalize() {
        mServer.stop();
    }

    // setEventListeners sets the listeners for the SocketIO server.
    private void setEventListeners() {
        mServer.addConnectListener(client -> {
            System.out.println("[DEBUG] Client connected to server");
            //On a connection do nothing for now.
        });

        mServer.addDisconnectListener(client -> {
            System.out.print("[DEBUG] Client disconnected.");
            logoutUser(client);
        });

        mServer.addEventListener(Events.LOGIN, String.class, this::handleLoginEvent);
        mServer.addEventListener(Events.CREATE_ACCOUNT, String.class, this::handleCreateAccountEvent);
        mServer.addEventListener(Events.CHAT, String.class, this::handleChatEvent);
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServer.start();
    }

    private void handleLoginEvent(SocketIOClient client, String data, AckRequest ack) {
        Player player = JSONUtils.fromJson(data, Player.class);
        player = (player == null) ? new Player() : player;

        Map<String, Player> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (player.isDefault() ||
                !registeredUsers.containsKey(player.getUsername()) ||
                !registeredUsers.get(player.getUsername()).getPassword().equals(player.getPassword())) {
            client.sendEvent(Events.LOGIN, new Player());
            logoutUser(client);
            return;
        }


        client.joinRoom("lobby");
        client.sendEvent(Events.LOGIN, player);
        loginUser(client, player);
    }

    private void handleCreateAccountEvent(SocketIOClient client, String data, AckRequest ack) {
        Player player = JSONUtils.fromJson(data, Player.class);

        Map<String, Player> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (registeredUsers.containsKey(player.getUsername())) {
            //Error existing player.
            client.sendEvent(Events.CREATE_ACCOUNT, new Player());
            return;
        }

        mStorageProvider.addRegisteredUser(player);
        client.sendEvent(Events.CREATE_ACCOUNT, player);
    }

    private void handleChatEvent(SocketIOClient client, String data, AckRequest ack) {
        if (!mActiveUsers.containsKey(client.getSessionId().toString())) {
            return;
        }

        User user = mActiveUsers.get(client.getSessionId().toString());
        mServer.getBroadcastOperations().sendEvent(Events.CHAT, user.player.getUsername() + ": " + data);
    }

    //#region UserManagement
    //Remove the client from the active users map.
    private void logoutUser(SocketIOClient client) {
        if (mActiveUsers.containsKey(client.getSessionId().toString())) {
            mActiveUsers.remove(client.getSessionId().toString());
            mServer.getBroadcastOperations().sendEvent(Events.PLAYER_LIST, getActiveUsers());
        }
    }

    //Add client to the active users map.
    private void loginUser(SocketIOClient client, Player player) {
        if (!mActiveUsers.containsKey(client.getSessionId().toString())) {
            Player playerWithoutPass = new Player(player.getUsername(), "");
            mActiveUsers.put(client.getSessionId().toString(), new User(client, playerWithoutPass));
            mServer.getBroadcastOperations().sendEvent(Events.PLAYER_LIST, getActiveUsers());
        }
    }

    private PlayerList getActiveUsers() {
        PlayerList list = new PlayerList();

        for (Map.Entry<String, User> pair : mActiveUsers.entrySet()) {
            list.getUsers().add(pair.getValue().player);
        }

        return list;
    }
    //endregion
}
