package client.ui.game;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Card;
import models.Player;

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
            @Override
            protected void updateItem(Card card, boolean b) {
                super.updateItem(card, b);
                if (card != null) {
                    setText(card.getName());
                } else {
                    setText("");
                }
            }
        };
    }
}
