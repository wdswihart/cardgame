package server.core.users;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Events;
import models.Player;
import models.requests.GameRequest;
import models.responses.GameState;
import server.GameServer;
import server.core.socketio.SocketIOServerProvider;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class MatchmakingProviderImpl implements MatchmakingProvider {
    @Inject
    ActiveUserProvider mActiveUserProvider;

    private Map<String, GameRequest> mPendingRequests = new HashMap<>();
    private Map<String, GameState> mGameMap = new HashMap<>();

    @Override
    public boolean isGameCreated(GameRequest gameRequest) {
        GameServer.User fromUser = mActiveUserProvider.getUserByUsername(gameRequest.getFromPlayer().getUsername());
        GameServer.User toUser = mActiveUserProvider.getUserByUsername(gameRequest.getToPlayer().getUsername());

        if (mGameMap.containsKey(fromUser.getClient().getSessionId().toString()) ||
                mGameMap.containsKey(toUser.getClient().getSessionId().toString())) {
            return true;
        }

        return false;
    }

    private void createGame(SocketIOClient client, GameRequest gameRequest) {
        mGameMap.put(client.getSessionId().toString(), new GameState(gameRequest.getFromPlayer(), gameRequest.getToPlayer()));
    }

    @Override
    public void sendPlayRequest(SocketIOClient client, GameRequest gameRequest) {
        Player requestingPlayer = gameRequest.getFromPlayer();
        GameServer.User requestingUser = mActiveUserProvider.getUserByUsername(requestingPlayer.getUsername());

        Player requestedPlayer = gameRequest.getToPlayer();
        GameServer.User requestedUser = mActiveUserProvider.getUserByUsername(requestedPlayer.getUsername());

        if (mPendingRequests.containsKey(requestingUser.getClient().getSessionId().toString())) {
            //TODO: Already have your one request out.
            return;
        }

        if (mGameMap.containsKey(requestedUser.getClient().getSessionId().toString())) {
            //TODO: Error player already in game.
            return;
        }

        createGame(client, gameRequest);
        mPendingRequests.put(requestingUser.getClient().getSessionId().toString(), gameRequest);
        //Send GameState to joined player.
        requestingUser.getClient().sendEvent(Events.START_GAME, mGameMap.get(requestingUser.getClient().getSessionId().toString()));
    }

    @Override
    public void acceptPlayRequest(SocketIOClient client, GameRequest gameRequest) {
        Player requestingPlayer = gameRequest.getFromPlayer();
        GameServer.User requestingUser = mActiveUserProvider.getUserByUsername(requestingPlayer.getUsername());

        Player requestedPlayer = gameRequest.getToPlayer();
        GameServer.User requestedUser = mActiveUserProvider.getUserByUsername(requestedPlayer.getUsername());

        if (!mPendingRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            //TODO: Invite no longer valid.
            return;
        }

        GameState game = mGameMap.get(requestedUser.getClient().getSessionId().toString());
        mGameMap.put(requestingUser.getClient().getSessionId().toString(), game);
        //Send GameState to joined player.
        requestingUser.getClient().sendEvent(Events.START_GAME, game);

        mPendingRequests.remove(requestedUser.getClient().getSessionId().toString());
        mPendingRequests.remove(requestingUser.getClient().getSessionId().toString());
    }

    @Override
    public void spectate(SocketIOClient client, GameRequest gameRequest) {

    }
}
