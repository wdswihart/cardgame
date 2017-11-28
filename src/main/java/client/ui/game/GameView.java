package client.ui.game;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GameView implements FxmlView<GameViewModel> {
    @FXML
    public ListView mOpponentsHand;

    @FXML
    public ListView mPlayersHand;

    @InjectViewModel
    private GameViewModel mGameViewModel;

    public void initialize() {
        mOpponentsHand.itemsProperty().bind(mGameViewModel.getPlayerOneHandProperty());
        mPlayersHand.itemsProperty().bind(mGameViewModel.getPlayerTwoHandProperty());
    }
}
