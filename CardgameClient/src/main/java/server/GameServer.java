package server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Inject;
import models.Events;
import models.Player;
import server.configuration.ConfigurationProvider;
import server.core.socketio.SocketIOServerProvider;
import server.core.users.ActiveUserProvider;
import server.handlers.ChatEventHandler;
import server.handlers.CreateAccountEventHandler;
import server.handlers.LoginEventHandler;
import server.handlers.StartGameEventHandler;
import storage.StorageProvider;
import models.responses.PlayerList;
import util.JSONUtils;

import java.util.*;

public class GameServer {
    private StorageProvider mStorageProvider;
    private ConfigurationProvider mConfigurationProvider;
    private SocketIOServerProvider mServerProvider;
    private ActiveUserProvider mActiveUserProvider;

    public static class User {
        // FIELDS:
        private SocketIOClient mClient;

        private Player mPlayer;

        // CONSTRUCTORS:
        public User(SocketIOClient client, Player player) {
            this.mClient = client;
            this.mPlayer = player;
        }

        public Player getPlayer() {
            return mPlayer;
        }

        public SocketIOClient getClient() {
            return mClient;
        }
    }

    @Inject
    public GameServer(StorageProvider storageProvider,
                      ConfigurationProvider configurationProvider,
                      SocketIOServerProvider serverProvider,
                      ActiveUserProvider activeUserProvider) {
        mStorageProvider = storageProvider;
        mConfigurationProvider = configurationProvider;
        mServerProvider = serverProvider;
        mActiveUserProvider = activeUserProvider;

        setEventListeners();
    }

    @Override
    public void finalize() {
        mServerProvider.stop();
    }

    // setEventListeners sets the listeners for the SocketIO server.
    private void setEventListeners() {
        mServerProvider.onConnect(client -> {
            System.out.println("[DEBUG] Client connected to server");
            //On a connection do nothing for now.
        });

        mServerProvider.onDisconnect(client -> {
            System.out.print("[DEBUG] Client disconnected.");
            mActiveUserProvider.removeUser(new User(client, new Player()));
        });

        mServerProvider.on(Events.LOGIN, LoginEventHandler.getHandler());
        mServerProvider.on(Events.CREATE_ACCOUNT, CreateAccountEventHandler.getHandler());
        mServerProvider.on(Events.CHAT, new ChatEventHandler().getHandler());
        mServerProvider.on(Events.START_GAME, new StartGameEventHandler().getHandler());
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServerProvider.start();
    }
}
