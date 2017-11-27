package storage;

import models.Player;

import java.util.Map;

public interface StorageProvider {
    enum Status {
        Ok,
        Error
    }

    Map<String, Player> getRegisteredUsers();
    Status addRegisteredUser(Player player);
}
