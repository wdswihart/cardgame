package client.core;

import client.core.socketio.SocketIOClientProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import models.Player;
import models.requests.GameRequest;
import models.responses.GameState;
import models.responses.PlayerList;
import util.JSONUtils;

@Singleton
public class GameProviderImpl implements GameProvider {
    private SocketIOClientProvider mClientProvider;
    private ConnectionProvider mConnectionProvider;

    Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();
    Property<ObservableList<Player>> mPendingInvitesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    @Inject
    public GameProviderImpl(SocketIOClientProvider clientProvider, ConnectionProvider connectionProvider) {
        mClientProvider = clientProvider;
        mConnectionProvider = connectionProvider;

        mClientProvider.getClient().on(Events.START_GAME, params -> {
            System.out.println("[START_GAME] Starting game: " + params[0]);

            GameState game = JSONUtils.fromJson(params[0], GameState.class);
            game = (game == null) ? new GameState() : game;
            mGameStateProperty.setValue(game);
        });

        mClientProvider.getClient().on(Events.INVITE_REQUEST, params -> {
            System.out.println("[INVITES] Invites: " + params[0]);

            PlayerList invites = JSONUtils.fromJson(params[0], PlayerList.class);
            invites = (invites == null) ? new PlayerList() : invites;
            mPendingInvitesProperty.setValue(FXCollections.observableList(invites.getPlayers()));
        });

        mClientProvider.getClient().on(Events.UPDATE_GAME, params -> {
            System.out.println("[UPDATE_GAME] GameState: " + params[0]);

            GameState game = JSONUtils.fromJson(params[0], GameState.class);
            game = (game == null) ? new GameState() : game;
            mGameStateProperty.setValue(game);
        });
    }

    @Override
    public Property<GameState> getGameStateProperty() {
        return mGameStateProperty;
    }

    @Override
    public Property<ObservableList<Player>> getPendingInvitesProperty() {
        return mPendingInvitesProperty;
    }

    @Override
    public void joinGame(Player player) {
        GameRequest gameRequest = new GameRequest();
        gameRequest.setFromPlayer(mConnectionProvider.getAuthenticatedUser().get());
        gameRequest.setToPlayer(player);

        mClientProvider.getClient().emit(Events.START_GAME, JSONUtils.toJson(gameRequest));
    }

    @Override
    public void leaveGame() {
    }
}
