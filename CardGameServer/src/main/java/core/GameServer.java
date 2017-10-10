package core;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private SocketIOServer mServer;
    private List<User> mUsers = new ArrayList<User>();

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
        public SocketIOClient client;

        public User(SocketIOClient client) {
            this.client = client;
        }

        public void shout() {
            System.out.println("I'm gay!");
        }
    }

    public GameServer(String hostname, int port) {
        Configuration config = new Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        mServer = new SocketIOServer(config);
    }

    public void startServer() {
        setEventListeners();

        mServer.start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mServer.stop();
    }

    private void setEventListeners() {
        mServer.addConnectListener(client -> {
            System.out.println("[DEBUG] Client connected to server");
        });

        mServer.addEventListener(Events.START_GAME_CLIENT, String.class, (client, data, ackRequest) -> {
            mUsers.add(new User(client));
        });
    }
}
