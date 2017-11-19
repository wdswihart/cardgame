package server.core.users;

import server.GameServer;

import java.util.Map;

public interface ActiveUserProvider {
    Map<String, GameServer.User> getActiveUsers();
    void addUser(GameServer.User user);
    void removeUser(GameServer.User user);
}
