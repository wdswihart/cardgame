package server.core.users;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Events;
import models.Player;
import models.requests.GameRequest;
import models.responses.GameState;
import models.responses.PlayerList;
import server.GameServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MatchmakingProviderImpl implements MatchmakingProvider {
    @Inject
    ActiveUserProvider mActiveUserProvider;

    private Map<String, GameRequest> mInboundRequests = new HashMap<>();
    private Map<String, List<GameRequest>> mOutboundRequests = new HashMap<>();
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

        if (mInboundRequests.containsKey(requestingUser.getClient().getSessionId().toString())) {
            //TODO: Already have your one request out.
            return;
        }

        if (mGameMap.containsKey(requestedUser.getClient().getSessionId().toString())) {
            //TODO: Error player already in game.
            return;
        }

        createGame(client, gameRequest);
        updateRequestsOnCreate(requestingUser, requestedUser, gameRequest);

        //Send GameState to joined player.
        requestingUser.getClient().sendEvent(Events.START_GAME, mGameMap.get(requestingUser.getClient().getSessionId().toString()));
        //Update the requested players invites.
        requestedUser.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(requestedUser.getClient()));
    }

    private PlayerList getPendingRequestsForClient(SocketIOClient client) {
        PlayerList playerList = new PlayerList();
        List<Player> list = playerList.getPlayers();

        List<GameRequest> requests = mOutboundRequests.get(client.getSessionId().toString());

        if (requests != null) {
            for (GameRequest req : requests) {
                list.add(req.getFromPlayer());
            }
        }

        return playerList;
    }

    private void updateRequestsOnCreate(GameServer.User requestingUser, GameServer.User requestedUser, GameRequest request) {
        mInboundRequests.put(requestingUser.getClient().getSessionId().toString(), request);
        clearOutboundRequests(requestingUser);
        //Add a pending request to the requested users invites.
        if (!mOutboundRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            mOutboundRequests.put(requestedUser.getClient().getSessionId().toString(), new ArrayList<>());
        }
        mOutboundRequests.get(requestedUser.getClient().getSessionId().toString()).add(request);
    }

    @Override
    public void acceptPlayRequest(SocketIOClient client, GameRequest gameRequest) {
        Player requestingPlayer = gameRequest.getFromPlayer();
        GameServer.User requestingUser = mActiveUserProvider.getUserByUsername(requestingPlayer.getUsername());

        Player requestedPlayer = gameRequest.getToPlayer();
        GameServer.User requestedUser = mActiveUserProvider.getUserByUsername(requestedPlayer.getUsername());

        if (!mInboundRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            //TODO: Invite no longer valid.
            return;
        }

        GameState game = mGameMap.get(requestedUser.getClient().getSessionId().toString());
        mGameMap.put(requestingUser.getClient().getSessionId().toString(), game);

        updateRequestsOnJoin(requestingUser, requestedUser, gameRequest);

        //Send GameState to joined player.
        requestingUser.getClient().sendEvent(Events.START_GAME, game);
    }

    private void updateRequestsOnJoin(GameServer.User requestingUser, GameServer.User requestedUser, GameRequest gameRequest) {
        mInboundRequests.remove(requestedUser.getClient().getSessionId().toString());
        mInboundRequests.remove(requestingUser.getClient().getSessionId().toString());
        clearOutboundRequests(requestingUser);
    }

    @Override
    public void spectate(SocketIOClient client, GameRequest gameRequest) {

    }

    private void clearOutboundRequests(GameServer.User user) {
        //If the requesting guy had some pending invites, cancel those.
        if (mOutboundRequests.containsKey(user.getClient().getSessionId().toString())) {
            List<GameRequest> requests = mOutboundRequests.get(user.getClient().getSessionId().toString());

            for (GameRequest req : requests) {
                //All the people that are waiting on the requesting guy to join will be kicked back to the lobby.
                GameServer.User u = mActiveUserProvider.getUserByUsername(req.getFromPlayer().getUsername());
                u.getClient().sendEvent(Events.START_GAME, new GameState());
                u.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(u.getClient()));
            }
        }
    }
}
