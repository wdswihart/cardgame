package client.core;

import client.core.socketio.SocketIOProvider;

import client.model.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import util.JSONUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConnectionProviderImpl implements ConnectionProvider {

    private ObjectProperty<User> mUserProperty = new SimpleObjectProperty<>();
    private SocketIOProvider mSocketIOProvider;

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
            mUserProperty.setValue(user);
        });
    }

    // METHODS:

    @Override
    public ObjectProperty<User> getCurrentUser() {
        return mUserProperty;
    }

    @Override
    public void loginUser(String username, String password) {
        System.out.println("Logging in as [" + username + "] with password [" + password + "]");
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new User(username, password)));
    }

    @Override
    public void logoutUser() {
        System.out.println("Logging out user: " + mUserProperty.get().getUsername());
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new User()));
    }
}
