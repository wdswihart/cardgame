package server.core.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Events;
import models.Player;
import models.responses.PlayerList;
import server.GameServer;
import server.core.socketio.SocketIOServerProvider;
import storage.StorageProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ActiveUserProviderImpl implements ActiveUserProvider {
    @Inject
    private StorageProvider mStorageProvider;

    @Inject
    private SocketIOServerProvider mServerProvider;

    private Map<String, GameServer.User> mActiveUsers = new HashMap<>();

    @Override
    public Map<String, GameServer.User> getActiveUsers() {
        return mActiveUsers;
    }

    @Override
    public void addUser(GameServer.User user) {
        if (!mActiveUsers.containsKey(user.getClient().getSessionId().toString())) {
            Player playerWithoutPass = new Player(user.getPlayer().getUsername(), "");

            mActiveUsers.put(user.getClient().getSessionId().toString(), new GameServer.User(user.getClient(), playerWithoutPass));
            broadcastPlayerList();
        }
    }

    @Override
    public void removeUser(GameServer.User user) {
        if (mActiveUsers.containsKey(user.getClient().getSessionId().toString())) {
            mActiveUsers.remove(user.getClient().getSessionId().toString());
            broadcastPlayerList();
        }
    }

    private void broadcastPlayerList() {
        //Create PlayerList response and send.
        PlayerList playerList = new PlayerList();

        //Get list of players from list of active users.
        List<Player> players = getActiveUsers().entrySet().stream().map(x -> x.getValue().getPlayer()).collect(Collectors.toList());

        playerList.getPlayers().addAll(players);
        mServerProvider.broadcast(Events.PLAYER_LIST, playerList);
    }
}
