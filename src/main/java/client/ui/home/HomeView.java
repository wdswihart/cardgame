package client.ui.home;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import models.Card;
import models.Player;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.responses.GameState;

public class HomeView implements FxmlView<HomeViewModel> {
    @InjectViewModel
    private HomeViewModel mHomeViewModel;

    @FXML
    public ListView<Player> mActiveUsersListView;

    @FXML
    public TextField mMessageField;

    @FXML
    public ListView mMessagesList;

    @FXML
    public ListView mPendingInvitesListView;

    @FXML
    public ListView<GameState> mActiveGamesListView;

    public void initialize() {
        mActiveGamesListView.itemsProperty().bind(mHomeViewModel.getActiveGamesProperty());
        mActiveGamesListView.setCellFactory(this::activeGameCellFactory);

        mHomeViewModel.getActiveUserProperty().addListener((observable, oldValue, newValue) -> {
            //Have to invoke setItems on the UI thread.
            //This is only an issue because SocketIO runs on a background thread.
            Platform.runLater(() -> {
                mActiveUsersListView.setItems(newValue);
            });
        });
        mActiveUsersListView.setCellFactory(param -> {
            return new ListCell<Player>() {
                @Override
                protected void updateItem(Player player, boolean b) {
                    super.updateItem(player, b);
                    if (player != null) {
                        setText(player.getUsername());

                        if (player.getUsername().equals(mHomeViewModel.getActiveUsername())) {
                            setTextFill(Paint.valueOf("RED"));
                        }
                        else {
                            setTextFill(Paint.valueOf("BLACK"));
                        }
                    }
                    else {
                        setText("");
                    }
                }
            };
        });

        mHomeViewModel.getSelectedInviteProperty().bind(mPendingInvitesListView.getSelectionModel().selectedItemProperty());
        mHomeViewModel.getSelectedActiveUserProperty().bind(mActiveUsersListView.getSelectionModel().selectedItemProperty());

        mHomeViewModel.getPendingInvitesProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                mPendingInvitesListView.setItems(newValue);
            });
        });
        //This is duplicated for right now, but the invites will change later.
        mPendingInvitesListView.setCellFactory(param -> {
            return new ListCell<Player>() {
                @Override
                protected void updateItem(Player player, boolean b) {
                    super.updateItem(player, b);
                    if (player != null) {
                        setText(player.getUsername());
                    }
                    else {
                        setText("");
                    }
                }
            };
        });

        mHomeViewModel.getMessagesListProperty().getValue().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                Platform.runLater(() -> {
                    if (c.next()) {
                        mMessagesList.getItems().addAll(c.getAddedSubList());
                    }
                });
            }
        });

        mMessageField.textProperty().bindBidirectional(mHomeViewModel.getMessageProperty());
    }

    private ListCell<GameState> activeGameCellFactory(ListView<GameState> param) {
        return new ListCell<GameState>() {
            @Override
            protected void updateItem(GameState gameState, boolean b) {
                super.updateItem(gameState, b);

                if (gameState != null) {
                    setText(gameState.getPlayerOne().getUsername() + " vs " + gameState.getPlayerTwo().getUsername());
                }
                else {
                    setText("");
                }
            }
        };
    }


    @FXML
    public void showCardDetailViewAction() {
        mHomeViewModel.getShowCardDetailViewCommand().execute();
    }

    @FXML
    public void showDraggableViewAction() {
        mHomeViewModel.getShowDraggableViewCommand().execute();
    }

    @FXML
    public void logoutAction() { mHomeViewModel.getLogoutCommand().execute(); }

    @FXML
    public void sendAction() {
        mHomeViewModel.getSendCommand().execute();
    }

    @FXML
    public void inviteUserAction() {
        mHomeViewModel.getInviteCommand().execute();
    }

    @FXML
    public void acceptInviteAction() { mHomeViewModel.getAcceptInviteCommand().execute(); }
}
