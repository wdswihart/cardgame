package server;

import client.model.User;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.netty.channel.ChannelHandlerContext;
import org.w3c.dom.events.Event;
import util.JSONUtils;

import java.beans.ExceptionListener;
import java.io.IOException;
import java.util.*;

public class GameServer {
    // FIELDS:

    private SocketIOServer mServer;
    private List<User> mUsers = new ArrayList<>();

    private Map<String, client.model.User> mRegisteredUsers = new HashMap();

    // CONSTRUCTORS:

    public GameServer(String hostname, int port) {
        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);

        mServer = new SocketIOServer(config);
        setEventListeners();
    }

    @Override
    public void finalize() {
        mServer.stop();
    }

    // CLASSES:

    public class Events {
        public static final String LOGIN = "Login";
        public static final String CREATE_ACCOUNT = "CreateAccount";
        public static final String PLAYER_JOINED = "PlayerJoined";

        public static final String START_GAME_CLIENT = "StartGameClient";
        public static final String START_GAME_SERVER = "StartGameServer";

        public static final String REFRESH_SERVER = "RefreshServer";
        public static final String MAINTAIN_SERVER = "MaintainServer";
        public static final String DRAW_SERVER = "DrawServer";

        public static final String MAIN_SERVER = "MainServer";

        public static final String ATTACK_CLIENT = "AttackClient";
        public static final String DEFEND_CLIENT = "DefendClient";
        public static final String DAMAGE_SERVER = "DamageClient";

        public static final String PLAY_CLIENT = "PlayClient";

        public static final String RESPOND_CLIENT = "RespondClient";
        public static final String RESPOND_SERVER = "RespondServer";
    }

    public class User {
        // FIELDS:
        public SocketIOClient client;

        // CONSTRUCTORS:
        public User(SocketIOClient client) {
            this.client = client;
        }

    }

    // METHODS:

    // setEventListeners sets the listeners for the SocketIO server.
    private void setEventListeners() {
        mServer.addConnectListener(client -> {
            System.out.println("[DEBUG] Client connected to server");

            User user = new User(client);
            mUsers.add(user);

            client.joinRoom("lobby");

            mServer.getRoomOperations("lobby").sendEvent(Events.PLAYER_JOINED, client.getRemoteAddress() + " joined the lobby.");
        });

        mServer.addDisconnectListener(client -> {
            System.out.print("[DEBUG] Client disconnected.");
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

        if (!mRegisteredUsers.containsKey(user.getUsername()) ||
                !mRegisteredUsers.get(user.getUsername()).getPassword().equals(user.getPassword())) {
            client.sendEvent(Events.LOGIN, new client.model.User());
            return;
        }

        client.sendEvent(Events.LOGIN, user);
    }

    private void handleCreateAccountEvent(SocketIOClient client, String data, AckRequest ack) {
        client.model.User user = JSONUtils.fromJson(data, client.model.User.class);

        if (mRegisteredUsers.containsKey(user.getUsername())) {
            //Error existing user.
            client.sendEvent(Events.CREATE_ACCOUNT, new client.model.User());
            return;
        }

        mRegisteredUsers.put(user.getUsername(), user);
        client.sendEvent(Events.CREATE_ACCOUNT, user);
    }
}
