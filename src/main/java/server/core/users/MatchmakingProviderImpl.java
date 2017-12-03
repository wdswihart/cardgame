package server.core.users;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.collections.*;
import models.Events;
import models.Player;
import models.requests.GameRequest;
import models.responses.GameState;
import models.responses.GameStateList;
import models.responses.PlayerList;
import server.GameServer;
import server.configuration.ConfigurationProvider;
import server.core.gameplay.GameStateMachine;
import server.core.socketio.SocketIOServerProvider;
import util.JSONUtils;

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

    @Inject
    SocketIOServerProvider mServerProvider;

    public MatchmakingProviderImpl() {
        mGameMap.addListener(new MapChangeListener<String, GameStateMachine>() {
            @Override
            public void onChanged(Change<? extends String, ? extends GameStateMachine> change) {
                notifyActiveGames();
            }
        });

        mPlayerReceivedRequests.addListener(new MapChangeListener<String, List<GameRequest>>() {
            @Override
            public void onChanged(Change<? extends String, ? extends List<GameRequest>> change) {
                GameServer.User user = mUsersProvider.getUsers().get(change.getKey());
                mPlayerReceivedRequests.get(change.getKey()).addListener(new ListChangeListener<GameRequest>() {
                    @Override
                    public void onChanged(Change<? extends GameRequest> c) {
                        user.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(change.getKey()));
                    }
                });
                user.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(change.getKey()));
            }
        });
    }

    private Map<String, GameRequest> mPlayerSentRequests = new HashMap<>();
    private ObservableMap<String, ObservableList<GameRequest>> mPlayerReceivedRequests = FXCollections.observableHashMap();
    private ObservableMap<String, GameStateMachine> mGameMap = FXCollections.observableHashMap();

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

    @Override
    public GameStateMachine getGameStateMachine(String userKey) {
        return mGameMap.get(userKey);
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
        requestedUser.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(requestedUser.getClient().getSessionId().toString()));
    }

    private PlayerList getPendingRequestsForClient(String clientId) {
        PlayerList playerList = new PlayerList();
        List<Player> list = playerList.getPlayers();

        List<GameRequest> requests = mPlayerReceivedRequests.get(clientId);

        if (requests != null) {
            for (GameRequest req : requests) {
                list.add(req.getFromPlayer());
            }
        }

        return playerList;
    }

    private void updateRequestsOnCreate(GameServer.User requestingUser, GameServer.User requestedUser, GameRequest request) {
        mPlayerSentRequests.put(requestingUser.getClient().getSessionId().toString(), request);
        clearReceivedRequests(requestingUser);
        //Add a pending request to the requested users invites.
        if (!mPlayerReceivedRequests.containsKey(requestedUser.getClient().getSessionId().toString())) {
            mPlayerReceivedRequests.put(requestedUser.getClient().getSessionId().toString(), FXCollections.observableArrayList());
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
        clearReceivedRequests(requestingUser);
    }

    @Override
    public void spectate(SocketIOClient client, GameRequest gameRequest) {
        GameServer.User user = mUsersProvider.getUsers().get(client.getSessionId().toString());
        GameServer.User playerOne = mUsersProvider.getUserByUsername(gameRequest.getToPlayer().getUsername());
        GameServer.User playerTwo = mUsersProvider.getUserByUsername(gameRequest.getFromPlayer().getUsername());


        if ((mGameMap.get(playerOne.getClient().getSessionId().toString()) == null ||
                mGameMap.get(playerTwo.getClient().getSessionId().toString()) == null) &&
                mGameMap.get(user.getClient().getSessionId().toString()) != null) {
            //Error, game not found or already in a game.
            return;
        }

        GameStateMachine gsm = mGameMap.get(playerOne.getClient().getSessionId().toString());
        gsm = (gsm == null) ? mGameMap.get(playerTwo.getClient().getSessionId().toString()) : gsm;

        clearReceivedRequests(user);
        gsm.addSpectator(user);
        mGameMap.put(client.getSessionId().toString(), gsm);
    }

    @Override
    public List<GameState> getActiveGames() {
        HashMap<String, GameState> activeGames = new HashMap<>();
        List<GameState> activeGameList = new ArrayList<>();

        for (Map.Entry<String, GameStateMachine> entry : mGameMap.entrySet()) {
            if (!activeGames.containsKey(entry.getKey())) {
                GameServer.User userOne = mUsersProvider.getUserByUsername(entry.getValue().getGameState().getPlayerOne().getUsername());
                GameServer.User userTwo = mUsersProvider.getUserByUsername(entry.getValue().getGameState().getPlayerTwo().getUsername());

                activeGames.put(userOne.getClient().getSessionId().toString(), new GameState());
                activeGames.put(userTwo.getClient().getSessionId().toString(), new GameState());

                for (Player player : entry.getValue().getGameState().getSpectatorList()) {
                    GameServer.User user = mUsersProvider.getUserByUsername(player.getUsername());

                    if (user == null) {
                        continue;
                    }

                    activeGames.put(user.getClient().getSessionId().toString(), new GameState());
                }

                activeGameList.add(entry.getValue().getGameState());
            }
        }

        return activeGameList;
    }

    @Override
    public void quitGame(SocketIOClient client) {
        GameStateMachine gsm = mGameMap.get(client.getSessionId().toString());

        if (gsm == null) {
            return;
        }

        GameServer.User user = mUsersProvider.getUsers().get(client.getSessionId().toString());
        gsm.removeUser(user);
        mGameMap.remove(client.getSessionId().toString());
    }

    private void notifyActiveGames() {
        mServerProvider.broadcast(Events.ACTIVE_GAMES, JSONUtils.toJson(new GameStateList(getActiveGames())));
    }


    private void clearReceivedRequests(GameServer.User user) {
        //If the requesting guy had some pending invites, cancel those.
        if (mPlayerReceivedRequests.containsKey(user.getClient().getSessionId().toString())) {
            List<GameRequest> requests = mPlayerReceivedRequests.get(user.getClient().getSessionId().toString());

            for (GameRequest req : requests) {
                //All the people that are waiting on the requesting guy to join will be kicked back to the lobby.
                GameServer.User u = mUsersProvider.getUserByUsername(req.getFromPlayer().getUsername());

                //We don't need to send this to the player we are starting the game with.
                if (!user.getPlayer().getUsername().equals(req.getToPlayer().getUsername())) {
                    u.getClient().sendEvent(Events.START_GAME, new GameState());
                    u.getClient().sendEvent(Events.INVITE_REQUEST, getPendingRequestsForClient(u.getClient().getSessionId().toString()));
                }
            }

            mPlayerReceivedRequests.put(user.getClient().getSessionId().toString(), FXCollections.observableArrayList());
        }
    }
}
