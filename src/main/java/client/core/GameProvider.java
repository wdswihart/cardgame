package client.core;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import models.Card;
import models.Player;
import models.responses.GameState;

import java.util.List;

public interface GameProvider {
    Property<GameState> getGameStateProperty();
    Property<ObservableList<Player>> getPendingInvitesProperty();

    void joinGame(Player player);
    void leaveGame();

    void drawCard();

    void playCard(Card card);

    void passTurn();

    void attack(List<Card> attackers);

    void quitGame();

    Property<ObservableList<GameState>> getActiveGames();
}
