package ui.CardDetailView;

import core.BaseViewController;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CardDetailView extends BaseViewController implements FxmlView<CardDetailViewModel> {
    @FXML
    private Button mBackButton;

    @FXML
    private ListView mCardListView;

    @InjectViewModel
    CardDetailViewModel mViewModel;

    public void initialize() {
        mBackButton.setOnMouseReleased(event -> {
            navigatePrevious();
        });

        mCardListView.setItems(mViewModel.getCardListProperty());
    }
}
