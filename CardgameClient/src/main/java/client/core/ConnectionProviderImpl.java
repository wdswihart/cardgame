package client.core;

import client.core.socketio.SocketIOProvider;

import models.Player;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import models.responses.PlayerList;
import util.JSONUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConnectionProviderImpl implements ConnectionProvider {

    private ObjectProperty<Player> mAuthenticatedUserProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Player> mCreatedUserProperty = new SimpleObjectProperty<>();

    private SocketIOProvider mSocketIOProvider;

    private List<Player> mActivePlayers = new ArrayList<>();
    private ObjectProperty<ObservableList<Player>> mActiveUsersProperty = new SimpleObjectProperty<>(FXCollections.observableList(mActivePlayers));

    private List<String> mMessages = new ArrayList<>();
    private ObjectProperty<ObservableList<String>> mMessagesProperty = new SimpleObjectProperty<>(FXCollections.observableList(mMessages));

    // CONSTRUCTORS:

    @Inject
    public ConnectionProviderImpl(SocketIOProvider socketIOProvider) {
        mSocketIOProvider = socketIOProvider;
        mActiveUsersProperty.setValue(new ObservableListWrapper<>(mActivePlayers));


        mSocketIOProvider.getClient().on(Events.LOGIN, params -> {
            Player player = JSONUtils.fromJson(params[0], Player.class);
            player = (player == null) ? new Player() : player;

            System.out.println("[LOGIN] Server notified player: " + player.getUsername() + " " + player.getPassword());
            mAuthenticatedUserProperty.setValue(player);
        });

        mSocketIOProvider.getClient().on(Events.CREATE_ACCOUNT, params -> {
            Player player = JSONUtils.fromJson(params[0], Player.class);
            player = (player == null) ? new Player() : player;

            System.out.println("[CREATE_ACCOUNT] Server notified player: " + player.getUsername() + " " + player.getPassword());
            mCreatedUserProperty.setValue(player);
        });

        mSocketIOProvider.getClient().on(Events.PLAYER_LIST, params -> {
            PlayerList playerList = JSONUtils.fromJson(params[0], PlayerList.class);
            playerList = (playerList == null) ? new PlayerList() : playerList;

            System.out.println("[PLAYER_LIST] PlayerList: " + params[0].toString());
            mActiveUsersProperty.setValue(FXCollections.observableList(playerList.getUsers()));
        });

        mSocketIOProvider.getClient().on(Events.CHAT, params -> {
            String message = params[0].toString();

            System.out.println("[CHAT] Message: " + message);
            mMessagesProperty.getValue().add(message);
        });
    }

    // METHODS:

    @Override
    public ObjectProperty<Player> getAuthenticatedUser() {
        return mAuthenticatedUserProperty;
    }

    @Override
    public ObjectProperty<Player> getCreatedUser() {
        return mCreatedUserProperty;
    }

    @Override
    public ObjectProperty<ObservableList<Player>> getActiveUsers() {
        return mActiveUsersProperty;
    }

    @Override
    public void loginUser(String username, String password) {
        System.out.println("Logging in as [" + username + "] with password [" + password + "]");
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new Player(username, password)));
    }

    @Override
    public void logoutUser() {
        System.out.println("Logging out player: " + mAuthenticatedUserProperty.get().getUsername());
        mSocketIOProvider.getClient().emit(Events.LOGIN, JSONUtils.toJson(new Player()));
    }

    @Override
    public void createAccount(String username, String password) {
        System.out.println("Creating account for: " + username);
        mSocketIOProvider.getClient().emit(Events.CREATE_ACCOUNT, JSONUtils.toJson(new Player(username, password)));
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
