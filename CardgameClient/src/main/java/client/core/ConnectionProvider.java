package client.core;

import java.util.Observable;

public interface ConnectionProvider {
    //Returns an observable User.
    //Thoughts are if you are notified a valid user, you are authenticated.
    //If you are notified an empty user, that would be logged out if authenticated.
    //If you are notified an empty user and you were not logged in, you failed to validate your credentials.
    Observable getCurrentUser();

    //Attempts to log the user in and notifies via the property returned by getCurrentUser();
    void loginUser(String username, String password);

    /*
    //Messages
    //You can get an observable String array that contains messages.
    //When a message is added, we will be notified.
    Observable<StringArray> getLobbyMessages();
    Observable<StringArray> getGameMessages();
    Observable<StringArray> getMessages(String room);

    //Returns a valid game if you successfully joined.
    //If you're the first or the invited user, you will actually play the game.
    //Any additional users are spectators.
    Observable<IGameProvider> joinGame(User user);
    */
}
