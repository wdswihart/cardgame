package client.ui.game;

import client.core.GameProvider;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import models.Card;
import models.responses.GameState;

public class GameViewModel implements ViewModel {
    private GameProvider mGameProvider;

    private Property<ObservableList<Card>> mPlayerHandProperty = new SimpleObjectProperty<>();
    private Property<ObservableList<Card>> mOpponentHandProperty = new SimpleObjectProperty<>();

    //Probably not going to expose this via a getter.
    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();

    @Inject
    public GameViewModel(GameProvider gameProvider) {
        mGameProvider = gameProvider;

        mGameStateProperty.addListener(this::onGameStateUpdated);
    }

    private void onGameStateUpdated(Observable observable, GameState newGameState, GameState oldGameState) {
        if (newGameState.isDefault()) {
            //Game is over here or something went wrong.
            return;
        }


    }
}
