package client.ui.game;

import client.ui.controls.CardControl;
import client.ui.controls.SmallCardControl;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public VBox mCardDetailBox;

    @FXML
    public HBox mWinningDisplayBox;

    //region Hands & Fields
    @FXML
    public ListView<Card> mOpponentsHandListView;

    @FXML
    public ListView<Card> mPlayersHandListView;

    @FXML
    public ListView<Card> mOpponentFieldListView;

    @FXML
    public ListView<Card> mPlayerFieldListView;
    //endregion

    //region Player Info
    @FXML
    public Text mPlayerNameText;

    @FXML
    public Text mOpponentNameText;

    @FXML
    public Text mPlayerHealthText;
    @FXML
    public Text mOpponentHealthText;

    @FXML
    public Text mPlayerDeckCountText;

    @FXML
    public Text mOpponentDeckCountText;

    @FXML
    public Text mPhaseText;

    @FXML
    public Text mWinnerText;

    //endregion

    //region Player Actions
    @FXML
    public VBox mGameControlBox;

    @FXML
    public Button mDrawButton;

    @FXML
    public Button mPlayCardButton;

    @FXML
    public Button mPassTurnButton;

    public Button mAttackButton;

    //endregion

    public void initialize() {
        mPhaseText.textProperty().bind(mGameViewModel.getPhaseProperty());
        mWinnerText.textProperty().bind(mGameViewModel.getWinnerProperty());

        setupOpponentProperties();
        setupPlayerProperties();
        setupVisibility();
    }

    private void setupPlayerProperties() {
        updatePlayer(mGameViewModel.getPlayerProperty(), null, mGameViewModel.getPlayerProperty().getValue());
        mGameViewModel.getPlayerProperty().addListener(this::updatePlayer);

        Property<ObservableList<Card>> playerHandProperty = mGameViewModel.getPlayerHandProperty();
        updatePlayerHand(playerHandProperty, null, playerHandProperty.getValue());
        playerHandProperty.addListener(this::updatePlayerHand);
        mGameViewModel.getSelectedPlayerCardProperty().bind(mPlayersHandListView.getSelectionModel().selectedItemProperty());

        mPlayersHandListView.setCellFactory(GameView::smallCardCellFactory);
        Property<ObservableList<Card>> playerDeckProperty = mGameViewModel.getPlayerDeckProperty();
        updatePlayerDeck(playerDeckProperty, null, playerDeckProperty.getValue());
        playerDeckProperty.addListener(this::updatePlayerDeck);


        Property<ObservableList<Card>> playerFieldProperty = mGameViewModel.getPlayerFieldProperty();
        updatePlayerField(playerFieldProperty, null, playerFieldProperty.getValue());
        mPlayerFieldListView.setCellFactory(GameView::cardCellFactory);
        mGameViewModel.getPlayerFieldProperty().addListener(this::updatePlayerField);

        mPlayerHealthText.textProperty().bind(mGameViewModel.getPlayerHealthProperty());
    }

    private void setupOpponentProperties() {
        updateOpponent(mGameViewModel.getOpponentProperty(), null, mGameViewModel.getOpponentProperty().getValue());
        mGameViewModel.getOpponentProperty().addListener(this::updateOpponent);

        Property<ObservableList<Card>> opponentHandProperty = mGameViewModel.getOpponentHandProperty();
        updateOpponentHand(opponentHandProperty, null, opponentHandProperty.getValue());
        opponentHandProperty.addListener(this::updateOpponentHand);

        mOpponentsHandListView.setCellFactory(GameView::smallCardCellFactory);
        Property<ObservableList<Card>> opponentDeckProperty = mGameViewModel.getOpponentDeckProperty();
        updateOpponentDeck(opponentDeckProperty, null, opponentDeckProperty.getValue());
        opponentDeckProperty.addListener(this::updateOpponentDeck);


        Property<ObservableList<Card>> opponentFieldProperty = mGameViewModel.getOpponentFieldProperty();
        updateOpponentField(opponentFieldProperty, null, opponentFieldProperty.getValue());
        mOpponentFieldListView.setCellFactory(GameView::cardCellFactory);
        mGameViewModel.getOpponentFieldProperty().addListener(this::updateOpponentField);

        mOpponentHealthText.textProperty().bind(mGameViewModel.getOpponentHealthProperty());
    }

    private void updatePlayerField(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mPlayerFieldListView.setItems(newVal);
        });
    }

    private void updateOpponentField(Observable observable, ObservableList<Card> oldVal, ObservableList<Card> newVal) {
        Platform.runLater(() -> {
            mOpponentFieldListView.setItems(newVal);
        });
    }

    private void setupVisibility() {
        mDrawButton.disableProperty().bind(mGameViewModel.getDrawButtonDisabledProperty());
        mPlayCardButton.disableProperty().bind(mGameViewModel.getPlayCardButtonDisabledProperty());
        mAttackButton.disableProperty().bind(mGameViewModel.getAttackButtonDisabledProperty());
        mGameControlBox.visibleProperty().bind(mGameViewModel.getGameControlVisibleProperty());
        mWinningDisplayBox.visibleProperty().bind(mGameViewModel.getWinningDisplayBoxVisibleProperty());
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

    public static ListCell<Card> cardCellFactory(ListView<Card> param) {
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

    public static ListCell<Card> smallCardCellFactory(ListView<Card> param) {
        return new ListCell<Card>() {
            private SmallCardControl controller;
            Node graphic;

            {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/controls/SmallCardControl.fxml"));
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

    @FXML
    public void playCardButtonAction() {
        mGameViewModel.getPlayCardCommand().execute();
    }

    @FXML
    public void passTurnAction() {
        mGameViewModel.getPassTurnCommand().execute();
    }

    @FXML
    public void attackAction() {
        mGameViewModel.getAttackCommand().execute();
    }

    @FXML
    public void quitGameAction() {
        mGameViewModel.getQuitGameCommand().execute();
    }
}
