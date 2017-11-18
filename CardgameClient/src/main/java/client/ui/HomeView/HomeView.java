package client.ui.HomeView;

import models.Player;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HomeView implements FxmlView<HomeViewModel> {
    @InjectViewModel
    private HomeViewModel mHomeViewModel;

    @FXML
    public ListView<Player> mActiveUsersListView;

    @FXML
    public TextField mMessageField;

    @FXML
    public ListView mMessagesList;

    public void initialize() {
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
}
