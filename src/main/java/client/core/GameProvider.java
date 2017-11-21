package client.core;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import models.Player;
import models.responses.GameState;

public interface GameProvider {
    Property<GameState> getGameStateProperty();
    Property<ObservableList<Player>> getPendingInvitesProperty();

    void joinGame(Player player);
    void leaveGame();
}
