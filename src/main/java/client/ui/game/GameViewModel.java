package client.ui.game;

import client.core.ConnectionProvider;
import client.core.GameProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

    private Property<String> mPhaseProperty = new SimpleStringProperty();

    //Probably not going to expose this via a getter.
    private Property<GameState> mGameStateProperty = new SimpleObjectProperty<>();

    //region Player Infos

    private Property<ObservableList<Card>> mPlayerDeckProperty = new SimpleObjectProperty<>();
    private Property<ObservableList<Card>> mOpponentDeckProperty = new SimpleObjectProperty<>();

    //endregion

    //region UI State Visibility
    private Property<Boolean> mDrawButtonVisibleProperty = new SimpleBooleanProperty(false);
    //endregion

    private Command mDrawCommand;

    @Inject
    public GameViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider, GameProvider gameProvider) {
        super(connectionProvider, navigationProvider);
        mGameProvider = gameProvider;
        mGameStateProperty = mGameProvider.getGameStateProperty();
        mGameStateProperty.addListener(this::onGameStateUpdated);

        mDrawCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mGameProvider.drawCard();
            }
        });
    }

    private void onGameStateUpdated(Observable observable, GameState oldVal, GameState newVal) {
        if (newVal.isDefault()) {
            //Game is over here or something went wrong.
            return;
        }

        //TODO: Handle setting player 1/2. Player should be the client. Opponent shoudl be the other guy.
        mPlayerProperty.setValue(newVal.getPlayerOne());
        mOpponentProperty.setValue(newVal.getPlayerTwo());

        mOpponentHandProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerTwoHand()));
        mPlayerHandProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerOneHand()));

        mOpponentDeckProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerTwoDeck()));
        mPlayerDeckProperty.setValue(FXCollections.observableArrayList(newVal.getPlayerOneDeck()));

        mPhaseProperty.setValue(newVal.getState().toString());

        updateVisibleComponents(newVal);
    }

    private void updateVisibleComponents(GameState gameState) {
        boolean isDrawState = gameState.getState() == GameState.State.Draw;
        boolean isActivePlayer = gameState.getActivePlayer().getUsername().equals(mConnectionProvider.getAuthenticatedUser().getValue().getUsername());
        mDrawButtonVisibleProperty.setValue(isActivePlayer && isDrawState);
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

    public Property<ObservableList<Card>> getPlayerDeckProperty() {
        return mPlayerDeckProperty;
    }

    public Property<ObservableList<Card>> getOpponentDeckProperty() {
        return mOpponentDeckProperty;
    }

    public Property<Boolean> getDrawButtonVisibleProperty() {
        return mDrawButtonVisibleProperty;
    }

    public Property<String> getPhaseProperty() {
        return mPhaseProperty;
    }


    public Command getDrawCommand() {
        return mDrawCommand;
    }
}