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
import server.configuration.ConfigurationProvider;
import server.core.gameplay.GameStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MatchmakingProviderImpl implements MatchmakingProvider {
    @Inject
    UsersProvider mUsersProvider;

    @Inject
    ConfigurationProvider mConfigurationProvider;

    private Map<String, GameRequest> mPlayerSentRequests = new HashMap<>();
    private Map<String, List<GameRequest>> mPlayerReceivedRequests = new HashMap<>();
    private Map<String, GameStateMachine> mGameMap = new HashMap<>();

    @Override
    public boolean isGameCreated(GameRequest gameRequest) {
        GameServer.User fromUser = mUsersProvider.getUserByUsername(gameRequest.getFromPlayer().getUsername());
        GameServer.User toUser = mUsersProvider.getUserByUsername(gameRequest.getToPlayer().getUsername());

        if (mGameMap.containsKey(fromUser.getClient().getSessionId().toString()) ||
                mGameMap.containsKey(toUser.getClient().getSessionId().toString())) {
            return true;
        }

        return false;
    }

    private void createGame(SocketIOClient client, GameRequest gameRequest) {
        GameState gameState = new GameState(gameRequest.getFromPlayer(), gameRequest.getToPlayer());
        mGameMap.put(client.getSessionId().toString(), new GameStateMachine(gameState, mUsersProvider, mConfigurationProvider));
    }

    @Override
    public void sendPlayRequest(SocketIOClient client, GameRequest gameRequest) {
        Player requestingPlayer = gameRequest.getFromPlayer();
        GameServer.User requestingUser = mUsersProvider.getUserByUsername(requestingPlayer.getUsername());

        Player requestedPlayer = gameRequest.getToPlayer();
        GameServer.User requestedUser = mUsersProvider.getUserByUsername(requestedPlayer.getUsername());

        if (mPlayerSentRequests.containsKey(requestingUser.getClient().getSessionId().toString())) {
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
        requestingUser.getClient().sendEvent(Events.START_GAME, mGameMap.get(requestingUser.getClient().getSessionId().toString()).getGameState());
        //Update the requested players invites.
        requestedUser.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(requestedUser.getClient()));
    }

    private PlayerList getPendingRequestsForClient(SocketIOClient client) {
        PlayerList playerList = new PlayerList();
        List<Player> list = playerList.getPlayers();

        List<GameRequest> requests = mPlayerReceivedRequests.get(client.getSessionId().toString());

        if (requests != null) {
            for (GameRequest req : requests) {
                list.add(req.getFromPlayer());
            }
        }

        return playerList;
    }

    private void updateRequestsOnCreate(GameServer.User requestingUser, GameServer.User requestedUser, GameRequest request) {
        mPlayerSentRequests.put(requestingUser.getClient().getSessionId().toString(), request);
        clearOutboundRequests(requestingUser);
        //Add a pending request to the requested users invites.
        if (!mPlayerReceivedRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            mPlayerReceivedRequests.put(requestedUser.getClient().getSessionId().toString(), new ArrayList<>());
        }
        mPlayerReceivedRequests.get(requestedUser.getClient().getSessionId().toString()).add(request);
    }

    @Override
    public void acceptPlayRequest(SocketIOClient client, GameRequest gameRequest) {
        Player requestingPlayer = gameRequest.getFromPlayer();
        GameServer.User requestingUser = mUsersProvider.getUserByUsername(requestingPlayer.getUsername());

        Player requestedPlayer = gameRequest.getToPlayer();
        GameServer.User requestedUser = mUsersProvider.getUserByUsername(requestedPlayer.getUsername());

        if (!mPlayerSentRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            //TODO: Invite no longer valid.
            return;
        }

        GameStateMachine game = mGameMap.get(requestedUser.getClient().getSessionId().toString());
        mGameMap.put(requestingUser.getClient().getSessionId().toString(), game);

        updateRequestsOnJoin(requestingUser, requestedUser, gameRequest);

        //Send GameState to joined player.
        requestingUser.getClient().sendEvent(Events.START_GAME, game.getGameState());
        game.fire(GameStateMachine.Trigger.PlayersReady);
    }

    private void updateRequestsOnJoin(GameServer.User requestingUser, GameServer.User requestedUser, GameRequest gameRequest) {
        mPlayerSentRequests.remove(requestedUser.getClient().getSessionId().toString());
        mPlayerSentRequests.remove(requestingUser.getClient().getSessionId().toString());
        clearOutboundRequests(requestingUser);
    }

    @Override
    public void spectate(SocketIOClient client, GameRequest gameRequest) {

    }

    private void clearOutboundRequests(GameServer.User user) {
        //If the requesting guy had some pending invites, cancel those.
        if (mPlayerReceivedRequests.containsKey(user.getClient().getSessionId().toString())) {
            List<GameRequest> requests = mPlayerReceivedRequests.get(user.getClient().getSessionId().toString());

            for (GameRequest req : requests) {
                //All the people that are waiting on the requesting guy to join will be kicked back to the lobby.
                GameServer.User u = mUsersProvider.getUserByUsername(req.getFromPlayer().getUsername());
                u.getClient().sendEvent(Events.START_GAME, new GameState());
                u.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(u.getClient()));
            }
        }
    }
}
