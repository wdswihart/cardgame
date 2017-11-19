package server.core.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import models.Events;
import models.Player;
import server.GameServer;
import server.core.socketio.SocketIOServerProvider;
import storage.StorageProvider;
import util.GuiceUtils;

import java.util.HashMap;
import java.util.Map;

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
            mServerProvider.broadcast(Events.PLAYER_LIST, getActiveUsers());
        }
    }

    @Override
    public void removeUser(GameServer.User user) {
        if (mActiveUsers.containsKey(user.getClient().getSessionId().toString())) {
            mActiveUsers.remove(user.getClient().getSessionId().toString());
            mServerProvider.broadcast(Events.PLAYER_LIST, getActiveUsers());
        }
    }
}
