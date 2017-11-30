package client.ui.game;

import client.core.ConnectionProvider;
import client.core.GameProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Card;
import models.Player;
import models.responses.GameState;

public class GameViewModel extends BaseViewModel {
    private GameProvider mGameProvider;

    private Property<ObservableList<Card>> mPlayerHandProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());
    private Property<ObservableList<Card>> mOpponentHandProperty = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private Property<Player> mPlayerProperty = new SimpleObjectProperty<>();
    private Property<Player> mOpponentProperty = new SimpleObjectProperty<>();

    //Probably not going to expose this via a getter.
    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();

    @Inject
    public GameViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider, GameProvider gameProvider) {
        super(connectionProvider, navigationProvider);
        mGameProvider = gameProvider;
        mGameStateProperty = mGameProvider.getGameStateProperty();
        mGameStateProperty.addListener(this::onGameStateUpdated);
    }

    private void onGameStateUpdated(Observable observable, GameState oldVal, GameState newVal) {
        if (newVal.isDefault()) {
            //Game is over here or something went wrong.
            return;
        }

        mPlayerProperty.setValue(newVal.getPlayerOne());
        mOpponentProperty.setValue(newVal.getPlayerTwo());

        mOpponentHandProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerTwoHand()));
        mPlayerHandProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerOneHand()));
    }

    public Property<ObservableList<Card>> getPlayerHandProperty() {
        return mPlayerHandProperty;
    }

    public Property<ObservableList<Card>> getOpponentHandProperty() {
        return mOpponentHandProperty;
    }


    public Property<Player> getPlayerProperty() {
        return mPlayerProperty;
    }

    public Property<Player> getOpponentProperty() {
        return mOpponentProperty;
    }
}