package client.core;

import client.core.socketio.SocketIOProvider;

import client.model.User;
import util.JSONUtils;

import javax.inject.Inject;
import java.util.Observable;

public class ConnectionProviderImpl implements ConnectionProvider {
    // FIELDS:

    private Observable mUser = new User("tester", "test");

    private SocketIOProvider mSocketIOProvider;

    // CLASSES:

    public class Events {
        public static final String PLAYER_JOINED = "PlayerJoined";
        public static final String CHAT = "Chat";
        public static final String LOGIN = "Login";
    }

    // CONSTRUCTORS:

    @Inject
    public ConnectionProviderImpl(SocketIOProvider socketIOProvider) {
        mSocketIOProvider = socketIOProvider;
        mSocketIOProvider.getClient().on(Events.LOGIN, params -> {
            User user = JSONUtils.fromJson(params[0], User.class);
            System.out.println("[LOGIN] Server notified user: " + user.getUsername() + " " + user.getPassword());
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
        System.out.println("Logging in as [" + username + "] with password [" + password + "]");
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new User(username, password)));
    }
}
