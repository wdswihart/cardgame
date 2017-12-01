package client.ui.discovery;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class DiscoveryView implements FxmlView<DiscoveryViewModel> {
    @InjectViewModel
    DiscoveryViewModel mViewModel;

    @FXML
    public ListView mServerList;

    public void initialize() {
        mViewModel.getSelectedServerProperty().bind(mServerList.getSelectionModel().selectedItemProperty());
        mServerList.setItems(mViewModel.getServersListProperty());
    }

    @FXML
    public void joinServerAction() {
        mViewModel.getJoinServerCommand().execute();
    }
}
