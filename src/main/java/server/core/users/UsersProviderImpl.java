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
public class UsersProviderImpl implements UsersProvider {
    @Inject
    private StorageProvider mStorageProvider;

    @Inject
    private SocketIOServerProvider mServerProvider;

    private Map<String, GameServer.User> mUsers = new HashMap<>();

    @Override
    public Map<String, GameServer.User> getUsers() {
        return mUsers;
    }

    @Override
    public GameServer.User getUserByUsername(String username) {
        for (Map.Entry<String, GameServer.User> e : mUsers.entrySet()) {
            if (e.getValue().getPlayer().getUsername().equals(username)) {
                return e.getValue();
            }
        }
        return null;
    }

    @Override
    public void addUser(GameServer.User user) {
        if (!mUsers.containsKey(user.getClient().getSessionId().toString())) {
            Player playerWithoutPass = new Player(user.getPlayer().getUsername(), "");

            mUsers.put(user.getClient().getSessionId().toString(), new GameServer.User(user.getClient(), playerWithoutPass));
            broadcastPlayerList();
        }
    }

    @Override
    public void removeUser(GameServer.User user) {
        if (mUsers.containsKey(user.getClient().getSessionId().toString())) {
            mUsers.remove(user.getClient().getSessionId().toString());
            broadcastPlayerList();
        }
    }

    private void broadcastPlayerList() {
        //Create PlayerList response and send.
        PlayerList playerList = new PlayerList();

        //Get list of players from list of active users.
        List<Player> players = getUsers().entrySet().stream().map(x -> x.getValue().getPlayer()).collect(Collectors.toList());

        playerList.getPlayers().addAll(players);
        mServerProvider.broadcast(Events.PLAYER_LIST, playerList);
    }
}
