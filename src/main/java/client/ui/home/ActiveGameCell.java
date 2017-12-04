package client.ui.home;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.responses.GameState;

import java.io.IOException;
import java.util.function.Function;

public class ActiveGameCell extends ListCell<GameState> {
    private Node mGraphic;
    private ActiveGameCellView mController;
    private SpectateCallback mSpectateCallback;

    public ActiveGameCell(SpectateCallback spectateCallback) {
        mSpectateCallback = spectateCallback;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/home/ActiveGameCellView.fxml"));
            mGraphic = loader.load();
            mController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(GameState gameState, boolean empty) {
        super.updateItem(gameState, empty);

        if (gameState != null) {
            mController.setGameState(gameState);
            mController.setSpectateCallback(mSpectateCallback);
            setGraphic(mGraphic);
        }
        else {
            setGraphic(null);
        }
    }

    public static Callback<ListView<GameState>, ListCell<GameState>> getFactory(SpectateCallback callback) {
        return (list) -> new ActiveGameCell(callback);
    }
}
