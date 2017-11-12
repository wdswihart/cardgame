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

    private ObjectProperty<User> mAuthenticatedUserProperty = new SimpleObjectProperty<>();
    private ObjectProperty<User> mCreatedUserProperty = new SimpleObjectProperty<>();

    private SocketIOProvider mSocketIOProvider;

    public class Events {
        public static final String PLAYER_JOINED = "PlayerJoined";
        public static final String CHAT = "Chat";
        public static final String LOGIN = "Login";
        public static final String CREATE_ACCOUNT = "CreateAccount";
    }

    // CONSTRUCTORS:

    @Inject
    public ConnectionProviderImpl(SocketIOProvider socketIOProvider) {
        mSocketIOProvider = socketIOProvider;

        mSocketIOProvider.getClient().on(Events.LOGIN, params -> {
            User user = JSONUtils.fromJson(params[0], User.class);
            user = (user == null) ? new User() : user;

            System.out.println("[LOGIN] Server notified user: " + user.getUsername() + " " + user.getPassword());
            mAuthenticatedUserProperty.setValue(user);
        });

        mSocketIOProvider.getClient().on(Events.CREATE_ACCOUNT, params -> {
            User user = JSONUtils.fromJson(params[0], User.class);
            user = (user == null) ? new User() : user;

            System.out.println("[CREATE_ACCOUNT] Server notified user: " + user.getUsername() + " " + user.getPassword());
            mCreatedUserProperty.setValue(user);
        });
    }

    // METHODS:

    @Override
    public ObjectProperty<User> getAuthenticatedUser() {
        return mAuthenticatedUserProperty;
    }

    @Override
    public ObjectProperty<User> getCreatedUser() {
        return mCreatedUserProperty;
    }

    @Override
    public void loginUser(String username, String password) {
        System.out.println("Logging in as [" + username + "] with password [" + password + "]");
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new User(username, password)));
    }

    @Override
    public void logoutUser() {
        System.out.println("Logging out user: " + mAuthenticatedUserProperty.get().getUsername());
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new User()));
    }

    @Override
    public void createAccount(String username, String password) {
        System.out.println("Creating account for: " + username);
        mSocketIOProvider.getClient().emit(Events.CREATE_ACCOUNT, JSONUtils.toJson(new User(username, password)));
    }
}
