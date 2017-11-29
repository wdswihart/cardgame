package client.ui.game;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Card;
import models.responses.GameState;
import util.JSONUtils;

public class GameView implements FxmlView<GameViewModel> {
    @FXML
    public ListView mOpponentsHand;

    @FXML
    public ListView mPlayersHand;

    @InjectViewModel
    private GameViewModel mGameViewModel;

    public void initialize() {
        mGameViewModel.getPlayerOneHandProperty().addListener(this::updatePlayerOneHand);
        mGameViewModel.getPlayerTwoHandProperty().addListener(this::updatePlayerTwoHand);
    }

    private void updatePlayerOneHand(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        System.out.println("Updating P1: " + newVal.size());
        Platform.runLater(() -> {
            mPlayersHand.setItems(newVal);
        });
    }

    private void updatePlayerTwoHand(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        System.out.println("Updating P2: " + newVal.size());
        Platform.runLater(() -> {
            mOpponentsHand.setItems(newVal);
        });
    }
}
