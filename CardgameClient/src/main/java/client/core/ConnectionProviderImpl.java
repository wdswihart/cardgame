package client.core;

import client.core.socketio.SocketIOProvider;

import client.model.User;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import transportmodels.UserList;
import util.JSONUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConnectionProviderImpl implements ConnectionProvider {

    private ObjectProperty<User> mAuthenticatedUserProperty = new SimpleObjectProperty<>();
    private ObjectProperty<User> mCreatedUserProperty = new SimpleObjectProperty<>();

    private SocketIOProvider mSocketIOProvider;

    private List<User> mActiveUsers = new ArrayList<>();
    private ObjectProperty<ObservableList<User>> mActiveUsersProperty = new SimpleObjectProperty<>(FXCollections.observableList(mActiveUsers));

    private List<String> mMessages = new ArrayList<>();
    private ObjectProperty<ObservableList<String>> mMessagesProperty = new SimpleObjectProperty<>(FXCollections.observableList(mMessages));

    // CONSTRUCTORS:

    @Inject
    public ConnectionProviderImpl(SocketIOProvider socketIOProvider) {
        mSocketIOProvider = socketIOProvider;
        mActiveUsersProperty.setValue(new ObservableListWrapper<>(mActiveUsers));


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

        mSocketIOProvider.getClient().on(Events.PLAYER_LIST, params -> {
            UserList userList = JSONUtils.fromJson(params[0], UserList.class);
            userList = (userList == null) ? new UserList() : userList;

            System.out.println("[PLAYER_LIST] UserList: " + params[0].toString());
            mActiveUsersProperty.setValue(FXCollections.observableList(userList.getUsers()));
        });

        mSocketIOProvider.getClient().on(Events.CHAT, params -> {
            String message = params[0].toString();

            System.out.println("[CHAT] Message: " + message);
            mMessagesProperty.getValue().add(message);
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
    public ObjectProperty<ObservableList<User>> getActiveUsers() {
        return mActiveUsersProperty;
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

    @Override
    public void connectToHost(String host) throws Exception {
        mSocketIOProvider.createNewClient(host);
    }

    @Override
    public ObjectProperty<ObservableList<String>> getMessages() {
        return mMessagesProperty;
    }

    @Override
    public void sendMessage(String message) {
        mSocketIOProvider.getClient().emit(Events.CHAT, message);
    }
}
