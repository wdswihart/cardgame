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
import models.requests.*;
import models.responses.GameState;
import models.responses.GameStateList;
import models.responses.PlayerList;
import util.JSONUtils;

import java.util.List;

@Singleton
public class GameProviderImpl implements GameProvider {
    private SocketIOClientProvider mClientProvider;
    private ConnectionProvider mConnectionProvider;

    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>(new GameState());
    private Property<ObservableList<Player>> mPendingInvitesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private Property<ObservableList<GameState>> mActiveGamesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private Property<ObservableList<String>> mGameMessagesProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());

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

        mClientProvider.getClient().on(Events.GAME_CHAT, params -> {
            System.out.println("[GAME_CHAT]: " + params[0]);
            mGameMessagesProperty.getValue().add(params[0].toString());
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

    @Override
    public void spectateGame(GameState targetGame) {
        mClientProvider.getClient().emit(Events.SPECTATE_GAME, JSONUtils.toJson(targetGame));
    }

    @Override
    public void defend(DefendRequest request) {
        mClientProvider.getClient().emit(Events.DAMAGE, JSONUtils.toJson(request));
    }

    @Override
    public Property<ObservableList<String>> getGameMessages() {
        return mGameMessagesProperty;
    }

    @Override
    public void sendChat(String message) {
        mClientProvider.getClient().emit(Events.GAME_CHAT, message);
    }
}
