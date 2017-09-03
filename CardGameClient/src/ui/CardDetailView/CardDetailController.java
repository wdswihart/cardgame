package ui.CardDetailView;

import core.BaseViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class CardDetailController extends BaseViewController {
    @FXML
    private Button mBackButton;

    public void initialize() {
        mBackButton.setOnMouseReleased(event -> {
            navigatePrevious();
        });
    }

    @Override
    protected Map<String, String> getFxmlFileMap() {
        return new HashMap<>();
    }
}
