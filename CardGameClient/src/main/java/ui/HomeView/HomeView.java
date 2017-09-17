package ui.HomeView;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.CardDetailView.CardDetailView;
import ui.DraggableView.DraggableView;

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
}
