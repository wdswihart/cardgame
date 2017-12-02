package client.core;

import client.core.socketio.SocketIOClientProvider;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Card;
import models.Events;
import models.Player;
import models.requests.AttackRequest;
import models.requests.DrawRequest;
import models.requests.GameRequest;
import models.requests.PlayCardRequest;
import models.responses.GameState;
import models.responses.GameStateList;
import models.responses.PlayerList;
import util.JSONUtils;

import java.util.List;

@Singleton
public class GameProviderImpl implements GameProvider {
    private SocketIOClientProvider mClientProvider;
    private ConnectionProvider mConnectionProvider;

    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();
    private Property<ObservableList<Player>> mPendingInvitesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private Property<ObservableList<GameState>> mActiveGamesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    @Inject
    public GameProviderImpl(SocketIOClientProvider clientProvider, ConnectionProvider connectionProvider) {
        mClientProvider = clientProvider;
        mConnectionProvider = connectionProvider;

        mClientProvider.getClient().on(Events.START_GAME, params -> {
            System.out.println("[START_GAME] Starting game: " + params[0]);

            GameState game = JSONUtils.fromJson(params[0], GameState.class);
            game = (game == null) ? new GameState() : game;

            synchronized (this) {
                mGameStateProperty.setValue(game);
            }
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

            synchronized (this) {
                mGameStateProperty.setValue(game);
            }
        });

        mClientProvider.getClient().on(Events.ACTIVE_GAMES, params -> {
            System.out.println("[ACTIVE_GAMES]: " + params[0]);

            GameStateList list = JSONUtils.fromJson(params[0], GameStateList.class);
            list = (list == null) ? new GameStateList() : list;

            mActiveGamesProperty.setValue(FXCollections.observableArrayList(list.getGameStates()));
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

    @Override
    public void drawCard() {
        mClientProvider.getClient().emit(Events.DRAW, JSONUtils.toJson(new DrawRequest()));
    }

    @Override
    public void playCard(Card card) {
        mClientProvider.getClient().emit(Events.PLAY_CARD, JSONUtils.toJson(new PlayCardRequest(card)));
    }

    @Override
    public void passTurn() {
        mClientProvider.getClient().emit(Events.PASS_TURN, "string");
    }

    @Override
    public void attack(List<Card> attackers) {
        mClientProvider.getClient().emit(Events.ATTACK, JSONUtils.toJson(new AttackRequest(attackers)));
    }

    @Override
    public void quitGame() {
        mClientProvider.getClient().emit(Events.QUIT_GAME, "string");
    }

    @Override
    public Property<ObservableList<GameState>> getActiveGames() {
        return mActiveGamesProperty;
    }
}
