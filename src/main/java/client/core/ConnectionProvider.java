package client.core;

import models.Player;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public interface ConnectionProvider {
    //Returns an observable Player.
    //Thoughts are if you are notified a valid player, you are authenticated.
    //If you are notified an empty player, that would be logged out if authenticated.
    //If you are notified an empty player and you were not logged in, you failed to validate your credentials.
    ObjectProperty<Player> getAuthenticatedUser();
    ObjectProperty<Player> getCreatedUser();
    ObjectProperty<ObservableList<Player>> getActiveUsers();

    //Attempts to log the player in and notifies via the property returned by getAuthenticatedUser();
    void loginUser(String username, String password);
    void logoutUser();
    void createAccount(String username, String password);

    //Connects to host.
    void connectToHost(String host) throws Exception;

    ObjectProperty<ObservableList<String>> getMessages();
    void sendMessage(String message);
}
