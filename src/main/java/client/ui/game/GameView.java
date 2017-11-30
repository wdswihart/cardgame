package client.ui.game;

import client.ui.controls.CardControl;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Card;
import models.Player;

import java.io.IOException;

public class GameView implements FxmlView<GameViewModel> {
    @InjectViewModel
    private GameViewModel mGameViewModel;

    @FXML
    public GridPane mRootPane;

    @FXML
    public GridPane mPlayerHeaderPane;

    @FXML
    public ListView<Card> mOpponentsHandListView;

    @FXML
    public ListView<Card> mPlayersHandListView;

    //region Player Infos
    @FXML
    public Text mPlayerNameText;
    @FXML
    public Text mOpponentNameText;

    @FXML
    public Text mPlayerDeckCountText;
    @FXML
    public Text mOpponentDeckCountText;
    //endregion


    //region Player Actions
    @FXML
    public Button mDrawButton;
    //endregion

    public void initialize() {
        mPlayerHeaderPane.prefWidthProperty().bind(mRootPane.widthProperty());

        mGameViewModel.getOpponentProperty().addListener(this::updateOpponent);
        mGameViewModel.getPlayerProperty().addListener(this::updatePlayer);

        mGameViewModel.getPlayerHandProperty().addListener(this::updatePlayerHand);
        mPlayersHandListView.setCellFactory(GameView::cardCellFactory);

        mGameViewModel.getOpponentHandProperty().addListener(this::updateOpponentHand);
        mOpponentsHandListView.setCellFactory(GameView::cardCellFactory);

        mGameViewModel.getPlayerDeckProperty().addListener(this::updatePlayerDeck);
        mGameViewModel.getOpponentDeckProperty().addListener(this::updateOpponentDeck);

        mDrawButton.visibleProperty().bind(mGameViewModel.getDrawButtonVisibleProperty());
    }

    private void updatePlayerDeck(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mPlayerDeckCountText.setText(String.valueOf(newVal.size()));
        });
    }

    private void updateOpponentDeck(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mOpponentDeckCountText.setText(String.valueOf(newVal.size()));
        });
    }

    private void updateOpponent(Observable observable, Player oldVal, Player newVal) {
        Platform.runLater(() -> {
            mOpponentNameText.setText(newVal.getUsername());
        });
    }

    private void updatePlayer(Observable observable, Player oldVal, Player newVal) {
        Platform.runLater(() -> {
            mPlayerNameText.setText(newVal.getUsername());
        });
    }

    private void updateOpponentHand(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mOpponentsHandListView.setItems(newVal);
        });
    }

    private void updatePlayerHand(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mPlayersHandListView.setItems(newVal);
        });
    }

    private static ListCell<Card> cardCellFactory(ListView<Card> param) {
        return new ListCell<Card>() {
            private CardControl controller;
            Node graphic;

            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/controls/CardControl.fxml"));
                    graphic = loader.load();
                    controller = loader.getController();
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            }

            @Override
            protected void updateItem(Card card, boolean b) {
                super.updateItem(card, b);
                if (card != null) {
                    controller.setCard(card);
                    setGraphic(graphic);
                } else {
                    setGraphic(null);
                }
            }
        };
    }


    @FXML
    public void drawButtonAction() {
        mGameViewModel.getDrawCommand().execute();
    }
}
