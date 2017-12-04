package server.core.users;

import com.corundumstudio.socketio.SocketIOClient;
import models.requests.GameRequest;
import models.responses.GameState;
import server.GameServer;
import server.core.gameplay.GameStateMachine;

import java.util.List;

public interface MatchmakingProvider {
    boolean isGameCreated(GameRequest gameRequest);

    //TODO: This is wrong, but can be fixed later.
    GameStateMachine getGameStateMachine(String userKey);

    void sendPlayRequest(SocketIOClient client, GameRequest gameRequest);

    void acceptPlayRequest(SocketIOClient client, GameRequest gameRequest);

    void spectate(SocketIOClient client, GameRequest gameRequest);

    List<GameState> getActiveGames();

    void quitGame(SocketIOClient client);
}
