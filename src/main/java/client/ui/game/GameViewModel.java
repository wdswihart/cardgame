package client.ui.game;

import client.core.ConnectionProvider;
import client.core.GameProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Card;
import models.responses.GameState;

public class GameViewModel extends BaseViewModel {
    private GameProvider mGameProvider;

    private Property<ObservableList<Card>> mPlayerOneHandProperty = new SimpleObjectProperty<>();
    private Property<ObservableList<Card>> mPlayerTwoHandProperty = new SimpleObjectProperty<>();

    //Probably not going to expose this via a getter.
    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();

    @Inject
    public GameViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider, GameProvider gameProvider) {
        super(connectionProvider, navigationProvider);
        mGameProvider = gameProvider;

        mGameStateProperty.addListener(this::onGameStateUpdated);
    }

    private void onGameStateUpdated(Observable observable, GameState newGameState, GameState oldGameState) {
        if (newGameState.isDefault()) {
            //Game is over here or something went wrong.
            return;
        }

        if (newGameState.getPlayerTwoHand().equals(oldGameState.getPlayerTwoHand())) {
            mPlayerTwoHandProperty.setValue(FXCollections.observableList(newGameState.getPlayerTwoHand()));
        }

        if (newGameState.getPlayerOneHand().equals(oldGameState.getPlayerOneHand())) {
            mPlayerOneHandProperty.setValue(FXCollections.observableList(newGameState.getPlayerOneHand()));
        }
    }

    public Property<ObservableList<Card>> getPlayerOneHandProperty() {
        return mPlayerOneHandProperty;
    }

    public Property<ObservableList<Card>> getPlayerTwoHandProperty() {
        return mPlayerTwoHandProperty;
    }
}
