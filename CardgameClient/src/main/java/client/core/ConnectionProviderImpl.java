package client.core;

import client.core.socketio.SocketIOProvider;

import client.model.User;

import javax.inject.Inject;
import java.util.Observable;

public class ConnectionProviderImpl implements ConnectionProvider {
    // FIELDS:

    private Observable mUser = new User("tester", "test");

    @Inject
    private SocketIOProvider mSocketIOProvider;

    // CLASSES:

    public class Events {
        public static final String PLAYER_JOINED = "PlayerJoined";
        public static final String CHAT = "Chat";
        public static final String LOGIN = "Login";
    }

    // CONSTRUCTORS:

    public ConnectionProviderImpl() {
        mSocketIOProvider.getClient().on(Events.LOGIN, params -> {
            User user = (User)params[0];
            user = (user == null) ? new User() : user;

            mUser.notifyObservers(user);
        });
    }

    // METHODS:

    @Override
    public Observable getCurrentUser() {
        return mUser;
    }

    @Override
    public void loginUser(String username, String password) {
        mSocketIOProvider.getClient().emit(Events.LOGIN, new User(username, password));
    }
}
