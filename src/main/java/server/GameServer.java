package server;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Events;
import models.Player;
import server.configuration.ConfigurationProvider;
import server.core.socketio.SocketIOServerProvider;
import server.core.users.UsersProvider;
import server.handlers.*;
import server.handlers.gameplay.DrawEventHandler;
import server.handlers.gameplay.PlayCardEventHandler;
import storage.StorageProvider;

public class GameServer {
    private StorageProvider mStorageProvider;
    private ConfigurationProvider mConfigurationProvider;
    private SocketIOServerProvider mServerProvider;
    private UsersProvider mUsersProvider;

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
                      UsersProvider usersProvider) {
        mStorageProvider = storageProvider;
        mConfigurationProvider = configurationProvider;
        mServerProvider = serverProvider;
        mUsersProvider = usersProvider;

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
            mUsersProvider.removeUser(new User(client, new Player()));
        });

        mServerProvider.on(Events.LOGIN, LoginEventHandler.getHandler());
        mServerProvider.on(Events.CREATE_ACCOUNT, CreateAccountEventHandler.getHandler());
        mServerProvider.on(Events.CHAT, ChatEventHandler.getHandler());
        mServerProvider.on(Events.START_GAME, StartGameEventHandler.getHandler());

        //Gameplay Events
        mServerProvider.on(Events.DRAW, DrawEventHandler.getHandler());
        mServerProvider.on(Events.PLAY_CARD, PlayCardEventHandler.getHandler());
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServerProvider.start();
    }
}
