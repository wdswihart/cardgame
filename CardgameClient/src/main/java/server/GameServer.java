package server;

import client.model.User;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import javafx.util.Pair;
import models.Events;
import org.w3c.dom.events.Event;
import server.configuration.ConfigurationProvider;
import storage.StorageProvider;
import transportmodels.UserList;
import util.JSONUtils;

import java.beans.ExceptionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class GameServer {
    private StorageProvider mStorageProvider;
    private ConfigurationProvider mConfigurationProvider;

    private SocketIOServer mServer;

    private Map<String, User> mActiveUsers = new HashMap<>();

    public class User {
        // FIELDS:
        public SocketIOClient client;

        public client.model.User user;

        // CONSTRUCTORS:
        public User(SocketIOClient client, client.model.User user) {
            this.client = client;
            this.user = user;
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
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServer.start();
    }

    private void handleLoginEvent(SocketIOClient client, String data, AckRequest ack) {
        client.model.User user = JSONUtils.fromJson(data, client.model.User.class);
        user = (user == null) ? new client.model.User() : user;

        Map<String, client.model.User> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (user.isDefault() ||
                !registeredUsers.containsKey(user.getUsername()) ||
                !registeredUsers.get(user.getUsername()).getPassword().equals(user.getPassword())) {
            client.sendEvent(Events.LOGIN, new client.model.User());
            logoutUser(client);
            return;
        }


        client.joinRoom("lobby");
        client.sendEvent(Events.LOGIN, user);
        loginUser(client, user);
    }

    private void handleCreateAccountEvent(SocketIOClient client, String data, AckRequest ack) {
        client.model.User user = JSONUtils.fromJson(data, client.model.User.class);

        Map<String, client.model.User> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (registeredUsers.containsKey(user.getUsername())) {
            //Error existing user.
            client.sendEvent(Events.CREATE_ACCOUNT, new client.model.User());
            return;
        }

        mStorageProvider.addRegisteredUser(user);
        client.sendEvent(Events.CREATE_ACCOUNT, user);
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
    private void loginUser(SocketIOClient client, client.model.User user) {
        if (!mActiveUsers.containsKey(client.getSessionId().toString())) {
            client.model.User userWithoutPass = new client.model.User(user.getUsername(), "");
            mActiveUsers.put(client.getSessionId().toString(), new User(client, userWithoutPass));
            mServer.getBroadcastOperations().sendEvent(Events.PLAYER_LIST, getActiveUsers());
        }
    }

    private UserList getActiveUsers() {
        UserList list = new UserList();

        for (Map.Entry<String, User> pair : mActiveUsers.entrySet()) {
            list.getUsers().add(pair.getValue().user);
        }

        return list;
    }
    //endregion
}
