package server.core.users;

import server.GameServer;

import java.util.Map;

public interface UsersProvider {
    Map<String, GameServer.User> getUsers();
    GameServer.User getUserByUsername(String username);
    void addUser(GameServer.User user);
    void removeUser(GameServer.User user);
}
