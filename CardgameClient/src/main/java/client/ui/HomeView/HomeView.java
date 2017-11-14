package client.ui.HomeView;

import client.converters.UserToStringConverter;
import client.model.User;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import util.JSONUtils;

public class HomeView implements FxmlView<HomeViewModel> {
    @InjectViewModel
    private HomeViewModel mHomeViewModel;

    @FXML
    public ListView<User> mActiveUsersListView;

    public void initialize() {
        mHomeViewModel.getActiveUserProperty().addListener((observable, oldValue, newValue) -> {
            //Have to invoke setItems on the UI thread.
            //This is only an issue because SocketIO runs on a background thread.
            Platform.runLater(() -> {
                mActiveUsersListView.setItems(newValue);
            });
        });
        mActiveUsersListView.setCellFactory(param -> {
            return new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean b) {
                    super.updateItem(user, b);
                    if (user != null) {
                        setText(user.getUsername());
                    }
                    else {
                        setText("");
                    }
                }
            };
        });
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
}
