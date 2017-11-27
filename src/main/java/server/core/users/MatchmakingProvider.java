package server.core.users;

import com.corundumstudio.socketio.SocketIOClient;
import models.requests.GameRequest;

public interface MatchmakingProvider {
    boolean isGameCreated(GameRequest gameRequest);

    void sendPlayRequest(SocketIOClient client, GameRequest gameRequest);

    void acceptPlayRequest(SocketIOClient client, GameRequest gameRequest);

    void spectate(SocketIOClient client, GameRequest gameRequest);
}
