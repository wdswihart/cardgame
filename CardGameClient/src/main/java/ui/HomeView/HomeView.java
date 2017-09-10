package ui.HomeView;

import core.BaseViewController;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.CardDetailView.CardDetailView;
import ui.DraggableView.DraggableView;

public class HomeView extends BaseViewController implements FxmlView<HomeViewModel> {
    @FXML
    private Button mCardDetailDemoButton;

    @FXML
    private Button mDraggableDemoButton;

    @InjectViewModel
    private HomeViewModel mHomeViewModel;

    public void initialize() {

        mCardDetailDemoButton.setOnMouseReleased(event -> {
            navigateTo(CardDetailView.class);
        });

        mDraggableDemoButton.setOnMouseReleased(event -> {
            navigateTo(DraggableView.class);
        });
    }
}
