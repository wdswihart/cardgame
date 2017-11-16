package storage;

import client.model.User;

import java.util.List;
import java.util.Map;

public interface StorageProvider {
    enum Status {
        Ok,
        Error
    }

    Map<String, User> getRegisteredUsers();
    Status addRegisteredUser(User user);
}
