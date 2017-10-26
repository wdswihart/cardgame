package core;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import io.netty.channel.ChannelHandlerContext;
import org.w3c.dom.events.Event;

import java.beans.ExceptionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class GameServer {
    // FIELDS:

    private SocketIOServer mServer;
    private List<User> mUsers = new ArrayList<>();

    // CONSTRUCTORS:

    public GameServer(String hostname, int port) {
        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);

        mServer = new SocketIOServer(config);
        setEventListeners();
    }

    // CLASSES:

    public class Events {
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

        // METHODS:

        public void shout() {
            System.out.println("Hooray!");
        }
    }

    // METHODS:

    // setEventListeners sets the listeners for the SocketIO server.
    private void setEventListeners() {
        mServer.addConnectListener(client -> {
            System.out.println("[DEBUG] Client connected to server");
        });

        mServer.addDisconnectListener(client -> {
            System.out.print("[DEBUG] Client disconnected.");
        });

        mServer.addListeners(new com.corundumstudio.socketio.listener.ExceptionListener() {
            @Override
            public void onEventException(Exception e, List<Object> args, SocketIOClient client) {

            }

            @Override
            public void onDisconnectException(Exception e, SocketIOClient client) {
                System.err.println("[ERROR] Connection forcibly closed by remote host.");
            }

            @Override
            public void onConnectException(Exception e, SocketIOClient client) {

            }

            @Override
            public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {
                return false;
            }
        });

        mServer.addEventListener(Events.START_GAME_CLIENT, String.class, (client, data, ackRequest) -> {
            mUsers.add(new User(client));
        });
    }

    // startServer starts up the SocketIO server.
    public void startServer() {
        mServer.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mServer.stop();
    }

    // MAIN:

    public static void main(String[] args) {
        (new GameServer("127.0.0.1", 8300)).startServer();
    }
}
