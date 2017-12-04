package client.ui.home;

import de.saxsys.mvvmfx.utils.commands.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.responses.GameState;

public class ActiveGameCellView {
    @FXML
    private Text mPlayerOneUsername;
    @FXML
    private Text mPlayerTwoUsername;

    private GameState mGameState = new GameState();
    private SpectateCallback mSpectateAction;

    public void initialize() {

    }

    public void setGameState(GameState gameState) {
        mGameState = gameState;
        mPlayerOneUsername.setText(gameState.getPlayerOne().getUsername());
        mPlayerTwoUsername.setText(gameState.getPlayerTwo().getUsername());
    }

    @FXML
    public void spectateAction() {
        if (mSpectateAction == null) {
            return;
        }

        mSpectateAction.call(mGameState);
    }

    public void setSpectateCallback(SpectateCallback spectateAction) {
        mSpectateAction = spectateAction;
    }
}
