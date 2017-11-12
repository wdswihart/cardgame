package client.ui.HomeView;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;

public class HomeView implements FxmlView<HomeViewModel> {
    @InjectViewModel
    private HomeViewModel mHomeViewModel;

    public void initialize() {

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
