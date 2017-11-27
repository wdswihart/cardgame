package client.ui.CardDetailView;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class CardDetailView implements FxmlView<CardDetailViewModel> {
    @FXML
    private ListView mCardListView;

    @InjectViewModel
    CardDetailViewModel mViewModel;

    public void initialize() {
        mCardListView.setItems(mViewModel.getCardListProperty());
    }

    @FXML
    public void navigatePreviousAction() {
        mViewModel.getNavigatePreviousCommand().execute();
    }
}
