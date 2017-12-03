package server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.inject.Inject;
import models.Events;
import models.Player;
import server.configuration.ConfigurationProvider;
import server.core.socketio.SocketIOServerProvider;
import server.core.users.UsersProvider;
import server.handlers.*;
import server.handlers.gameplay.*;
import storage.StorageProvider;

import java.io.IOException;
import java.net.*;

public class GameServer {
    private StorageProvider mStorageProvider;
    private ConfigurationProvider mConfigurationProvider;
    private SocketIOServerProvider mServerProvider;
    private UsersProvider mUsersProvider;

    private DatagramSocket mDatagramSocket;

    public static class User {
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

        startDiscoveryServer();
    }

    private void startDiscoveryServer() {
        try {
            mDatagramSocket = new DatagramSocket(12000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        new Thread(() -> {

            while (true) {
                byte[] buf = new byte[1000];
                DatagramPacket dgp = new DatagramPacket(buf, buf.length);

                try {
                    System.out.println("[DiscoveryServer]: Waiting for data.");
                    mDatagramSocket.receive(dgp);

                    System.out.println("[DiscoveryServer]: Received discovery request.");

                    new Thread(() -> {
                        sendAddress(dgp.getAddress().getHostAddress(), dgp.getPort());
                    }).start();;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendAddress(String clientAddress, int clientPort) {
        byte[] buf = new byte[1000];
        DatagramPacket dgp = new DatagramPacket(buf, buf.length);
        try {
            buf = (InetAddress.getLocalHost().getHostAddress() + ":" + mConfigurationProvider.getPort()).getBytes();

            DatagramSocket socket = new DatagramSocket();

            for (int i = 0; i < 10; i++) {
                System.out.println("[DiscoveryServer]: Sending packet to address: " + dgp.getAddress());
                DatagramPacket packetOut = new DatagramPacket(buf, buf.length, InetAddress.getByName(clientAddress), clientPort);
                socket.send(packetOut);
                Thread.sleep(i * 2000);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
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
        mServerProvider.on(Events.PASS_TURN, PassTurnEventHandler.getHandler());
        mServerProvider.on(Events.ATTACK, AttackEventHandler.getHandler());

        mServerProvider.on(Events.QUIT_GAME, QuitGameEventHandler.getHandler());
        mServerProvider.on(Events.SPECTATE_GAME, SpectateGameEventHandler.getHandler());
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServerProvider.start();
    }
}
